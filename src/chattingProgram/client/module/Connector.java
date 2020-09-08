package chattingProgram.client.module;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

import chattingProgram.server.config.ServerConfiguration;
import chattingProgram.server.model.request.Ping;
import chattingProgram.server.model.request.RequestModel;
import chattingProgram.server.model.response.ResponseModel;

public class Connector {

    private Socket sock;
    private InetSocketAddress ipep;
    private ObjectOutputStream requestData;
    private ObjectInputStream responseData;
    private GuiManager guiManager;

    Receiver recvThread;

    public void createGuiManager(){
        this.guiManager = new GuiManager();
    }

    public void guiManager(GuiProfile profile){
        try{
            guiManager.guiGenerator(profile, this);
        }catch (Exception e) {
            destoryAll();
            System.out.println("Connector-guiManager():" + e);
            e.printStackTrace();
        }
    }

    public void connectToServer() {
        try {
            this.sock = new Socket();
            this.ipep = new InetSocketAddress(ServerConfiguration.IP, ServerConfiguration.PORT);
            this.sock.connect(ipep);

            this.requestData = new ObjectOutputStream(sock.getOutputStream());
            this.responseData = new ObjectInputStream(sock.getInputStream());

            this.recvThread = new Receiver(responseData, this);
            recvThread.start();
            System.out.println("===started Receiver Thread===");

            if(sock.isConnected()) {
                System.out.println("서버와 연결 성공 [Client :"+sock.getInetAddress()+"--->"+sock.getLocalAddress()+": Server]");
            }
        }catch (Exception e) {
            System.out.println("Connector-connectToServer():" + e);
            e.printStackTrace();
        }
    }

    //Receiver가 서버로 부터 응답을 받으면 실행되는 메서드
    public void responseSignal(ResponseModel response){
        System.out.println("invoke responseSignal()");
        String requestType = response.requestType;

        if(requestType.equals(RequestModel.PING)){
            System.out.println("[ping]응답 성공");
        }else{
            guiManager.guiAccessor(response);
        }
    }

    private void reconnect(){
        try {
            this.sock = new Socket();
            this.ipep = new InetSocketAddress(ServerConfiguration.IP, ServerConfiguration.PORT);
            this.sock.connect(ipep);

            this.requestData = new ObjectOutputStream(sock.getOutputStream());
            this.responseData = new ObjectInputStream(sock.getInputStream());

            if(!sock.isClosed()) {
                JOptionPane.showMessageDialog(null, "서버와 연결이 끊겨져 재연결을 시도합니다.", "재연결 시도",JOptionPane.WARNING_MESSAGE);
                System.out.println("서버와 재연결 성공 [Client :"+sock.getInetAddress()+"--->"+sock.getLocalAddress()+": Server]");
                JOptionPane.showMessageDialog(null, "연결에 성공하였습니다.", "재연결 성공",JOptionPane.INFORMATION_MESSAGE);
                this.recvThread = new Receiver(responseData, this);
                recvThread.start();
            }
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "연결 실패", "재연결 시도",JOptionPane.WARNING_MESSAGE);
            System.out.println("Connector-reconnect():" + e);
            e.printStackTrace();
        }
    }

    public void ping() throws Exception{
        requestData.writeObject(new Ping());
        System.out.println("[ping]"+sock.getLocalAddress());
    }

    public void destoryAll(){
        JOptionPane.showMessageDialog(null, "서버와 연결이 안되어 있습니다.", "네트워크 오류",JOptionPane.WARNING_MESSAGE);
        try{
            if(requestData != null){
                System.out.println("requestData close");
                requestData.close();
            }
        }catch (Exception e){
            System.out.println("Connector-destoryAll():" + e);
            e.printStackTrace();
        }

        try{
            if(responseData != null){
                System.out.println("responseData close");
                responseData.close();
            }
        }catch (Exception e){
            System.out.println("Connector-destoryAll():" + e);
            e.printStackTrace();
        }

        try{
            if(sock != null){
                System.out.println("sock close");
                sock.close();
            }
        }catch (Exception e){
            System.out.println("Connector-destoryAll():" + e);
            e.printStackTrace();
        }

        reconnect();
    }
    
    public void request(RequestModel sendToServer) {

        try {
            // 서버에게 요청을 보낸다. 서버에게 받는 응답은 Receiver 쓰레드가 처리할 것이다.
            requestData.writeObject(sendToServer);
        }catch(SocketException se){
            destoryAll();
        }catch (Exception e) {
            System.out.println("Connector-request():" + e);
            e.printStackTrace();
        }

    }
}
