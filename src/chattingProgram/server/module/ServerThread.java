package chattingProgram.server.module;

import java.io.IOException;
import java.util.LinkedList;

import chattingProgram.server.config.ProtocolOption;
import chattingProgram.server.model.ChattingRoom;
import chattingProgram.server.model.ServerState;
import chattingProgram.server.model.request.BroadcastExitUserRequest;
import chattingProgram.server.model.request.RequestModel;
import chattingProgram.server.model.response.ResponseModel;

public class ServerThread extends Thread{

	private ServerState state;
	private ServerController controller;
	private LinkedList<ServerState> clientList;

	public ServerThread(ServerState state, LinkedList<ServerState> clientList) {
		state.initialize();
		this.state = state;
		this.controller = new ServerController(state, clientList);
		this.clientList = clientList;
		clientList.add(this.state);
	}

	@Override
	public void run(){
		while(true){
			try {
				followProtocol(controller.choiceService((RequestModel)state.getObjectInputStream().readObject()));
			} catch (Exception e) {
				System.out.println("ServerThread:run() : " + e);
				e.printStackTrace();
				state.destroy();
				clientList.remove(this.state);

				ResponseModel response = new ResponseModel(new BroadcastExitUserRequest());
				response.data.put("senderId", state.getSession().getId());
				response.data.put("msg", "님과 연결이 끊겼습니다.");
				
				try {
					for(ServerState client : clientList) {
						if(client != this.state) {
							client.getObjectOutputStream().writeObject(response);
						}
					} 
				}catch (IOException e1) {
					System.out.println("유저 에러 공지 실패 : "+e1);
					e1.printStackTrace();
				}
				System.out.println("clientList["+clientList.size()+"] : "+clientList);
				for(ChattingRoom chattingRoom : state.getChattingRoomList()) {
					if(chattingRoom.getRoomType() == ChattingRoom.OPEN_CHATTING) {
						System.out.println("클라이언트 유저 오픈 채팅방 접속, 현재 오픈태팅방 유저 수 : "+chattingRoom.getParticipants().size());
					}
				}
				return;
			}
		}
	}

	public void followProtocol(ResponseModel response) throws IOException {
		int protocol = response.protocol;

		if(protocol == ProtocolOption.SINGLE){
			state.getObjectOutputStream().writeObject(response);
			return;
		}

		synchronized (clientList){
			if(protocol == ProtocolOption.BROADCAST){
				for(ServerState client : clientList){
					client.getObjectOutputStream().writeObject(response);
				}
				return;
			}

			if(protocol == ProtocolOption.SPECIFIC){

				return;
			}
		}
	}

}
