package hotel.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hotel.credit.CreditCard;

class TestRoomBookingIntegration {
	
	Booking booking;
	Room room;
	@Mock Guest guest;
	@Mock CreditCard creditCard;
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Date date = format.parse("03-03-2012");
		
		room = new Room(85, RoomType.SINGLE);
		booking = new Booking(guest, room, date, 3, 3, creditCard);
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
		booking.checkIn();
		
		//assert
		assertTrue(booking.isCheckedIn());
		assertFalse(room.isReady());	
	}
	
	
	@Test
	void testCheckinRoomOccupied() {
		//arrange
		room.checkin();
		assertTrue(booking.isPending());
		assertFalse(room.isReady());
		
		//act
		Executable e = () -> booking.checkIn();
		
		//assert
		Throwable t = assertThrows(RuntimeException.class, e);
		assertEquals("Cannot checkin to a room that is not READY", t.getMessage());
	}
	
	
	@Test
	void testCheckinBookingNotPending() {
		//arrange
		booking.checkIn();
		assertFalse(booking.isPending());
		assertFalse(room.isReady());
		
		//act
		Executable e = () -> booking.checkIn();

		//assert
		Throwable t = assertThrows(RuntimeException.class, e);
		assertEquals("cannot checkin when state pending", t.getMessage());

	}
}
