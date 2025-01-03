package com.example.bayan.Repostiry;

import com.example.bayan.Model.Border;
import com.example.bayan.Model.CustomsBroker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomBrokerRepository extends JpaRepository<CustomsBroker,Integer> {


    CustomsBroker findCustomsBrokerById(Integer id);


    CustomsBroker getCustomsBrokersByLicenseNumber(String licenseNumber);

    List<CustomsBroker> getCustomsBrokerByBorderName(String border);

//    List<CustomsBroker> getCustomsBrokerByCustomerName(String customerName);

    List<CustomsBroker> getCustomsBrokerByUserFullName(String name);

    List<CustomsBroker>getCustomsBrokerByLicenseType(String licenseType);



}
