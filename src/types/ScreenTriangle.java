package types;

public class ScreenTriangle {
    private final ScreenPoint a;
    private final ScreenPoint b;
    private final ScreenPoint c;

    public record BoundingRect(int xmin, int ymin, int xmax, int ymax) {}

    @Override
    public String toString() {
        return "ScreenTriangle{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }

    public ScreenTriangle(ScreenPoint a, ScreenPoint b, ScreenPoint c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

//    public boolean isCW() {
//        return EdgeFunction.edgeFunction(a.getX(), a.getY(), b.getX(), b.getY(), c.getX(), c.getY()) > 0;
//    }

    public BoundingRect getBoundingRect() {
        var xmin = Math.min(a.getX(), Math.min(b.getX(), c.getX()));
        var xmax = Math.max(a.getX(), Math.max(b.getX(), c.getX()));
        var ymin = Math.min(a.getY(), Math.min(b.getY(), c.getY()));
        var ymax = Math.max(a.getY(), Math.max(b.getY(), c.getY()));

        return new BoundingRect(xmin, ymin, xmax, ymax);
    }

    public ScreenPoint getA() {
        return a;
    }

    public ScreenPoint getB() {
        return b;
    }

    public ScreenPoint getC() {
        return c;
    }
}
