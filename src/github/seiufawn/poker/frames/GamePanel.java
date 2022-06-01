package github.seiufawn.poker.frames;

import github.seiufawn.poker.Frame;
import github.seiufawn.poker.Player;
import github.seiufawn.poker.Poker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GamePanel extends PanelBase {

    int defaultPokerSize = 5;
    int pokerSelectIndex = -1;

    // 自己的手牌
    java.util.List<Poker> pokers = new ArrayList<>();

    // 自己的手牌按键
    java.util.List<JButton> btnPokers = new ArrayList<>();

    // 玩家信息
    java.util.List<JLabel[]> playerInfos;

    // 显示提示
    JLabel showTips;

    // 显示提示计时器
    Timer showTipsTimer;

    // 上一张牌
    Poker lastPoker;

    // 上一张牌按钮
    JButton btnLastPoker;

    public GamePanel() {
        super("游戏界面");
        setLayout(null);

        showTips = addLabel("", 360, 330, 300, 20);
        showTips.setVisible(false);
        add(showTips);

        JButton btnPlayPoker = new JButton("出牌");
        btnPlayPoker.setBounds(275, 350, 100, 50);
        btnPlayPoker.setFont(Frame.BOLD_20);
        btnPlayPoker.setForeground(Color.WHITE);
        btnPlayPoker.setBackground(Color.GRAY);
        add(btnPlayPoker);

        JButton btnGiveUp = new JButton("放弃");
        btnGiveUp.setBounds(475, 350, 100, 50);
        btnGiveUp.setFont(Frame.BOLD_20);
        btnGiveUp.setForeground(Color.WHITE);
        btnGiveUp.setBackground(Color.GRAY);
        add(btnGiveUp);

        btnLastPoker = new JButton(":)");
        btnLastPoker.setBounds(340, 200, 80, 120);
        btnLastPoker.setFont(Frame.BOLD_20);
        btnLastPoker.setBackground(Color.white);
        btnLastPoker.setVisible(false);
        add(btnLastPoker);

        // 初始化玩家信息文本块
        playerInfos = Arrays.asList(
                new JLabel[]{
                        addLabel("1", 30, 360, 300, 20),
                        addLabel("", 30, 380, 300, 20)
                },
                new JLabel[]{
                        addLabel("2", 120, 180, 300, 20),
                        addLabel("", 120, 200, 300, 20)
                },
                new JLabel[]{
                        addLabel("3", 400, 20, 300, 20),
                        addLabel("", 400, 40, 300, 20)
                },
                new JLabel[]{
                        addLabel("4", 680, 180, 300, 20),
                        addLabel("", 680, 200, 300, 20)
                }
        );
        playerInfos.forEach(playerInfo -> playerInfo[0].setFont(Frame.BOLD_20));

//        addLabel("当前出牌的是：", 370, 160, 300, 20);
        addLabel("上家打出的手牌:", 360, 140, 300, 20);

        setPokerBtnSize(defaultPokerSize);

        btnPlayPoker.addActionListener(e -> {
            // TODO 出牌逻辑
            if (pokerSelectIndex == -1) {
                showTips("请选择要出的牌");
            } else {
                defaultPokerSize--;
                remove(btnPokers.get(pokerSelectIndex)); // TODO 边界
                setPokerBtnSize(defaultPokerSize);
                pokerSelectIndex = -1;
                addLabel("请选择要出的牌", 360, 330, 300, 20).setVisible(false);
                updateUI();
            }
        });

        btnGiveUp.addActionListener(e -> {
            // TODO 放弃逻辑
            defaultPokerSize++;
            setPokerBtnSize(defaultPokerSize);
        });

    }

    // 展示提示
    public void showTips(String message) {
        showTips.setText(message);
        showTips.setVisible(true);
        updateUI();
        if (showTipsTimer != null) {
            showTipsTimer.stop();
        }
        //设置1.5s后消失
        showTipsTimer = new Timer(1500, e -> {
            showTips.setVisible(false);
            updateUI();
            showTipsTimer = null;
        });
        showTipsTimer.start();
    }

    // 设置玩家名
    public void setPlayerInfo(java.util.List<Player> players) {
        int myIndex = -1;
        // 获取自身位置
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player.name.equals(Frame.playerName)) {
                myIndex = i;
                break;
            }
        }
        if (myIndex == -1) return;
        // 变换 players
        for (int index = myIndex - 1; index >= 0; index--) {
            players.add(players.remove(index));
        }
        // 幅值
        for (int i = 0; i < playerInfos.size(); i++) {
            JLabel[] playerInfo = playerInfos.get(i);
            playerInfo[0].setText("玩家 " + players.get(i).name);
            playerInfo[1].setText("手牌数: " + players.get(i).pokerSize);
        }
    }

    // 设置上一张手牌
    public void setLastPoker(Poker poker) {
        lastPoker = poker;
        btnLastPoker.setVisible(poker != null);
        if (poker != null) {
            btnLastPoker.setText(poker.name);
        }
    }

    // 设置牌按钮控件
    public void setPokerBtnSize(int size) {
        // 清空Button控件
        btnPokers.forEach(this::remove); // 简化for循环 但是有限制 目前这个情况是可用的
        btnPokers.clear();
        // 重新生成牌队列所有元素
        int x = 430 - (int) (size / 2D * 80);
        // TODO 采用对象池技术可以进一步优化
        for (int i = 0; i < size; i++) {
            JButton btn = new JButton(String.valueOf(i));
            add(btn);
            btnPokers.add(btn);
            btn.setFont(Frame.BOLD_20);
            btn.setBounds(x + 80 * i, 420, 70, 120);
            btn.setVisible(true);
            btn.setBackground(Color.LIGHT_GRAY);
            int finalI = i;
            btn.addActionListener(e -> {
                //TODO 点击牌的逻辑
                int lastSelectIndex = pokerSelectIndex;
                if (pokerSelectIndex == finalI) {
                    pokerSelectIndex = -1;
                } else {
                    pokerSelectIndex = finalI;
                    Random random = new Random();
                    btn.setBackground(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    btn.setForeground(new Color(
                            255 - btn.getBackground().getRed(),
                            255 - btn.getBackground().getGreen(),
                            255 - btn.getBackground().getBlue()
                    ));
                }
                if (lastSelectIndex != -1) {
                    btnPokers.get(lastSelectIndex).setBackground(Color.LIGHT_GRAY);
                    btnPokers.get(lastSelectIndex).setForeground(Color.BLACK);
                }
            });
        }
        //更新Panel面板
        updateUI();
    }
}
