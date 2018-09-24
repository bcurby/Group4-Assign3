package hotel.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
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
    @Mock CreditCard creditCardHelper;
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
    Room room;
    
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
        control.creditCardHelper = creditCardHelper;
        
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
        assertTrue(State.ROOM == control.state);
        
        
    }
    @Test
    void testCreditDetailsEnteredCreditApproved() {
        //arrange
        control.state = State.CREDIT;
        control.guest = guest;
        control.room = room;
        control.cost = cost;
        control.arrivalDate = arrivalDate;
        control.stayLength = stayLength;
        control.occupantNumber = occupantNumber;
        
        ArgumentCaptor<String> descCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> vendorCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> numCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> stayCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> cardNumCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Double> costCaptor = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Long> confNumCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<BookingUI.State> uiStateCaptor = ArgumentCaptor.forClass(BookingUI.State.class);
        
        when(CreditCard.makeCreditCard(any(),anyInt(), anyInt())).thenReturn(creditCard);
        when(authorizer, authorize(any(), anyDouble())).thenReturn(true);
        when(hotel.book(room, guest, arrivalDate, stayLength, occupantNumber, creditCard)).thenReturn(confNum);
        when(room.getDescription()).thenReturn("Description");
        when(room.getId()).thenReturn(101);
        when(guest.getName()).thenReturn("Eric");
        when(creditCard.getVendor()).thenReturn(cardType.getVendor());
        when(creditCard.getNumber()).thenReturn(cardNum);
        
        assertTrue(control.state == State.CREDIT);
        
        //act
        control.creditDetailsEntered(cardType.VISA, 1,1);
        
        //assert
        verify(CreditCard).makeCreditCard(any(),anyInt(),anyInt());
        verify(authorizer).authorize(creditCard, cost);
        verify(hotel).book(room,guest,arrivalDate, stayLength, occupantNumber, creditCard);
        verify(bookingUI).displayConfirmedBooking(descCaptor.capture(), numCaptor.capture(), dateCaptor.capture(), stayCaptor.capture(), costCaptor.capture(), confNumCaptor.capture());
        verify(bookingUI).setState(uiStateCaptor.capture());
        
        assertEquals("Description", descCaptor.getValue());
        assertTrue(101 == numCaptor.getValue());
        assertEquals(arrivalDate, dateCaptor.getValue());
        assertTrue(1 == stayCaptor.getValue());
        assertEquals("Eric", nameCaptor.getValue());
        assertEquals("Visa", vendorCaptor.getValue());
        assertTrue(1 ==  cardNumCaptor.getValue());
        assertEquals(cost, costCaptor.getValue(), 0.001);
        assertTrue(confNum == confNumCaptor.getValue());
        assertTrue(BookingUI.State.COMPLETED == uiStateCaptor.getValue());
        assertTrue(State.COMPLETED == control.state);

    }
    @Test
    void testCreditDetailsEnteredCreditNotApproved() {
        //arrange
        control.state = State.CREDIT;
        control.guest = guest;
        control.room = room;
        control.cost = cost;
        control.arrivalDate = arrivalDate;
        control.stayLength = stayLength;
        control.occupantNumber = occupantNumber;
        
        ArgumentCaptor<String> mesgCaptor = ArgumentCaptor.forClass(String.class);
        when(creditCardHelper.makeCreditCard(any(),anyInt(),anyInt())).thenReturn(creditCard);
        when(authorizer.authorize(any(0, anyDouble())).thenReturn(false);
        when(creditCard.getVendor()).thenReturn(cardType.getVendor());
        when(creditCard.getNumber()).thenReturn(cardNum);
        String expectedMessage = String.format("\n%$ credit card number %d was not authorised for $%.2f\n", cardType.getVendor(), cardNum, cost);
        assertTrue(control.state == State.CREDIT);
        
        //act
        control.creditDetailsEntered(cardType.VISA, 1,1);
        
        //assert
        verify(CreditCard).makeCreditCard(any(),anyInt(),anyInt());
        verify(autrhorizer).authorize(creditCard, cost);
        verify(bookingUI).displayMessage(mesgCaptor.capture());
        assertEquals(expectedMessage, mesgCaptor.getValue());
        assertTrue(State.CREDIT == control.state);
        
    }
    
   @Test
   void testCreditDetailsEnteredControlNotInCreditState() {
       //arrange
       control.state = State.TIMES;
       control.guest = guest;
       control.room = room;
       control.cost = cost;
       control.arrivalDate = arrivalDate;
       control.stayLength = stayLength;
       control.occupantNumber = occupantNumber;
       
       String expectedMessage = String.format("BookingCTL: bookingTimesEntered : bad state : %s", State.values());
       
       assertTrue(control.state != State.CREDIT);
       
       //act
       Executable e = () -> control.creditDetailsEntered(cardType.VISA, 1,1);
       
       //assert
       Throwable t = assertThrows(RuntimeException.class, e);
       assertEquals(expectedMessage, t.getMessage());
       assertTrue(State.TIMES == control.state);
       
   }
    
    
}
