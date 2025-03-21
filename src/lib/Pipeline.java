package lib;

import raster.ZBuffer;
import transforms.Camera;
import transforms.Col;
import transforms.Mat4;
import transforms.Vec3D;
import types.*;

import javax.sound.sampled.Line;
import java.util.Arrays;

import static lib.LinearInterpolation.interpolateCol;
import static lib.TriangleRasterization.rasterize;

public class Pipeline {
    static int thickness = 1;

    record Triangle(Vertex a, Vertex b, Vertex c) {
    }

    static ScreenPoint toWindow(Vec3D p, Col col, double u, double v, int width, int height) {
        var x = (int) Math.round((p.getX() + 1) * (width - 1) / 2.0);
        var y = (int) Math.round((1 - p.getY()) * (height - 1) / 2.0);
        return new ScreenPoint(x, y, p.getZ(), col, u, v);
    }

    static Triangle orderTriangle(Vertex v1, Vertex v2, Vertex v3) {
        Vertex[] vertices = {v1, v2, v3};
        Arrays.sort(vertices, (a, b) -> Double.compare(b.getPoint().getZ(), a.getPoint().getZ()));
        return new Triangle(vertices[0], vertices[1], vertices[2]);
    }

    public static void transform(Solid solid, Camera camera, Mat4 proj, ZBuffer zbuffer, RenderMode renderMode) {
        var m = solid.getModelTransformation();
        var v = camera.getViewMatrix();

        var transform = m.mul(v).mul(proj);
        var s = solid.getTransformed(transform);

        var vb = s.getVertexBuffer();
        var ib = s.getIndexBuffer();
        for (Part part : s.getPartsBuffer()) {
            var curr = part.getStart();
            for (int i = 0; i < part.getCount(); i++) {
                switch (part.getType()) {
                    case LINE -> {
                        var idx1 = ib.get(curr);
                        curr++;
                        var idx2 = ib.get(curr);
                        curr++;

                        var v1 = vb.get(idx1);
                        var v2 = vb.get(idx2);

                        var v1dehomog = v1.dehomogenize();
                        var v2dehomog = v2.dehomogenize();

                        // orezani v z

                        var p1 = toWindow(v1dehomog, v1.getColor(), v1.getU(), v1.getV(), zbuffer.getWidth(), zbuffer.getHeight());
                        var p2 = toWindow(v2dehomog, v2.getColor(), v2.getU(), v2.getV(), zbuffer.getWidth(), zbuffer.getHeight());

                        var l = new ScreenLine(p1, p2, 1);

                        LineRasterizerBresenham.drawLine(l, zbuffer, LineRasterizerBresenham.LineColor.INTERPOLATED);
                    }
                    case TRIANGLE -> {
                        var idx1 = ib.get(curr);
                        curr++;
                        var idx2 = ib.get(curr);
                        curr++;
                        var idx3 = ib.get(curr);
                        curr++;

                        var v1 = vb.get(idx1);
                        var v2 = vb.get(idx2);
                        var v3 = vb.get(idx3);

                        var t = orderTriangle(v1, v2, v3);

                        double zmin = 1.0;
                        if (t.a.getPoint().getZ() < zmin) {
                            continue;
                        }

                        var tac = (zmin - t.c.getPoint().getZ()) / (t.a.getPoint().getZ() - t.c.getPoint().getZ());
                        var acx = t.a.getPoint().getX() * (1 - tac) + t.c.getPoint().getX() * tac;
                        var acy = t.a.getPoint().getY() * (1 - tac) + t.c.getPoint().getY() * tac;
                        var accol = interpolateCol(t.a.getColor(), t.c.getColor(), tac);
                        var acu = t.a.getU() * (1 - tac) + t.c.getU() * tac;
                        var acv = t.a.getV() * (1 - tac) + t.c.getV() * tac;

                        var a = toWindow(t.a.dehomogenize(), t.a.getColor(), t.a.getU(), t.a.getV(), zbuffer.getWidth(), zbuffer.getHeight());
                        var ac = toWindow(new Vec3D(acx, acy, zmin), accol, acu, acv, zbuffer.getWidth(), zbuffer.getHeight());

                        if (t.b.getPoint().getZ() < zmin) {
                            var tab = (zmin - t.b.getPoint().getZ()) / (t.a.getPoint().getZ() - t.b.getPoint().getZ());
                            var abx = t.a.getPoint().getX() * (1 - tab) + t.b.getPoint().getX() * tab;
                            var aby = t.a.getPoint().getY() * (1 - tab) + t.b.getPoint().getY() * tab;
                            var abcol = interpolateCol(t.a.getColor(), t.b.getColor(), tab);
                            var abu = t.a.getU() * (1 - tab) + t.b.getU() * tab;
                            var abv = t.a.getV() * (1 - tab) + t.b.getV() * tab;

                            var ab = toWindow(new Vec3D(abx, aby, zmin), abcol, abu, abv, zbuffer.getWidth(), zbuffer.getHeight());

                            switch (renderMode) {
                                case LINE -> {
                                    LineRasterizerBresenham.drawLine(a, ab, zbuffer, thickness, LineRasterizerBresenham.LineColor.WHITE);
                                    LineRasterizerBresenham.drawLine(ab, ac, zbuffer, thickness, LineRasterizerBresenham.LineColor.WHITE);
                                    LineRasterizerBresenham.drawLine(ac, a, zbuffer, thickness, LineRasterizerBresenham.LineColor.WHITE);
                                }
                                case FILL -> rasterize(new ScreenTriangle(a, ab, ac, s.getTexture()), zbuffer);
                            }
                            continue;
                        }

                        var b = toWindow(t.b.dehomogenize(), t.b.getColor(), t.b.getU(), t.b.getV(), zbuffer.getWidth(), zbuffer.getHeight());
                        if (t.c.getPoint().getZ() < zmin) {
                            var tbc = (zmin - t.c.getPoint().getZ()) / (t.b.getPoint().getZ() - t.c.getPoint().getZ());
                            var bcx = t.b.getPoint().getX() * (1 - tbc) + t.c.getPoint().getX() * tbc;
                            var bcy = t.b.getPoint().getY() * (1 - tbc) + t.c.getPoint().getY() * tbc;
                            var bccol = interpolateCol(t.b.getColor(), t.c.getColor(), tbc);
                            var bcu = t.b.getU() * (1 - tbc) + t.b.getU() * tbc;
                            var bcv = t.b.getV() * (1 - tbc) + t.b.getV() * tbc;

                            var bc = toWindow(new Vec3D(bcx, bcy, zmin), bccol, bcu, bcv, zbuffer.getWidth(), zbuffer.getHeight());

                            switch (renderMode) {
                                case FILL -> {
                                    rasterize(new ScreenTriangle(a, b, bc, s.getTexture()), zbuffer);
                                    rasterize(new ScreenTriangle(a, bc, ac, s.getTexture()), zbuffer);
                                }
                                case LINE -> {
                                    LineRasterizerBresenham.drawLine(a, b, zbuffer, thickness, LineRasterizerBresenham.LineColor.WHITE);
                                    LineRasterizerBresenham.drawLine(b, bc, zbuffer, thickness, LineRasterizerBresenham.LineColor.WHITE);
                                    LineRasterizerBresenham.drawLine(bc, a, zbuffer, thickness, LineRasterizerBresenham.LineColor.WHITE);

                                    LineRasterizerBresenham.drawLine(a, bc, zbuffer, thickness, LineRasterizerBresenham.LineColor.WHITE);
                                    LineRasterizerBresenham.drawLine(bc, ac, zbuffer, thickness, LineRasterizerBresenham.LineColor.WHITE);
                                    LineRasterizerBresenham.drawLine(ac, a, zbuffer, thickness, LineRasterizerBresenham.LineColor.WHITE);
                                }

                            }
                            continue;
                        }

                        var c = toWindow(t.c.dehomogenize(), t.c.getColor(), t.c.getU(), t.c.getV(), zbuffer.getWidth(), zbuffer.getHeight());
                        switch (renderMode) {
                            case FILL -> rasterize(new ScreenTriangle(a, b, c, s.getTexture()), zbuffer);
                            case LINE -> {
                                LineRasterizerBresenham.drawLine(a, b, zbuffer, thickness, LineRasterizerBresenham.LineColor.WHITE);
                                LineRasterizerBresenham.drawLine(b, c, zbuffer, thickness, LineRasterizerBresenham.LineColor.WHITE);
                                LineRasterizerBresenham.drawLine(c, a, zbuffer, thickness, LineRasterizerBresenham.LineColor.WHITE);
                            }
                        }
                    }
                }
            }
        }
    }
}
