package hotel.checkout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.HotelHelper;
import hotel.checkout.CheckoutCTL.State;
import hotel.credit.CreditAuthorizer;
import hotel.credit.CreditCard;
import hotel.credit.CreditCardHelper;
import hotel.credit.CreditCardType;
import hotel.entities.Booking;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.Room;
import hotel.entities.RoomType;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class TestCheckoutScenarios {
	
	@Mock CheckoutUI ui;
	@Mock Room room;
	@Mock Guest guest;
	@Mock CreditAuthorizer authorizer;
	@Mock CreditCardHelper creditCardHelper;
	@Mock CreditCard creditCard;
	
	Hotel hotel;
	CheckoutCTL control;
	
	double total;
	State state;
	int roomId;
	CreditCardType type;
	int number;
	int ccv;
	int wrongRoomId;
	
	Date date;
	static SimpleDateFormat format;
	
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		format = new SimpleDateFormat("dd-MM-yyyy");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		hotel = HotelHelper.loadHotel();
		control = new CheckoutCTL(hotel);
		control.checkoutUI = ui;
		
		date = format.parse("08-09-2012");
		
		control.authorizer = authorizer;
		control.creditCardHelper = creditCardHelper;
		
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testExistingGuestChecksOutOfTheirRoom() {
		//arrange
		number = 1;
		roomId = 1;
		total = 111.11;
		type = CreditCardType.VISA;
		
		//act
		control.roomIdEntered(roomId);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.ACCEPT);
		
		control.chargesAccepted(true);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.CREDIT);

		control.creditDetailsEntered(type, number, ccv);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.COMPLETED);
	}
	
	
	@Test
	void testExistingGuestChecksOutOfTheirRoomAfterEnteringWrongRoomIdFirst() {
		//arrange
		number = 1;
		roomId = 1;
		wrongRoomId = 2;
		total = 111.11;
		type = CreditCardType.VISA;
		
		//act
		control.roomIdEntered(wrongRoomId);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.ROOM);
		
		control.roomIdEntered(roomId);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.ACCEPT);
		
		control.chargesAccepted(true);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.CREDIT);

		control.creditDetailsEntered(type, number, ccv);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.COMPLETED);
	}
	
	
	@Test
	void testExistingGuestWhenChargesNotAccepted() {
		//arrange
		number = 1;
		roomId = 1;
		total = 111.11;
		type = CreditCardType.VISA;
		
		//act
		control.roomIdEntered(roomId);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.ACCEPT);
				
		control.chargesAccepted(false);
		verify(ui).displayMessage(anyString());
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.CANCELLED);
		
	}
	
	
	@Test
	void testExistingGuestWhenFirstCreditCardNotAuthorizedButSecondCreditCardWorks() {
		//arrange
		number = 1;
		roomId = 1;
		total = 111.11;
		type = CreditCardType.VISA;
		
		//act
		control.roomIdEntered(roomId);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.ACCEPT);
				
		control.chargesAccepted(true);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.CREDIT);

		control.creditDetailsEntered(type, 7, ccv);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.CREDIT);
		
		control.creditDetailsEntered(type, number, ccv);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.COMPLETED);
	}
	
	
	@Test
	void testExistingGuestWhenCreditCardNotAuthorizedThenCheckoutCancelled() {
		//arrange
		number = 1;
		roomId = 1;
		total = 111.11;
		type = CreditCardType.VISA;
		
		//act
		control.roomIdEntered(roomId);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.ACCEPT);
				
		control.chargesAccepted(true);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.CREDIT);

		control.creditDetailsEntered(null, number, ccv);
		verify(ui).displayMessage(anyString());
		verify(ui, times(1)).setState(any());
		assertTrue(control.state == CheckoutCTL.State.CANCELLED);
	}
}
