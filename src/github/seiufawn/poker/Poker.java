package github.seiufawn.poker;


import java.text.MessageFormat;

public class Poker implements Comparable<Poker> {
    //牌的名字/大小/花色
    public Integer value;
    public Suit suit;


    public Poker(Integer value, Suit suit) {
        this.suit = suit;
        this.value = value;
    }

    // 11 12 13
    // 14 15
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
                        poker.value = Integer.valueOf(args[1]);
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
}
