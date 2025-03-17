package raster;

import transforms.Col;
import types.ScreenPoint;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster<Col> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void setPixelWithZTest(int x, int y, double z, Col color) {
        if (depthBuffer.getValue(x,y).isEmpty() || depthBuffer.getValue(x,y).get() < z) {
//            System.out.println("rip "+ depthBuffer.getValue(x,y).get() + depthBuffer.getValue(x,y).isEmpty() + " " + (depthBuffer.getValue(x,y).get() < z));
            return;
        }
        imageBuffer.setValue(x,y,color);
        depthBuffer.setValue(x,y,z);
//        System.out.println("drawn");
    }

    public void setPixelWithZTest(ScreenPoint p) {
        setPixelWithZTest(p.getX(), p.getY(), p.getZ(), p.getColor());
    }

    public int getWidth()
    {
        return imageBuffer.getWidth();
    }

    public int getHeight() {
        return imageBuffer.getHeight();
    }

    public void clear() {
        imageBuffer.clear();
        depthBuffer.clear();
    }
}
