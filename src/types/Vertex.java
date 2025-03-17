package types;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private final Point3D point;
    private final Col color;
    private final double u,v;
    private final Vec3D norm;

    public Vertex(Point3D point, Col color, double u, double v, Vec3D norm) {
        this.point = point;
        this.color = color;
        this.u = u;
        this.v = v;
        this.norm = norm;
    }

    public Vertex(Vertex vertex) {
        this.point = new Point3D(vertex.point);
        this.color = new Col(vertex.color);
        this.u = vertex.u;
        this.v = vertex.v;
        this.norm = new Vec3D(vertex.norm);
    }

    public Point3D getPoint() {
        return point;
    }

    public Col getColor() {
        return color;
    }

    public double getU() {
        return u;
    }

    public double getV() {
        return v;
    }

    public Vec3D getNorm() {
        return norm;
    }

    public List<Double> toBuffer() {
        var buffer = new ArrayList<Double>(13);
        buffer.add(getPoint().getX());
        buffer.add(getPoint().getY());
        buffer.add(getPoint().getZ());
        buffer.add(getPoint().getW());
        buffer.add(getColor().getR());
        buffer.add(getColor().getG());
        buffer.add(getColor().getB());
        buffer.add(getColor().getA());
        buffer.add(getU());
        buffer.add(getV());
        buffer.add(getNorm().getX());
        buffer.add(getNorm().getY());
        buffer.add(getNorm().getZ());
        return buffer;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "point=" + point +
                ", color=" + color +
                ", u=" + u +
                ", v=" + v +
                ", norm=" + norm +
                '}';
    }

    public Vec3D dehomogenize() {
        return new Vec3D(
            point.getX()/point.getW(),
            point.getY()/point.getW(),
            point.getZ()/point.getW()
        );
    }

}
