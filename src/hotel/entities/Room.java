package hotel.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hotel.credit.CreditCard;
import hotel.utils.IOUtils;

public class Room {
	
	private enum State {READY, OCCUPIED}
	
	private int id;
	private RoomType roomType;
	private List<Booking> bookings;
	private State state;
	private BookingHelper bookingHelper;

	
	public Room(int id, RoomType roomType) {
		this.id = id;
		this.roomType = roomType;
		bookings = new ArrayList<>();
		state = State.READY;
		bookingHelper = BookingHelper.getInstance();
	}
	

	public String toString() {
		return String.format("Room : %d, %s", id, roomType);
	}


	public int getId() {
		return id;
	}
	
	public String getDescription() {
		return roomType.getDescription();
	}
	
	
	public RoomType getType() {
		return roomType;
	}
	
	public boolean isAvailable(Date arrivalDate, int stayLength) {
		IOUtils.trace("Room: isAvailable");
		for (Booking b : bookings) {
			if (b.doTimesConflict(arrivalDate, stayLength)) {
				return false;
			}
		}
		return true;
	}
	
	
	public boolean isReady() {
		return state == State.READY;
	}
	
<<<<<<< HEAD
=======
	
>>>>>>> master
	public boolean isOccupied() {
		return state == State.OCCUPIED;
	}


	public Booking book(Guest guest, Date arrivalDate, int stayLength, int numberOfOccupants, CreditCard creditCard) {
		if (!isAvailable(arrivalDate, stayLength)) {
			throw new RuntimeException("Cannot create an overlapping booking");	
		}
		Booking booking = bookingHelper.makeBooking(guest, this, arrivalDate, stayLength, numberOfOccupants, creditCard);
		this.bookings.add(booking);
		return booking;		
	}


	public void checkin() {
		if (this.state != State.READY) {
			throw new RuntimeException("Cannot checkin to a room that is not READY");
		}
		this.state = State.OCCUPIED;
	}


	public void checkout(Booking booking) {
		if (this.state != State.OCCUPIED) {
			throw new RuntimeException("Cannot checkout of a room that is not OCCUPIED");
		}
		if (!bookings.contains(booking)) {
			throw new RuntimeException("Cannot checkout of a room that is not related to the booking");
		}
		this.bookings.remove(booking);
		this.state = State.READY;
	}
}
