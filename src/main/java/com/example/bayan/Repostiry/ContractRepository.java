package com.example.bayan.Repostiry;

import com.example.bayan.Model.Contract;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract,Integer> {

    Contract findRentalById(Integer id);
   List<Contract> findRentalByBrokerId(Integer rentalID);

   Contract findRentalByCustomerIdAndBrokerId(Integer customerId, Integer brokerID);

   List<Contract> findRentalByCustomerId(Integer customerID);

    Contract findContractByCustomerIdAndBrokerIdAndStatus(Integer customerId, Integer brokerId,String status );
   @Query("SELECT r FROM Contract r " +
           "WHERE r.customer.id = :customerId " +
           "AND r.status NOT IN ('Completed', 'Cancelled')")
   List<Contract> findNonCompletedNonCancelledRentalsByCustomerId(@Param("customerId")
                                                                Integer customerId);

   @Query("SELECT r FROM Contract r " +
           "WHERE r.broker.id = :brokerId " +
           "AND r.status NOT IN ('Completed', 'Cancelled')")
   List<Contract> findNonCompletedNonCancelledRentalsByBrokerId(@Param("brokerId") Integer brokerId);

}
