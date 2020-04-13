package co.uk.project.sample.web;

import co.uk.project.sample.core.domain.PhoneDirectory;
import co.uk.project.sample.core.exception.FatalException;
import co.uk.project.sample.service.PhoneDirectoryService;
import co.uk.project.sample.web.presentation.dto.ContractDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class PhoneDirectoryControllerTest {

    private static final String PHONE_NUMBER_1 = "PhoneNumber1";
    private static final String PHONE_NUMBER_2 = "PhoneNumber2";
    private static final int CUSTOMER_ID = 1;
    private static final int INVALID_CUSTOMER = 999;
    private static final HttpStatus STATUS_OK = HttpStatus.OK;

    @Mock
    private PhoneDirectoryService mockPhoneDirectoryService;

    @InjectMocks
    private PhoneDirectoryController phoneDirectoryController ;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldGetAllAvailablePhoneNumbers() {
         List<String> expectedResult = Arrays.asList(PHONE_NUMBER_1, PHONE_NUMBER_2);
        //given
        given(mockPhoneDirectoryService.getAllAvailablePhoneNumbers()).willReturn(expectedResult);
        //when
        List<String> availablePhoneNumbers = phoneDirectoryController.getAllAvailablePhoneNumbers();
        //then
        assertEquals(2, availablePhoneNumbers.size());
        assertEquals(expectedResult, availablePhoneNumbers);
        verify(mockPhoneDirectoryService).getAllAvailablePhoneNumbers();
        verifyNoMoreInteractions(mockPhoneDirectoryService);
    }

    @Test
    public void shouldGetEmptyListIfNoPhoneNumbersAvailable() {
        List<String> expectedResult = new ArrayList<>();
        //given
        given(mockPhoneDirectoryService.getAllAvailablePhoneNumbers()).willReturn(expectedResult);
        //when
        List<String> availablePhoneNumbers = phoneDirectoryController.getAllAvailablePhoneNumbers();
        //then
        assertEquals(0, availablePhoneNumbers.size());
        assertEquals(expectedResult, availablePhoneNumbers);
        verify(mockPhoneDirectoryService).getAllAvailablePhoneNumbers();
        verifyNoMoreInteractions(mockPhoneDirectoryService);
    }

    @Test
    public void shouldGetAllPhoneNumbersForCustomer() {
        List<String> expectedResult = Arrays.asList(PHONE_NUMBER_1, PHONE_NUMBER_2);
        //given
        given(mockPhoneDirectoryService.getAllPhoneNumbersForCustomer(CUSTOMER_ID)).willReturn(expectedResult);
        //when
        List<String> customerPhoneNumbers = phoneDirectoryController.getAllPhoneNumbersForCustomer(CUSTOMER_ID);
        //then
        assertEquals(2, customerPhoneNumbers.size());
        assertEquals(expectedResult, customerPhoneNumbers);
        verify(mockPhoneDirectoryService).getAllPhoneNumbersForCustomer(CUSTOMER_ID);
        verifyNoMoreInteractions(mockPhoneDirectoryService);
    }

    @Test
    public void shouldNotGetAnyPhoneNumbersForInvalidCustomer() {
        List<String> expectedResult = new ArrayList<>();
        //given
        given(mockPhoneDirectoryService.getAllPhoneNumbersForCustomer(INVALID_CUSTOMER)).willReturn(expectedResult);
        //when
        List<String> customerPhoneNumbers = phoneDirectoryController.getAllPhoneNumbersForCustomer(INVALID_CUSTOMER);
        //then
        assertEquals(0, customerPhoneNumbers.size());
        assertEquals(expectedResult, customerPhoneNumbers);
        verify(mockPhoneDirectoryService).getAllPhoneNumbersForCustomer(INVALID_CUSTOMER);
        verifyNoMoreInteractions(mockPhoneDirectoryService);
    }

    @Test
    public void shouldActivatePhoneNumberForCustomer() throws Exception {

        ContractDto newContractDto = new ContractDto(CUSTOMER_ID, PHONE_NUMBER_1);
        PhoneDirectory expectedResult = new PhoneDirectory();
        //given
        given(mockPhoneDirectoryService.activatePhoneNumberForCustomer(newContractDto)).willReturn(expectedResult);
        //when
        ResponseEntity response = phoneDirectoryController.activatePhoneNumberForCustomer(newContractDto);
        //then
        assertNotNull(response.getBody());
        assertEquals(STATUS_OK, response.getStatusCode());
        verify(mockPhoneDirectoryService).activatePhoneNumberForCustomer(newContractDto);
        verifyNoMoreInteractions(mockPhoneDirectoryService);
    }

    @Test
    public void shouldThrowFatalExceptionWhileActivatingPhoneNumberForCustomer() throws Exception{

        ContractDto newContractDto = new ContractDto(CUSTOMER_ID, PHONE_NUMBER_1);
        //given
        given(mockPhoneDirectoryService.activatePhoneNumberForCustomer(newContractDto)).willThrow(FatalException.class);
        //when
        ResponseEntity response = phoneDirectoryController.activatePhoneNumberForCustomer(newContractDto);
        //then
        assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
    }

    @Test
    public void shouldThrowExceptionWhileActivatingPhoneNumberForCustomer() throws Exception {

        ContractDto newContractDto = new ContractDto(CUSTOMER_ID, PHONE_NUMBER_1);
        //given
        given(mockPhoneDirectoryService.activatePhoneNumberForCustomer(newContractDto)).willThrow(Exception.class);
        //when
        ResponseEntity response = phoneDirectoryController.activatePhoneNumberForCustomer(newContractDto);
        //then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void shouldNotActivatePhoneNumberForCustomerIfThereIsNoContractDetails() throws Exception {

        //given
        given(mockPhoneDirectoryService.activatePhoneNumberForCustomer(null)).willReturn(null);
        //when
        ResponseEntity response = phoneDirectoryController.activatePhoneNumberForCustomer(null);
        //then
        assertNull(response.getBody());
        assertEquals(STATUS_OK, response.getStatusCode());
        verify(mockPhoneDirectoryService).activatePhoneNumberForCustomer(null);
        verifyNoMoreInteractions(mockPhoneDirectoryService);
    }
}