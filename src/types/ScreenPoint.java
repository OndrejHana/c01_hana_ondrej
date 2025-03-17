package types;

import transforms.Col;
import transforms.Vec3D;

public class ScreenPoint {
    private final int x;
    private final int y;
    private final double z;
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

    public ScreenPoint(int x, int y, double z, Col color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
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
}
