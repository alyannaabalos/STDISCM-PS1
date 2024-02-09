import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParticleSimulatorPanel extends JPanel {
    private final DrawPanel drawPanel = new DrawPanel();
    private final ParticleController particleController = new ParticleController();

    public ParticleSimulatorPanel() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        JPanel feedbackPanel = new JPanel(new BorderLayout());

        JLabel feedbackLabel = new JLabel(" ");
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane feedbackScrollPane = new JScrollPane(feedbackLabel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        feedbackScrollPane.setBorder(null);
        feedbackPanel.add(feedbackScrollPane, BorderLayout.CENTER);
        feedbackPanel.setMaximumSize(feedbackPanel.getPreferredSize());

        drawPanel.setPreferredSize(new Dimension(1280, 720));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(feedbackPanel, BorderLayout.NORTH);
        mainPanel.add(drawPanel, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        JTextField xInput = new JTextField(5);
        JTextField yInput = new JTextField(5);
        JTextField angleInput = new JTextField(5);
        JTextField velocityInput = new JTextField(5);
        JButton addButton = new JButton("Add Particle");

        inputPanel.add(new JLabel("X:"));
        inputPanel.add(xInput);
        inputPanel.add(new JLabel("Y:"));
        inputPanel.add(yInput);
        inputPanel.add(new JLabel("Angle:"));
        inputPanel.add(angleInput);
        inputPanel.add(new JLabel("Velocity:"));
        inputPanel.add(velocityInput);
        inputPanel.add(addButton);

        addButton.addActionListener(e -> {
            try {
                int x = Integer.parseInt(xInput.getText());
                int y = Integer.parseInt(yInput.getText());
                double angle = Math.toRadians(Double.parseDouble(angleInput.getText()));
                double velocity = Double.parseDouble(velocityInput.getText());

                particleController.addParticle(x, y, angle, velocity);

                feedbackLabel.setText(String.format("Particle added with position (%d, %d), angle: %.1f degrees, velocity: %.1f pixels/second", x, y, Math.toDegrees(angle), velocity));
                feedbackPanel.revalidate();
                feedbackPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ParticleSimulatorPanel.this, "Please enter valid numbers for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        new Timer(16, e -> updateAndRepaint()).start(); // Roughly 60 FPS
    }

    private void updateAndRepaint() {
        particleController.updateParticles(drawPanel.getWidth(), drawPanel.getHeight());
        drawPanel.repaint();
    }

    private class DrawPanel extends JPanel {
        public DrawPanel() {
            setBackground(Color.PINK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.BLACK);

            particleController.drawParticles(g);
        }
    }
}
