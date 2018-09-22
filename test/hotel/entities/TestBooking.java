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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@ExtendWith(MockitoExtension.class)
class TestBooking {
	
	Booking booking;
	//@Mock Room room;
	@Mock Guest guest;
	@Mock CreditCard creditCard;
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	//@Spy Map<Long, Booking> bookingsByConfirmationNumber = new HashMap<Long, Booking>();
    @Spy ArrayList<Booking> bookings;
	
    @InjectMocks Room room = new Room(99, RoomType.SINGLE);
    
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
		
		//room = new Room(101, RoomType.SINGLE);
		booking = new Booking(guest, room, date, 0, 0, creditCard);
		
	}

	
	@AfterEach
	void tearDown() throws Exception {
	}

	
	@Test
	void testCheckinTwiceShouldThrowException() {		
		//arrange		
		booking.checkIn();
				
		//act
		Executable executable = () -> booking.checkIn();		
				
		//assert
		assertThrows(RuntimeException.class, executable);
		
	}
	
	@Test
	void testSuccessfulCheckInShouldUpdateStateToOccupied() {	
		//arrange		
						
		//act
		booking.checkIn();		
			
		//assert
		assertTrue(room.isOccupied());
		assertTrue(booking.isCheckedIn());
	}
	
	@Test
	void testAddServiceChargeSuccessful() {		
		//arrange
		booking.checkIn();
		ServiceType barFridge = ServiceType.BAR_FRIDGE;
				
		//act
		booking.addServiceCharge(barFridge, 7.99);	
				
		//assert
		assertEquals(barFridge, booking.getCharges().get(0).getType());
	}
	
	@Test
	void testCheckOutWhenNotCheckedInRuntimeException() {		
		//arrange				
				
		//act
		Executable executable = () -> booking.checkOut();		
				
		//assert
		assertThrows(RuntimeException.class, executable);
		
	}
	
	@Test
	void testSuccessfulCheckOut() {	
		//arrange
		bookings.add(booking);
		booking.checkIn();				
		
		//act
		booking.checkOut();		
			
		//assert
		assertTrue(room.isReady());
		assertTrue(booking.isCheckedOut());
	}

}
