package github.saukiya.poker.data;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Poker implements Comparable<Poker> {
    // 特殊牌
    final static List<Integer> SPECIALS = Arrays.asList(3, 8, 5, 10, 14, 15);

    //牌的名字/大小/花色
    public Integer value;
    public Suit suit;

    public Poker(Integer value, Suit suit) {
        this.suit = suit;
        this.value = value;
    }

    public String getName() {
        String name;
        switch (value) {
            case 15:
                return "大王";
            case 14:
                return "小王";
            case 13:
                name = "K";
                break;
            case 12:
                name = "Q";
                break;
            case 11:
                name = "J";
                break;
            case 1:
                name = "A";
                break;
            default:
                name = String.valueOf(value);
                break;
        }
        return suit.show + name;
    }

    @Override
    public int compareTo(Poker o) {
        int valueCompareTo = Integer.compare(value, o.value);
        return valueCompareTo != 0 ? valueCompareTo : Integer.compare(suit.ordinal(), o.suit.ordinal());
    }

    @Override
    public String toString() {
        return MessageFormat.format("value:{0},suit:{1}", value, suit);
    }

    /**
     * 字符串转Poker
     *
     * @param string 字符串
     * @return Poker 失败则是null
     */
    public static Poker FormString(String string) {
        Poker poker = new Poker(null, null);
        for (String s : string.split(",")) {
            String[] args = s.split(":");
            if (args.length > 1) {
                switch (args[0]) {
                    case "value":
                        poker.value = Integer.parseInt(args[1]);
                        break;
                    case "suit":
                        poker.suit = Suit.valueOf(args[1]);
                        break;
                }
            }
        }
        if (poker.value != null && poker.suit != null) {
            return poker;
        } else {
            return null;
        }
    }

    /**
     * 创建牌库（并赋值）
     */
    public static ArrayList<Poker> createPokers() {
        ArrayList<Poker> pokerLib = new ArrayList<>();
        for (int i = 15; i > 0; i--) {
            switch (i) {
                case 15:
                case 14:
                    pokerLib.add(new Poker(i, Suit.king));
                    break;
                default:
                    int finalI = i;
                    IntStream.range(0, 4).forEach(b -> pokerLib.add(new Poker(finalI, Suit.values()[b])));
                    break;
            }
        }
        return pokerLib;
    }

    /**
     * 获取牌类型
     *
     * @return 2 - 功能牌(??), 1 - 特殊牌(仙人), 0 - 普通牌(妖怪)
     */
    public int getType() {
        switch (value) {
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


    /**
     * 比较两个牌的大小(游戏中的)
     *
     * @param poker1 准备要打的牌
     * @param poker2 场中的牌
     * @return 1 - A 大于 B, 0 - 等于, -1 - A 小于 B
     */
    public static int compare(Poker poker1, Poker poker2) {
        // 如果场中没有牌 则直接返回1;
        if (poker2 == null) return 1;
        // 获取牌类型
        int centerPokerType = poker2.getType();

        // 在打牌的过程中 功能牌始终可以打出去
        switch (poker1.getType()) {
            // A2 1
            case 2:
                return 1;
            // A1是特殊牌
            // A1 B1 （15>14>10>5>8>3）
            // A1 B0 （1，A为10, -1）
            case 1:
                if (centerPokerType == 1) {
                    int index1 = SPECIALS.indexOf(poker1.value);
                    int index2 = SPECIALS.indexOf(poker2.value);
                    return Integer.compare(index1, index2);
                }
                if (poker1.value == 10) return -1;
                return 1;
            // A1是普通牌
            // A0 B1 （-1，B为10，1）
            // A0 B0 （正常排序）
            default:
                if (centerPokerType == 0) {
                    return Integer.compare(poker1.value, poker2.value);
                }
                if (poker2.value == 10) return 1;
                return -1;
        }
    }
}
