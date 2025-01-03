package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.CustomsBrokerDTO;
import com.example.bayan.DTO.IN.UpdateCustomsBrokerDTO;
import com.example.bayan.DTO.OUT.CustomBrokerDTO;
import com.example.bayan.DTO.OUT.CustomBrokerFilterDTO;
import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.CustomBrokerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomBrokerService {


    private final CustomBrokerRepository customBrokerRepository;
    private final AuthRepository authRepository;

    // register new Custom Broker
    public void register(CustomsBrokerDTO customsBrokerDTO){

        MyUser broker = new MyUser();
        broker.setUsername(customsBrokerDTO.getUsername());
        broker.setPassword(new BCryptPasswordEncoder().encode(customsBrokerDTO.getPassword()));
        broker.setFullName(customsBrokerDTO.getFullName());
        broker.setEmail(customsBrokerDTO.getEmail());
        broker.setPhoneNumber(customsBrokerDTO.getPhoneNumber());
        broker.setRole("BROKER");
        broker.setCreatedAt(LocalDateTime.now());
        authRepository.save(broker);

        CustomsBroker customsBroker = new CustomsBroker();
        customsBroker.setCommercialLicense(customsBrokerDTO.getCommercialLicense());
        customsBroker.setLicenseNumber(customsBrokerDTO.getLicenseNumber());
        customsBroker.setLicenseType(customsBrokerDTO.getLicenseType());
        customsBroker.setCompanyName(customsBrokerDTO.getCompanyName());

        customsBroker.setUser(broker);

        customBrokerRepository.save(customsBroker);

    }

    // get .. MyProfile .. Custom Broker
    public CustomBrokerDTO myProfile(Integer broker_id){
      MyUser user = authRepository.findMyUserById(broker_id);
      if (user==null){
          throw new ApiException("Broker Id is wrong");
      }
      CustomsBroker broker = customBrokerRepository.findCustomsBrokerById(broker_id);
      return new CustomBrokerDTO(user.getUsername(),user.getEmail(),user.getPhoneNumber(), user.getFullName(), broker.getLicenseNumber(),broker.getCompanyName(),broker.getCommercialLicense(),broker.getLicenseType());
    }

    // update
    public void updateMyAccount(Integer broker_id , UpdateCustomsBrokerDTO customsBrokerDTO){
      MyUser oldBroker = authRepository.findMyUserById(broker_id);
        if (oldBroker==null){
            throw new ApiException("Broker Id is wrong");
        }
        oldBroker.setUsername(customsBrokerDTO.getUsername());

        oldBroker.setFullName(customsBrokerDTO.getFullName());
        oldBroker.setEmail(customsBrokerDTO.getEmail());
        oldBroker.setPhoneNumber(customsBrokerDTO.getPhoneNumber());
        oldBroker.setUpdatedAt(LocalDateTime.now());
        authRepository.save(oldBroker);

        CustomsBroker customsBroker = oldBroker.getBroker();
        customsBroker.setCommercialLicense(customsBrokerDTO.getCommercialLicense());
        customsBroker.setLicenseNumber(customsBrokerDTO.getLicenseNumber());
        customsBroker.setLicenseType(customsBrokerDTO.getLicenseType());
        customsBroker.setCompanyName(customsBrokerDTO.getCompanyName());
        customBrokerRepository.save(customsBroker);
    }

    // delete
    public void deleteMyAccount(Integer broker_id){
      MyUser broker = authRepository.findMyUserById(broker_id);
      if(broker==null){
          throw new ApiException("Wrong Id");
      }
    authRepository.delete(broker);
    }


    // *** EndPoint to Get Customs Broker By License Number
    public CustomBrokerFilterDTO getByLicenseNumber(String lNumber) {
        CustomsBroker customsBroker = customBrokerRepository.getCustomsBrokersByLicenseNumber(lNumber);

        if (customsBroker == null) {
            throw new ApiException("No Customs Broker found with license number: " + lNumber);
        }

        return mapToCustomBrokerFilterDTO(customsBroker);
    }

    // *** EndPoint to Get All Customs Brokers Working at a Specific Border
    public List<CustomBrokerFilterDTO> getAllCustomsByBorder(String border) {
        List<CustomsBroker> customsBrokers = customBrokerRepository.getCustomsBrokerByBorderName(border);

        if (customsBrokers.isEmpty()) {
            throw new ApiException("No Customs Brokers found for the border: " + border);
        }

        return customsBrokers.stream()
                .map(this::mapToCustomBrokerFilterDTO)
                .collect(Collectors.toList());
    }

    // *** EndPoint to Get All Customs Brokers By Name
    public List<CustomBrokerFilterDTO> getAllCustomsByName(String name) {
        List<CustomsBroker> customsBrokers = customBrokerRepository.getCustomsBrokerByUserFullName(name);

        if (customsBrokers.isEmpty()) {
            throw new ApiException("No Customs Brokers found with the name: " + name);
        }

        return customsBrokers.stream()
                .map(this::mapToCustomBrokerFilterDTO)
                .collect(Collectors.toList());
    }

    // *** EndPoint to Get All Customs Brokers By License Type
    public List<CustomBrokerFilterDTO> getAllCustomsByLicenseType(String type) {
        List<CustomsBroker> customsBrokers = customBrokerRepository.getCustomsBrokerByLicenseType(type);

        if (customsBrokers.isEmpty()) {
            throw new ApiException("No Customs Brokers found with license type: " + type);
        }

        return customsBrokers.stream()
                .map(this::mapToCustomBrokerFilterDTO)
                .collect(Collectors.toList());
    }


    // Helper method to map CustomsBroker entity to CustomBrokerFilterDTO
    private CustomBrokerFilterDTO mapToCustomBrokerFilterDTO(CustomsBroker customsBroker) {
        return new CustomBrokerFilterDTO(
                customsBroker.getUser().getFullName(),
                customsBroker.getLicenseNumber(),
                customsBroker.getCommercialLicense(),
                customsBroker.getLicenseType()
        );
    }



}
