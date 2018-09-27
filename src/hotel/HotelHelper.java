package hotel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;
import hotel.entities.Booking;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.Room;
import hotel.entities.RoomType;
import hotel.entities.ServiceType;
import hotel.utils.IOUtils;

public class HotelHelper {

	public static Hotel loadHotel() throws Exception {
		
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		//Date date = format.parse("01-01-2001");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date date = format.parse("01-01-0001");
		cal.setTime(date);
		
		
		Hotel hotel = new Hotel();
		hotel.addRoom(RoomType.SINGLE, 101);
		hotel.addRoom(RoomType.DOUBLE, 201);
		hotel.addRoom(RoomType.TWIN_SHARE, 301);
		
		//Guest guest1 = new Guest("Fred", "Nurke", 2);
		//CreditCard card1 = new CreditCard(CreditCardType.VISA, 2, 2);
		
		Guest guest = hotel.registerGuest("Herman", "1313 Mockingbird Lane", 333);
		CreditCard card = new CreditCard(CreditCardType.VISA, 4, 4);
		
		Guest guest2 = hotel.registerGuest("Stormy", "6 High Drive", 999);
        CreditCard card2 = new CreditCard(CreditCardType.VISA, 5, 5);
        
        Guest guest3 = hotel.registerGuest("Trump", "1600 Pen Ave", 666);
        CreditCard card3 = new CreditCard(CreditCardType.MASTERCARD, 5, 5);
		
		
		Room room = hotel.findAvailableRoom(RoomType.TWIN_SHARE, date, 1);
		long confNo = hotel.book(room, guest, date, 1, 2, card);
		Booking booking = hotel.findBookingByConfirmationNumber(confNo);
		hotel.checkin(confNo);
		booking.addServiceCharge(ServiceType.ROOM_SERVICE, 7.00);
		
		IOUtils.trace("HotelHelper");
		for (RoomType rt : RoomType.values()) {
			Map<Integer, Room> rooms = hotel.roomsByType.get(rt);
			for (Integer id : rooms.keySet()) {
				IOUtils.outputln(rooms.get(id));
			}
		}		
		return hotel;
	}

	
	public static void saveHotel() {}

}
