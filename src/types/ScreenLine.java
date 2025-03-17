package types;

public class ScreenLine {
    private final ScreenPoint p1;
    private final ScreenPoint p2;
    private final int thickness;

    public ScreenLine(ScreenPoint p1, ScreenPoint p2, int thickness) {
        this.p1 = p1;
        this.p2 = p2;
        this.thickness = thickness;
    }

    public int getX1() {
        return p1.getX();
    }

    public int getY1() {
        return p1.getY();
    }

    public int getX2() {
        return p2.getX();
    }

    public int getY2() {
        return p2.getY();
    }

    public ScreenPoint getP1() {
        return p1;
    }

    public ScreenPoint getP2() {
        return p2;
    }

    public int getThickness() {
        return thickness;
    }
}
