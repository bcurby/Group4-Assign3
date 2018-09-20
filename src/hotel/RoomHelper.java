package hotel;

import hotel.entities.Room;
import hotel.entities.RoomType;

public class RoomHelper {
	
	private static RoomHelper self;
	
	public static RoomHelper getInstance() {
		if (self == null) {
			self = new RoomHelper();
		}
		return self;
	}
	
	
	public Room makeRoom(int id, RoomType roomType) {
		
		return new Room (id, roomType);
	}

}
