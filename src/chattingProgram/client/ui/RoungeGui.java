package chattingProgram.client.ui;

import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import chattingProgram.client.module.Connector;
import chattingProgram.server.model.request.OpenChattingRequest;
import chattingProgram.server.model.request.ShowMyUsernameRequest;

//Err-400
public class RoungeGui extends JFrame implements ActionListener, GUI{
	@Override
	public String getGuiName() {
		return GUI.ROUNGE;
	}
	private static final long serialVersionUID = 1L;

	Container container;
	ImageIcon backgroundImg;
	JButton sendMailBt, mailBoxBt, roomBt, checkUsersBt;
	JTextArea msgScreen;
	JTextField sendMsgField;
	JScrollPane scrollPane;
	Connector connector;
	JLabel userName;
	String roomName = "openChat";
	String id;
	JEditorPane chatDisplay;
	StringBuffer chattingText = new StringBuffer();
	JFrame mainFrame;
	
	public RoungeGui(Connector connector){
		this.connector = connector;
		this.mainFrame = this;
		connector.request(new ShowMyUsernameRequest());
		connector.request(
				new OpenChattingRequest()
				.setFirstAccess(true)
				);
		mainFrame.setTitle("Tap.Rounge");
		mainFrame.setSize(500, 830); //창 사이즈 설정
		loadImg();
		putButtons();
		putUserName();
		putChatBox();
		putBackgroundImg();
		mainFrame.setLayout(null);
		putFrame();
		revalidate();
		repaint();
	}

	/***프레임 설정***/
	void putFrame(){
		mainFrame.setVisible(true); //창 보이기 true
		mainFrame.setLocation(100,100); // 창 뜨는 위치
		mainFrame.setResizable(false); //창 변경 불가
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //x 클릭시 프로그램 자동 종료
	}

	/***유저이름 표시***/
	void putUserName() {
		userName = new JLabel("", JLabel.CENTER);
		userName.setFont(new Font("돋음",Font.BOLD,30));
		//		userLabel.setBorder(new LineBorder(Color.LIGHT_GRAY, 3, true));
		userName.setBounds(17, 42, 464, 68);
		mainFrame.add(userName);
	}

	/***채팅 컴포넌트 구현***/
	void putChatBox() {
		sendMsgField = new JTextField("입력란",16);
		sendMsgField.setBounds(17, 717, 461, 49);
		sendMsgField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		sendMsgField.setOpaque(false); //true : 기본설정 / false : 투명하게하기

		JPanel chatDisplayFrame = new JPanel(); //서브프레임 생성
		chatDisplayFrame.setBounds(17, 249, 461, 469);
		
		chatDisplay = new JEditorPane(); //서브프레임에 넣을 텍스트 컴포넌트 생성
		chatDisplay.setContentType("text/plain");
		chatDisplay.setFont(new Font("Serif",Font.BOLD,17));
		chatDisplay.setEditable(false);
		
		scrollPane = new JScrollPane(chatDisplay);
		scrollPane.setOpaque(false);
		scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		//As needed 즉 필요에의해서 내용이 많아지면 스크롤 바가 www생김w
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//가로 스크롤은 안만든다.
		chatDisplayFrame.add(scrollPane);
		chatDisplayFrame.setLayout(new BoxLayout(chatDisplayFrame, BoxLayout.X_AXIS));
		mainFrame.add(chatDisplayFrame);
		
		/*** 오픈채팅 EVENT 입력란 enter 키 반응***/ 
		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!sendMsgField.getText().equals("")) {
					connector.request(
							new OpenChattingRequest()
							.setMsg(sendMsgField.getText())
							);
					sendMsgField.setText("");
				}
			}
		};
		sendMsgField.addActionListener(action);

		mainFrame.add(sendMsgField);
	}

	/***백그라운드 설정***/
	void putBackgroundImg() {
		changeSizeImg(backgroundImg, 500, 800);
		JPanel backgroundP = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(backgroundImg.getImage(), 0, 0, null);
				//				setOpaque(false);
				//				super.paintComponent(g);
			}
		};
		backgroundP.setBounds(0,0, 600, 900);
		mainFrame.add(backgroundP);
	}

	/***버튼 구현***/
	void putButtons() {
		sendMailBt = new JButton();
		//		sendMailBt.setFont(new Font("돋음",Font.BOLD,15)); //폰트 편집
		//		sendMailBt.setEnabled(false); //버튼 활성 / 비활성
		//		sendMailBt.setVisible(false); // 버튼 보이게 하기 / 비활성
		sendMailBt.setContentAreaFilled(false); //버튼 텍스트 활성 / 비활성
		sendMailBt.setFocusPainted(false); //클릭시 포커싱하기 활성 / 비활성
		sendMailBt.setBorderPainted(true); //border 활성 / 비활성
		sendMailBt.setBounds(17, 134, 106, 90);  //위치지정
		mainFrame.add(sendMailBt);
		sendMailBt.addActionListener(this);

		mailBoxBt = new JButton();
		mailBoxBt.setContentAreaFilled(false);
		mailBoxBt.setFocusPainted(false);
		mailBoxBt.setBounds(137, 134, 106, 90);
		mainFrame.add(mailBoxBt);
		mailBoxBt.addActionListener(this);

		roomBt = new JButton();
		roomBt.setContentAreaFilled(false);
		roomBt.setFocusPainted(false);
		roomBt.setBounds(256, 134, 106, 90);
		mainFrame.add(roomBt);
		roomBt.addActionListener(this);

		checkUsersBt = new JButton();
		checkUsersBt.setContentAreaFilled(false);
		checkUsersBt.setFocusPainted(false);
		checkUsersBt.setBounds(374, 134, 106, 90);
		mainFrame.add(checkUsersBt);
		checkUsersBt.addActionListener(this);
	}

	/***이미지 불러오기***/
	void loadImg() {
		try {
			backgroundImg = new ImageIcon(ImageIO.read(new File("./image/rounge.png")));
		} catch (IOException e) {
			System.out.println("UiClient Err-402");
		}
	}

	/***이미지 사이즈 변경하기***/
	void changeSizeImg(ImageIcon img, int x, int y) {
		Image originImg = img.getImage();
		Image changedImg = originImg.getScaledInstance(x, y, Image.SCALE_SMOOTH);
		img = new ImageIcon(changedImg);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == sendMailBt) {
			JOptionPane.showMessageDialog(null, "죄송합니다.\n이 서비스는 아직 사용하실 수 없습니다.", "운영자",  JOptionPane.WARNING_MESSAGE);
			System.out.println("편지쓰기 클릭");
			return;
		} else if (obj == mailBoxBt) {
			JOptionPane.showMessageDialog(null, "죄송합니다.\n이 서비스는 아직 사용하실 수 없습니다.", "운영자",  JOptionPane.WARNING_MESSAGE);
			System.out.println("편지함 클릭");
			return;
		} else if (obj == roomBt) {
			JOptionPane.showMessageDialog(null, "죄송합니다.\n이 서비스는 아직 사용하실 수 없습니다.", "운영자",  JOptionPane.WARNING_MESSAGE);
			System.out.println("채팅방 클릭");
		} else if (obj == checkUsersBt) {
			JOptionPane.showMessageDialog(null, "죄송합니다.\n이 서비스는 아직 사용하실 수 없습니다.", "운영자",  JOptionPane.WARNING_MESSAGE);
			System.out.println("접속현황 클릭");
		}
	}
	public void showMyUsernameResult(HashMap<String, Object> data) {
		String userName = (String)data.get("username");
		this.userName.setText(userName);
		this.id = userName;
	}
	public void openChattingResult(HashMap<String, Object> data) {
		String senderId = (String)data.get("senderId");
		String msg = (String)data.get("msg");
		System.out.println("openChattingResult() - [id:"+this.id+"] ");
		
		//Gui에 해당 유저 정보를 서버로부터 얻어 왔는지 안왔는지 확인하고, 없으면 다시 오픈채팅방 접속을 요청한다.
		if(this.id == null && this.userName.getText().equals("")) {
			connector.request(
					new OpenChattingRequest()
					.setFirstAccess(true)
					);
			return;
		}
		if(this.id.equals(senderId) && this.userName.getText().equals(senderId)) {
			if(data.get("welcome") != null) {
				this.chatDisplay.setText(chattingText.append(((String)data.get("welcome"))+"\n").toString());
				this.chatDisplay.setCaretPosition(chatDisplay.getDocument().getLength());
			} else {
				this.chatDisplay.setText(chattingText.append("나 : "+msg+"\n").toString());
				this.chatDisplay.setCaretPosition(chatDisplay.getDocument().getLength());
			}
		} else { //다른 사람이 보낸 메시지를 Echo받은 경우
			if(data.get("inform") != null) {
				this.chatDisplay.setText(chattingText.append(((String)data.get("inform"))+"\n").toString());
				this.chatDisplay.setCaretPosition(chatDisplay.getDocument().getLength());
			} else {
				this.chatDisplay.setText(chattingText.append("["+senderId+"] : "+msg+"\n").toString());
				this.chatDisplay.setCaretPosition(chatDisplay.getDocument().getLength());
			}
		}
	}

}
