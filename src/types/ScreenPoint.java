package types;

import transforms.Col;

public class ScreenPoint {
    private final int x;
    private final int y;
    private final double z;
    private final double u, v;
    private final Col color;

    @Override
    public String toString() {
        return "ScreenPoint{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", color=" + color +
                '}';
    }

    public ScreenPoint(int x, int y, double z, Col color, double u, double v) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.u = u;
        this.v = v;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Col getColor() {
        return color;
    }

    public double getU() {
        return u;
    }

    public double getV() {
        return v;
    }
}
