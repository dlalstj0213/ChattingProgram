package chattingProgram.client.ui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import chattingProgram.client.module.Connector;
import chattingProgram.client.module.GuiManagerMode;
import chattingProgram.client.module.GuiProfile;
import chattingProgram.server.model.request.LoginRequest;

//Err-10
public class LoginGui extends JFrame implements ActionListener, GUI{
	@Override
	public String getGuiName() {
		return GUI.LOGIN;
	}
	//아이디에 관한
	JTextField idF;
	//////비밀번호에관한///////////
	JPasswordField pwT;;//3-2
	///////로그인에관한 ///////////////////
	JButton loginBt;
	JPanel ioginP;
	/////////회원가입에관한/////////
	JButton memberBt;
	JPanel memberP;
	BufferedImage img=null;
	ImageIcon backgroundImg;
	Connector connector;

	public LoginGui(Connector connector) {
		this.connector = connector;
		launchLoginGui();
	}

	void launchLoginGui() {
		set();
		firstPage();
		putBackground();
	}

	void firstPage() {
		//이미지 불러오기
		try{
			File f = new File("./image/login.png");
			BufferedImage bi = ImageIO.read(f);
			backgroundImg = new ImageIcon(bi);
		}catch(IOException ie){
			System.out.println("Login Err-11 : "+ie);
		}
		//아이디
		//		setLayout(null);
		idF=new JTextField("아이디 입력");
		idF.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		idF.setOpaque(false); //true : 기본설정 / false : 투명화
		idF.setBounds(75,305,295,53);
		//마우스클릭시 field 지우기
		idF.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				idF.setText("");
			}
		});
		add(idF);

		//비밀번호
		pwT=new JPasswordField("비밀번호 입력",20);
		pwT.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		pwT.setOpaque(false); //true : 기본설정 / false : 투명화
		//마우스클릭시 field 지우기
		pwT.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				pwT.setText("");
			}
		});
		add(pwT);



		loginBt=new JButton();
		loginBt.setContentAreaFilled(false);//버튼텍스트 비활성
		loginBt.setFocusPainted(false);
		loginBt.setBorderPainted(true);
		memberBt=new JButton();
		memberBt.setContentAreaFilled(false);//버튼텍스트 비활성
		memberBt.setFocusPainted(false);
		memberBt.setBorderPainted(true);
		add(loginBt); add(memberBt);
		

		pwT.setBounds(75,352,295,53);

		loginBt.setBounds(70, 415,295,53);

		memberBt.setBounds(70, 470,295, 53);

		putBackground();
		revalidate();
		repaint();

		/////////////////addACtionListener//////////////////////
		loginBt .addActionListener(this);
		memberBt.addActionListener(this);
		/////////////////////////////////////////////////////////	
		revalidate();
		repaint();
	}
	void set(){

		setTitle("로그인");
		setSize(439,620);
		setVisible(true);
		setLocation(200,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		revalidate();
		repaint();
	}

	void putBackground() {
		JPanel backgroundP = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(backgroundImg.getImage(),0,0,null);
			}
		};
		add(backgroundP);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == loginBt){
			String userName = idF.getText();
			char[] password = pwT.getPassword();
			String passwordCheck = new String(password);

			if(userName.equals("") && passwordCheck.equals("")) {
				JOptionPane.showMessageDialog(null, "아이디와 비밀번호를\n입력해주세요.", "로그인실패", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if(userName.equals("")) {
				JOptionPane.showMessageDialog(null, "아이디를 입력해주세요.", "로그인실패", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if(passwordCheck.equals("")) {
				JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.", "로그인실패", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			//서버에 요청할 데이터
			connector.request(
					new LoginRequest()
					.setId(userName)
					.setPassword(passwordCheck));
		}else if(obj==memberBt){
			GuiProfile profile = new GuiProfile();
			profile.setGuiName(GUI.SIGNUP).setMode(GuiManagerMode.NEW_WINDOW_AND_CLOSE_ALL_OLD_WINDOW);
			connector.guiManager(profile);
		}
	}
	
	public void loginResult(HashMap<String, Object> data) {
		String result = (String)data.get("result");
		
		if(result.equals("success")) {
			JOptionPane.showMessageDialog(null, "로그인 성공", "성공",JOptionPane.INFORMATION_MESSAGE);
			GuiProfile profile = new GuiProfile();
			profile.setGuiName(GUI.ROUNGE).setMode(GuiManagerMode.NEW_WINDOW_AND_CLOSE_ALL_OLD_WINDOW);
			connector.guiManager(profile);
		}else if(result.equals("fail")){
			JOptionPane.showMessageDialog(null, "로그인 실패", "실패",JOptionPane.ERROR_MESSAGE);
			return;
		}else if(result.equals("blocked")) {
			JOptionPane.showMessageDialog(null, "이미 접속중인 아이디입니다.", "누구냐 넌...",JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
}