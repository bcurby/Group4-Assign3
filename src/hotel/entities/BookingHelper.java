package hotel.entities;

import java.util.Date;

import hotel.credit.CreditCard;

public class BookingHelper {
	
	public Booking  makeBooking(Guest guest, Room room, Date arrivalDate,
			int stayLength, int numberOfOccupants, CreditCard creditCard) {
		
		return new Booking(guest, room, arrivalDate, stayLength, numberOfOccupants, creditCard);
	}
}
