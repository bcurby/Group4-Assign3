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
	void testBooking() {
		//arrange
		when(bookingHelper.makeBooking(guest, room, arrivalDate, stayLength, numberOfOccupants, creditCard)).thenReturn(booking);
		//act
		Booking actual = room.book(guest, arrivalDate, stayLength, numberOfOccupants, creditCard);
		//assert
		assertNotNull(actual);
		assertTrue(bookings.contains(actual));
	}

}
