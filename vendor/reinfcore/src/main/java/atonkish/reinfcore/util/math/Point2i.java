package atonkish.reinfcore.util.math;

/** Exact behavioral reconstruction of the 1.21.11 helper. */
public final class Point2i {
    private int x;
    private int y;

    public Point2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
