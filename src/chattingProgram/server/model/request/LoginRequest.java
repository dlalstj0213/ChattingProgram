package chattingProgram.server.model.request;

import java.io.Serializable;

import chattingProgram.client.ui.GUI;

public class LoginRequest implements RequestModel, Serializable {
    @Override
    public String getRequestType() {
        return RequestModel.LOGIN;
    }
    @Override
    public String getResponseTarget() {
        return GUI.LOGIN;
    }


    private String id;
    private String password;

    public String getId() {
        return id;
    }

    public LoginRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }


}
