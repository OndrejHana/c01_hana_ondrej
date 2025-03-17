package lib;

import transforms.Col;
import transforms.Mat4Identity;
import transforms.Point3D;
import types.Part;
import types.Solid;
import types.Topology;
import types.Vertex;

import java.awt.*;
import java.util.List;

public class SolidGenerator {
    public static Solid getSolid1() {
        var w = new Col(Color.WHITE.getRGB());
        return new Solid(
                List.of(
                        new Vertex(new Point3D(-1.0, 0, -1.0), w, 0, 0, null),
                        new Vertex(new Point3D(0, 0, 1.0), w, 0, 0, null),
                        new Vertex(new Point3D(1.0, 0, -1.0), w, 0, 0, null),
                        new Vertex(new Point3D(0, 1.0, 0), w, 0, 0, null),

                        new Vertex(new Point3D(0, 0, 0), new Col(1.0, 0, 0), 0, 0, null),
                        new Vertex(new Point3D(0, 0, 0), new Col(0, 1.0, 0), 0, 0, null),
                        new Vertex(new Point3D(0, 0, 0), new Col(0, 0, 1.0), 0, 0, null),
                        new Vertex(new Point3D(1.0, 0, 0), new Col(1.0, 0, 0), 0, 0, null),
                        new Vertex(new Point3D(0, 1.0, 0), new Col(0, 1.0, 0), 0, 0, null),
                        new Vertex(new Point3D(0, 0, 1.0), new Col(0, 0, 1.0), 0, 0, null)
                ),
                List.of(0, 1, 2, 0, 1, 3, 1, 2, 3, 0, 2, 3, 4, 7, 5, 8, 6, 9),
                List.of(
                        new Part(0, 4, Topology.TRIANGLE),
                        new Part(12, 3, Topology.LINE)
                ),
                new Mat4Identity()
        );
    }

    public static Solid getSolid2() {
        var a = new Point3D(-1.0, -1.0, 0);
        var b = new Point3D(-1.0, 1.0, 0);
        var c = new Point3D(1.0, 0, 0);
        var d = new Point3D(0, 0, 1.0);

        return new Solid(
                List.of(
                        new Vertex(new Point3D(a), new Col(1.0, 0, 0), 0, 0, null),
                        new Vertex(new Point3D(a), new Col(0, 0, 1.0), 0, 0, null),
                        new Vertex(new Point3D(b), new Col(1.0, 0, 0), 0, 0, null),
                        new Vertex(new Point3D(b), new Col(0, 1.0, 0), 0, 0, null),
                        new Vertex(new Point3D(c), new Col(0, 1.0, 0), 0, 0, null),
                        new Vertex(new Point3D(c), new Col(0, 0, 1.0), 0, 0, null),
                        new Vertex(new Point3D(d), new Col(1.0, 0, 0), 0, 0, null),
                        new Vertex(new Point3D(d), new Col(0, 1.0, 0), 0, 0, null),
                        new Vertex(new Point3D(d), new Col(0, 0, 1.0), 0, 0, null)
                ),
                List.of(0, 3, 5, 0, 2, 6, 3, 7, 4, 5, 8, 1),
                List.of(
                        new Part(0, 4, Topology.TRIANGLE)
                ),
                new Mat4Identity()
        );
    }

    public static Solid getSolid3() {
        return new Solid(
                List.of(
                        new Vertex(new Point3D(-0.5, 0.5, 0), new Col(1.0, 0, 0), 0, 0, null),
                        new Vertex(new Point3D(0.5, -0.5, 0), new Col(1.0, 0, 0), 0, 0, null),
                        new Vertex(new Point3D(0, 0, 1.0), new Col(1.0, 0, 0), 0, 0, null),
                        new Vertex(new Point3D(0, 0, 0), new Col(1.0, 1.0, 1.0), 0, 0, null),
                        new Vertex(new Point3D(0, 0, -1.0), new Col(1.0, 1.0, 1.0), 0, 0, null)
                ),
                List.of(0, 1, 2, 3, 4),
                List.of(new Part(0, 2, Topology.TRIANGLE), new Part(3, 1, Topology.LINE)),
                new Mat4Identity()
        );
    }

    public static Solid getSolid4() {
        return new Solid(
                List.of(
                        new Vertex(new Point3D(0, 0.5, 0), new Col(1.0, 0, 0), 0, 0, null),
                        new Vertex(new Point3D(0, -0.5, 0), new Col(0, 1.0, 0), 0, 0, null),
                        new Vertex(new Point3D(1.0, 0, 0.5), new Col(0, 0, 1.0), 0, 0, null)
                        ),
                List.of(0, 1, 2),
                List.of(new Part(0, 1, Topology.TRIANGLE)),
                new Mat4Identity()
        );
    }
}
