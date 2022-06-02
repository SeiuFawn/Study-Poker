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

    public Player(Socket socket) {
        Server.players.add(this);
        this.socket = new PlayerSocket(this, socket);
        this.socket.start();
        this.uuid = UUID.randomUUID();
        this.name = uuid.toString();
    }

    public void startGame(ArrayList<Poker> pokerLib) {
        IntStream.range(0, 5).forEach(b -> {
            Poker poker = pokerLib.get((int) (Math.random() * pokerLib.size()));
            pokers.add(poker);
            pokerLib.remove(poker);
        });
    }

    public void givePoker(Poker poker) {
        pokers.add(poker);
    }

    @Override
    public String toString() {
        return MessageFormat.format("name:{0},size:{1}", name, pokers.size());
    }
}
