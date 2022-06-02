package github.saukiya.poker;

import github.saukiya.poker.data.Player;
import github.saukiya.poker.data.Poker;
import github.saukiya.poker.data.Suit;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    int s = 2;

    /**
     * 主函数
     */
    public static void main(String[] args) {
        //创建玩家
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(null));
        players.add(new Player(null));
        players.add(new Player(null));

        //创建牌组
        ArrayList<Poker> pokerLib = Poker.build();
        for (Player player : players) {
            player.startGame(pokerLib);
            player.pokers.sort(Comparator.comparingInt(o -> o.suit.value));
            player.pokers.sort(Comparator.comparingInt(o -> o.value));
        }
        for (Player player : players) {
            System.out.println(player.name + " 的手牌是：" + player.pokers.stream().map(poker ->
                    poker.suit.show + poker.value + " ").collect(Collectors.joining()));
        }

        pokerLib.sort(Comparator.comparingInt(o -> o.suit.value));
        pokerLib.sort(Comparator.comparingInt(o -> o.value));
        System.out.println("牌库剩余卡牌: ");
        System.out.println(pokerLib.stream().map(poker ->
                poker.suit.show + poker.value + " "
        ).collect(Collectors.joining()));
    }
}
