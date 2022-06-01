package github.seiufawn.poker.frames;

import github.seiufawn.poker.Frame;

import javax.swing.*;

public class HomePanel extends PanelBase {

    public HomePanel() {
        super("游戏入口");
        setLayout(null);

        JButton b1 = new JButton("游戏规则");
        JButton b2 = new JButton("开始游戏");

        b1.setFont(Frame.BLOD_20);
        b2.setFont(Frame.BLOD_20);

        b1.setBounds(340, 170, 200, 50);
        b2.setBounds(340, 300, 200, 50);

        add(b1);
        add(b2);

        //跳转游戏规则
        b1.addActionListener(e -> {
            close();
            new HelpPanel();
        });

        //跳转游戏界面
        b2.addActionListener(e -> {
            close();
            new GamePanel();
        });
    }

}
