package lib;

import transforms.Point3D;

public class Clip {
    static void clip(Point3D a, Point3D b, Point3D c) {
        // orezani podle z souradnice

        // 1. seradit vrcholy podle z
        var zmin = 0;
        if (a.getZ() < zmin) {
            //clip
            return;
        }

        if (b.getZ() < zmin) {
            // trojuhelnik je prerusen na hranach AB, AC - interpolovat kde je z = 0
            // udelam interpolacni koeficient podle z
            var tab = (0 - b.getZ()) / (a.getZ() - b.getZ());
            var abx = a.getX() * (1 - tab) + b.getX();
            var aby = a.getY() * (1 - tab) + b.getY();
            var ab = new Point3D(abx, aby, 0);

            var tac = (0 - c.getZ()) / (a.getZ() - c.getZ());
            var acx = a.getX() * (1 - tac) + c.getX();
            var acy = a.getY() * (1 - tac) + c.getY();
            var ac = new Point3D(acx, acy, 0);

            // rasterize(a, ab, ac)
            return;
        }

        if (c.getZ() < zmin) {
            var tbc = (0 - c.getZ()) / (b.getZ() - c.getZ());
            var bcx = b.getX() * (1 - tbc) + c.getX();
            var bcy = b.getY() * (1 - tbc) + c.getY();
            var bc = new Point3D(bcx, bcy, 0);

            var tac = (0 - c.getZ()) / (a.getZ() - c.getZ());
            var acx = a.getX() * (1 - tac) + c.getX();
            var acy = a.getY() * (1 - tac) + c.getY();
            var ac = new Point3D(acx, acy, 0);

            // rasterize(a, b, bc)
            // rasterize(a, bc, ac)
            return;
        }

        // rasterize(a,b,c)
    }
}
