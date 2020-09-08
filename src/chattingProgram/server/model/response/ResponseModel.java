package chattingProgram.server.model.response;

import java.io.Serializable;
import java.util.HashMap;

import chattingProgram.server.config.ProtocolOption;
import chattingProgram.server.model.request.RequestModel;

public class ResponseModel implements Serializable {

    public String requestType;
    public String responseTarget;

    public ResponseModel(RequestModel request){
        this.requestType = request.getRequestType();
        this.responseTarget = request.getResponseTarget();
    }

    public HashMap<String,Object> data = new HashMap<String, Object>();

    /**
     * single : one-to-one통신, 요청을 보낸 client에게만 응답 [default]
     * broadcast :  broadcasting, 모든 client들에게 응답
     * specific : 특정 client들에게만 응답
     */
    public int protocol = ProtocolOption.SINGLE;

}
