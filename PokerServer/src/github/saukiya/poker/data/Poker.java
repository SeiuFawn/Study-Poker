package github.saukiya.poker.data;


import java.text.MessageFormat;

public class Poker {
    //牌的名字/大小/花色
    public String name;
    public Integer value;
    public Suit suit;

    public Poker(String name, Integer value, Suit suit) {
        this.name = name;
        this.suit = suit;
        this.value = value;
    }

    @Override
    public String toString() {
        return MessageFormat.format("name:{0},value:{1},suit:{2}", name, value, suit);
    }

    /**
     * 字符串转Poker
     *
     * @param string 字符串
     * @return Poker 失败则是null
     */
    public static Poker FormString(String string) {
        Poker poker = new Poker(null, null, null);
        for (String s : string.split(",")) {
            String[] args = s.split(":");
            if (args.length > 1) {
                switch (args[0]) {
                    case "name":
                        poker.name = args[1];
                        break;
                    case "value":
                        poker.value = Integer.parseInt(args[1]);
                        break;
                    case "suit":
                        poker.suit = Suit.valueOf(args[1]);
                        break;
                }
            }
        }
        if (poker.name != null && poker.value != null && poker.suit != null) {
            return poker;
        } else {
            return null;
        }
    }
}
