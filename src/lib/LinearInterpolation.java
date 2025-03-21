package lib;

import transforms.Col;

public class LinearInterpolation {
    public static double interpolateZ(int ax, int ay, double az, int bx, int by, double bz, int x, int y) {
        var t = (double)(bx - x) / (double)(bx - ax);
        return az * (1 - t) + bz * t;
    }

    public static Col interpolateCol(int ax, int ay, Col aCol, int bx, int by, Col bCol, int x, int y) {
        var t = (bx - x) / (double)(bx - ax);
        return interpolateCol(aCol, bCol, t);
    }

    public static Col interpolateCol(Col aCol, Col bCol, double t) {
        var r = bCol.getR() * (1 - t) + aCol.getR() * t;
        var g = bCol.getG() * (1 - t) + aCol.getG() * t;
        var b = bCol.getB() * (1 - t) + aCol.getB() * t;

        return new Col(r,g,b);
    }

}
