package github.seiufawn.poker;


public class Poker {
    //牌的名字/大小/花色
    public String name;
    public Integer value;
    public Suit suit;

    Poker(String name, Integer value, Suit suit) {
        this.name = name;
        this.suit = suit;
        this.value = value;
    }
}
