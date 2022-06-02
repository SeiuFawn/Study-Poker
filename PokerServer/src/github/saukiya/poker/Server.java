package github.saukiya.poker;

import github.saukiya.poker.data.Player;
import github.saukiya.poker.data.Poker;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Server {

    public static List<Player> players = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(23333);

            while (true) {

                // TODO 等待玩家
                while (players.size() < 4) {
                    // 服务器等待连接 然后传入Player构造器
                    System.out.println("JoinPlayer -> " +  new Player(serverSocket.accept()).socket.getIpAndPort());
                }
                System.out.println("玩家人齐了 开始抽卡");
                // 创建卡堆 洗牌
                List<Poker> pokers = Poker.createPokers();
                Collections.shuffle(pokers);

                // 从卡池中发卡
                IntStream.range(0, 5).forEach(i -> players.forEach(player -> player.pokers.add(pokers.remove(0))));
                for (Player player : players) {
                    player.socket.setPokers(player.pokers);
                    player.socket.setPlayers(players);
                }

                broadcastMessage("游戏开始!");

                // 玩家回合
                int playerIndex = 0;
                // 发排顺序 true为顺序 false为逆序
                boolean order = true;
                // 当前玩家
                Player currentPlayer;
                // 上一张牌
                Poker lastPoker = null;
                // 进入回合循环
                game:
                while (players.size() == 4) {
                    // 通知该玩家回合
                    currentPlayer = players.get(playerIndex);
                    // 清空玩家当前牌 / 放弃状态
                    currentPlayer.currentPoker = null;
                    currentPlayer.giveUp = false;
                    // 通知回合
                    playerRound(currentPlayer);

                    // 等待牌 / 放弃
                    while (true) {
                        // 等待牌
                        if (currentPlayer.currentPoker != null) {
                            // 如果不比场中的牌大 则发送信息给该玩家
                            if (Poker.compare(currentPlayer.currentPoker, lastPoker) != 1) {
                                currentPlayer.socket.showMessage("该牌不符合要求!");
                                currentPlayer.currentPoker = null;
                                // 回到等待牌逻辑
                                continue;
                            }

                            // 判定生效 刷新当前玩家的手牌 以及通知其他玩家手牌数
                            currentPlayer.takePoker(currentPlayer.currentPoker);
                            currentPlayer.socket.setPokers(currentPlayer.pokers);
                            setPlayers();

                            // 如果是功能牌
                            if (currentPlayer.currentPoker.getType() == 2) {
                                switch (currentPlayer.currentPoker.value) {
                                    case 1:
                                        broadcastMessage("玩家使用了 " + currentPlayer.currentPoker.getName() + " 调换了出牌顺序~");
                                        order = !order;
                                        break;
                                    case 2:
                                        broadcastMessage("玩家使用了 " + currentPlayer.currentPoker.getName() + " 跳过了自己的回合~");
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                // 如果不是功能牌 设置lastPoker
                                lastPoker = currentPlayer.currentPoker;
                                setLastPoker(currentPlayer.name, lastPoker);
                                broadcastMessage("玩家打出了 " + currentPlayer.currentPoker.getName() + " 到下个玩家");
                            }

                            // 如果当前玩家手牌数等于0 则判定当前玩家胜出 并且break出循环
                            if (currentPlayer.pokers.size() == 0) {
                                Thread.sleep(5000);
                                broadcastMessage("玩家 " + currentPlayer.name + " 赢了~");
                                break game;
                            } else {
                                break;
                            }
                        }
                        // 如果放弃出牌 则从卡池中抽一张牌给当前玩家
                        else if(currentPlayer.giveUp) {
                            // 先判断卡池的卡还有没有
                            if (pokers.size() == 0) {
                                broadcastMessage("没有卡发啦 游戏平局!");
                                // 跳出到game
                                break game;
                            }
                            // 清空 lastPoker
                            lastPoker = null;
                            setLastPoker(null, null);

                            // 发卡给该玩家
                            currentPlayer.pokers.add(pokers.remove(0));
                            currentPlayer.socket.setPokers(currentPlayer.pokers);
                            broadcastMessage(currentPlayer.name + " 放弃该回合");
                            break;
                        }
                        else if (players.size() != 4) {
                            // 等待牌过程中玩家人数少于4 退出游戏逻辑
                            break game;
                        }
                        else {
                            Thread.sleep(100);
                        }
                    }

                    // 设置下一个玩家的索引
                    if (order) {
                        if (++playerIndex == 4) playerIndex = 0;
                    } else {
                        if (--playerIndex == -1) playerIndex = 3;
                    }
                    // 睡大觉
                    Thread.sleep(3000);
                }
                // 如果是因为玩家减少而退出游戏流程 则通知其他玩家
                if (players.size() != 4) {
                    broadcastMessage("因玩家退出 游戏终止");
                }

                // 游戏结束 重置当前已存在的player的Pokers
                players.forEach(p -> {
                    p.pokers.clear();
                    p.socket.setPokers(p.pokers);
                });
                setPlayers();
                setLastPoker(null, null);
                // 回到上面的 while 循环 继续等待玩家或者直接开始
                Thread.sleep(4000);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("服务器寄了!");
            e.printStackTrace();
        }
    }

    public static void setPlayers() {
        players.forEach(player -> player.socket.setPlayers(players));
    }

    public static void tackPlayer(Player player) {
        players.remove(player);
        players.forEach(p -> p.socket.setPlayers(players));
    }

    public static void broadcastMessage(String message) {
        players.forEach(player -> player.socket.showMessage(message));
    }

    public static void playerRound(Player currentPlayer) {
        players.forEach(player -> player.socket.playerRound(currentPlayer.name));
    }

    public static void setLastPoker(String playerName, Poker poker) {
        players.forEach(player -> player.socket.setLastPoker(playerName, poker));
    }

}
