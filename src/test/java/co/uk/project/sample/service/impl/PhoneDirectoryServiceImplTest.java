package co.uk.project.sample.service.impl;

import co.uk.project.sample.core.dao.PhoneDirectoryDao;
import co.uk.project.sample.core.dao.impl.PhoneDirectoryDaoImpl;
import co.uk.project.sample.core.domain.Phone;
import co.uk.project.sample.core.domain.PhoneDirectory;
import co.uk.project.sample.core.exception.FatalException;
import co.uk.project.sample.service.PhoneDirectoryService;
import co.uk.project.sample.web.presentation.dto.ContractDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class PhoneDirectoryServiceImplTest {

    public static final Phone PHONE_1 = new Phone(6, "PhoneNumber1", false);
    public static final Phone PHONE_2 = new Phone(7, "PhoneNumber2", false);
    public static final int CUSTOMER_ID = 111;
    public static final String PHONE_NUMBER_1 = "PhoneNumber1";
    public static final String PHONE_NUMBER_2 = "PhoneNumber2";
    public static final String PHONE_NUMBER_1_UNKNOWN = "Unknown PhoneNumber";

    public List<Phone> allAvailablePhoneNumbers = new ArrayList<>(Arrays.asList(
            PHONE_1,
            PHONE_2
    ));

    @Mock
    private PhoneDirectoryDao mockPhoneDirectoryDao;

    @InjectMocks
    private PhoneDirectoryServiceImpl phoneDirectoryServiceImpl;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldGetAllAvailablePhoneNumbers() {
        List<String> expectedResult = Arrays.asList(PHONE_NUMBER_1, PHONE_NUMBER_2);
        //given
        given(mockPhoneDirectoryDao.findAllAvailablePhoneNumbers()).willReturn(allAvailablePhoneNumbers);
        //when
        List<String> availablePhoneNumbers = phoneDirectoryServiceImpl.getAllAvailablePhoneNumbers();
        //then
        assertEquals(2, availablePhoneNumbers.size());
        assertEquals(expectedResult, availablePhoneNumbers);
        verify(mockPhoneDirectoryDao).findAllAvailablePhoneNumbers();
        verifyNoMoreInteractions(mockPhoneDirectoryDao);
    }

    @Test
    public void shouldGetNoPhoneNumbers() {
        given(mockPhoneDirectoryDao.findAllAvailablePhoneNumbers()).willReturn(null);
        //when
        List<String> availablePhoneNumbers = phoneDirectoryServiceImpl.getAllAvailablePhoneNumbers();
        //then
        assertEquals(0, availablePhoneNumbers.size());
        verify(mockPhoneDirectoryDao).findAllAvailablePhoneNumbers();
        verifyNoMoreInteractions(mockPhoneDirectoryDao);
    }

    @Test
    public void shouldGetNoPhoneNumbers1() {
        given(mockPhoneDirectoryDao.findAllAvailablePhoneNumbers()).willReturn(new ArrayList<>());
        //when
        List<String> availablePhoneNumbers = phoneDirectoryServiceImpl.getAllAvailablePhoneNumbers();
        //then
        assertEquals(0, availablePhoneNumbers.size());
        verify(mockPhoneDirectoryDao).findAllAvailablePhoneNumbers();
        verifyNoMoreInteractions(mockPhoneDirectoryDao);
    }

    @Test
    public void shouldGetPhoneNumbersForCustomer111() {
        List<String> expectedResult = Arrays.asList(PHONE_NUMBER_1, PHONE_NUMBER_2);
        //given
        given(mockPhoneDirectoryDao.fetchPhoneNumbersForCustomer(CUSTOMER_ID)).willReturn(allAvailablePhoneNumbers);
        //when
        List<String> phoneNumbers = phoneDirectoryServiceImpl.getAllPhoneNumbersForCustomer(CUSTOMER_ID);
        //then
        assertEquals(2, phoneNumbers.size());
        assertEquals(expectedResult, phoneNumbers);
        verify(mockPhoneDirectoryDao).fetchPhoneNumbersForCustomer(CUSTOMER_ID);
        verifyNoMoreInteractions(mockPhoneDirectoryDao);
    }


    @Test
    public void shouldGetNoPhoneNumbersForCustomer111() {
        given(mockPhoneDirectoryDao.findAllAvailablePhoneNumbers()).willReturn(new ArrayList<>());
        //when
        List<String> availablePhoneNumbers = phoneDirectoryServiceImpl.getAllPhoneNumbersForCustomer(CUSTOMER_ID);
        //then
        assertEquals(0, availablePhoneNumbers.size());
        verify(mockPhoneDirectoryDao).fetchPhoneNumbersForCustomer(CUSTOMER_ID);
        verifyNoMoreInteractions(mockPhoneDirectoryDao);
    }

    @Test
    public void shouldActivatePhoneNumberForCustomer111() throws Exception {
        PhoneDirectory expectedPhoneDirectory = new PhoneDirectory();
        ContractDto newContractDto = new ContractDto(CUSTOMER_ID, PHONE_NUMBER_1);
        given(mockPhoneDirectoryDao.activatePhoneNumberForCustomer(newContractDto)).willReturn(expectedPhoneDirectory);
        //when
        PhoneDirectory actualPhoneDirectory = phoneDirectoryServiceImpl.activatePhoneNumberForCustomer(newContractDto);
        //then
        assertEquals(expectedPhoneDirectory, actualPhoneDirectory);
        verify(mockPhoneDirectoryDao).activatePhoneNumberForCustomer(newContractDto);
        verifyNoMoreInteractions(mockPhoneDirectoryDao);
    }

    @Test(expected = FatalException.class)
    public void shouldThrowFatalExceptionWhileActivatingPhoneNumberForCustomer111() throws Exception {
        ContractDto newContractDto = new ContractDto(CUSTOMER_ID, PHONE_NUMBER_1_UNKNOWN);
        given(mockPhoneDirectoryDao.activatePhoneNumberForCustomer(newContractDto)).willThrow(FatalException.class);
        //when
        phoneDirectoryServiceImpl.activatePhoneNumberForCustomer(newContractDto);
    }
}