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
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


import hotel.RoomHelper;
import hotel.checkin.CheckinCTL;
import hotel.checkin.CheckinUI;
import hotel.credit.CreditCard;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@ExtendWith(MockitoExtension.class)
class TestCheckInCTL {
	
	java.util.Date arrivalDate;
	SimpleDateFormat format;
	
	@Mock CheckinUI checkInUI;
	@Mock Guest guest;
	@Mock CreditCard creditCard;
	@Mock Booking booking;
	@Mock Room room;
	@Mock Hotel hotel;
	@InjectMocks CheckinCTL checkinCTL;

    
    //@InjectMocks Room room = new Room(99, RoomType.SINGLE);    
    

//	@Test
//	void testCheckInConfirmedShouldThrowException() {		
//		//arrange		
//		
//		
//		//act
//		Executable executable = () -> checkinCTL.checkInConfirmed(true);		
//				
//		//assert
//		assertThrows(RuntimeException.class, executable);
//		
//	}
	
	@BeforeEach
    void setUp() throws Exception {
        format = new SimpleDateFormat("dd-MM-yyyy");
        arrivalDate = format.parse("11-09-2011");
    }
	
	
	@Test
	void testCheckinCTLConfirmed() {		
		//arrange
		
		when(hotel.findBookingByConfirmationNumber(1234567)).thenReturn(booking);
		when(booking.isPending()).thenReturn(true);
		when(booking.getRoom()).thenReturn(room);
		when(booking.getGuest()).thenReturn(guest);
		when(booking.getCreditCard()).thenReturn(creditCard);
		when(booking.getArrivalDate()).thenReturn(arrivalDate);
		when(room.isReady()).thenReturn(true);
		/*when(checkInUI.displayCheckinMessage("test", 123, format.parse("21-09-2018"), 2, "John Smith", 
				"Visa", 12345, 1234567)).thenReturn(true);*/
		checkinCTL.confirmationNumberEntered(1234567);
		
		//act
		checkinCTL.checkInConfirmed(true);		
				
		//assert
		verify(hotel).checkin(any());		
	}
	
	
//	@Test
//	void testCheckinCTLCancelled() {		
//		//arrange		
//		
//		
//		//act
//		Executable executable = () -> checkinCTL.checkInConfirmed(true);		
//				
//		//assert
//		assertThrows(RuntimeException.class, executable);
//		
//	}
	
}
