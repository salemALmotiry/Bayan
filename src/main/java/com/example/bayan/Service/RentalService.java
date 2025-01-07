package com.example.bayan.Service;

import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.OUT.BrokerRentalsDTO;
import com.example.bayan.DTO.OUT.CustomerRentalsDTO;
import com.example.bayan.DTO.OUT.ContractDTO;
import com.example.bayan.Model.Customer;
import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Model.Contract;
import com.example.bayan.Repostiry.CustomBrokerRepository;
import com.example.bayan.Repostiry.CustomerRepository;
import com.example.bayan.Repostiry.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final ContractRepository contractRepository;
    private final CustomBrokerRepository customBrokerRepository;
    private  final CustomerRepository customerRepository;



    ///  CRUD - Start

    public List<ContractDTO> getCustomerRental(Integer customerId) {
        List<Contract> contracts = contractRepository.findRentalByCustomerId(customerId);

        List<ContractDTO> contractDTOS = new ArrayList<>();

        for (Contract contract : contracts) {
            ContractDTO contractDTO = new ContractDTO();

            contractDTO.setStartDateTime(LocalDate.now());
            contractDTO.setNumberOfOrder(contract.getNumberOfOrder());
            contractDTO.setTotalPrice(contract.getTotalPrice());
            contractDTO.setSupPrice(contract.getSupPrice());
            contractDTO.setStatus(contract.getStatus());

            contractDTOS.add(contractDTO);
        }

        return contractDTOS;
    }

    public List<ContractDTO> getBrokerRental(Integer brokerId) {
        List<Contract> contracts = contractRepository.findRentalByBrokerId(brokerId);

        List<ContractDTO> contractDTOS = new ArrayList<>();

        for (Contract contract : contracts) {
            ContractDTO contractDTO = new ContractDTO();

            contractDTO.setStartDateTime(LocalDate.now());
            contractDTO.setNumberOfOrder(contract.getNumberOfOrder());
            contractDTO.setTotalPrice(contract.getTotalPrice());
            contractDTO.setSupPrice(contract.getSupPrice());
            contractDTO.setStatus(contract.getStatus());

            contractDTOS.add(contractDTO);
        }

        return contractDTOS;
    }


    public CustomerRentalsDTO getCustomerWithRentals(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);
          if(customer==null){
         throw  new ApiException("Customer with ID " + customerId + " not found.");}

        CustomerRentalsDTO customerDTO = new CustomerRentalsDTO();
        customerDTO.setFullName(customer.getUser().getFullName());
        customerDTO.setEmail(customer.getUser().getEmail());
        customerDTO.setPhoneNumber(customer.getUser().getPhoneNumber());


        List<Contract> contracts = contractRepository.findNonCompletedNonCancelledRentalsByCustomerId(customerId);

        List<ContractDTO> contractDTOS = new ArrayList<>();
             for(Contract contract : contracts) {
                 ContractDTO contractDTO = new ContractDTO();
                 contractDTO.setStartDateTime(contract.getStartDate());
                 contractDTO.setNumberOfOrder(contract.getNumberOfOrder());
                 contractDTO.setTotalPrice(contract.getTotalPrice());
                 contractDTO.setSupPrice(contract.getSupPrice());
                 contractDTO.setStatus(contract.getStatus());

                 contractDTOS.add(contractDTO);
             }
         customerDTO.setContractDTOS(contractDTOS);
             return customerDTO;
    }


    public BrokerRentalsDTO getBrokerWithRentals(Integer brokerId) {

        CustomsBroker customsBroker=customBrokerRepository.findCustomsBrokerById(brokerId)
;
        if(customsBroker==null){
            throw  new ApiException("Customer with ID " + brokerId + " not found.");}

        BrokerRentalsDTO brokerDto = new BrokerRentalsDTO();
        brokerDto.setFullName(customsBroker.getUser().getFullName());
        brokerDto.setPhoneNumber(customsBroker.getUser().getPhoneNumber());
        brokerDto.setCompanyName(customsBroker.getCompanyName());


        List<Contract> contracts = contractRepository.findNonCompletedNonCancelledRentalsByCustomerId(brokerId);

        List<ContractDTO> contractDTOS = new ArrayList<>();
        for(Contract contract : contracts) {
            ContractDTO contractDTO = new ContractDTO();
            contractDTO.setStartDateTime(LocalDate.now());
            contractDTO.setNumberOfOrder(contract.getNumberOfOrder());
            contractDTO.setTotalPrice(contract.getTotalPrice());
            contractDTO.setSupPrice(contract.getSupPrice());
            contractDTO.setStatus(contract.getStatus());

            contractDTOS.add(contractDTO);
        }
        brokerDto.setContractDTOS(contractDTOS);
        return brokerDto;
    }






}

