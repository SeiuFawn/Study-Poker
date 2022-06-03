package github.seiufawn.poker.socket;

import github.seiufawn.poker.Frame;
import github.seiufawn.poker.Player;
import github.seiufawn.poker.Poker;
import github.seiufawn.poker.frames.GamePanel;
import github.seiufawn.poker.frames.HomePanel;

import java.io.*;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 客户端通信工具类
 */
public class ClientSocket extends Thread {

    public static String host = "127.0.0.1";

    // 服务器链接
    public Socket socket;

    // 游戏界面主体
    public GamePanel handler;

    // 写入流
    BufferedWriter writer;

    // 输入流
    BufferedReader reader;

    public ClientSocket(GamePanel handler) {
        this.handler = handler;
        try {
            socket = new Socket(host, 23333);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sendMessage("setName", Frame.playerName);
        } catch (IOException e) {
            e.printStackTrace();
            // 链接失败 退出程序
            System.exit(0);
        }
    }

    public void sendPoker(Poker poker) {
        sendMessage("sendPoker" , poker.toString());
    }

    public void giveUp() {
        sendMessage("giveUp");
    }

    /**
     * 发送消息给服务器
     *
     * @param type 消息类型
     * @param args 参数
     */
    protected void sendMessage(String type, String... args) {
        String message = MessageFormat.format("[{0}] {1}\n", type, String.join(" ", args));
        try {
            System.out.println("send:" + message);
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("send Message Error: " + message);
        }
    }

    /**
     * 处理服务端消息逻辑
     *
     * @param type 消息类型
     * @param args 参数
     */
    protected void inputMessage(String type, String[] args) throws IOException {
        // 根据类型处理数据
        switch (type) {
            // 切换回合 - 玩家名
            case "playerRound":
                if (args.length > 0) {
                    handler.playerRound(args[0]);
                }
                break;
            // 玩家打出牌 - 玩家名 牌
            case "setLastPoker":
                if (args.length > 1) {
                    // 将 args[1] 转成一个Poker
                    handler.setLastPoker(args[0], Poker.FormString(args[1]));
                } else {
                    handler.setLastPoker(null, null);
                }
                break;
            // 设置玩家 - 玩家1 玩家2 玩家3 玩家4
            case "setPlayers":
                List<Player> players = new ArrayList<>();
                for (String arg : args) {
                    // 将每个arg转换成一个Player
                    players.add(Player.FormString(arg));
                }
                handler.setPlayers(players);
                break;
            // 设置手牌 - 牌1 牌2 牌3 牌4 5678...
            case "setPokers":
                List<Poker> pokers = new ArrayList<>();
                for (String arg : args) {
                    // 将 arg 转成一个Poker
                    Poker poker = Poker.FormString(arg);
                    if (poker != null)
                    pokers.add(poker);
                }
                handler.setPokers(pokers);
                break;
            // 显示消息
            case "showMessage":
                handler.showMessage(String.join(" ", args));
                break;
            // 返回主界面
            case "back":
                handler.close();
                new HomePanel();
                break;
            // 退出程序
            case "exit":
                System.exit(0);
                break;
            default:
                System.out.println("input type error: " + type);
        }
    }

    /**
     * 处理服务端消息逻辑
     */
    @Override
    public void run() {
        // 定义接收规则 "[类型] 消息"
        Pattern p = Pattern.compile("\\[(.+?)] ?(.*?)");
        String input;
        try {
            while (socket != null && (input = reader.readLine()) != null) {
                Matcher matcher = p.matcher(input);
                if (matcher.matches() && matcher.groupCount() > 1) {
                    String[] args = matcher.group(2).split(" ");
                    System.out.println("input: " + matcher.group(1) + " -> " + Arrays.toString(args));
                    inputMessage(matcher.group(1), args);
                } else {
                    System.out.println("input unknown: " + input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("input end");
    }

    public void close() throws IOException {
        socket.shutdownOutput();
        socket.shutdownInput();
        reader.close();
        writer.close();
        socket.close();
    }
}
