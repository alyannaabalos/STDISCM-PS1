import javax.swing.*;
import java.awt.*;

public class ParticleSimulatorPanel extends JPanel {
    private final DrawPanel drawPanel;
    private final ParticleController particleController;

    public ParticleSimulatorPanel() {
        drawPanel = new DrawPanel();
        particleController = new ParticleController();
        setLayout(new BorderLayout());
        add(drawPanel, BorderLayout.CENTER);
        new Timer(16, e -> updateAndRepaint()).start(); // Roughly 60 FPS
    }

    private void updateAndRepaint() {
        particleController.updateParticles(drawPanel.getWidth(), drawPanel.getHeight());
        drawPanel.repaint();
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    private class DrawPanel extends JPanel {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1000, 440);
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

            particleController.drawParticles(g);

            g.setColor(Color.WHITE);
            
            particleController.drawWalls(g);
        }
    }
}
