package chattingProgram.server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import chattingProgram.server.config.ServerConfiguration;
import chattingProgram.server.model.ChattingRoom;
import chattingProgram.server.model.ServerState;
import chattingProgram.server.model.User;
import chattingProgram.server.module.ServerRepository;
import chattingProgram.server.module.ServerThread;

public class MainServer {
    private static void startServer(){
        try {
            // 서버 객체 생성
            ServerSocket server = new ServerSocket();
            InetSocketAddress ipep = new InetSocketAddress(ServerConfiguration.IP, ServerConfiguration.PORT);
            server.bind(ipep);

            // Database 접근 객체 (모든 Thread들이 공유)
            ServerRepository repo = new ServerRepository();
            repo.checkDatabaseState();

            // 서버에 연결된 client들의 정보를 얻는 객체 (모든 Thread들이 공유)
            LinkedList<ServerState> clientList = new LinkedList<ServerState>();

            // 서버의 채팅방에 대한 정보를 저장하는 객체 + Open채팅방 개설 (모든 Thread들이 공유)
            LinkedList<ChattingRoom> roomList = new LinkedList<ChattingRoom>();
            ChattingRoom openChattingRoom = new ChattingRoom();
            openChattingRoom.setRoomType(ChattingRoom.OPEN_CHATTING);
            openChattingRoom.setParticipants(new LinkedList<User>());
            roomList.add(openChattingRoom);
            
            // 서버 리스닝 시작
            System.out.println("=====서버가 시작되었음=====");
            while(true){
                Socket sock = server.accept();
                ServerState state = new ServerState();
                state.setSocket(sock);
                state.setRepository(repo);
                state.setChattingRoomList(roomList);
                ServerThread getClient = new ServerThread(state, clientList);
                getClient.start();
                System.out.println(":::클라이언트가 접속함["+sock.getInetAddress()+"]");
                System.out.println("clientList["+clientList.size()+"] : "+clientList);
            }
        } catch (Exception e) {
            System.out.println("MainServer:startServer() : " + e);
        }
    }

    public static void main(String[] args) {
        startServer();
    }
}