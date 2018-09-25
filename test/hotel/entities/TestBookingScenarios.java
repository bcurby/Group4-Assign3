package hotel.entities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import hotel.entities.Hotel;
import hotel.entities.*;
import hotel.HotelHelper;
import hotel.booking.BookingCTL;
import hotel.booking.BookingUI;
import hotel.credit.CreditCardType;

class TestBookingScenarios {

    Hotel hotel;
    @Mock BookingUI ui;
    BookingCTL control;
    static SimpleDateFormat format;
    Date date;
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
        
        hotel = HotelHelper.loadHotel();
        control = new BookingCTL(hotel);
        control.bookingUI = ui;
        date = format.parse("10-10-2008");
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void testExistingGuestBooksAvailRoom() throws Exception{
        //arrange
        int phone = 333;
        RoomType roomType = RoomType.SINGLE;
        int occupantNumber = 1;
        int stayLength = 1;
        CreditCardType cardType = CreditCardType.VISA;
        int cardNum = 4;
        int ccv = 4;
        
        
        
        //act
        control.phoneNumberEntered(phone);
        verify(ui).displayGuestDetails(any(), any(),anyInt());
        verify(ui, times(1)).setState(any());
        assertTrue(control.State == BookingCTL.State.ROOM);
        
        control.roomTypeAndOccupantsEntered(roomType, occupantNumber);
        verify(ui, times(2)).setState(any());
        assertTrue(control.State == BookingCTL.State.TIMES);
        
        control.bookingTimesEntered(date, stayLength);
        control.creditDetailsEntered(cardType, cardNum, ccv);
        
        
        //assert
        
       
        verify(ui).displayBookingDetails(any(), any(),anyInt(), anyDouble())  ;
        verify(ui).displayConfirmedBooking(any(), anyInt(), any(), anyInt(), any(), any(), anyInt(), anyDouble(), anyLong());
        verify(ui).times(4).setState(any());
        
        
        
    }

}
