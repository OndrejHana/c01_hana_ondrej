package raster;

import transforms.Col;

import java.awt.*;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster<Col> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void setPixelWithZTest(int x, int y, double z, Col color) {
        if (depthBuffer.getValue(x,y).isEmpty() || depthBuffer.getValue(x,y).get() < z) return;
        imageBuffer.setValue(x,y,color);
    }
}
