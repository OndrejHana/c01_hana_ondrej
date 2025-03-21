import lib.Pipeline;
import raster.ZBuffer;
import transforms.*;
import types.RenderMode;
import types.Solid;
import view.Window;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static lib.SolidGenerator.getSolid2;
import static lib.SolidGenerator.getSolid4;

public class GameLoop {
    static final double cameraSpeed = 0.5;
    static final double sensitivity = 0.005;
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

    static RenderMode renderMode = RenderMode.FILL;
    static Mat4 currProj = projPer;
    private static int lastX = width / 2;
    private static int lastY = height / 2;
    static Camera camera = new Camera(
            new Vec3D(0, 0, 0),
            Math.PI,
            0,
            10,
            false
    );

    static ArrayList<Solid> scene = new ArrayList<>(List.of(getSolid2(), getSolid4().getTransformed(new Mat4Scale(3).mul(new Mat4Transl(0, -4.0, 0)))));

    private static final int TARGET_FPS = 60;
    private static final long OPTIMAL_FRAME_TIME = 1000000000L / TARGET_FPS;
    public static long LAST_FRAME_TIME;
    public static volatile boolean running = true;

    static void animate(int solid) {
        var s = scene.get(solid);
        scene.set(solid, s.getTransformed(new Mat4RotZ(1.0/60*Math.PI)));
    }

    static void render() {
        var panel = window.getPanel();
        zbuffer.clear();
        scene.forEach(solid -> Pipeline.transform(solid, camera, currProj, zbuffer, renderMode));
        panel.repaint();
    }

    public static void run() {
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
                if (e.getKeyCode() == KeyEvent.VK_M) {
                    if (renderMode == RenderMode.LINE) {
                        renderMode = RenderMode.FILL;
                    } else {
                        renderMode = RenderMode.LINE;
                    }
                }
            }
        });

        long lastLoopTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;

            animate(0);
            render();

            long endTime = System.nanoTime();
            LAST_FRAME_TIME = endTime - now; // time spent updating + rendering
            long sleepTime = (OPTIMAL_FRAME_TIME - LAST_FRAME_TIME) / 1000000; // convert ns to ms
            System.out.println(LAST_FRAME_TIME / 1000000);

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}
