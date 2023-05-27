package com.italloramon.apicepcorreios.service;

import com.italloramon.apicepcorreios.ApiCepCorreiosApplication;
import com.italloramon.apicepcorreios.exception.NoContentException;
import com.italloramon.apicepcorreios.exception.NoReadyException;
import com.italloramon.apicepcorreios.model.Address;
import com.italloramon.apicepcorreios.model.AddressStatus;
import com.italloramon.apicepcorreios.model.Status;
import com.italloramon.apicepcorreios.repository.AddressRepository;
import com.italloramon.apicepcorreios.repository.AddressStatusRepository;
import com.italloramon.apicepcorreios.repository.SetupRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CorreiosService {
    private static Logger logger = LoggerFactory.getLogger(CorreiosService.class);

    private final AddressRepository addressRepository;
    private final AddressStatusRepository addressStatusRepository;
    private final SetupRepository setupRepository;
    @Value("${setup.on.startup}")
    private boolean setupOnStartup;
    public Status getStatus() {
        return addressStatusRepository.findById(AddressStatus.DEFAULT_ID)
                .orElse(AddressStatus.builder().status(Status.NEED_SETUP).build())
                .getStatus();
    }

    public Address getAddressByZipCode(String zipcode) throws NoContentException, NoReadyException {
        if (!getStatus().equals(Status.READY)) {
            throw new NoReadyException();
        }

        return addressRepository.findById(zipcode).orElseThrow(NoContentException::new);
    }

    private void saveStatus(Status status) {
        addressStatusRepository.save(AddressStatus.builder()
                .id(AddressStatus.DEFAULT_ID)
                .status(status)
                .build());
    }

    @EventListener(ApplicationStartedEvent.class)
    protected void setupOnStartup() {
        if (!setupOnStartup) {
            return;
        }

        try {
            setup();
        } catch (Exception ex) {
            ApiCepCorreiosApplication.close(-1);
        }
    }
    public void setup() throws Exception {
        logger.info("---------------");
        logger.info("SETUP RUNNING");
        logger.info("---------------");

        if (getStatus().equals(Status.NEED_SETUP)) {
            saveStatus(Status.RUNNING);
            try {
                addressRepository.saveAll(setupRepository.getFromOrigin());
                saveStatus(Status.READY);
            } catch (Exception ex) {
                saveStatus(Status.NEED_SETUP);
                throw ex;
            }
        }

        logger.info("---------------");
        logger.info("SETUP COMPLETED");
        logger.info("---------------");
    }
}
