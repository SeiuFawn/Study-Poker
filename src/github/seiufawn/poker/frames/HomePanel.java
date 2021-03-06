package github.seiufawn.poker.frames;

import github.seiufawn.poker.Frame;

import javax.swing.*;

public class HomePanel extends PanelBase {

    public HomePanel() {
        super("游戏入口");
        setLayout(null);

        JButton b1 = new JButton("游戏规则");
        JButton b2 = new JButton("开始游戏");

        b1.setFont(Frame.BOLD_20);
        b2.setFont(Frame.BOLD_20);

        b1.setBounds(340, 300, 200, 50);
        b2.setBounds(340, 400, 200, 50);

        add(b1);
        add(b2);

        addLabel("请输入名字:", 340, 150, 200, 50).setFont(Frame.BOLD_20);
        JTextField jtf = new JTextField(Frame.playerName);
        jtf.setFont(Frame.PLAIN_20);
        jtf.setBounds(340, 200, 200, 50);
        add(jtf);

        //跳转游戏规则
        b1.addActionListener(e -> {
            close();
            new HelpPanel();
        });

        //跳转游戏界面
        b2.addActionListener(e -> {
            // 检查玩家是否在输入框中输入自己de名字
            if (!jtf.getText().isEmpty()) {
                Frame.playerName = jtf.getText();

                close();
                new GamePanel();
            }
        });
        updateUI();
    }
}
