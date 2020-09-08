package chattingProgram.server.model.request;

import java.io.Serializable;

import chattingProgram.client.ui.GUI;

public class ShowMyUsernameRequest  implements RequestModel, Serializable {
    @Override
    public String getRequestType() {
        return RequestModel.SHOW_MY_USERNAME;
    }

    @Override
    public String getResponseTarget() {
        return GUI.ROUNGE;
    }
}
