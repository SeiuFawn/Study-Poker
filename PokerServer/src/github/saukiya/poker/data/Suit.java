package github.saukiya.poker.data;

public enum Suit {
    //牌的花色和花色大小，错误则为*
    spade("黑桃", 1),
    heart("红心", 2),
    diamond("红菱", 3),
    club("黑梅", 4),
    king("♔", 5);

    public final String show;
    public final int value;

    Suit(String show, int value) {
        this.show = show;
        this.value = value;
    }
}
