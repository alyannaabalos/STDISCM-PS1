import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends JFrame {
    private final DrawPanel drawPanel = new DrawPanel();
    private final List<Particle> particles = new ArrayList<>();
    private final JLabel feedbackLabel = new JLabel(" ");

    public MainScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Physics Particle Simulator");
        //setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        JPanel feedbackPanel = new JPanel(new BorderLayout());
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane feedbackScrollPane = new JScrollPane(feedbackLabel,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        feedbackScrollPane.setBorder(null);
        feedbackPanel.add(feedbackScrollPane, BorderLayout.CENTER);
        feedbackPanel.setMaximumSize(feedbackPanel.getPreferredSize());

        drawPanel.setPreferredSize(new Dimension(1280, 720));

        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(feedbackPanel, BorderLayout.NORTH);
        mainPanel.add(drawPanel, BorderLayout.CENTER);

        // Adding panels to the frame
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

                Particle particle = new Particle(x, y, Math.cos(angle) * velocity, Math.sin(angle) * velocity);
                particles.add(particle);

                feedbackLabel.setText(String.format("Particle added with position (%d, %d), angle: %.1f degrees, velocity: %.1f pixels/second", x, y, Math.toDegrees(angle), velocity));
                feedbackPanel.revalidate();
                feedbackPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(MainScreen.this, "Please enter valid numbers for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        new Timer(16, e -> updateAndRepaint()).start(); // Roughly 60 FPS

        pack(); // Adjust the window size based on the preferred sizes of its components
        setLocationRelativeTo(null); // Center the window after packing
    }

    private void updateAndRepaint() {
        int canvasWidth = drawPanel.getWidth();
        int canvasHeight = drawPanel.getHeight();

        for (Particle particle : particles) {
            particle.update(canvasWidth, canvasHeight);
        }
        drawPanel.repaint();
    }


    private class DrawPanel extends JPanel {
        public DrawPanel() {
            // Attempt to set the background color directly
            setBackground(Color.PINK);
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Ensure the background is filled with pink, especially useful for custom painting
            g.setColor(Color.BLACK);
    
            // Then draw all particles on top of the pink background
            for (Particle particle : particles) {
                particle.draw(g);
            }
        }
    }
    

    private class Particle {
        double x, y, vx, vy;

        public Particle(int x, int y, double vx, double vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        void update(int canvasWidth, int canvasHeight) {
            x += vx;
            y += vy;

            if (x <= 5 || x >= canvasWidth - 5) {
                vx *= -1;
                x = Math.max(5, Math.min(x, canvasWidth - 5));
            }

            if (y <= 5 || y >= canvasHeight - 5) {
                vy *= -1;
                y = Math.max(5, Math.min(y, canvasHeight - 5));
            }
        }

        void draw(Graphics g) {
            g.fillOval((int) x - 5, (int) y - 5, 10, 10); // Draw particles as small circles
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainScreen ms = new MainScreen();
            ms.setVisible(true);
        });
    }
}
