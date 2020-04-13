package co.uk.project.sample.core.dao.impl;

import co.uk.project.sample.core.domain.Customer;
import co.uk.project.sample.core.domain.Phone;
import co.uk.project.sample.core.domain.PhoneDirectory;
import co.uk.project.sample.core.exception.FatalException;
import co.uk.project.sample.web.presentation.dto.ContractDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PhoneDirectoryDaoImplTest {

    private static final Integer UNKNOWN_CUSTOMER = 999;
    public static final  Phone PHONE_NUMBER_1 = new Phone(1, "0123456789", true);
    public static final  Phone PHONE_NUMBER_2 = new Phone(2, "0223456789", true);
    public static final  Phone PHONE_NUMBER_3 = new Phone(3, "0323456789", true);
    public static final  Phone PHONE_NUMBER_4 = new Phone(4, "0423456789", true);
    public static final  Phone PHONE_NUMBER_5 = new Phone(5, "0523456789", true);
    public static final  Phone PHONE_NUMBER_6 = new Phone(6, "0623456789", false);
    public static final  Phone PHONE_NUMBER_7 = new Phone(7, "0723456789", false);
    public static final  Phone PHONE_NUMBER_8 = new Phone(8, "0823456789", false);

    public static final  Phone PHONE_NUMBER_6_ACTIVATED = new Phone(6, "0623456789", true);
    public static final  Customer CUSTOMER_111 = new Customer(111, "David", "Belton");
    public static final  Customer CUSTOMER_112 = new Customer(112, "Jim", "Jones");
    public static final  Customer CUSTOMER_113 = new Customer(113, "Matt", "Amis");


    //Test Data Source - 1  (Phone Entity) - Phone Table
    private List<Phone> allAvailablePhoneNumbers = new ArrayList<>(Arrays.asList(
            PHONE_NUMBER_6,
            PHONE_NUMBER_7,
            PHONE_NUMBER_8
    ));

    //Test Data Source - 2 (Customer Entity) - Customer Table
    private List<Customer> customers =  new ArrayList<>(Arrays.asList(
            CUSTOMER_111,
            CUSTOMER_112,
            CUSTOMER_113
    ));

    //Test Data Source - 3  (PhoneDirectory Entity) - Intersect Table
    private List<PhoneDirectory> phoneDirectory = new ArrayList<>(Arrays.asList(
            new PhoneDirectory( CUSTOMER_111, PHONE_NUMBER_1),
            new PhoneDirectory( CUSTOMER_111, PHONE_NUMBER_2),
            new PhoneDirectory( CUSTOMER_112, PHONE_NUMBER_3),
            new PhoneDirectory( CUSTOMER_112, PHONE_NUMBER_4),
            new PhoneDirectory( CUSTOMER_112, PHONE_NUMBER_5)
    ));


    @InjectMocks
    private PhoneDirectoryDaoImpl phoneDirectoryDaoImpl;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldFindAllAvailablePhoneNumbers() {
        //when
        List<Phone> availableNumbers = phoneDirectoryDaoImpl.findAllAvailablePhoneNumbers();
        //then
        assertEquals(3, availableNumbers.size());
        assertEquals(allAvailablePhoneNumbers, availableNumbers);
    }

    @Test
    public void shouldFetchPhoneNumbersForCustomer111() {
        //when
        List<Phone> phoneNumbers = phoneDirectoryDaoImpl.fetchPhoneNumbersForCustomer(111);
        //then
        assertEquals(2, phoneNumbers.size());
        assertEquals(PHONE_NUMBER_1, phoneNumbers.get(0));
        assertEquals(PHONE_NUMBER_2, phoneNumbers.get(1));
    }

    @Test
    public void shouldFetchPhoneNumbersForCustomer112() {
        //when
        List<Phone> phoneNumbers = phoneDirectoryDaoImpl.fetchPhoneNumbersForCustomer(112);
        //then
        assertEquals(3, phoneNumbers.size());
        assertEquals(PHONE_NUMBER_3, phoneNumbers.get(0));
        assertEquals(PHONE_NUMBER_4, phoneNumbers.get(1));
        assertEquals(PHONE_NUMBER_5, phoneNumbers.get(2));
    }

    @Test
    public void shouldFetchPhoneNumbersForCustomerUNKNOWN() {
        //when
        List<Phone> phoneNumbers = phoneDirectoryDaoImpl.fetchPhoneNumbersForCustomer(UNKNOWN_CUSTOMER);
        //then
        assertEquals(0, phoneNumbers.size());
    }

    @Test(expected = FatalException.class)
    public void shouldThrowFatalExceptionWhileFetchingPhoneNumbersForNull() {
        //when
        phoneDirectoryDaoImpl.fetchPhoneNumbersForCustomer(null);
    }

    @Test
    public void shouldActivatePhoneNumberForCustomer111() {
        ContractDto newContractDto = new ContractDto(111, "0623456789");
        //when
        List<Phone> availableNumbersBefore = phoneDirectoryDaoImpl.findAllAvailablePhoneNumbers();
        //then
        assertEquals(3, availableNumbersBefore.size());

        //when
        PhoneDirectory phoneDirectory = phoneDirectoryDaoImpl.activatePhoneNumberForCustomer(newContractDto);
        assertNotNull( phoneDirectory);
        assertEquals(PHONE_NUMBER_6_ACTIVATED, phoneDirectory.getPhoneId());
        assertEquals(CUSTOMER_111, phoneDirectory.getCustomerId());

        //when
        List<Phone> availableNumbersAfter = phoneDirectoryDaoImpl.findAllAvailablePhoneNumbers();
        //then
        assertEquals(2, availableNumbersAfter.size());
    }


    @Test
    public void shouldThrowFatalExceptionActivatePhoneNumberIfContractIsNull() {
        String expectedErrorMessage = "Phone contract details cannot be null";
        //when
        List<Phone> availableNumbersBefore = phoneDirectoryDaoImpl.findAllAvailablePhoneNumbers();
        //then
        assertEquals(3, availableNumbersBefore.size());

        String actualErrorMessage = null;
        //when
        try {
            phoneDirectoryDaoImpl.activatePhoneNumberForCustomer(null);
        } catch(FatalException ex) {
            actualErrorMessage = ex.getMessage();
        }
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }


    @Test
    public void shouldThrowExceptionWhileActivatingPhoneNumberForUnknownCustomer() {
        String expectedErrorMessage = "Sorry! No customer details are found!";
        ContractDto newContractDto = new ContractDto(UNKNOWN_CUSTOMER, "0623456789");
        //when
        List<Phone> availableNumbersBefore = phoneDirectoryDaoImpl.findAllAvailablePhoneNumbers();
        //then
        assertEquals(3, availableNumbersBefore.size());

        String actualErrorMessage = null;
        //when
        try {
            phoneDirectoryDaoImpl.activatePhoneNumberForCustomer(newContractDto);
        } catch(FatalException ex) {
            actualErrorMessage = ex.getMessage();
        }

        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void shouldThrowExceptionWhileActivatingInvalidPhoneNumberForCustomer111() {
        String expectedErrorMessage = "Sorry! The selected phone number isn't available!";
        ContractDto newContractDto = new ContractDto(111, "Invalid Number");
        //when
        List<Phone> availableNumbersBefore = phoneDirectoryDaoImpl.findAllAvailablePhoneNumbers();
        //then
        assertEquals(3, availableNumbersBefore.size());

        String actualErrorMessage = null;
        //when
        try {
            phoneDirectoryDaoImpl.activatePhoneNumberForCustomer(newContractDto);
        } catch(FatalException ex) {
            actualErrorMessage = ex.getMessage();
        }

        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void shouldThrowExceptionWhileActivatingSamePhoneNumberTwiceForCustomer() {
        String expectedErrorMessage = "Sorry! The selected phone number isn't available!";
        ContractDto newContractDto = new ContractDto(111, "0623456789");
        //when
        List<Phone> availableNumbersBefore = phoneDirectoryDaoImpl.findAllAvailablePhoneNumbers();
        //then
        assertEquals(3, availableNumbersBefore.size());


        //Activate for the first time
        PhoneDirectory phoneDirectory = phoneDirectoryDaoImpl.activatePhoneNumberForCustomer(newContractDto);
        assertNotNull( phoneDirectory);
        assertEquals(PHONE_NUMBER_6_ACTIVATED, phoneDirectory.getPhoneId());
        assertEquals(CUSTOMER_111, phoneDirectory.getCustomerId());

        //After activating 0623456789 for customer 111
        //when
        List<Phone> availableNumbersAfterActivatingPhoneNumber0623456789 = phoneDirectoryDaoImpl.findAllAvailablePhoneNumbers();
        //then
        assertEquals(2, availableNumbersBefore.size());


        //Activate 0623456789 for the second time
        ContractDto newContractDto1 = new ContractDto(112, "0623456789");
         String actualErrorMessage = null;
        //when
        try {
            phoneDirectoryDaoImpl.activatePhoneNumberForCustomer(newContractDto1);
        } catch(FatalException ex) {
            actualErrorMessage = ex.getMessage();
        }

        assertEquals(expectedErrorMessage, actualErrorMessage);
    }
}