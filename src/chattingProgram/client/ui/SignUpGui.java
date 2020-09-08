package chattingProgram.client.ui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import chattingProgram.server.model.request.CheckIdRequest;
import chattingProgram.server.model.request.SignUpRequest;

public class SignUpGui extends JFrame implements ActionListener, GUI {
	@Override
	public String getGuiName() {
		return GUI.SIGNUP;
	}
	
	String newUserName;
	String passwordCheck;
	
	JTextField idT;
	JPasswordField pass;
	JButton join, cancel;
	BufferedImage img2=null;
	ImageIcon backgroundImg;

	boolean isFirstAccess = true;
	boolean isCheckedId = false; //중복확인
	String alreadyCheckedId = null;
	Connector connector;

	public SignUpGui(Connector connector) {
		this.connector = connector;
		launchSingUpGUI();
	}

	void launchSingUpGUI() {
		setUi();
		memberSh();
	}

	void memberSh() {
		try{
			File f = new File("./image/createAccount.png");
			BufferedImage bii = ImageIO.read(f);
			backgroundImg = new ImageIcon(bii);
		}catch(IOException ie){
			System.out.println("Login Err-11 : "+ie);
		}
		idT=new JTextField();
		idT.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		idT.setOpaque(false); //true : 기본설정 / false : 투명화
		idT.setBounds(75,305,295,53);
		add(idT);
		pass=new JPasswordField(20);
		pass.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		pass.setOpaque(false); //true : 기본설정 / false : 투명화
		add(pass);


		join=new JButton();
		join.setContentAreaFilled(false);//버튼텍스트 비활성
		join.setFocusPainted(false);
		join.setBorderPainted(true);
		cancel=new JButton();
		cancel.setContentAreaFilled(false);//버튼텍스트 비활성
		cancel.setFocusPainted(false);
		cancel.setBorderPainted(true);
		add(join); add(cancel);

		pass.setBounds(75,352,295,53);
		join.setBounds(70, 415,295,53);
		cancel.setBounds(70, 470,295, 53);

		putBackground2();
		revalidate();
		repaint();

		join.addActionListener(this);
		cancel.addActionListener(this);
	}

	void setUi(){

		setTitle("회원가입");
		setSize(439,620);
		setVisible(true);
		setLocation(200,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		revalidate();
		repaint();
	}

	void putBackground2() {
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
		if (obj == join) {
			newUserName = idT.getText().trim();
			char[] newPassword = pass.getPassword();
			passwordCheck = new String(newPassword);

			if(newUserName.equals("") && passwordCheck.equals("")) {
				JOptionPane.showMessageDialog(null, "아이디와 비밀번호를\n입력해주세요.", "회원가입 실패", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if(newUserName.equals("")) {
				JOptionPane.showMessageDialog(null, "아이디를 입력해주세요.", "회원가입 실패", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if(passwordCheck.equals("")) {
				JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.", "회원가입 실패", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			connector.request(
					new CheckIdRequest()
					.setId(newUserName)
					);
		}else if(obj == cancel) {
			GuiProfile profile = new GuiProfile();
			profile.setGuiName(GUI.LOGIN).setMode(GuiManagerMode.NEW_WINDOW_AND_CLOSE_ALL_OLD_WINDOW);
			connector.guiManager(profile);
		}
	}
	
	public void checkIdResult(HashMap<String, Object> data) {
		boolean isExist = (boolean)data.get("result");
		if(isExist) {
			JOptionPane.showMessageDialog(null, "중복된 아이디가 존재합니다.\n다른 아이디로 가입해주세요.", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
			isCheckedId = false;
		} else {
			connector.request(
					new SignUpRequest()
					.setId(newUserName)
					.setPassword(passwordCheck));
			isCheckedId = true;
		}
	}
	
	public void signUpResult(HashMap<String, Object> data) {
		String result = (String)data.get("result");
		GuiProfile profile = new GuiProfile();
		profile.setGuiName(GUI.LOGIN).setMode(GuiManagerMode.NEW_WINDOW_AND_CLOSE_ALL_OLD_WINDOW).setTarget(this);
		connector.guiManager(profile);
		JOptionPane.showMessageDialog(null, "회원가입에 성공하였습니다. \n다시 로그인 해주세요.", result, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void main(String[] args) {
		new SignUpGui(new Connector());
	}
}
