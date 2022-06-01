package github.seiufawn.poker;

import github.seiufawn.poker.frames.HomePanel;
import github.seiufawn.poker.frames.PanelBase;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    //静态化字体
    public static Font BLOD_20 = new Font("微软雅黑", Font.BOLD,20);
    public static Font PLAIN_20 = new Font("微软雅黑", Font.PLAIN,20);
    public static Font PLAIN_15 = new Font("微软雅黑", Font.PLAIN,15);

    public static Frame inst;

    // 当前界面
    public static PanelBase currentPanel;

    // 玩家名字
    public static String playerName;

    public Frame() {
        setLocation(300, 300);
        setSize(900, 600);
    }

    public static void main(String[] args) {
        inst = new Frame();
        inst.setVisible(true);
        new HomePanel();
    }
}