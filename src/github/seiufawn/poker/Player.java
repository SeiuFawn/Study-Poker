package github.seiufawn.poker;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.IntStream;

public class Player {

    public final ArrayList<Poker> pokers = new ArrayList<>();
    public UUID uuid;
    public String name;

    Player(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public void start(ArrayList<Poker> pokerLib) {
        IntStream.range(0, 5).forEach(b -> {
            Poker poker = pokerLib.get((int) (Math.random() * pokerLib.size()));
            pokers.add(poker);
            pokerLib.remove(poker);
        });
    }
}
