package chattingProgram.server.model.request;

import java.io.Serializable;

import chattingProgram.client.ui.GUI;

public class OpenChattingRequest implements RequestModel, Serializable {
    @Override
    public String getRequestType() {
        return RequestModel.OPEN_CHATTING;
    }
    @Override
    public String getResponseTarget() {
        return GUI.ROUNGE;
    }

    private String msg;
    private boolean isFirstAccess = false; //기본값 false
    
	public String getMsg() {
		return msg;
	}
	public OpenChattingRequest setMsg(String msg) {
		this.msg = msg;
		return this;
	}
	public boolean isFirstAccess() {
		return isFirstAccess;
	}
	public OpenChattingRequest setFirstAccess(boolean isFirstAccess) {
		this.isFirstAccess = isFirstAccess;
		return this;
	}
}
