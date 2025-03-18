package types;

import transforms.Mat4;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Solid {
    private final List<Vertex> vertexBuffer;
    private final List<Integer> indexBuffer;
    private final List<Part> partsBuffer;
    private final Mat4 modelTransformation;
    private final Optional<BufferedImage> texture;

    public Optional<BufferedImage> getTexture() {
        return texture;
    }

    public Solid(List<Vertex> vertexBuffer, List<Integer> indexBuffer, List<Part> partsBuffer, Mat4 modelTransformation, Optional<BufferedImage> texture) {
        this.vertexBuffer = vertexBuffer;
        this.indexBuffer = indexBuffer;
        this.partsBuffer = partsBuffer;
        this.modelTransformation = modelTransformation;
        this.texture = texture;
    }

    public Solid getTransformed(Mat4 t) {
        var newVB = new ArrayList<Vertex>(vertexBuffer.size());
        for (Vertex v : vertexBuffer) {
            var p = v.getPoint().mul(t);
            newVB.add(new Vertex(p, v.getColor(), v.getU(), v.getV(), v.getNorm()));
        }
        return new Solid(newVB, new ArrayList<>(indexBuffer), new ArrayList<>(partsBuffer), new Mat4(modelTransformation), texture);
    }

    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public List<Part> getPartsBuffer() {
        return partsBuffer;
    }

    public Mat4 getModelTransformation() {
        return modelTransformation;
    }

    @Override
    public String toString() {
        return "Solid{" +
                "vertexBuffer=" + vertexBuffer +
                ", indexBuffer=" + indexBuffer +
                ", partsBuffer=" + partsBuffer +
                ", modelTransformation=" + modelTransformation +
                '}';
    }
}
