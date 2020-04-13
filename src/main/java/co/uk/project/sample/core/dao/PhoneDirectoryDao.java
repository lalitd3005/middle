package co.uk.project.sample.core.dao;

import co.uk.project.sample.core.domain.Phone;
import co.uk.project.sample.core.domain.PhoneDirectory;
import co.uk.project.sample.web.presentation.dto.ContractDto;

import java.util.List;

public interface PhoneDirectoryDao {
    List<Phone> findAllAvailablePhoneNumbers();
    List<Phone> fetchPhoneNumbersForCustomer(Integer customerId);
    PhoneDirectory activatePhoneNumberForCustomer(ContractDto contractDto);
}
