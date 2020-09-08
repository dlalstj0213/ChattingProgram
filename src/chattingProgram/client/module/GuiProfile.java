package chattingProgram.client.module;

import javax.swing.*;

public class GuiProfile {
    private final static String rootPackage = "chattingProgram.client.ui.";
    private String guiName;
    private int mode;
    private JFrame target;

    public String getGuiName() {
        return rootPackage+guiName;
    }

    public GuiProfile setGuiName(String guiName) {
        this.guiName = guiName;
        return this;
    }

    public int getMode() {
        return mode;
    }

    public GuiProfile setMode(int mode) {
        this.mode = mode;
        return this;
    }

    public JFrame getTarget() {
        return target;
    }

    public GuiProfile setTarget(JFrame target) {
        this.target = target;
        return this;
    }
}
