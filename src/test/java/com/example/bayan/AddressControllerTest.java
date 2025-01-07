package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.Model.Address;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AddressController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class AddressControllerTest {

    @Mock
    private AddressService addressService;

    @Autowired
    private MockMvc mockMvc;

    private MyUser mockUser;
    private Address mockAddress1, mockAddress2;
    private List<Address> addressList;

    @BeforeEach
    void setUp() {
        // إعداد المستخدم الوهمي
        mockUser = new MyUser();
        mockUser.setId(1);
        mockUser.setUsername("testUser");

        // إعداد العناوين الوهمية
        mockAddress1 = new Address();
        mockAddress1.setId(2);
        mockAddress1.setCity("Riyadh");
        mockAddress1.setNeighborhood("Al Olaya");
        mockAddress1.setStreet("Main Street");
        mockAddress1.setPostalCode("12345");
        mockAddress1.setBuildingNumber("10");


        mockAddress2 = new Address();
        mockAddress2.setId(2);
        mockAddress2.setCity("Riyadh");
        mockAddress2.setNeighborhood("Al Olaya");
        mockAddress2.setStreet("Main Street");
        mockAddress2.setPostalCode("12345");
        mockAddress2.setBuildingNumber("10");


        addressList = Arrays.asList(mockAddress1, mockAddress2);
    }

    @Test
    public void testAddAddress() throws Exception {
        Mockito.doNothing().when(addressService).addDeliveryAddress(anyInt(), any(Address.class));

        mockMvc.perform(post("/api/v1/bayan/address/add-address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockAddress1)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Address added Successfully"));
    }



    @Test
    public void testUpdateAddress() throws Exception {
        Mockito.doNothing().when(addressService).updateAddress(anyInt(), anyInt(), any(Address.class));

        mockMvc.perform(put("/api/v1/bayan/address/update-address/address-id/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockAddress1)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Address updated successfully"));
    }

    @Test
    public void testDeleteAddress() throws Exception {
        Mockito.doNothing().when(addressService).deleteAddress(anyInt(), anyInt());

        mockMvc.perform(delete("/api/v1/bayan/address/delete-address/address-id/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Address deleted Successfully"));
    }
}
