package github.seiufawn.poker;

public enum Suit {
    //牌的花色和花色大小，错误则为*
    spade("♠", 1),
    heart("♥", 2),
    diamond("♦", 3),
    club("♣", 4),
    king("♔", 5),
    error("*", -1);

    public final String show;
    public final int value;

    Suit(String show, int value) {
        this.show = show;
        this.value = value;
    }

}
