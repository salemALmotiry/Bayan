package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.CustomsBrokerDTO;
import com.example.bayan.DTO.OUT.CustomBrokerDTO;
import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.CustomBrokerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomBrokerService {


    private final CustomBrokerRepository customBrokerRepository;
    private final AuthRepository authRepository;

    // register new Custom Broker
    public void register(CustomsBrokerDTO customsBrokerDTO){
        MyUser broker = new MyUser();

        broker.setUsername(customsBrokerDTO.getUsername());
        String hashedPassword = new BCryptPasswordEncoder().encode(customsBrokerDTO.getPassword());
        broker.setPassword(hashedPassword);
        broker.setFullName(customsBrokerDTO.getFullName());
        broker.setEmail(customsBrokerDTO.getEmail());
        broker.setPhoneNumber(customsBrokerDTO.getPhoneNumber());
        broker.setRole("BROKER");
        broker.setCreatedAt(LocalDateTime.now());
        authRepository.save(broker);

        CustomsBroker customsBroker = new CustomsBroker();
        customsBroker.setCommercialLicense(customsBroker.getLicenseNumber());
        customsBroker.setLicenseNumber(customsBroker.getLicenseNumber());
        customsBroker.setLicenseType(customsBroker.getLicenseType());
        customsBroker.setCompanyName(customsBroker.getCompanyName());
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
    public void updateMyAccount(Integer broker_id , CustomsBrokerDTO customsBrokerDTO){
      MyUser oldBroker = authRepository.findMyUserById(broker_id);
        if (oldBroker==null){
            throw new ApiException("Broker Id is wrong");
        }
        oldBroker.setUsername(customsBrokerDTO.getUsername());
        String hashedPassword = new BCryptPasswordEncoder().encode(customsBrokerDTO.getPassword());
        oldBroker.setPassword(hashedPassword);
        oldBroker.setFullName(customsBrokerDTO.getFullName());
        oldBroker.setEmail(customsBrokerDTO.getEmail());
        oldBroker.setPhoneNumber(customsBrokerDTO.getPhoneNumber());
        oldBroker.setUpdatedAt(LocalDateTime.now());
        authRepository.save(oldBroker);

        CustomsBroker customsBroker = new CustomsBroker();
        customsBroker.setCommercialLicense(customsBroker.getLicenseNumber());
        customsBroker.setLicenseNumber(customsBroker.getLicenseNumber());
        customsBroker.setLicenseType(customsBroker.getLicenseType());
        customsBroker.setCompanyName(customsBroker.getCompanyName());
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


}
