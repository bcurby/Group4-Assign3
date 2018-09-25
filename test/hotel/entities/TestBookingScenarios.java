package hotel.entities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import hotel.entities.Hotel;
import hotel.entities.hotelHelper;
import hotel.HotelHelper;
import hotel.booking.BookingCTL;
import hotel.booking.BookingUI;

class TestBookingScenarios {

    Hotel hotel;
    @Mock BookingUI ui;
    BookingCTL control;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        hotel = HotelHelper.loadHotel();
        control = new BookingCTL(hotel);
        
        
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void testExistingGuestBooksAvailRoom() {
        //arrange
        int phone = 333;
        
        //act
        control.phoneNumberEntered(phone);
        
        //assert
        verify(ui).displayGuestDetails(any(), any(),anyInt())
        
        
    }

}
