package lib;

import raster.ZBuffer;
import transforms.Col;
import types.ScreenTriangle;

public class TriangleRasterization {
    static int edgeFunctionCCW(int ax, int ay, int bx, int by, int px, int py) {
        return (px - ax) * (by - ay) - (py - ay) * (bx - ax);
    }

    static int edgeFunctionCW(int ax, int ay, int bx, int by, int px, int py) {
        return (bx - ax) * (py - ay) - (by - ay) * (px - ax);
    }

    public static void rasterize(ScreenTriangle t, ZBuffer zbuffer) {
        var a = t.getA();
        var b = t.getB();
        var c = t.getC();

        var abc = (double) edgeFunctionCCW(a.getX(), a.getY(), b.getX(), b.getY(), c.getX(), c.getY());

        if (abc > 0) {
            rasterizeCCW(t, zbuffer);
        } else {
            rasterizeCW(t , zbuffer);
        }
    }

    static void rasterizeCW(ScreenTriangle t, ZBuffer zbuffer) {
        var a = t.getA();
        var b = t.getB();
        var c = t.getC();

        var abc = (double) edgeFunctionCW(a.getX(), a.getY(), b.getX(), b.getY(), c.getX(), c.getY());

        var xmin = Math.min(a.getX(), Math.min(b.getX(), c.getX()));
        var xmax = Math.max(a.getX(), Math.max(b.getX(), c.getX()));
        var ymin = Math.min(a.getY(), Math.min(b.getY(), c.getY()));
        var ymax = Math.max(a.getY(), Math.max(b.getY(), c.getY()));

        xmin = Math.max(xmin, 0);
        xmax = Math.min(xmax, zbuffer.getWidth() - 1);
        ymin = Math.max(ymin, 0);
        ymax = Math.min(ymax, zbuffer.getHeight() - 1);

        for (int y = ymin; y <= ymax; y++) {
            for (int x = xmin; x <= xmax; x++) {
                var bcp = edgeFunctionCW(b.getX(), b.getY(), c.getX(), c.getY(), x, y);
                var cap = edgeFunctionCW(c.getX(), c.getY(), a.getX(), a.getY(), x, y);
                var abp = edgeFunctionCW(a.getX(), a.getY(), b.getX(), b.getY(), x, y);

                if (bcp >= 0 && cap >= 0 && abp >= 0) {
                    var w0 = bcp / abc;
                    var w1 = cap / abc;
                    var w2 = abp / abc;

                    var acol = a.getColor();
                    var bcol = b.getColor();
                    var ccol = c.getColor();

                    var pr = (acol.getR() * w0) + (bcol.getR() * w1) + (ccol.getR() * w2);
                    var pg = (acol.getG() * w0) + (bcol.getG() * w1) + (ccol.getG() * w2);
                    var pb = (acol.getB() * w0) + (bcol.getB() * w1) + (ccol.getB() * w2);

                    var col = new Col(pr, pg, pb);

                    var z = a.getZ() * w0 + b.getZ() * w1 + c.getZ() * w2;

                    zbuffer.setPixelWithZTest(x, y, z, col);
                }
            }
        }
    }

    static void rasterizeCCW(ScreenTriangle t, ZBuffer zbuffer) {
        var a = t.getA();
        var b = t.getB();
        var c = t.getC();

        var abc = (double) edgeFunctionCCW(a.getX(), a.getY(), b.getX(), b.getY(), c.getX(), c.getY());

        var xmin = Math.min(a.getX(), Math.min(b.getX(), c.getX()));
        var xmax = Math.max(a.getX(), Math.max(b.getX(), c.getX()));
        var ymin = Math.min(a.getY(), Math.min(b.getY(), c.getY()));
        var ymax = Math.max(a.getY(), Math.max(b.getY(), c.getY()));

        xmin = Math.max(xmin, 0);
        xmax = Math.min(xmax, zbuffer.getWidth() - 1);
        ymin = Math.max(ymin, 0);
        ymax = Math.min(ymax, zbuffer.getHeight() - 1);

        for (int y = ymin; y <= ymax; y++) {
            for (int x = xmin; x <= xmax; x++) {
                var bcp = edgeFunctionCCW(b.getX(), b.getY(), c.getX(), c.getY(), x, y);
                var cap = edgeFunctionCCW(c.getX(), c.getY(), a.getX(), a.getY(), x, y);
                var abp = edgeFunctionCCW(a.getX(), a.getY(), b.getX(), b.getY(), x, y);

                if (bcp >= 0 && cap >= 0 && abp >= 0) {
                    var w0 = bcp / abc;
                    var w1 = cap / abc;
                    var w2 = abp / abc;

                    var acol = a.getColor();
                    var bcol = b.getColor();
                    var ccol = c.getColor();

                    var pr = (acol.getR() * w0) + (bcol.getR() * w1) + (ccol.getR() * w2);
                    var pg = (acol.getG() * w0) + (bcol.getG() * w1) + (ccol.getG() * w2);
                    var pb = (acol.getB() * w0) + (bcol.getB() * w1) + (ccol.getB() * w2);

                    var col = new Col(pr, pg, pb);

                    var z = a.getZ() * w0 + b.getZ() * w1 + c.getZ() * w2;

                    zbuffer.setPixelWithZTest(x, y, z, col);
                }
            }
        }
    }
}
