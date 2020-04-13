package co.uk.project.sample.core.dao.impl;

import co.uk.project.sample.core.dao.PhoneDirectoryDao;
import co.uk.project.sample.core.domain.Customer;
import co.uk.project.sample.core.domain.Phone;
import co.uk.project.sample.core.domain.PhoneDirectory;
import co.uk.project.sample.core.exception.FatalException;
import co.uk.project.sample.web.presentation.dto.ContractDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PhoneDirectoryDaoImpl implements PhoneDirectoryDao {

    public Phone PHONE_NUMBER_1 = new Phone(1, "0123456789", true);
    public Phone PHONE_NUMBER_2 = new Phone(2, "0223456789", true);
    public Phone PHONE_NUMBER_3 = new Phone(3, "0323456789", true);
    public Phone PHONE_NUMBER_4 = new Phone(4, "0423456789", true);
    public Phone PHONE_NUMBER_5 = new Phone(5, "0523456789", true);
    public Phone PHONE_NUMBER_6 = new Phone(6, "0623456789", false); //Available
    public Phone PHONE_NUMBER_7 = new Phone(7, "0723456789", false); //Available
    public Phone PHONE_NUMBER_8 = new Phone(8, "0823456789", false); //Available

    public Customer CUSTOMER_111 = new Customer(111, "David", "Belton");
    public Customer CUSTOMER_112 = new Customer(112, "Jim", "Jones");
    public Customer CUSTOMER_113 = new Customer(113, "Matt", "Amis");


    //Data Source - 1   (Phone Entity) - Phone Table
    private List<Phone> allAvailablePhoneNumbers = new ArrayList<>(Arrays.asList(
            PHONE_NUMBER_6,
            PHONE_NUMBER_7,
            PHONE_NUMBER_8
    ));

    //Data Source - 2  (Phone Directory Entity) - Intersect Table
    private List<PhoneDirectory> phoneDirectory = new ArrayList<>(Arrays.asList(
            new PhoneDirectory( CUSTOMER_111, PHONE_NUMBER_1),
            new PhoneDirectory( CUSTOMER_111, PHONE_NUMBER_2),
            new PhoneDirectory( CUSTOMER_112, PHONE_NUMBER_3),
            new PhoneDirectory( CUSTOMER_112, PHONE_NUMBER_4),
            new PhoneDirectory( CUSTOMER_112, PHONE_NUMBER_5)
    ));

    //Data Source - 3 (Customer Entity) - Customer Table
    private List<Customer> customers =  new ArrayList<>(Arrays.asList(
            CUSTOMER_111,
            CUSTOMER_112,
            CUSTOMER_113
    ));

    @Override
    public List<Phone> findAllAvailablePhoneNumbers() {
        return allAvailablePhoneNumbers;
    }

    @Override
    public List<Phone> fetchPhoneNumbersForCustomer(Integer customerId) {
        if(customerId == null) {
            throw new FatalException("CustomerId cannot be null");
        }
         return this.phoneDirectory
                .stream()
                .filter(phoneDirectory -> phoneDirectory.getCustomerId().getId() == customerId)
                .collect(Collectors.toList())
                .stream()
                .map(phoneDirectory -> phoneDirectory.getPhoneId())
                .collect(Collectors.toList());
    }

    @Override
    public PhoneDirectory activatePhoneNumberForCustomer(ContractDto contractDto) {
        if(contractDto == null) {
            throw new FatalException("Phone contract details cannot be null");
        }
        PhoneDirectory phoneDirectory =  new PhoneDirectory();;
        this.customers
                .stream()
                .filter(customer -> customer.getId() == contractDto.getCustomerId())
                .findFirst() //return optional
                .map(customer -> {
                    phoneDirectory.setCustomerId(customer);
                    return customer;
                }).orElseThrow(() -> new FatalException("Sorry! No customer details are found!"));

        if(phoneDirectory.getCustomerId() != null) {
            this.allAvailablePhoneNumbers
                    .stream()
                    .filter(phone -> phone.getPhoneNumber().equals(contractDto.getPhoneNumber()))
                    .findFirst() //return optional
                    .map(phone -> {
                        phone.setActive(true);
                        phoneDirectory.setPhoneId(phone);
                        this.allAvailablePhoneNumbers.remove(phone); // make the PhoneNumber unavailable
                        return phone;
                    })
                    .orElseThrow(() -> new FatalException("Sorry! The selected phone number isn't available!"));

            if(phoneDirectory.getPhoneId() != null) {
                this.phoneDirectory.add(phoneDirectory);
            }
        }
        return phoneDirectory;
    }
}
