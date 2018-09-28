package hotel.checkout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
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

import hotel.checkout.CheckoutCTL.State;
import hotel.credit.CreditAuthorizer;
import hotel.credit.CreditCard;
import hotel.credit.CreditCardHelper;
import hotel.credit.CreditCardType;
import hotel.entities.Booking;
import hotel.entities.Hotel;
import hotel.entities.Room;
import hotel.entities.RoomType;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



class TestCheckoutCTL {
	
	@Mock CheckoutUI checkoutUI;
	@Mock Hotel hotel;
	@Mock CreditAuthorizer authorizer;
	@Mock CreditCardHelper creditCardHelper;
	@Mock CreditCard creditCard;
	
	private double cost;
	double total;
	State state;
	int roomId;
	CreditCardType cardType;
	int number;
	int ccv;

	static SimpleDateFormat format;
	
	CheckoutCTL control;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		control = new CheckoutCTL(hotel);
		control.checkoutUI = checkoutUI;
		control.authorizer = authorizer;
		control.creditCardHelper = creditCardHelper;
		
		number = 1;
		roomId = 1;
		total = 111.11;
		cardType = CreditCardType.VISA;
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	//2nd attempt at testing creditDetailsEntered (1st below)
	@Test
	void creditDetailsEnteredControlNotInCreditState() {
		//arrange
		control.state = State.ACCEPT;
		control.total =total;
		control.roomId = roomId;
		
		String expectedMessage = String.format("Bad State: state is not set to CREDIT");
		
		assertTrue(control.state != State.CREDIT);
		
		//act
		Executable e = () -> control.creditDetailsEntered(cardType.VISA, 7, 1);
		
		//assert
		Throwable t= assertThrows(RuntimeException.class, e);
		assertEquals(expectedMessage, t.getMessage());
		assertTrue(State.ACCEPT == control.state);
	}

	
	@Test
	void testCreditDetailsEnteredCreditApproved() {
		//arrange
		control.state = State.CREDIT;
		control.total =total;
		control.roomId = roomId;
		
		ArgumentCaptor<String> cardChargedMessageCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> roomIdCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<CheckoutUI.State> uiStateCaptor = ArgumentCaptor.forClass(CheckoutUI.State.class);
		
		when(creditCardHelper.makeCreditCard(any(),anyInt(),anyInt())).thenReturn(creditCard);
		when(authorizer.authorize(any(), anyDouble())).thenReturn(true);
		hotel.checkout(anyInt());
		//when(checkoutUI.displayMessage(anyString())).thenReturn("Message");
		
		assertTrue(control.state == State.CREDIT);
		
		//act
		control.creditDetailsEntered(cardType.VISA, 1, 1);
		
		//assert
		verify(creditCardHelper).makeCreditCard(any(),anyInt(),anyInt());
		verify(authorizer).authorize(creditCard, total);
		
		assertEquals("Credit card was successfully charged", cardChargedMessageCaptor.getValue());
		assertTrue(1 == roomIdCaptor.getValue());
		
		assertTrue(CheckoutUI.State.COMPLETED == uiStateCaptor.getValue());
		assertTrue(State.COMPLETED == control.state);
	}

	
	@Test
	void testCreditDetailsEnteredCreditNotApproved() {
		//arrange
		control.state = State.CREDIT;
		control.total =total;
		control.roomId = roomId;
		
		ArgumentCaptor<String> mesgCaptor = ArgumentCaptor.forClass(String.class);
		
		when(creditCardHelper.makeCreditCard(any(),anyInt(),anyInt())).thenReturn(creditCard);
		when(authorizer.authorize(any(), anyDouble())).thenReturn(false);
		
		String expectedMessage = "Credit card was not authorized";
		assertTrue(control.state == State.CREDIT);
		
		//act
		control.creditDetailsEntered(cardType, number, ccv);
		
		//assert
		verify(creditCardHelper).makeCreditCard(any(),anyInt(),anyInt());
		verify(authorizer).authorize(creditCard, total);
		verify(checkoutUI).displayMessage(mesgCaptor.capture());
		assertEquals(expectedMessage, mesgCaptor.getValue());
		assertTrue(State.CREDIT == control.state);


	}
	
	//1st attempt at testing creditDetailsEntered
	
	CreditCardType type = CreditCardType.VISA;
	

	@InjectMocks CheckoutCTL checkoutCTL;


	
	@Test
	void testCreditDetailsEnteredConflict() {
		//arrange
		checkoutCTL.state = State.ROOM;
		checkoutCTL.creditDetailsEntered(type, number, ccv);
		assertFalse(State.CREDIT == checkoutCTL.state);
		
		//act
		Executable e = () -> checkoutCTL.creditDetailsEntered(type, number, ccv);
		Throwable t = assertThrows(RuntimeException.class, e);
				
		//assert
		assertEquals("Bad State: state is not set to CREDIT", t.getMessage());
	}

	
	@Test
	void testCreditDetailsEnteredCardNotAuthorized() {
		//arrange
		checkoutCTL.state = State.CREDIT;
		checkoutCTL.creditDetailsEntered(type, number, ccv);
		assertTrue(State.CREDIT == checkoutCTL.state);
		
		String expectedMessage = "Credit card was not authorized";
		
		//act
		when(authorizer.authorize(creditCard, total)).thenReturn(false);
		
		//assert
		assertEquals("Credit card was not authorized", checkoutUI.getMessage());
	}
	
	
	@Test 
	void testCreditDetailsEnteredAuthorized() {
		//arrange
		checkoutCTL.state = State.CREDIT;
		checkoutCTL.creditDetailsEntered(type, number, ccv);
		assertTrue(State.CREDIT == checkoutCTL.state);
		
		String expectedMessage = "Credit card was successfully charged";
		
		//act
		when(authorizer.authorize(creditCard, total)).thenReturn(true);
		when(hotel.checkout(anyInt()));
		
		//assert
		assertEquals("Credit card was successfully charged", checkoutUI.getMessage());
		assertTrue(State.COMPLETED == checkoutCTL.state);
	}
}
