package co.uk.project.sample.web;


import co.uk.project.sample.core.domain.PhoneDirectory;
import co.uk.project.sample.core.exception.FatalException;
import co.uk.project.sample.service.PhoneDirectoryService;
import co.uk.project.sample.web.presentation.dto.ContractDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/phoneDirectory/api")
public class PhoneDirectoryController {
    private static final Logger LOG = LogManager.getLogger(PhoneDirectoryController.class);

    private static final String GET_AVAILABLE_PHONE_NUMBERS = "/availablePhoneNumbers";
    private static final String GET_ALL_PHONE_NUMBERS_FOR_CUSTOMER = "/{customerId}/phoneNumbers";
    private static final String ACTIVATE_A_PHONE_NUMBER_FOR_CUSTOMER = "/activate";
    private static final String CUSTOMER_ID = "customerId";
    private static final String PHONE_NUMBER = "phoneNumber";

    @Autowired
    private PhoneDirectoryService phoneDirectoryService;

    @RequestMapping(value = GET_AVAILABLE_PHONE_NUMBERS, method = RequestMethod.GET)
    public List<String> getAllAvailablePhoneNumbers() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Enter: getAllAvailablePhoneNumbers()");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Exit: getAllAvailablePhoneNumbers()");
        }

        return phoneDirectoryService.getAllAvailablePhoneNumbers();
    }

    @RequestMapping(value = GET_ALL_PHONE_NUMBERS_FOR_CUSTOMER, method = RequestMethod.GET)
    public List<String> getAllPhoneNumbersForCustomer(@PathVariable(CUSTOMER_ID) Integer customerId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Enter: getAllPhoneNumbersForCustomer(customerId = [" + customerId + "])");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Exit: getAllPhoneNumbersForCustomer(customerId = [" + customerId + "])");
        }

        return phoneDirectoryService.getAllPhoneNumbersForCustomer(customerId);
    }

    @RequestMapping(value = ACTIVATE_A_PHONE_NUMBER_FOR_CUSTOMER, method = RequestMethod.POST)
    public ResponseEntity<Object> activatePhoneNumberForCustomer(@RequestBody ContractDto phoneContractDto ) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Enter: activatePhoneNumberForCustomer(phoneContractDto = [" + phoneContractDto + "])");
        }
        PhoneDirectory phoneDirectory = null;
        try {
            phoneDirectory = phoneDirectoryService.activatePhoneNumberForCustomer(phoneContractDto);
        } catch(FatalException ex) {
            return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.EXPECTATION_FAILED);
        } catch(Exception ex) {
            return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Exit: activatePhoneNumberForCustomer(phoneContractDto = [" + phoneContractDto + "])");
        }

        return new ResponseEntity<Object>(phoneDirectory, HttpStatus.OK);
    }
}
