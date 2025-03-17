package lib;

import raster.ZBuffer;
import transforms.Col;
import types.ScreenLine;
import types.ScreenPoint;

import java.awt.*;

import static lib.LinearInterpolation.interpolateCol;
import static lib.LinearInterpolation.interpolateZ;

public class LineRasterizerBresenham {
    public enum LineColor {
        INTERPOLATED,
        WHITE,
    }

    public static void drawLine(ScreenLine l, ZBuffer raster, LineColor c) {
        var a = l.getP1();
        var b = l.getP2();
        drawLine(a.getX(), a.getY(), a.getZ(), a.getColor(), b.getX(), b.getY(), b.getZ(), b.getColor(), l.getThickness(), raster, c);
    }

    public static void drawLine(ScreenPoint a, ScreenPoint b, ZBuffer raster, int thickness, LineColor c) {
        drawLine(a.getX(), a.getY(), a.getZ(), a.getColor(), b.getX(), b.getY(), b.getZ(), b.getColor(), thickness, raster, c);
    }

    // Implementoval jsem Bresenhamův algoritmus kvůli jeho efektivitě a rozšířenosti.
    // Velká výhoda bresenhamova algoritmu je, že nevyžaduje provádění žádných floating point operací.
    // Aby se vyhnul floating point operacím, v algoritmu se využívá rozhodovacího parametru p, který
    // je v každém kroku inkrementální vypočten a podle něj se rozhoduje který pixel bude vykreslen
    public static void drawLine(int x1, int y1, double z1, Col acol, int x2, int y2, double z2, Col bcol, int thickness, ZBuffer raster, LineColor c) {
        // Kontroluje se jestli čára vede horizontálně nebo vertikálně.
        if (Math.abs(x2 - x1) > Math.abs(y2 - y1)) {
            drawLineH(x1, y1, z1, acol, x2, y2, z2, bcol, thickness, raster, c);
        } else {
            drawLineV(x1, y1, z1, acol, x2, y2, z2, bcol, thickness, raster, c);
        }
    }

    // Implementace bresenhamova algoritmu pro vykreslování horizontálních čar
    private static void drawLineH(int x1, int y1, double z1, Col acol, int x2, int y2, double z2, Col bcol, int thickness, ZBuffer raster, LineColor c) {

        // Bresenhamův algoritmus ze základu funguje pouze v prvním oktantu, takže
        // pokud vede čára ve 2. nebo 3. kvadrantu, prohodí hodnoty pro správné vykreslení
        if (x1 > x2) {
            var temp = x1;
            x1 = x2;
            x2 = temp;
            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        int dx = x2 - x1;
        int dy = y2 - y1;

        // Rozhoduje směr kterým se bude čára vykreslovat. v Případě že by čára
        // vedla ve 3. nebo 4. kvadrantu chceme obrátit hodnoty x a y.
        int dir;
        if (dy > 0) {
            dir = 1;
        } else {
            dir = -1;
        }

        dy *= dir;

        if (dx != 0) {
            var y = y1;
            // Vytvoření rozhodovacího parametru v prvním kroce.
            var p = 2 * dy - dx;

            for (var i = x1; i <= x2; i++) {
                int offset = 0;
                int yoffset = 0;
                for (var j = 0; j < thickness; j++) {
                    if (y + yoffset > 0) {
                        var z = interpolateZ(x1, y1, z1, x2, y2, z2, i, y);
                        Col col;
                        if (c == LineColor.INTERPOLATED) {
                            col = interpolateCol(y1, x1, acol, y2, x2, bcol, i, y);
                        } else if (c == LineColor.WHITE) {
                            col = new Col(Color.WHITE.getRGB());
                        } else {
                            System.out.println("line color not set");
                            return;
                        }
                        raster.setPixelWithZTest(i, y + yoffset, z, col);
                    }
                    yoffset = (Math.abs(yoffset) + 1) * (-1);
                }

                // pokud je p > 0, tak je potřeba v dalším kroce skočit.
                if (p >= 0) {
                    y += dir;
                    p = p - 2 * dx;
                }
                p = p + 2 * dy;
            }
        }
    }

    // Implementace Bresenhamova algoritmu s prohozenými x a y hodnotami, pro vykreslování vertikálních čar
    private static void drawLineV(int x1, int y1, double z1, Col acol, int x2, int y2, double z2, Col bcol, int thickness, ZBuffer raster, LineColor c) {
        if (y1 > y2) {
            var temp = x1;
            x1 = x2;
            x2 = temp;
            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        int dx = x2 - x1;
        int dy = y2 - y1;

        int dir;
        if (dx > 0) {
            dir = 1;
        } else {
            dir = -1;
        }

        dx *= dir;

        if (dy != 0) {
            var x = x1;
            var p = 2 * dx - dy;

            for (var i = y1; i <= y2; i++) {
                int xoffset = 0;
                for (var j = 0; j < thickness; j++) {
                    if (x + xoffset > 0) {
                        // prohozeno, protoze vykresluji vertikalni caru
                        var z = interpolateZ(y1, x1, z1, y2, x2, z2, i, x);
                        Col col;
                        if (c == LineColor.INTERPOLATED) {
                            col = interpolateCol(y1, x1, acol, y2, x2, bcol, i, x);
                        } else if (c == LineColor.WHITE) {
                            col = new Col(Color.WHITE.getRGB());
                        } else {
                            System.out.println("line color not set");
                            return;
                        }
                        raster.setPixelWithZTest(x + xoffset, i, z, col);
                    }
                    xoffset = (Math.abs(xoffset) + 1) * (-1);
                }

                if (p >= 0) {
                    x += dir;
                    p = p - 2 * dy;
                }
                p = p + 2 * dx;
            }
        }
    }
}
