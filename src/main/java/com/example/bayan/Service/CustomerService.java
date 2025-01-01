package com.example.bayan.Service;

import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.CustomerDTO;
import com.example.bayan.Model.Customer;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomerService {

private final AuthRepository authRepository;
    private final CustomerRepository customerRepository;


    public com.example.bayan.DTO.OUT.CustomerDTO getMyAccount(Integer userId){
            MyUser myUser=authRepository.findMyUserById(userId);

            if(myUser==null){
                throw new ApiException("Customer with this"+userId+" does not exist");

            }
            Customer customer=customerRepository.findCustomerById(userId);

            return new com.example.bayan.DTO.OUT.CustomerDTO(myUser.getUsername(), myUser.getFullName(), myUser.getEmail(),
                    myUser.getPhoneNumber(), customer.getCompanyName());
            }

    public void registerCustomer(CustomerDTO customerDTO){
        MyUser myUser=authRepository.findMyUserByUsername(customerDTO.getUsername());
        if(myUser!=null){
            throw new ApiException("User already exists");
        }

        MyUser myUser1=new MyUser();

        myUser1.setUsername(customerDTO.getUsername());

        myUser1.setPassword(new BCryptPasswordEncoder().encode(customerDTO.getPassword()));

        myUser1.setEmail(customerDTO.getEmail());

        myUser1.setPhoneNumber(customerDTO.getPhoneNumber());

        myUser1.setFullName(customerDTO.getFullName());

        myUser1.setRole("CUSTOMER");

        authRepository.save(myUser1);


        Customer customer=new Customer();
        customer.setCompanyName(customerDTO.getCompanyName());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setUser(myUser1);

        customerRepository.save(customer);



    }

    public void updateCustomerAccount(Integer customerID,CustomerDTO customerDTO){
        MyUser oldCustomer=authRepository.findMyUserById(customerID);
        if(oldCustomer==null){
            throw new ApiException(" Customer id is wrong");
        }

        oldCustomer.setUsername(customerDTO.getUsername());
        oldCustomer.setPassword(new BCryptPasswordEncoder().encode(customerDTO.getPassword()));
        oldCustomer.setEmail(customerDTO.getEmail());
        oldCustomer.setPhoneNumber(customerDTO.getPhoneNumber());
        oldCustomer.setUpdatedAt(LocalDateTime.now());

        authRepository.save(oldCustomer);

        Customer customer=new Customer();
        customer.setCompanyName(customerDTO.getCompanyName());

        customerRepository.save(customer);
    }





}
