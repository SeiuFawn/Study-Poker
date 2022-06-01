package github.seiufawn.poker.frames;

import github.seiufawn.poker.Frame;

import javax.swing.*;

// 面板基类
public class PanelBase extends JPanel {

    String title;

    // 实例化界面并且自动添加到窗口中, 并且设置大小
    public PanelBase(String title, int wight, int height) {
        this(title);
        Frame.inst.setSize(wight, height);
        Frame.currentPanel = this;
    }

    // 实例化界面并且自动添加到窗口中
    public PanelBase(String title) {
        this.title = title;
        Frame.inst.setTitle(title);
        Frame.inst.add(this);
    }

    //添加文字类
    public JLabel addLabel(String text, int x, int y, int width, int height) {
        JLabel newLabel = new JLabel(text);
        newLabel.setFont(Frame.PLAIN_15);
        newLabel.setBounds(x, y, width, height);
        add(newLabel);
        return newLabel;
    }
    /**
     * 关闭界面
     */
    public void close() {
        setVisible(false);
    }
}
