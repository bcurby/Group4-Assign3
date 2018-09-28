package hotel.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
}
