package ru.drifles.crpg.common;

public final class Position {
    private int x;
    private int y;

    public Position() {
        this.x = 0;
        this.y = 0;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(int xy) {
        this.x = xy;
        this.y = xy;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveUp() {
        this.y += 1;
    }

    public void moveDown() {
        this.y -= 1;
    }

    public void moveLeft() {
        this.x -= 1;
    }

    public void moveRight() {
        this.x += 1;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
