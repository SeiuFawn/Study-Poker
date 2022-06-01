package github.seiufawn.poker;

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
        players.add(new Player(UUID.randomUUID(), "P1"));
        players.add(new Player(UUID.randomUUID(), "P2"));
        players.add(new Player(UUID.randomUUID(), "P3"));

        //创建牌组
        ArrayList<Poker> pokerLib = build();
        //洗牌
        Collections.shuffle(pokerLib);
        for (Player player : players) {
            player.start(pokerLib);
            player.pokers.sort(Comparator.comparingInt(o -> o.suit.value));
            player.pokers.sort(Comparator.comparingInt(o -> o.value));
        }
        for (Player player : players) {
            System.out.println(player.name + " 的手牌是：" + player.pokers.stream().map(poker ->
                    poker.suit.show + poker.name + " ").collect(Collectors.joining()));
        }

        pokerLib.sort(Comparator.comparingInt(o -> o.suit.value));
        pokerLib.sort(Comparator.comparingInt(o -> o.value));
        System.out.println("牌库剩余卡牌: ");
        System.out.println(pokerLib.stream().map(poker ->
                poker.suit.show + poker.name + " "
        ).collect(Collectors.joining()));

    }

    /**
     * 创建牌库（并赋值）
     */
    public static ArrayList<Poker> build() {
        ArrayList<Poker> pokerLib = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            switch (i) {
                case 15: {
                    pokerLib.add(new Poker("大王", i, Suit.king));
                    break;
                }
                case 14: {
                    pokerLib.add(new Poker("小王", i, Suit.king));
                    break;
                }
                case 13: {
                    int finalI = i;
                    //生成四张牌
                    IntStream.range(0, 4).forEach(b -> pokerLib.add(new Poker("K", finalI, getSuit(b))));
                    break;
                }
                case 12: {
                    int finalI = i;
                    //生成四张牌
                    IntStream.range(0, 4).forEach(b -> pokerLib.add(new Poker("Q", finalI, getSuit(b))));
                    break;
                }
                case 11: {
                    int finalI = i;
                    //生成四张牌
                    IntStream.range(0, 4).forEach(b -> pokerLib.add(new Poker("J", finalI, getSuit(b))));
                    break;
                }
                case 1: {
                    int finalI = i;
                    //生成四张牌
                    IntStream.range(0, 4).forEach(b -> pokerLib.add(new Poker("A", finalI, getSuit(b))));

                    break;
                }
                default: {
                    int finalI = i;
                    IntStream.range(0, 4).forEach(b -> pokerLib.add(new Poker(String.valueOf(finalI), finalI, getSuit(b))));
                    break;
                }
            }
        }
        return pokerLib;
    }

    /**
     * 获取花色并返回
     *
     * @param value
     * @return Suit
     */
    public static Suit getSuit(int value) {
        switch (value) {
            case 0: {
                return Suit.spade;
            }
            case 1: {
                return Suit.heart;
            }
            case 2: {
                return Suit.diamond;
            }
            case 3: {
                return Suit.club;
            }
            default: {
                break;
            }
        }
        return Suit.error;
    }

    /**
     * 比较两个牌的大小(游戏中的)
     *
     * @param poker1 牌A
     * @param poker2 牌B
     * @return 1 - A 大于 B, 0 - 等于, -1 - A 小于 B
     */
    public static int compare(Poker poker1, Poker poker2) {
        // 获取牌类型
        int pokerType1 = getType(poker1);
        int pokerType2 = getType(poker2);

        // 在打牌的过程中 功能牌始终可以打出去
        List<Integer> specials = Arrays.asList(3, 8, 5, 10, 14, 15);
        switch (pokerType1) {
            // A2 1
            case 2:
                return 1;
            // A1是特殊牌
            // A1 B1 （15>14>10>5>8>3）
            // A1 B0 （1，A为10, -1）
            case 1:
                if (pokerType2 == 1) {
                    int index1 = specials.indexOf(poker1.value);
                    int index2 = specials.indexOf(poker2.value);
                    return Integer.compare(index1, index2);
                }
                if (poker1.value == 10) return -1;
                return 1;
            // A1是普通牌
            // A0 B1 （-1，B为10，1）
            // A0 B0 （正常排序）
            default:
                if (pokerType2 == 0) {
                    return Integer.compare(poker1.value, poker2.value);
                }
                if (poker2.value == 10) return 1;
                return -1;
        }
    }

    /**
     * 获取牌类型
     *
     * @param poker 牌对象
     * @return 2 - 功能牌(??), 1 - 特殊牌(仙人), 0 - 普通牌(妖怪)
     */
    public static int getType(Poker poker) {
        switch (poker.value) {
            // 如果是功能牌
            case 1:
            case 2:
                return 2;
            // 如果是特殊牌
            case 15:
            case 14:
            case 10:
            case 8:
            case 5:
            case 3:
                return 1;
            //如果是普通牌
            default:
                return 0;
        }
    }
}
