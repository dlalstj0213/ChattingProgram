package chattingProgram.server.model.request;

import java.io.Serializable;

public class BroadcastExitUserRequest implements RequestModel, Serializable{
	@Override
	public String getRequestType() {
		return RequestModel.BROADCAST_EXIT_USER;
	}
	
	@Override
	public String getResponseTarget() {
		return "ALL";
	}
}
