package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.Model.Address;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Repostiry.AddressRepository;
import com.example.bayan.Repostiry.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AuthRepository authRepository;

    // add address by customer for delivery
    public void addDeliveryAddress(Integer customer_id,Address address){
        MyUser customer =  authRepository.findMyUserById(customer_id);
        if(customer==null){
            throw new ApiException("Wrong customer Id");
        }
        address.setCustomer(customer.getCustomer());
        addressRepository.save(address);
        authRepository.save(customer);
    }

    // get myAddresses
    public List<Address> myAddresses(Integer customer_id) {
        MyUser customer = authRepository.findMyUserById(customer_id);
        if (customer == null) {
            throw new ApiException("Wrong customer Id");
        }
        return  addressRepository.findAddressesByCustomerId(customer_id);
    }

    // update address
    public void updateAddress(Integer customer_id,Integer address_id, Address address) {
      MyUser customer = authRepository.findMyUserById(customer_id);
      if (customer == null) {
          throw new ApiException("Wrong customer Id");
      }
      Address oldAddress = addressRepository.findAddressById(address_id);
        if (oldAddress == null) {
            throw new ApiException("Wrong address Id");
        }
      if(!customer_id.equals(oldAddress.getCustomer().getId())){
          throw new ApiException("you are not authorized to update this address");
      }
      oldAddress.setCity((address.getCity()));
      oldAddress.setStreet(address.getStreet());
      oldAddress.setNeighborhood(address.getNeighborhood());
      oldAddress.setPostalCode(address.getPostalCode());
      oldAddress.setBuildingNumber(address.getBuildingNumber());
      addressRepository.save(oldAddress);
    }

    // delete address
    public void deleteAddress(Integer customer_id, Integer address_id) {
        MyUser customer = authRepository.findMyUserById(customer_id);
        if (customer == null) {
            throw new ApiException("Wrong customer Id");
        }
        Address oldAddress = addressRepository.findAddressById(address_id);
        if (oldAddress == null) {
            throw new ApiException("Wrong address Id");
        }
        if(!customer_id.equals(oldAddress.getCustomer().getId())){
            throw new ApiException("you are not authorized to update this address");
        }
        customer.getCustomer().getAddresses().remove(oldAddress);
        authRepository.save(customer);
        addressRepository.delete(oldAddress);
    }

}
