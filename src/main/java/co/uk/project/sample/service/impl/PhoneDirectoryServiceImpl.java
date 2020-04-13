package co.uk.project.sample.service.impl;

import co.uk.project.sample.core.dao.PhoneDirectoryDao;
import co.uk.project.sample.core.domain.Phone;
import co.uk.project.sample.core.domain.PhoneDirectory;
import co.uk.project.sample.service.PhoneDirectoryService;
import co.uk.project.sample.web.presentation.dto.ContractDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PhoneDirectoryServiceImpl implements PhoneDirectoryService {

    private static final Logger LOG = LogManager.getLogger(PhoneDirectoryServiceImpl.class);

    @Autowired
    private PhoneDirectoryDao phoneDirectoryDao;

    @Override
    public List<String> getAllAvailablePhoneNumbers() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Enter: getAllPhoneNumbers()");
        }
        List<String> phoneNumbers = new ArrayList<>();
        List<Phone> allPhoneNumbers = phoneDirectoryDao.findAllAvailablePhoneNumbers();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Exit: getAllPhoneNumbers()");
        }

        return buildPhoneNumbersList(allPhoneNumbers, phoneNumbers);
    }

    @Override
    public List<String> getAllPhoneNumbersForCustomer(Integer customerId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Enter: getAllPhoneNumbersForCustomer()");
        }

        List<String> phoneNumbers = new ArrayList<>();
        List<Phone> allPhoneNumbers = phoneDirectoryDao.fetchPhoneNumbersForCustomer(customerId);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Exit: getAllPhoneNumbersForCustomer()");
        }

        return buildPhoneNumbersList(allPhoneNumbers, phoneNumbers);
    }

    @Override
    public PhoneDirectory activatePhoneNumberForCustomer(ContractDto contractDto) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Enter: activatePhoneNumber(contractDto = [" + contractDto + "])");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Exit: activatePhoneNumber(contractDto = [" + contractDto + "])");
        }
        return phoneDirectoryDao.activatePhoneNumberForCustomer(contractDto);
    }

    private List<String> buildPhoneNumbersList(List<Phone> phoneNumbersList, List<String> phoneNumbers) {

        if(phoneNumbersList != null && phoneNumbersList.size() > 0) {
            for(Phone phone: phoneNumbersList) {
                phoneNumbers.add(phone.getPhoneNumber());
            }
        }

        return phoneNumbers;
    }
}
