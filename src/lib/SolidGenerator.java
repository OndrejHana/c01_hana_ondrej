package lib;

import transforms.Col;
import transforms.Mat4Identity;
import transforms.Mat4Transl;
import transforms.Point3D;
import types.Part;
import types.Solid;
import types.Topology;
import types.Vertex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
                new Mat4Identity(),
                Optional.empty()
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
                new Mat4Transl(0,2.0,0),
                Optional.empty()
        );
    }

    public static Solid getSolid3() {
        return new Solid(
                List.of(
                        new Vertex(new Point3D(0, 0.2, 0), new Col(1.0, 0, 0), 0, 0, null),
                        new Vertex(new Point3D(0, -0.2, 0), new Col(1.0, 0, 0), 0, 0, null),
                        new Vertex(new Point3D(0, 0, 1.0), new Col(1.0, 0, 0), 0, 0, null),
                        new Vertex(new Point3D(0, 0, 0), new Col(1.0, 1.0, 1.0), 0, 0, null),
                        new Vertex(new Point3D(0, 0, -1.0), new Col(1.0, 1.0, 1.0), 0, 0, null)
                ),
                List.of(0, 1, 2, 3, 4),
                List.of(new Part(0, 1, Topology.TRIANGLE), new Part(3, 1, Topology.LINE)),
                new Mat4Identity(),
                Optional.empty()
        );
    }

    public static Solid getSolid4() {
        try {

            return new Solid(
                    List.of(
                            new Vertex(new Point3D(0, 0.5, 0), new Col(1.0, 0, 0), 0, 1.0, null),
                            new Vertex(new Point3D(0, -0.5, 0), new Col(0, 1.0, 0), 1.0, 0.5, null),
                            new Vertex(new Point3D(1.0, 0, 0.5), new Col(0, 0, 1.0), 0, 0, null)
                    ),
                    List.of(0, 1, 2),
                    List.of(new Part(0, 1, Topology.TRIANGLE)),
                    new Mat4Identity(),
                    Optional.of(ImageIO.read(new File("./resources/P3080010.jpg")))
            );
        } catch (IOException e) {
            System.out.println("could not load texture");
            return null;
        }
    }

    public static Solid getAxis() {
        List<Vertex> vertices = List.of(
                // X-axis (red)
                new Vertex(new Point3D(2.0, 0.0, 0.0), new Col(1.0, 0.0, 0.0), 0, 0, null),
                new Vertex(new Point3D(1.0, 0.1, 0.0), new Col(1.0, 0.0, 0.0), 0, 0, null),
                new Vertex(new Point3D(1.0, -0.1, 0.0), new Col(1.0, 0.0, 0.0), 0, 0, null),
                new Vertex(new Point3D(0.0, 0.0, 0.0), new Col(1.0, 1.0, 1.0), 0, 0, null),
                new Vertex(new Point3D(1.0, 0.0, 0.0), new Col(1.0, 1.0, 1.0), 0, 0, null),

                // Y-axis (green)
                new Vertex(new Point3D(0.0, 2.0, 0.0), new Col(0.0, 1.0, 0.0), 0, 0, null),
                new Vertex(new Point3D(0.1, 1.0, 0.0), new Col(0.0, 1.0, 0.0), 0, 0, null),
                new Vertex(new Point3D(-0.1, 1.0, 0.0), new Col(0.0, 1.0, 0.0), 0, 0, null),
                new Vertex(new Point3D(0.0, 0.0, 0.0), new Col(1.0, 1.0, 1.0), 0, 0, null),
                new Vertex(new Point3D(0.0, 1.0, 0.0), new Col(1.0, 1.0, 1.0), 0, 0, null),

                // Z-axis (blue)
                new Vertex(new Point3D(0.0, 0.0, 2.0), new Col(0.0, 0.0, 1.0), 0, 0, null),
                new Vertex(new Point3D(0.1, 0.1, 1.0), new Col(0.0, 0.0, 1.0), 0, 0, null),
                new Vertex(new Point3D(-0.1, -0.1, 1.0), new Col(0.0, 0.0, 1.0), 0, 0, null),
                new Vertex(new Point3D(0.0, 0.0, 0.0), new Col(1.0, 1.0, 1.0), 0, 0, null),
                new Vertex(new Point3D(0.0, 0.0, 1.0), new Col(1.0, 1.0, 1.0), 0, 0, null)
        );

        List<Integer> indices = List.of(
                0, 1, 2, 3, 4,
                5, 6, 7, 8, 9,
                10, 11, 12, 13, 14
        );

        List<Part> parts = List.of(
                new Part(0, 1, Topology.TRIANGLE),
                new Part(3, 1, Topology.LINE),

                new Part(5, 1, Topology.TRIANGLE),
                new Part(8, 1, Topology.LINE),

                new Part(10, 1, Topology.TRIANGLE),
                new Part(13, 1, Topology.LINE)
        );

        return new Solid(
                vertices,
                indices,
                parts,
                new Mat4Identity(),
                Optional.empty()
        );
    }
}
