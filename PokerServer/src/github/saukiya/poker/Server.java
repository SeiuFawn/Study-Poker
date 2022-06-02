package github.saukiya.poker;

import github.saukiya.poker.data.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static List<Player> players = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(23333);
            // TODO 等待玩家
            while (players.size() < 4) {
                // 服务器等待连接 然后传入Player构造器
                System.out.println("JoinPlayer -> " +  new Player(serverSocket.accept()).socket.getIpAndPort());
            }
            System.out.println("玩家人齐了 开始抽卡");
            while (players.size() == 4) {
                // 这里处理逻辑?

            }
        } catch (IOException e) {
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

}
