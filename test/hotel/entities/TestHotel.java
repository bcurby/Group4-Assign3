package hotel.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.credit.CreditCard;

import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.verify;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@ExtendWith(MockitoExtension.class)
class TestHotel {


    java.util.Date arrivalDate;
    int stayLength;
    int numberOfOccupants;
    int roomId;
    long confirmationNumber;
    private Object serviceType;
    SimpleDateFormat format;
    RoomType roomType = RoomType.SINGLE;
    
    @Mock CreditCard mockCard;
    @Mock Booking mockBooking;
    @Mock ServiceCharge mockService;
    @Mock Room mockRoom;
    @Mock Guest mockGuest;
    
    @Spy Map<Long, Booking> bookingsByConfirmationNumber = new HashMap<Long, Booking>();
    @Spy ArrayList<Booking> bookings;
     
    @InjectMocks Hotel hotel;
    @InjectMocks Room room = new Room(99, RoomType.SINGLE);

   
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
        format = new SimpleDateFormat("dd-MM-yyyy");
        arrivalDate = format.parse("11-09-2011");
        stayLength = 1;
        roomId = 99;
        numberOfOccupants = 1;
        confirmationNumber = 11092001105L;
        Object serviceType;
    }

    @AfterEach
    void tearDown() throws Exception {
    }


    
    @Test
    void testBookAllValid() {
       //arrange
        when(mockRoom.book(mockGuest, arrivalDate, stayLength, numberOfOccupants, mockCard)).thenReturn(mockBooking);
        when(mockBooking.getConfirmationNumber()).thenReturn(confirmationNumber);
        assertEquals(0, bookingsByConfirmationNumber.size());
      
       
       //act
        long actual= hotel.book(mockRoom, mockGuest, arrivalDate, stayLength, numberOfOccupants, mockCard);
        
       //assert
        verify(mockRoom).book(mockGuest, arrivalDate, stayLength, numberOfOccupants, mockCard);
        assertEquals(confirmationNumber, actual);
        assertEquals(1, bookingsByConfirmationNumber.size());
        assertEquals(mockBooking, hotel.findBookingByConfirmationNumber(actual));

    }
    @Test
    void testBookRoomThrowsException() {
        //arrange
        when(mockRoom.book(mockGuest, arrivalDate, stayLength, numberOfOccupants, mockCard)).thenThrow(new RuntimeException("Error"));
        assertEquals(0, bookingsByConfirmationNumber.size());
      
        
       //act
        Executable e = () -> hotel.book(mockRoom, mockGuest, arrivalDate, stayLength, numberOfOccupants, mockCard);
        Throwable t =assertThrows(RuntimeException.class,e);;
        
        
       //assert
        verify(mockRoom).book(mockGuest, arrivalDate, stayLength, numberOfOccupants, mockCard);
        assertEquals(0, bookingsByConfirmationNumber.size());
        assertEquals("Error", t.getMessage());

    }
    @Test
    void testAddServiceChargeWithValidID() {
        // arrange
        when(mockRoom.book(mockGuest, arrivalDate, stayLength, numberOfOccupants, mockCard)).thenReturn(mockBooking);
        
        Object cost;
        when(mockService.book(serviceType, cost)).thenReturn (mockService);
        int roomID=99;
        hotel.activeBookingsByRoomId.put(99,  mockBooking);
             //when(mockRoom.book(mockGuest, arrivalDate, stayLength, numberOfOccupants, mockCard)).thenReturn(mockBooking);
        
        //act
        hotel.addServiceCharge(roomID, ServiceType.BAR_FRIDGE, 25);
        
        //assert
        verify(mockRoom).book(mockGuest, arrivalDate, stayLength, numberOfOccupants, mockCard);

        assertEquals(mockBooking, hotel.findBookingByConfirmationNumber(confirmationNumber));
        verify(hotel).book(any(Room.class), any(Guest.class), any(Date.class), anyInt(), anyInt(), any(CreditCard.class));
    }

    @Test
    void testCheckout() {
        //arrange
        bookings.add(mockBooking);
        room.checkin();
        assertEquals(1, bookings.size());
        assertTrue(room.isOccupied());
        
        //act
        room.checkout(mockBooking);
        
        //assert
        verify(bookings).remove(mockBooking);
        assertTrue(room.isReady());
        assertEquals(0, bookings.size());
    }
    
}
