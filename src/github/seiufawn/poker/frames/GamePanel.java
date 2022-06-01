package github.seiufawn.poker.frames;

import github.seiufawn.poker.Frame;

import javax.swing.*;

public class GamePanel extends PanelBase {

    public GamePanel(){
        super("游戏界面");
        setLayout(null);

        JButton btnPlayPoker = new JButton("出牌");
        JButton btnGiveUp = new JButton("放弃");

        btnPlayPoker.setBounds(280, 330, 100, 50);
        btnPlayPoker.setFont(Frame.BLOD_20);
        btnGiveUp.setBounds(480, 330, 100, 50);
        btnGiveUp.setFont(Frame.BLOD_20);

        add(btnPlayPoker);
        add(btnGiveUp);

        int defaultPokerSize = 5;

        //TODO 换成List
        JButton[] poker = new JButton[defaultPokerSize]; //{null, null, null, null, null}
        for (int i = 0; i < defaultPokerSize; i++) {
            poker[i] = new JButton("2");
            poker[i].setFont(Frame.PLAIN_15);
            poker[i].setBounds((50+80*i), 400, 70, 120);
            add(poker[i]);
            poker[i].addActionListener(e -> {
                //TODO 点击牌的逻辑
            });
        }
        for (JButton jButton : poker) {
            jButton.setText("牌名");
            jButton.setVisible(true); // 不显示牌
        }

        btnPlayPoker.addActionListener(e -> {
            // TODO 出牌逻辑
        });

        btnGiveUp.addActionListener(e -> {
            // TODO 放弃逻辑

        });

    }
}
