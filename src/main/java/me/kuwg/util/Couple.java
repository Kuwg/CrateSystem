package me.kuwg.util;

public class Couple <X, Y> {
    private final X x;
    private final Y y;

    public Couple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public Y getY() {
        return y;
    }

    public X getX() {
        return x;
    }
}
