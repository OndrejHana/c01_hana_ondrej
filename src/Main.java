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

import static lib.SolidGenerator.*;

public class Main {
    static final double cameraSpeed = 0.5;
    static final double mouseSensitivity = 0.005;
    static final int width = 800;
    static final int height = 600;
    static Vec2D center = new Vec2D((double) width / 2, (double) height / 2);

    static final Window window = new Window(width, height);
    static final ZBuffer zbuffer = new ZBuffer(window.getPanel().getRaster());
    static Camera camera = new Camera(
            new Vec3D(5, 0, 0),
            Math.PI,
            0,
//            Math.PI - (Math.PI / 12),
//            (Math.PI / 12),
            3,
            true
    );
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

    static void render(List<Solid> s) {
        var panel = window.getPanel();

        zbuffer.clear();
        s.forEach(solid -> Pipeline.transform(solid, camera, currProj, zbuffer));
        panel.repaint();
    }

    public static void main(String[] args) {
        var s = getSolid2();
        List<Solid> solids = List.of(
                s
//                new Solid(s.getVertexBuffer(), s.getIndexBuffer(), s.getPartsBuffer(), new Mat4Transl(1.0, 0, 0).mul(new Mat4Scale(1.5)))
        );
        render(solids);

        window.getPanel().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                var deltaX = e.getX() / (center.getX() / Math.PI);
                var deltaY = e.getY() / (center.getY() / Math.PI * 0.5);

                camera = camera
                        .withAzimuth(deltaX)
                        .withZenith(deltaY);

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
                    System.out.println(camera.getPosition());
                }
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    camera = camera.backward(cameraSpeed);
                    System.out.println(camera.getPosition());
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