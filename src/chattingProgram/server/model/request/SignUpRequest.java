package chattingProgram.server.model.request;

import java.io.Serializable;

import chattingProgram.client.ui.GUI;

public class SignUpRequest implements RequestModel, Serializable {
	@Override
	public String getRequestType() { 
		return RequestModel.SIGNUP; 
	}
	@Override
	public String getResponseTarget() {
		return GUI.SIGNUP;
	}


	private String id;
	private String password;

	public String getId() {
		return id;
	}

	public SignUpRequest setId(String id) {
		this.id = id;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public SignUpRequest setPassword(String password) {
		this.password = password;
		return this;
	}
}
