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
import java.util.HashMap;
import java.util.Map;


@ExtendWith(MockitoExtension.class)
class TestHotel {

    @Mock Room mockRoom;
    @Mock Guest mockGuest;
    java.util.Date arrivalDate;
    int stayLength;
    int numberOfOccupants;
    @Mock CreditCard mockCard;
    @Spy Map<Long, Booking> bookingsByConfirmationNumber = new HashMap<Long, Booking>();
    SimpleDateFormat format;
    
    @Mock Booking mockBooking;
    long confirmationNumber;
    
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
        format = new SimpleDateFormat("dd-MM-yyyy");
        arrivalDate = format.parse("11-09-2001");
        stayLength = 1;
        numberOfOccupants = 1;
        confirmationNumber = 11092001105L;
        
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @InjectMocks Hotel hotel;
    
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

}
