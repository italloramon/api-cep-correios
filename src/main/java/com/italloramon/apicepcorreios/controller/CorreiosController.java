package com.italloramon.apicepcorreios.controller;

import com.italloramon.apicepcorreios.exception.NoContentException;
import com.italloramon.apicepcorreios.exception.NoReadyException;
import com.italloramon.apicepcorreios.model.Address;
import com.italloramon.apicepcorreios.service.CorreiosService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CorreiosController {

    private final CorreiosService correiosService;
    @GetMapping("/status")
    public String getStatus() {
        return "Status: " + correiosService.getStatus();
    }

    @GetMapping("/zipcode/{zipcode}")
    public Address getAddressByZipCode(@PathVariable("zipcode") String zipcode) throws NoContentException, NoReadyException {
        return correiosService.getAddressByZipCode(zipcode);
        //Address address = new Address();
        //address.setZipcode(zipcode);
        //return address;
        // Equals to:
        // return Address.builder().zipcode(zipcode).build();
    }
}
