package github.saukiya.poker;

import github.saukiya.poker.data.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    static List<Player> players = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(23333);
            // TODO 等待玩家
            while (players.size() < 4) {
                // 服务器等待连接 然后传入Player构造器
                Player player = new Player(serverSocket.accept());
                players.add(player);
                setPlayers();
                System.out.println("新增玩家 -> " + player.socket.socket.getInetAddress() + ":" + player.socket.socket.getPort());
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

}
