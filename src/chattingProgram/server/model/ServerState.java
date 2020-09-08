package chattingProgram.server.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.LinkedList;

import chattingProgram.server.module.ServerRepository;

public class ServerState {

    private Socket socket; // TCP 네트워킹 소켓
    private ServerRepository repository; // Database에 접근하는 객체
    private LinkedList<ChattingRoom> chattingRoomList; //채팅방 목록에 접근하는 객체
    private ObjectInputStream objectInputStream; // Client로부터 데이터를 객체 형태로 받는 객체
    private ObjectOutputStream objectOutputStream; // Client로 데이터를 객체 형태를 보내는 객체
    private boolean isInitialized = false;
    private boolean isDestroyed = false;
    private ZonedDateTime connectTime; // 연결된 시간
    private User session;

    // 통신용 객체들을 Socket을 기반으로 모두 초기화하기
    public void initialize(){
        try{
            if(!isInitialized){
                this.objectInputStream = new ObjectInputStream(socket.getInputStream());
                this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                isInitialized = true;
                this.connectTime = ZonedDateTime.now();

            }else{
                System.out.println("이미 초기화가 되었습니다.");
            }
        }catch(Exception e){
            System.out.println("ServerState-initialize() Error:"+e);
        }
    }

    // 통신 객체들 모두 닫아주기
    public void destroy(){

        try {
            if(!isDestroyed){

                InetAddress disconnect = socket.getInetAddress(); // 연결 해제 대상
                if(objectInputStream != null) { objectInputStream.close(); };
                if(objectOutputStream != null) { objectOutputStream.close(); };
                if(socket != null) { socket.close(); };
                isDestroyed = true;
                System.out.println("통신 객체들이 close되었습니다. -- 클라이언트 접속 해제["+disconnect+"]");

            }else{
                System.out.println("이미 모든 통신 객체들이 close되었습니다.");
            }
        } catch (Exception e) {
            System.out.println("ServerState-destroy() Error:"+e);
        }
    }

    // 로그인 여부 확인
    public boolean isLogined(){
        return this.session != null;
    }

    /* Getters and Setters */
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ServerRepository getRepository() {
        return repository;
    }

    public void setRepository(ServerRepository repository) {
        this.repository = repository;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public void setObjectInputStream(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }

    public ZonedDateTime getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(ZonedDateTime connectTime) {
        this.connectTime = connectTime;
    }

    public User getSession() {
        return session;
    }

    public void setSession(User session) {
        this.session = session;
    }

	public LinkedList<ChattingRoom> getChattingRoomList() {
		return chattingRoomList;
	}
	public void setChattingRoomList(LinkedList<ChattingRoom> chattingRoomList) {
		this.chattingRoomList = chattingRoomList;
	}
}
