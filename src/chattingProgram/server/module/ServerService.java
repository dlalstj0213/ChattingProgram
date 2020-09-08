package chattingProgram.server.module;

import java.util.LinkedList;

import chattingProgram.server.config.ProtocolOption;
import chattingProgram.server.model.ChattingRoom;
import chattingProgram.server.model.ServerState;
import chattingProgram.server.model.User;
import chattingProgram.server.model.request.CheckIdRequest;
import chattingProgram.server.model.request.LoginRequest;
import chattingProgram.server.model.request.OpenChattingRequest;
import chattingProgram.server.model.request.ShowMyUsernameRequest;
import chattingProgram.server.model.request.SignUpRequest;
import chattingProgram.server.model.response.ResponseModel;

public class ServerService {

	private ServerState state;
	private ServerRepository repository;
	private LinkedList<ServerState> clientList;

	public ServerService(ServerState state, LinkedList<ServerState> clientList) {
		this.state = state;
		this.repository = state.getRepository();
		this.clientList = clientList;
	}

	public ResponseModel loginService(LoginRequest request) throws Exception{
		boolean isLogined = false;
		ResponseModel response = new ResponseModel(request);

		if(repository.findUserInfo(request.getId(), request.getPassword())){
			for(ServerState client : clientList){
				if( client.getSession() != null && request.getId().equals(client.getSession().getId())){
					isLogined = true;
				}
			}

			if(!isLogined){
				this.state.setSession(new User(repository.getUserInfo(request.getId())));
				response.data.put("result","success");
			}else{
				response.data.put("result","blocked");
			}

		}else{
			response.data.put("result","fail");
		}
		System.out.println("isLogined : "+isLogined);
		return response;
	}

	// 아이디 중복 확인 서비스
	public ResponseModel checkIdService(CheckIdRequest request) throws Exception{
		ResponseModel response = new ResponseModel(request);
		if(repository.findUserInfo(request.getId())){
			response.data.put("result",new Boolean(true));
		}else{
			response.data.put("result",new Boolean(false));
		}
		return response;
	}

	// 회원가입 서비스
	public ResponseModel signUpService(SignUpRequest request) throws Exception{
		ResponseModel response = new ResponseModel(request);
		if(repository.updateUserInfo(request)){
			response.data.put("result", "회원가입 성공");
		}else{
			response.data.put("result", "회원가입 실패");
		}
		return response;
	}

	// 로그인 후 자신의 유저 이름 알려주기(RoungeGui에다가..)
	public ResponseModel showMyUsernameService(ShowMyUsernameRequest request) throws Exception{
		ResponseModel response = new ResponseModel(request);
		response.data.put("username",state.getSession().getId());
		return response;
	}

	///채팅 서비스///
	public ResponseModel openChattingService(OpenChattingRequest request) {
		ResponseModel response = new ResponseModel(request);
		response.data.put("senderId", state.getSession().getId());
		if(request.isFirstAccess()) {
			response.data.put("inform", "알림 : ["+state.getSession().getId()+"]님이 오픈채팅방(Rounge)에 들어오셨습니다.");
			response.data.put("welcome", "=====접속을 환영합니다=====");
			for(ChattingRoom chattingRoom : state.getChattingRoomList()) {
				if(chattingRoom.getRoomType() == ChattingRoom.OPEN_CHATTING) {
					chattingRoom.getParticipants().add(state.getSession());
					System.out.println(state.getSession().getId()+" 유저 오픈 채팅방 접속, 현재 오픈채팅방 유저 수 : "+chattingRoom.getParticipants().size());
				}
			}
		} else {
			response.data.put("msg", request.getMsg());
		}
		response.protocol = ProtocolOption.BROADCAST;
		return response;
	}
}
