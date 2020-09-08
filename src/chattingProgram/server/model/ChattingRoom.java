package chattingProgram.server.model;

import java.util.LinkedList;

public class ChattingRoom {
	public final static int OPEN_CHATTING =1;
	public final static int ROOM_CHATTING =2;
	
	private String roomName;
	private int roomType;
	private String owner;
	private String roomPassword;
	private LinkedList<User> participants;
	
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public int getRoomType() {
		return roomType;
	}
	public void setRoomType(int roomType) {
		this.roomType = roomType;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getRoomPassword() {
		return roomPassword;
	}
	public void setRoomPassword(String roomPassword) {
		this.roomPassword = roomPassword;
	}
	public LinkedList<User> getParticipants() {
		return participants;
	}
	public void setParticipants(LinkedList<User> participants) {
		this.participants = participants;
	}
	
}
