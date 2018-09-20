package hotel.checkout;

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
import hotel.credit.CreditCardType;
import hotel.entities.Booking;
import hotel.entities.Room;
import hotel.entities.RoomType;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.ArrayList;
import java.util.Date;


@ExtendWith(MockitoExtension.class)
class TestCheckoutCTL {
	
	@Mock CreditCard creditCard;
	
	
	int roomId = 1;
	int number = 1;
	int ccv = 1;
	CreditCardType type = CreditCardType.VISA;
	

	@InjectMocks CheckoutCTL checkoutCTL;

	
	@BeforeEach
	void setUp() throws Exception {
	}

	
	@AfterEach
	void tearDown() throws Exception {
	}

	
	@Test
	void testCreditDetailsEnteredConflict() {
		//arrange
		checkoutCTL.creditDetailsEntered(type, number, ccv);
		assertFalse(checkoutCTL.isCredit());
		
		//act
		Executable e = () -> checkoutCTL.creditDetailsEntered(type, number, ccv);
		Throwable t = assertThrows(RuntimeException.class, e);
				
		//assert
		assertEquals("Bad State: state is not set to CREDIT", t.getMessage());
	}

}
