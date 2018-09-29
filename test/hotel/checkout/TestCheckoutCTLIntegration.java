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
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



class TestCheckoutCTLIntegration {
	
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
	
	
	@Test
	void testCreditDetailsEnteredCreditNotApprovedWithRealCreditCard() {
		//arrange
		control.state = State.CREDIT;
		control.total =total;
		control.roomId = roomId;
		
		ArgumentCaptor<String> mesgCaptor = ArgumentCaptor.forClass(String.class);
		
		//when(creditCardHelper.makeCreditCard(any(),anyInt(),anyInt())).thenReturn(creditCard);
		creditCardHelper = new CreditCardHelper();
		control.creditCardHelper = creditCardHelper;
		
		//when(authorizer.authorize(any(), anyDouble())).thenReturn(false);
		authorizer = CreditAuthorizer.getInstance();
		control.authorizer = authorizer;
		
		String expectedMessage = "Credit card was not authorized";
		assertTrue(control.state == State.CREDIT);
		number = 9;
		
		//act
		control.creditDetailsEntered(cardType, number, ccv);
		
		//assert
		//verify(creditCardHelper).makeCreditCard(any(),anyInt(),anyInt());
		//verify(authorizer).authorize(creditCard, total);
		verify(checkoutUI).displayMessage(mesgCaptor.capture());
		assertEquals(expectedMessage, mesgCaptor.getValue());
		assertTrue(State.CREDIT == control.state);
	}

	
	@Test
	void testCreditDetailsEnteredCreditApprovedWithRealCreditCard() {
		//arrange
		control.state = State.CREDIT;
		control.total =total;
		control.roomId = roomId;
		
		ArgumentCaptor<String> cardChargedMessageCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> roomIdCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<CheckoutUI.State> uiStateCaptor = ArgumentCaptor.forClass(CheckoutUI.State.class);
		
		//when(creditCardHelper.makeCreditCard(any(),anyInt(),anyInt())).thenReturn(creditCard);
		creditCardHelper = new CreditCardHelper();
		control.creditCardHelper = creditCardHelper;
		
		//when(authorizer.authorize(any(), anyDouble())).thenReturn(true);
		authorizer = CreditAuthorizer.getInstance();
		control.authorizer = authorizer;
		
		hotel.checkout(anyInt());
		//when(checkoutUI.displayMessage(anyString())).thenReturn("Message");
		
		assertTrue(control.state == State.CREDIT);
		
		//act
		control.creditDetailsEntered(cardType.VISA, 1, 1);
		
		//assert
		//verify(creditCardHelper).makeCreditCard(any(),anyInt(),anyInt());
		//verify(authorizer).authorize(creditCard, total);
		
		assertEquals("Credit card was successfully charged", cardChargedMessageCaptor.getValue());
		assertTrue(1 == roomIdCaptor.getValue());
		
		assertTrue(CheckoutUI.State.COMPLETED == uiStateCaptor.getValue());
		assertTrue(State.COMPLETED == control.state);
	}
	
	
	@Test
	void testCreditDetailsEnteredCreditApprovedWithRealCreditCardandRealRoomId() {
		//arrange
		control.state = State.CREDIT;
		control.total =total;
		control.roomId = roomId;
		roomId = 25;
				
		ArgumentCaptor<String> cardChargedMessageCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> roomIdCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<CheckoutUI.State> uiStateCaptor = ArgumentCaptor.forClass(CheckoutUI.State.class);
		
		//when(creditCardHelper.makeCreditCard(any(),anyInt(),anyInt())).thenReturn(creditCard);
		creditCardHelper = new CreditCardHelper();
		control.creditCardHelper = creditCardHelper;
		
		//when(authorizer.authorize(any(), anyDouble())).thenReturn(true);
		authorizer = CreditAuthorizer.getInstance();
		control.authorizer = authorizer;
		
		hotel.checkout(roomId);
		//when(checkoutUI.displayMessage(anyString())).thenReturn("Message");
		
		assertTrue(control.state == State.CREDIT);
		
		//act
		control.creditDetailsEntered(cardType.VISA, 1, 1);
		
		//assert
		//verify(creditCardHelper).makeCreditCard(any(),anyInt(),anyInt());
		//verify(authorizer).authorize(creditCard, total);
		
		assertEquals("Credit card was successfully charged", cardChargedMessageCaptor.getValue());
		assertTrue(25 == roomIdCaptor.getValue());
		
		assertTrue(CheckoutUI.State.COMPLETED == uiStateCaptor.getValue());
		assertTrue(State.COMPLETED == control.state);
	}
	
	
	@Test
	void testCreditDetailsEnteredCreditApprovedWithEverythingReal() {
		//arrange
		control.state = State.CREDIT;
		control.total =total;
		control.roomId = roomId;
		roomId = 25;
		
		hotel = new Hotel();
		control.hotel = hotel;
		
		ArgumentCaptor<String> cardChargedMessageCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> roomIdCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<CheckoutUI.State> uiStateCaptor = ArgumentCaptor.forClass(CheckoutUI.State.class);
		
		//when(creditCardHelper.makeCreditCard(any(),anyInt(),anyInt())).thenReturn(creditCard);
		creditCardHelper = new CreditCardHelper();
		control.creditCardHelper = creditCardHelper;
		
		//when(authorizer.authorize(any(), anyDouble())).thenReturn(true);
		authorizer = CreditAuthorizer.getInstance();
		control.authorizer = authorizer;
		
		hotel.checkout(roomId);
		//when(checkoutUI.displayMessage(anyString())).thenReturn("Message");
		
		assertTrue(control.state == State.CREDIT);
		
		//act
		control.creditDetailsEntered(cardType.VISA, 1, 1);
		
		//assert
		//verify(creditCardHelper).makeCreditCard(any(),anyInt(),anyInt());
		//verify(authorizer).authorize(creditCard, total);
		
		assertEquals("Credit card was successfully charged", cardChargedMessageCaptor.getValue());
		assertTrue(25 == roomIdCaptor.getValue());
		
		assertTrue(CheckoutUI.State.COMPLETED == uiStateCaptor.getValue());
		assertTrue(State.COMPLETED == control.state);
	}
}
