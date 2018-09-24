package hotel.entities;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.text.SimpleDateFormat;
import java.util.Date;
import hotel.booking.BookingCTL;
import hotel.booking.BookingUI;
import hotel.booking.BookingUI.State;
import hotel.credit.CreditAuthorizer;
import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;


class TestBookingCytrlIntegration {
    
    @Mock BookingUI bookingUI;
    @Mock Hotel hotel;
    @Mock CreditAuthorizer authorizer;
    @Mock CreditCard CreditCardHelper;
    @Mock Guest guest;
    @Mock CreditCard creditCard;
    
    private double cost;
    private State state;
    int phoneNumber;
    RoomType selectedRoomType;
    int occupantNumber;
    Date arrivalDate;
    int stayLength;
    int cardNum;
    CreditCardType cardType;
    long confNum;
    Room room;
    int ccv;
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
        control.creditCardHelper = CreditCardHelper;
        
        arrivalDate = format.parse("11-09-2001");
        stayLength = 1;
        occupantNumber = 1;
        cardNum = 4;
        cardType = CreditCardType.VISA;
        cost = 111.11;
        confNum = 11092001101L;
        
        
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    void testCreditDetailsEnteredControlNotApprovedWithRealCreditCard() {
        //arrange
        control.state = State.CREDIT;
        control.guest = guest;
        control.room = room;
        control.cost = cost;
        control.arrivalDate = arrivalDate;
        control.stayLength = stayLength;
        control.occupantNumber = occupantNumber;
        CreditCardHelper = new CreditCardHelper();
        control.creditCardHelper = CreditCardHelper;
        authorizer = CreditAuthorizer.getInstance();
        control.authorizer = authorizer;
        cardNum =4;
        
        
        String expectedMessage = String.format("\n%$ credit card number %d was not authorised for $%.2f\n", cardType.getVendor(), cardNum, cost);
        assertTrue(control.state == State.CREDIT);
        ArgumentCaptor<String> mesgCaptor = ArgumentCaptor.forClass(String.class);
        
        //act
        control.creditDetailsEntered(cardType, cardNum,ccv);
        
        //assert
        verify(bookingUI).displayMessage(mesgCaptor.capture());
        assertEquals(expectedMessage, mesgCaptor.getValue());
        assertTrue(State.CREDIT == control.state);
       
        
    }
    
    @Test
    void testCreditDetailsEnteredCreditApprovedWithRealCreditCard() {
        //arrange
        control.state = State.CREDIT;
        control.guest = guest;
        control.room = room;
        control.cost = cost;
        control.arrivalDate = arrivalDate;
        control.stayLength = stayLength;
        control.occupantNumber = occupantNumber;
        control.creditCardHelper = new CreditCardHelper();
        control.authorizer = CreditAuthorizer.getInstance();
        
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
        
       
        when(hotel.book(any(), any(), any(), anyInt(), anyInt(), any(CreditCard.class))).thenReturn(confNum);
        when(room.getDescription()).thenReturn("Description");
        when(room.getId()).thenReturn(101);
        when(guest.getName()).thenReturn("Herman");
       
        assertTrue(control.state == State.CREDIT);
        
        //act
        control.creditDetailsEntered(cardType.VISA, 4,4);
        
        //assert
      
        verify(hotel.book(any(), any(), any(), anyInt(), anyInt(), any(CreditCard.class)));
        verify(bookingUI).displayConfirmedBooking(descCaptor.capture(), numCaptor.capture(),
                dateCaptor.capture(), stayCaptor.capture(), costCaptor.capture(), 
                confNumCaptor.capture());
        verify(bookingUI).setState(uiStateCaptor.capture());
        
        assertEquals("Description", descCaptor.getValue());
        assertTrue(101 == numCaptor.getValue());
        assertEquals(arrivalDate, dateCaptor.getValue());
        assertTrue(1 == stayCaptor.getValue());
        assertEquals("Herman", nameCaptor.getValue());
        assertEquals("Visa", vendorCaptor.getValue());
        assertTrue(4 ==  cardNumCaptor.getValue());
        assertEquals(cost, costCaptor.getValue(), 0.001);
        assertTrue(confNum == confNumCaptor.getValue());
        assertTrue(BookingUI.State.COMPLETED == uiStateCaptor.getValue());
        assertTrue(State.COMPLETED == control.state);

    }
    @Test
    void testCreditDetailsApprovedCreditRealCardAndGuestAndRoom() {
        //arrange
        control.state = State.CREDIT;
        control.arrivalDate = arrivalDate;
        control.stayLength = stayLength;
        control.cost = cost;
        control.occupantNumber = occupantNumber;
        CreditCardType cardType = CreditCardType.VISA;
        control.creditCardHelper = new CreditCardHelper();
        control.authorizer = CreditAuthorizer.getInstance();
        int roomNum = 101;
        RoomType roomType = RoomType.SINGLE;
        control.room = room;
        String name = "Herman";
        String address = "1313 Mockingbird Lane";
        int phonenum = 333;
        guest = new Guest(name, address, phonenum);
        control.guest = guest;
        
        ArgumentCaptor<String> descCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> numCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Integer> stayCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> vendorCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> cardNumCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Double> costCaptor = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Long> confNumCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<BookingUI.State> uiStateCaptor = ArgumentCaptor.forClass(BookingUI.State.class);
        
        when(hotel.book(any(), any(), any(), anyInt(), anyInt(), any(CreditCard.class))).thenReturn(confNum);
        assertTrue(control.State == state.CREDIT);
        
        //act
        control.creditDetailsEntered(cardType, cardNum, ccv);
      
        //assert
       verify(hotel.book(any(Room.class), any(Guest.class), any(), anyInt(), anyInt(), any(CreditCard.class)));
       verify(bookingUI).displayConfirmedBooking(descCaptor.capture(), numCaptor.capture(), dateCaptor.capture(), stayCaptor.capture(), nameCaptor.capture(), vendorCaptor.capture(), cardNumCaptor.capture(), costCaptor.capture(), confNumCaptor.capture());
       verify(bookingUI).setState(uiStateCaptor.capture());
       assertEquals(roomType.getDescription(), descCaptor.getValue());
       System.out.println(room.getID);
       System.out.println(numCaptor.capture());
       assertTrue(room.getId() == numCaptor.getValue());
       assertEquals(arrivalDate, dateCaptor.getValue());
       assertTrue(stayLength == stayCaptor.getValue());
       assertEquals(name, nameCaptor.getValue());
       assertEquals(cardType.getVendor(), vendorCaptor.getValue());
       assertTrue(cardNum == cardNumCaptor.getValue());
       assertEquals(cost, costCaptor.getValue(), 0.001);
       assertTrue(confNum == confNumCaptor.getValue());
       assertTrue(BookingUI.State.COMPLETED == uiStateCaptor.getValue());
       assertTrue(State.COMPLETED == control.State);
       
        
    }
    
    @Test
    void testCreditDetailsApprovedEverythingReal() {
        //arrange
        control.state = State.CREDIT;
        control.arrivalDate = arrivalDate;
        control.stayLength = stayLength;
        control.cost = cost;
        control.occupantNumber = occupantNumber;
        CreditCardType cardType = CreditCardType.VISA;
        control.creditCardHelper = new CreditCardHelper();
        control.authorizer = CreditAuthorizer.getInstance();
        hotel = new Hotel();
        control.hotel = hotel;
        
        int roomNum = 101;
        RoomType roomType = RoomType.SINGLE;
        control.room = room;
        String name = "Herman";
        String address = "1313 Mockingbird Lane";
        int phonenum = 333;
        guest = new Guest(name, address, phonenum);
        control.guest = guest;
        
        ArgumentCaptor<String> descCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> numCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Integer> stayCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> vendorCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> cardNumCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Double> costCaptor = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Long> confNumCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<BookingUI.State> uiStateCaptor = ArgumentCaptor.forClass(BookingUI.State.class);
        
        
        assertTrue(control.State == state.CREDIT);
        
        //act
        control.creditDetailsEntered(cardType, cardNum, ccv);
      
        //assert
       verify(bookingUI).displayConfirmedBooking(descCaptor.capture(), numCaptor.capture(), 
               dateCaptor.capture(), stayCaptor.capture(), nameCaptor.capture(), vendorCaptor.capture(), 
               cardNumCaptor.capture(), costCaptor.capture(), confNumCaptor.capture());
       verify(bookingUI).setState(uiStateCaptor.capture());
       assertEquals(roomType.getDescription(), descCaptor.getValue());
       System.out.println(room.getId);
       System.out.println(numCaptor.capture());
       assertTrue(room.getId() == numCaptor.getValue());
       assertEquals(arrivalDate, dateCaptor.getValue());
       assertTrue(stayLength == stayCaptor.getValue());
       assertEquals(name, nameCaptor.getValue());
       assertEquals(cardType.getVendor(), vendorCaptor.getValue());
       assertTrue(cardNum == cardNumCaptor.getValue());
       assertEquals(cost, costCaptor.getValue(), 0.001);
       System.out.println(confNumCaptor.getValue());
       assertTrue(confNum == confNumCaptor.getValue());
       assertTrue(BookingUI.State.COMPLETED == uiStateCaptor.getValue());
       assertTrue(State.COMPLETED == control.State);
       
    
       assertNotNull(hotel.findBookingByConfirmationNumber(confNum));
    }
    
    
    
}
