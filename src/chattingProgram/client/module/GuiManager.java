package chattingProgram.client.module;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JFrame;

import chattingProgram.client.ui.GUI;
import chattingProgram.client.ui.LoginGui;
import chattingProgram.client.ui.RoungeGui;
import chattingProgram.client.ui.SignUpGui;
import chattingProgram.server.model.request.RequestModel;
import chattingProgram.server.model.response.ResponseModel;

public class GuiManager {

	private LinkedList<JFrame> currentGui;

	public GuiManager() {
		this.currentGui = new LinkedList<JFrame>();
	}

	public void guiGenerator(GuiProfile profile, Connector connector) throws Exception{

		connector.ping();

		int mode = profile.getMode();

		if(mode == GuiManagerMode.NEW_WINDOW_AND_CLOSE_ALL_OLD_WINDOW){
			while(!currentGui.isEmpty()){
				currentGui.peek().dispose();
				currentGui.pop();
			}
			generate(profile,connector);
		}else if(mode == GuiManagerMode.NEW_WINDOW_AND_STAY_ALL_OLD_WINDOW){
			generate(profile,connector);
		}else if(mode == GuiManagerMode.CLOSE_THIS_WINDOW){
			profile.getTarget().dispose();
			currentGui.remove(profile.getTarget());
		}
	}

	private void generate(GuiProfile profile, Connector connector) throws Exception{
		Class<?> gui = Class.forName(profile.getGuiName());
		Constructor<?> createGui = gui.getConstructor(Connector.class);
		currentGui.add((JFrame)createGui.newInstance(connector));
	}

	public void guiAccessor(ResponseModel response){
		String type = response.requestType;
		HashMap<String,Object> data = response.data;

		for(JFrame frame : currentGui){
			GUI gui = (GUI)frame;

			if(gui.getGuiName().equals(response.responseTarget)){

				if(type.equals(RequestModel.LOGIN)){
					((LoginGui)gui).loginResult(data);
				}

				if(type.equals(RequestModel.CHECK_ID)){
					((SignUpGui)gui).checkIdResult(data);
				}

				if(type.equals((RequestModel.SIGNUP))){
					((SignUpGui)gui).signUpResult(data);
				}

				if(type.equals((RequestModel.OPEN_CHATTING))){
					((RoungeGui)gui).openChattingResult(data);
				}
				if(type.equals((RequestModel.SHOW_MY_USERNAME))){
					((RoungeGui)gui).showMyUsernameResult(data);
				}
			}
			if(response.responseTarget.equals("ALL")) {
				if(gui instanceof RoungeGui) {
					((RoungeGui)gui).openChattingResult(data);
				}
			}
		}
	}
}
