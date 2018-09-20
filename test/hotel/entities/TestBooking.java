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
import hotel.credit.CreditCard;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


@ExtendWith(MockitoExtension.class)
class TestBooking {
	
	Booking booking;
	Room room;
	@Mock Guest guest;
	@Mock CreditCard creditCard;
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

		
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Date date = format.parse("01-01-2018");
		
		room = new Room(101, RoomType.SINGLE);
		booking = new Booking(guest, room, date, 0, 0, creditCard);
	}

	
	@AfterEach
	void tearDown() throws Exception {
	}

	
	@Test
	void testCheckinRoomAvailable() {
		
		//arrange		
		assertTrue(booking.isPending());
		
		//act	
		booking.checkIn();
		
		//assert
		assertTrue(booking.isCheckedIn());
		
	}		


}
