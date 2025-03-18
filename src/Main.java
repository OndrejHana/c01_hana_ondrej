import lib.Pipeline;
import raster.ZBuffer;
import transforms.*;
import types.Solid;
import view.Window;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

import static lib.SolidGenerator.getSolid2;
import static lib.SolidGenerator.getSolid4;

public class Main {
    static final double cameraSpeed = 0.5;
    static final double mouseSensitivity = 0.005;
    static final int width = 800;
    static final int height = 600;

    static final Window window = new Window(width, height);
    static final ZBuffer zbuffer = new ZBuffer(window.getPanel().getRaster());

    static Mat4PerspRH projPer = new Mat4PerspRH(
            Math.toRadians(90),
            (double) zbuffer.getHeight() / zbuffer.getWidth(),
            0.1,
            100);
    static Mat4OrthoRH projOrtho = new Mat4OrthoRH(
            5,
            5,
            -1.0,
            10.0
    );
    static Mat4 currProj = projPer;
    private static int lastX = width / 2;
    private static int lastY = height / 2;
    private static final double sensitivity = 0.005; // Adjust for desired responsiveness
    static Camera camera = new Camera(
            new Vec3D(0, 0, 0),
            Math.PI,
            0,
            3,
            false
    );

    static void render(List<Solid> s) {
        var panel = window.getPanel();
        zbuffer.clear();
        s.forEach(solid -> Pipeline.transform(solid, camera, currProj, zbuffer));
        panel.repaint();
    }

    public static void main(String[] args) {
        List<Solid> solids = List.of(getSolid2(), getSolid4().getTransformed(new Mat4Transl(0,-1.0,0)));
        render(solids);

        window.getPanel().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                int dx = x - lastX;
                int dy = y - lastY;

                camera = camera
                        .addAzimuth(dx * sensitivity)
                        .addZenith(dy * sensitivity * -1);

                lastX = x;
                lastY = y;

                render(solids);
            }
        });

        window.getPanel().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    camera = camera.down(cameraSpeed);
                }
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    camera = camera.up(cameraSpeed);
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    camera = camera.left(cameraSpeed);
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    camera = camera.right(cameraSpeed);
                }
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    camera = camera.forward(cameraSpeed);
                }
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    camera = camera.backward(cameraSpeed);
                }
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    if (currProj == projPer) {
                        currProj = projOrtho;
                    } else {
                        currProj = projPer;
                    }
                }
                render(solids);
            }
        });
    }
}