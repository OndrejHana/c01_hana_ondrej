import lib.Pipeline;
import raster.ZBuffer;
import transforms.*;
import types.RenderMode;
import types.Solid;
import types.TransformationMode;
import view.Window;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static lib.SolidGenerator.*;

public class GameLoop {
    static final double cameraSpeed = 0.5;
    static final double sensitivity = 0.005;
    static final int width = 800;
    static final int height = 600;

    static final int TARGET_FPS = 60;
    static final long OPTIMAL_FRAME_TIME = 1000000000L / TARGET_FPS;
    static long LAST_FRAME_TIME;

    static final Window window = new Window(width, height);
    static final ZBuffer zbuffer = new ZBuffer(window.getPanel().getRaster());

    static Camera camera = new Camera(
            new Vec3D(0, 0, 0),
            Math.PI,
            0,
            10,
            false
    );

    static int lastX = width / 2;
    static int lastY = height / 2;

    static Mat4PerspRH projPer = new Mat4PerspRH(
            Math.toRadians(90),
            (double) zbuffer.getHeight() / zbuffer.getWidth(),
            0.1,
            100.0);
    static Mat4OrthoRH projOrtho = new Mat4OrthoRH(
            20.0,
            20.0,
            0.1,
            10.0
    );

    static RenderMode renderMode = RenderMode.FILL;
    static Mat4 currProj = projPer;

    static Optional<Integer> selected = Optional.empty();
    static TransformationMode currTransformationMode = TransformationMode.NONE;

    static ArrayList<Solid> scene = new ArrayList<>(List.of(
            getSolid1(),
            getSolid2().getTransformed(new Mat4Scale(3).mul(new Mat4Transl(0, -4.0, 0))),
            getAxis()
    ));

    static void animate(int solid) {
        var s = scene.get(solid);
        scene.set(solid, s.getTransformed(new Mat4RotZ(Math.PI/60)));
    }

    static void transform(int direction) {
        if (selected.isEmpty()) {
            return;
        }

        var curr = scene.get(selected.get());
        curr = switch (currTransformationMode) {
            case TRANSLATION -> switch (direction) {
                case KeyEvent.VK_LEFT -> curr.getTransformed(new Mat4Transl(0,-1.0,0));
                case KeyEvent.VK_RIGHT -> curr.getTransformed(new Mat4Transl(0,1.0,0));
                case KeyEvent.VK_DOWN -> curr.getTransformed(new Mat4Transl(0,0, -1.0));
                case KeyEvent.VK_UP -> curr.getTransformed(new Mat4Transl(0,0, 1.0));
                default -> curr;
            };
            case SCALE -> switch (direction) {
                case KeyEvent.VK_UP -> curr.getTransformed(new Mat4Scale(1.2));
                case KeyEvent.VK_DOWN -> curr.getTransformed(new Mat4Scale(0.8));
                default -> curr;
            };
            case ROTATION -> switch (direction) {
                case KeyEvent.VK_UP -> curr.getTransformed(new Mat4RotY(-Math.PI/30));
                case KeyEvent.VK_DOWN -> curr.getTransformed(new Mat4RotY(Math.PI/30));
                case KeyEvent.VK_LEFT -> curr.getTransformed(new Mat4RotZ(-Math.PI/30));
                case KeyEvent.VK_RIGHT -> curr.getTransformed(new Mat4RotZ(Math.PI/30));
                default -> curr;
            };
            case NONE -> curr;
        };

        scene.set(selected.get(), curr);
    }

    static void render() {
        var panel = window.getPanel();
        zbuffer.clear();
        for (int i = 0; i < scene.size(); i++) {
            var solid = scene.get(i);
            var isselected = selected.isPresent() && selected.get() == i;
            Pipeline.transform(solid, camera, currProj, zbuffer, renderMode, isselected);
        }
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

                lastX = x;
                lastY = y;

                camera = camera
                        .addAzimuth(dx * sensitivity)
                        .addZenith(dy * sensitivity * -1);
            }
        });

        window.getPanel().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() >= 0x30 && e.getKeyCode() < (0x30 + scene.size())) {
                    selected = Optional.of(e.getKeyCode()-0x30);
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    selected = Optional.empty();
                }

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

                if (e.getKeyCode() == KeyEvent.VK_T) {
                    currTransformationMode = TransformationMode.TRANSLATION;
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    currTransformationMode = TransformationMode.ROTATION;
                }
                if (e.getKeyCode() == KeyEvent.VK_Y) {
                    currTransformationMode = TransformationMode.SCALE;
                }

                if (e.getKeyCode() >= 0x25 && e.getKeyCode() <= 0x28 && selected.isPresent() && currTransformationMode != TransformationMode.NONE) {
                    transform(e.getKeyCode());
                }
            }
        });

        long lastLoopTime = System.nanoTime();
        while (true) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;

            animate(0);
            render();

            long endTime = System.nanoTime();
            LAST_FRAME_TIME = endTime - now; // time spent updating + rendering
            long sleepTime = (OPTIMAL_FRAME_TIME - LAST_FRAME_TIME) / 1000000; // convert ns to ms

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
