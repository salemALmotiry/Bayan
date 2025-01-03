package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.Offer.OfferDTO;
import com.example.bayan.DTO.IN.Offer.OfferWithDeliveryDTO;
import com.example.bayan.DTO.OUT.CustomerOfferDTO;
import com.example.bayan.Model.*;
import com.example.bayan.Repostiry.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {


    private final AuthRepository authRepository;
    private final PostRepository postRepository;
    private final CustomBrokerRepository customBrokerRepository;
    private final OfferRepository offerRepository;
    private final OrdersRepository ordersRepository;
    private final CustomerRepository customerRepository;

    private final EmailService emailService;
    private final NotificationRepository notificationRepository;

    // ____________________Broker Offer Without Delivery________________
    public void createOffer(Integer userId, OfferDTO offerDTO) {
        // Validate the broker
        MyUser broker = authRepository.findMyUserById(userId);
        if (broker == null) {
            throw new ApiException("Broker with ID " + userId + " not found.");
        }

        // Validate the post
        Post post = postRepository.findPostById(offerDTO.getPostId());
        if (post == null) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " not found.");
        }

        // Ensure the post status is pending
        if (!"Pending".equalsIgnoreCase(post.getStatus())) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " is not available for offers (current status: " + post.getStatus() + ").");
        }

        // Ensure there isn't already an existing pending offer for the same broker and post
        if (offerRepository.existsByPostAndBrokerAndOfferStatus(post, broker.getBroker(), "Pending")) {
            throw new ApiException("A pending offer already exists for this post and broker.");
        }

        // Create the offer
        Offer offer = new Offer();
        offer.setPost(post);
        offer.setPrice(offerDTO.getPrice());
        offer.setBroker(customBrokerRepository.findCustomsBrokerById(userId));
        offer.setOfferStatus("PENDING");
        offer.setDeliveryIncluded(false);

        offerRepository.save(offer);
    }

    public void updateOffer(Integer userId,Integer offerId, OfferDTO offerDTO) {
        MyUser myUser = authRepository.findMyUserById(userId);


        if (myUser == null){
            throw new ApiException("Broker not found");
        }

        Post post = postRepository.findPostById(offerDTO.getPostId());

        if (post == null){
            throw new ApiException("Post not found");
        }

        if (!post.getStatus().equals("Pending")){
            throw new ApiException("Post is taken by broker");
        }


        Offer offer = offerRepository.findOfferById(offerId);

        if (offer == null){
            throw new ApiException("Offer not found");
        }
        if (!offer.getOfferStatus().equals("Pending")){
            throw new ApiException("Offer is not Pending");
        }
        offer.setPost(post);
        offer.setPrice(offerDTO.getPrice());
        offer.setOfferStatus("pending");
        offer.setDeliveryIncluded(false);

        offerRepository.save(offer);



    }

    public void deleteOffer(Integer userId,Integer offerId) {
        MyUser broker = authRepository.findMyUserById(userId);

        if(broker==null){
            throw new ApiException("Broker with this"+userId+" does not exist");
        }

        Offer offer=offerRepository.findOfferById(offerId);

        if(offer==null){
            throw new ApiException("Offer with this"+offerId+" does not exist");
        }

        if (!offer.getOfferStatus().equals("Pending")){
            throw new ApiException("Offer is not Pending");
        }

        offerRepository.delete(offer);
    }

    // ____________________broker offer with delivery________________
    public void createOfferWithDelivery(Integer userId, OfferWithDeliveryDTO offerDTO) {
        MyUser myUser = authRepository.findMyUserById(userId);

        if (myUser == null) {
            throw new ApiException("Broker with ID " + userId + " not found.");
        }

        Post post = postRepository.findPostById(offerDTO.getPostId());

        if (post == null) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " not found.");
        }

        if (!"Pending".equals(post.getStatus())) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " is already taken by another broker.");
        }

        if (!post.getHasDelivery()){
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " has no delivery.");
        }

        Offer offer = new Offer();
        offer.setPost(post);
        offer.setPrice(offerDTO.getPrice().add(offerDTO.getDeliveryPrice()));
        offer.setBroker(customBrokerRepository.findCustomsBrokerById(userId));
        offer.setOfferStatus("Pending");
        offer.setDeliveryIncluded(true);
        offer.setDeliveryPrice(offerDTO.getDeliveryPrice());

        offerRepository.save(offer);
    }

    public void updateOfferWithDelivery(Integer userId, Integer offerId, OfferWithDeliveryDTO offerDTO) {
        MyUser myUser = authRepository.findMyUserById(userId);

        if (myUser == null) {
            throw new ApiException("Broker with ID " + userId + " not found.");
        }

        Post post = postRepository.findPostById(offerDTO.getPostId());

        if (post == null) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " not found.");
        }

        if (!"Pending".equals(post.getStatus())) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " is already taken by another broker.");
        }

        Offer offer = offerRepository.findOfferById(offerId);

        if (offer == null) {
            throw new ApiException("Offer with ID " + offerId + " not found.");
        }

        if (!"Pending".equals(offer.getOfferStatus())) {
            throw new ApiException("Offer with ID " + offerId + " is not in Pending status.");
        }

        offer.setPost(post);
        offer.setPrice(offerDTO.getPrice().add(offerDTO.getDeliveryPrice()));
        offer.setOfferStatus("Pending");
        offer.setDeliveryIncluded(true);
        offer.setDeliveryPrice(offerDTO.getDeliveryPrice());

        offerRepository.save(offer);
    }

    public void removeOffer(Integer userId, Integer offerId) {
        MyUser broker = authRepository.findMyUserById(userId);

        if (broker == null) {
            throw new ApiException("Broker with ID " + userId + " not found.");
        }

        Offer offer = offerRepository.findOfferById(offerId);

        if (offer == null) {
            throw new ApiException("Offer with ID " + offerId + " not found.");
        }

        if (!"Pending".equals(offer.getOfferStatus())) {
            throw new ApiException("Offer with ID " + offerId + " is not in Pending status and cannot be deleted.");
        }

        offerRepository.delete(offer);
    }

    //____________________________________________________________________

    public void acceptOffer(Integer userId, Integer offerId) {
        // Validate the user
        MyUser customer = authRepository.findMyUserById(userId);
        if (customer == null) {
            throw new ApiException("Customer with ID " + userId + " not found.");
        }

        // Validate the offer
        Offer offer = offerRepository.findOfferByIdAndPost_CustomerId(offerId, customer.getId());

        if (offer == null) {
            throw new ApiException("Offer with ID " + offerId + " not found.");
        }

        // Check if the offer is already accepted
        if (!"PENDING".equals(offer.getOfferStatus())) {
            throw new ApiException("Offer with ID " + offerId + " cannot be accepted as it is not pending.");
        }

        // Create a new order
        Orders order = new Orders();
        order.setStatus("PLACED");
        order.setPaymentStatus("PENDING");

        order.setOffer(offer);
        // Associate the offer with the order
        offer.setOrder(order);
        offer.setOfferStatus("ACCEPTED");

        // Save the order and update the offer
        ordersRepository.save(order);
        offerRepository.save(offer);

        // notification for the customer
        Notification notification = new Notification();
        notification.setMassage("لقد قبلت هذا العرض");
        notification.setCreateAt(LocalDateTime.now());
        notification.setMyUser(customer);

        notificationRepository.save(notification);

        // notification for the custom broker that his order has been accepted
        Notification notification2= new Notification();
        notification2.setMassage("لقد تم قبول عرضك");
        notification2.setCreateAt(LocalDateTime.now());
        notification2.setMyUser(offer.getBroker().getUser());
        notificationRepository.save(notification2);

        sendAcceptanceEmailToBroker(offer);
       sendAcceptanceEmailToCustomer(offer);

    }


    private void sendAcceptanceEmailToCustomer(Offer offer) {
        String subject = "تم قبول العرض: " + offer.getPost().getTitle();
        String body = "عزيزي " + offer.getPost().getCustomer().getUser().getFullName() + ",\n\n"
                + "تم قبول عرضك للإعلان بعنوان '" + offer.getPost().getTitle() + "'.\n"
                + "تفاصيل الطلب:\n\n"
                + "سعر العرض: " + offer.getPrice() + " ريال سعودي\n"
                + "حالة الطلب: تم وضعه\n"
                + "حالة الدفع: قيد الانتظار\n\n"
                + "شكرًا لاستخدامك خدماتنا!\n\n"
                + "أطيب التحيات,\n"
                + "فريق بيان";

        emailService.sendEmail(offer.getPost().getCustomer().getUser().getEmail(), subject, body);
    }

    private void sendAcceptanceEmailToBroker(Offer offer) {
        String subject = "تم قبول عرضك!";
        String body = "عزيزي " + offer.getBroker().getUser().getFullName() + ",\n\n"
                + "تهانينا! تم قبول عرضك للإعلان بعنوان '" + offer.getPost().getTitle() + "'.\n"
                + "تفاصيل العرض:\n\n"
                + "سعر العرض: " + offer.getPrice() + " ريال سعودي\n"
                + "اسم العميل: " + offer.getBroker().getUser().getFullName() + "\n"
                + "حالة الطلب: تم وضعه\n"
                + "حالة الدفع: قيد الانتظار\n\n"
                + "يرجى متابعة الإجراءات التالية بناءً على الطلب.\n\n"
                + "أطيب التحيات,\n"
                + "فريق بيان";

        emailService.sendEmail(offer.getBroker().getUser().getEmail(), subject, body);
    }

    //EndPPoint for Customer he can see all The Offer for his post
    public List<CustomerOfferDTO> getAllOffersforOnePost(Integer postID, Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);

        if (customer == null) {
            throw new ApiException("Customer with ID " + customerId + " not found.");
        }

        Post post = postRepository.findPostById(postID);
        if (post == null) {
            throw new ApiException("Post with ID " + postID + " not found");
        }

        List<Offer> offers = offerRepository.getAllOfferByPost(post);

        return offers.stream()
                .map(offer -> {
                    CustomerOfferDTO dto = new CustomerOfferDTO();
                    dto.setFullName(offer.getBroker().getUser().getFullName());
                    dto.setCompanyName(offer.getBroker().getCompanyName());
                    dto.setPrice(offer.getPrice());
                    return dto;
                })
                .sorted(Comparator.comparing(CustomerOfferDTO::getPrice))
                .toList();
    }

}
