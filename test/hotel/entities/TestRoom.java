package hotel.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.ArrayList;
import java.util.Date;


@ExtendWith(MockitoExtension.class)
class TestRoom {

	@Mock Booking booking;
	@Mock Booking newBooking;
	@Spy ArrayList<Booking> bookings;
	@Mock Guest guest;
	@Mock Date arrivalDate;
	@Mock CreditCard creditCard;
	@Mock BookingHelper bookingHelper;
	
	
	int roomId = 1;
	RoomType roomType = RoomType.SINGLE;
	int stayLength = 1;
	int numberOfOccupants = roomType.getMaxOccupancy();
	
	
	@InjectMocks Room room = new Room(1, RoomType.SINGLE);
	
	
	@BeforeEach
	void setUp() throws Exception {
	}

	
	@AfterEach
	void tearDown() throws Exception {
	}

	
	@Test
	void testBookingNoConflict() {
		//arrange
		bookings.add(booking);
		when(bookingHelper.makeBooking(guest, room, arrivalDate, stayLength, numberOfOccupants, creditCard)).thenReturn(newBooking);
		when(booking.doTimesConflict(any(Date.class), anyInt())).thenReturn(false);
		
		//act
		Booking actual = room.book(guest, arrivalDate, stayLength, numberOfOccupants, creditCard);
		
		//assert
		verify(booking).doTimesConflict(arrivalDate, stayLength);
		assertNotNull(actual);
		assertTrue(bookings.contains(actual));
	}
	
	
	@Test
	void testBookingWithConflict() {
		//arrange
		bookings.add(booking);
		when(booking.doTimesConflict(any(Date.class), anyInt())).thenReturn(true);
		
		//act
		Executable e = () -> room.book(guest, arrivalDate, stayLength, numberOfOccupants, creditCard);
		Throwable t = assertThrows(RuntimeException.class, e);
		
		//assert
		verify(booking).doTimesConflict(arrivalDate, stayLength);
		assertEquals("Cannot create an overlapping booking", t.getMessage());
	}
	
	
	@Test
	void testCheckinWhenOccupied() {
		//arrange
		room.checkin();
		assertTrue(room.isOccupied());
		
		//act
		Executable e = () -> room.checkin();
		Throwable t = assertThrows(RuntimeException.class, e);
		
		//assert
		assertEquals("Cannot checkin to a room that is not READY", t.getMessage());
	}
	
	
	
	
}
