import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class ParticleSimulatorPanel extends JPanel {
    private final DrawPanel drawPanel;
    private final WallController wallController;
    private final DynamicThreadManager threadManager; 
    private Thread gameThread;
    private volatile boolean running = false;
    private FPSTracker fpsTracker = new FPSTracker(); 

    public ParticleSimulatorPanel() {
        drawPanel = new DrawPanel();
        wallController = new WallController();
        setLayout(new BorderLayout());
        add(drawPanel, BorderLayout.CENTER);
        threadManager = new DynamicThreadManager(wallController);

        drawPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                drawPanel.removeComponentListener(this);
                startGameLoop();
            }
        });
    }

    public void startGameLoop() {
        threadManager.setCanvasSize(drawPanel.getWidth(), drawPanel.getHeight());
        running = true;
        gameThread = new Thread(this::gameLoop);
        gameThread.start();
    }

    private void gameLoop() {
        final long targetDelay = 1000 / 60; // Target delay in milliseconds for 60 FPS

        while (running) {
            long now = System.currentTimeMillis();
            
            fpsTracker.update(); 
            if (fpsTracker.getFPS() != 0) {
                drawPanel.setFps(fpsTracker.getFPS()); 
                threadManager.checkAndAdjustThread(); 
            }

            updateAndRepaint(); 
            threadManager.updateProcessingTimes();

            long sleepTime = targetDelay - (System.currentTimeMillis() - now);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    running = false;
                }
            }
        }
    }

    private void updateAndRepaint() {
        threadManager.updateParticles(); 
        SwingUtilities.invokeLater(drawPanel::repaint);
    }

    public void stopGameLoop() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public WallController getWallController() {
        return wallController;
    }

    public DynamicThreadManager getDynamicThreadManager() {
        return threadManager;
    }

    private class DrawPanel extends JPanel {
        private double fpsToDisplay = 0;

        public void setFps(double fps) {
            this.fpsToDisplay = fps;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1280, 720);
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        public DrawPanel() {
            super();
            setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.WHITE);

            threadManager.drawParticles(g);

            g.setColor(Color.WHITE);
            
            wallController.drawWalls(g);

            g.setColor(Color.RED); 
            g.drawString(String.format("FPS: %.2f", fpsToDisplay), 10, 20);

            g.setColor(Color.BLUE); 
            g.drawString(String.format("Number of Particles: %d", threadManager.getParticleSize()), 100, 20);
            
        }
    }
}
