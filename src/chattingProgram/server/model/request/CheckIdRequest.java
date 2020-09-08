package chattingProgram.server.model.request;

import java.io.Serializable;

import chattingProgram.client.ui.GUI;

public class CheckIdRequest implements RequestModel, Serializable {
    @Override
    public String getRequestType() {
        return RequestModel.CHECK_ID;
    }

    @Override
    public String getResponseTarget() {
        return GUI.SIGNUP;
    }

    private String id;

    public String getId() {
        return id;
    }

    public CheckIdRequest setId(String id) {
        this.id = id;
        return this;
    }


}
