package github.saukiya.poker.socket;

import github.saukiya.poker.Server;
import github.saukiya.poker.data.Player;
import github.saukiya.poker.data.Poker;

import java.io.*;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerSocket extends Thread {

    // 客户端链接
    public Socket socket;

    // 玩家
    public Player player;

    // 写入流
    BufferedWriter writer;

    // 输入流
    BufferedReader reader;

    public PlayerSocket(Player player, Socket socket) {
        this.player = player;
        this.socket = socket;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playerRound(String playerName) {
        sendMessage("playerRound", playerName);
    }

    public void setLastPoker(String playerName, Poker poker) {
        sendMessage("setLastPoker", playerName, poker.toString());
    }

    public void setPlayers(List<Player> players) {
        String[] args = players.stream().map(Player::toString).toArray(String[]::new);
        sendMessage("setPlayers", args);
    }

    public void setPokers(List<Poker> pokers) {
        String[] args = pokers.stream().map(Poker::toString).toArray(String[]::new);
        sendMessage("setPokers", args);
    }

    public void showMessage(String message) {
        sendMessage("showMessage", message);
    }

    public void back() {
        sendMessage("back");
    }

    public void exit() {
        sendMessage("exit");
    }

    /**
     * 发送消息给客户端
     *
     * @param type 消息类型
     * @param args 参数
     */
    protected void sendMessage(String type, String... args) {
        String message = MessageFormat.format("[{0}] {1}\n", type, String.join(" ", args));
        try {
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("send Message Error: " + message);
        }
    }

    /**
     * TODO 处理客户端消息逻辑
     *
     * @param type 类型
     * @param args 参数
     */
    protected void inputMessage(String type, String[] args) throws IOException {
        switch (type) {
            case "setName":
                if (args.length > 0)
                player.name = args[0];
                Server.setPlayers();
                break;
            default:
                System.out.println("input type error[" + player.name + "]: " + type);
        }
    }

    /**
     * 处理服务端消息逻辑
     */
    @Override
    public void run() {
        // 定义接收规则 "[类型] 消息"
        Pattern p = Pattern.compile("\\[(.+?)] ?(.+?)");
        String input;
        try {
            while (socket != null && (input = reader.readLine()) != null) {
                Matcher matcher = p.matcher(input);
                if (matcher.matches() && matcher.groupCount() > 1) {
                    String[] args = matcher.groupCount() > 2 ? matcher.group(2).split(" ") : new String[0];
                    System.out.println("input[" + player.name + "]: " + matcher.group(1) + " -> " + Arrays.toString(args));
                    inputMessage(matcher.group(1), args);
                } else {
                    System.out.println("input unknown[" + player.name + "]: " + input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("input[" + player.name + "] end");
    }
}
