package github.seiufawn.poker.frames;

import github.seiufawn.poker.Frame;
import github.seiufawn.poker.Player;
import github.seiufawn.poker.Poker;
import github.seiufawn.poker.socket.ClientSocket;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class GamePanel extends PanelBase {

    ClientSocket socket;

    int defaultPokerSize = 5;
    int pokerSelectIndex = -1;

    java.util.List<JButton> btnLogic = new ArrayList<>();

    // 自己的手牌
    java.util.List<Poker> pokers = new ArrayList<>();

    // 自己的手牌按键
    java.util.List<JButton> btnPokers = new ArrayList<>();

    // 玩家信息
    java.util.List<JLabel[]> playerInfos;

    // 显示提示
    JLabel showMessage;

    // 显示提示
    JLabel showTips;

    JLabel playerRound;

    // 上一张牌
    Poker lastPoker;

    // 上一张牌按钮
    JButton btnLastPoker;

    public GamePanel() {
        super("游戏界面");
        setLayout(null);

        // 初始化提示
        showTips = addLabel("", 360, 330, 300, 20);
        showTips.setVisible(false);
        add(showTips);

        // 初始化消息
        showMessage = addLabel("", 200, 100, 600, 20);
        showMessage.setVisible(false);
        showMessage.setFont(Frame.BOLD_20);
        add(showMessage);

        // 初始化出牌和放弃
        JButton btnPlayPoker = new JButton("出牌");
        btnPlayPoker.setBounds(275, 350, 100, 50);
        btnPlayPoker.setFont(Frame.BOLD_20);
        btnPlayPoker.setForeground(Color.WHITE);
        btnPlayPoker.setBackground(Color.GRAY);
        add(btnPlayPoker);
        btnLogic.add(btnPlayPoker);
        btnPlayPoker.addActionListener(e -> {
            // 出牌逻辑
            if (pokerSelectIndex == -1) {
                showTips("请选择要出的牌");
            } else {
                defaultPokerSize--;
                // 发送消息: 出牌
                socket.sendPoker(pokers.get(pokerSelectIndex));
                // 重置颜色
                btnPokers.get(pokerSelectIndex).setBackground(Color.LIGHT_GRAY);
                // 先本地更新一下牌组
                pokerSelectIndex = -1;
            }
        });
        JButton btnGiveUp = new JButton("放弃");
        btnGiveUp.setBounds(475, 350, 100, 50);
        btnGiveUp.setFont(Frame.BOLD_20);
        btnGiveUp.setForeground(Color.WHITE);
        btnGiveUp.setBackground(Color.GRAY);
        add(btnGiveUp);
        btnLogic.add(btnGiveUp);
        btnGiveUp.addActionListener(e -> {
            // 放弃逻辑
            // 发送消息: 放弃
            socket.giveUp();
        });
        btnLogic.forEach(jbtn -> jbtn.setVisible(false));

        // 初始化当前牌
        btnLastPoker = new JButton(":)");
        btnLastPoker.setBounds(340, 200, 80, 120);
        btnLastPoker.setFont(Frame.BOLD_12);
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

        playerRound = addLabel("当前回合：{playerName}", 370, 160, 300, 20);
        playerRound.setVisible(false);
        addLabel("上家打出的手牌:", 360, 140, 300, 20);

        // 建立连接
        socket = new ClientSocket(this);
        socket.start();

    }

    // 展示提示
    public void showTips(String message) {
        showTips.setText(message);
        showTips.setVisible(true);
        updateUI();
        //设置1.5s后消失
        setDelayAction("ShowMessage", 1500, () -> {
            showTips.setVisible(false);
            updateUI();
        });
    }

    // 展示提示
    public void showMessage(String message) {
        showMessage.setText(message);
        showMessage.setVisible(true);
        updateUI();
        //设置1.5s后消失
        setDelayAction("ShowMessage", 1500, () -> {
            showMessage.setVisible(false);
            updateUI();
        });
    }

    // 设置玩家名
    public void setPlayers(java.util.List<Player> players) {
        // 判断人齐了
        boolean done = playerInfos.size() == players.size();

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
        // 给JLabel赋值
        for (int i = 0; i < playerInfos.size(); i++) {
            JLabel[] playerInfo = playerInfos.get(i);
            Player player = players.size() > i ? players.get(i) : null;
            if (player != null) {
                playerInfo[0].setText("玩家 " + player.name);
                playerInfo[1].setText("手牌数: " + player.size);
                // 如果人齐了 才显示
                playerInfo[1].setVisible(done);
            } else {
                playerInfo[0].setText("等待中 ");
            }
        }
    }

    // 设置上一张手牌
    public void setLastPoker(String playerName, Poker poker) {
        lastPoker = poker;
        btnLastPoker.setVisible(poker != null);
        if (poker != null) {
            btnLastPoker.setText(poker.getName());
        }
    }

    // 轮到哪个玩家的回合
    public void playerRound(String playerName) {
        // 如果是自己的回合则显示按钮
        boolean showBtnLogic = playerName.equals(Frame.playerName) && pokers.size() != 0;
        btnLogic.forEach(btn -> btn.setVisible(showBtnLogic));

        // 显示当前回合的玩家
        playerRound.setText("当前回合：" + playerName);
        playerRound.setVisible(true);
    }

    // 设置牌
    public void setPokers(java.util.List<Poker> list) {
        Collections.sort(list);
        pokers = list;
        int size = pokers.size();
        if (size == 0) {
            btnLogic.forEach(btn -> btn.setVisible(false));
        }
        // 清空Button控件
        btnPokers.forEach(this::remove); // 简化for循环 但是有限制 目前这个情况是可用的
        btnPokers.clear();
        // 重新生成牌队列所有元素
        int x = 430 - (int) (size / 2D * 90);
        // TODO 采用对象池技术可以进一步优化
        for (int i = 0; i < size; i++) {
            JButton btn = new JButton(pokers.get(i).getName());
            add(btn);
            btnPokers.add(btn);
            btn.setFont(Frame.BOLD_12);
            btn.setBounds(x + 90 * i, 420, 80, 120);
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
                }
                if (lastSelectIndex != -1) {
                    btnPokers.get(lastSelectIndex).setBackground(Color.LIGHT_GRAY);
                }
            });
        }
        //更新Panel面板
        updateUI();
    }

    @Override
    public void close() {
        super.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
