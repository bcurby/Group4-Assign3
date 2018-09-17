package hotel.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

class TestBooking {
	
	@Mock Booking booking;
	@Mock Booking newBooking;
	@Spy ArrayList<Booking> bookings;
	@Mock Guest guest;
	@Mock Date arrivalDate;
	@Mock CreditCard creditCard;
	//@Mock BookingHelper bookingHelper;
	
	
	int roomId = 1;
	RoomType roomType = RoomType.SINGLE;
	int stayLength = 1;
	int numberOfOccupants = roomType.getMaxOccupancy();
	
	
	@InjectMocks Room room = new Room(1, RoomType.SINGLE);

	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	
	@BeforeEach
	void setUp() throws Exception {
	}

	
	@AfterEach
	void tearDown() throws Exception {
	}

	
	@Test
	void testCheckinRoomAvailable() {
	
		
		//arrange
		
		assertTrue(booking.isPending());
		assertTrue(room.isReady());
		
		//act
		Executable e = () -> booking.checkIn();
		
		//assert
		assertTrue(booking.isCheckedIn());
		assertFalse(room.isReady());
	}

}
