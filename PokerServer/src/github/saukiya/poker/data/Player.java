package github.saukiya.poker.data;

import github.saukiya.poker.Server;
import github.saukiya.poker.socket.PlayerSocket;

import java.net.Socket;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.IntStream;

public class Player {

    public PlayerSocket socket;

    public final ArrayList<Poker> pokers = new ArrayList<>();

    public UUID uuid;

    public String name;

    public Poker currentPoker;

    public boolean giveUp;

    public Player(Socket socket) {
        Server.players.add(this);
        this.socket = new PlayerSocket(this, socket);
        this.socket.start();
        this.uuid = UUID.randomUUID();
    }

    public void startGame(ArrayList<Poker> pokerLib) {
        IntStream.range(0, 5).forEach(b -> {
            Poker poker = pokerLib.get((int) (Math.random() * pokerLib.size()));
            pokers.add(poker);
            pokerLib.remove(poker);
        });
    }

    /**
     * 删除手牌
     * 因为不和pokers中的牌同一个对象(由PlayerSocket通信中生成的Poker) 所以只能用if判断
     *
     * @param removePoker
     */
    public boolean takePoker(Poker removePoker) {
        return pokers.removeIf(poker -> poker.value.equals(removePoker.value) && poker.suit == removePoker.suit);
    }

    @Override
    public String toString() {
        return MessageFormat.format("name:{0},size:{1}", name, pokers.size());
    }
}
