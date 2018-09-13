package hotel.entities;

import java.util.Date;

import hotel.credit.CreditCard;

public class BookingHelper {
	
	private static BookingHelper self;
	
	public static BookingHelper getInstance() {
		if (self == null) {
			self = new BookingHelper();
		}
		return self;
	}
	
	public Booking  makeBooking(Guest guest, Room room, Date arrivalDate,
			int stayLength, int numberOfOccupants, CreditCard creditCard) {
		
		return new Booking(guest, room, arrivalDate, stayLength, numberOfOccupants, creditCard);
	}
}
