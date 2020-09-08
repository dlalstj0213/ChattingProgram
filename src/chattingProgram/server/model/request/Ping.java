package chattingProgram.server.model.request;

import java.io.Serializable;

public class Ping implements RequestModel, Serializable {
    @Override
    public String getRequestType() {
        return RequestModel.PING;
    }

    @Override
    public String getResponseTarget() {
        return "NONE";
    }

}
