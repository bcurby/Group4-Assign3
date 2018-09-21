package hotel.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hotel.booking.BookingCTL;
import hotel.booking.BookingUI;
import hotel.booking.BookingUI.State;
import hotel.credit.CreditAuthorizer;
import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;


class TestBookingCtrl {

    @Mock BookingUI bookingUI;
    @Mock Hotel hotel;
    @Mock CreditAuthorizer authorizer;
    @Mock CreditCardHelper creditCardHelper;
    @Mock Guest guest;
    @Mock CreditCard creditCard;
    
    private double cost;
    State state;
    int phoneNumber;
    RoomType selectedRoomType;
    int occupantNumber;
    Date arrivalDate;
    int stayLength;
    int cardNum;
    CreditCardType cardType;
    long confNum;
    
    static SimpleDateFormat format;
    BookingCTL control;
    
    
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        format = new SimpleDateFormat("dd-MM-yyyy");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        control = new BookingCTL(hotel);
        control.bookingUI = bookingUI;
        control.authorizer = authorizer;
        control.creditcardHelper = creditCardHelper;
        
        arrivalDate = format.parse("11-09-2001");
        stayLength = 1;
        occupantNumber = 1;
        cardNum = 1;
        cardType = CreditCardType.VISA;
        cost = 111.11;
        confNum = 11092001101L;
        
        
        
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void testPhoneNumberEnteredGuestExists() {
        //arrange
        asertTrue(control.state == State.PHONE);
        when(hotel.isRegistered(anyInt())).thenReturn(true);
        when(hotel.findGuestByPhoneNumber(anyInt())).thenReturn(guest);
        when(guest.getName()).thenReturn("Eric");
        when(guest.getAddress()).thenReturn("461");
        when(guest.getPhoneNumber()).thenReturn(1);
        
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> addressCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> phoneCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<BookingUI.State> uiStateCaptor = ArgumentCaptor.forClass(BookingUI.State.class);
        
        //act
        control.phoneNumberEntered(1);
        
        //assert
        verify(hotel).isRegistered(anyInt());
        verify(hotel).findGuestByPhoneNumber(anyInt());
        verify(bookingUI).displayGuestdetails(nameCaptor.capture(), addressCaptor.capture(), phoneCaptor.capture());
        verify(bookingUI).setState(uiStateCaptor.capture());
        
        assertEquals("Eric", nameCaptor.getValue());
        assertEquals("461", addressCaptor.getValue());
        assertTrue(1 == phoneCaptor.getValue());
        assertTrue(BookingUI.State.ROOM == uiStateCaptor.getValue());
        assertTrue(state.ROOM == control.state);
        
        
        
        
        
        
    }

}
