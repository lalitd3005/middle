package co.uk.project.sample.service;

import co.uk.project.sample.core.domain.PhoneDirectory;
import co.uk.project.sample.web.presentation.dto.ContractDto;

import java.util.List;

public interface PhoneDirectoryService {

    List<String> getAllAvailablePhoneNumbers();
    List<String> getAllPhoneNumbersForCustomer(Integer customerId);
    PhoneDirectory activatePhoneNumberForCustomer(ContractDto contractDto) throws Exception;
}
