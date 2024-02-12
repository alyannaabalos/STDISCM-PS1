import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputFeedbackWindow extends JFrame {
    private DynamicThreadManager threadManager;
    private WallController wallController;
    private JLabel feedbackLabel;

    public InputFeedbackWindow(DynamicThreadManager threadManager, WallController wallController) {
        this.wallController = wallController;
        this.threadManager = threadManager;
        setTitle("Particle Controls");
        setSize(750, 200); // Adjust the size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Feedback label setup
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setHorizontalAlignment(JLabel.CENTER);
        JScrollPane feedbackScrollPane = new JScrollPane(new JPanel(new FlowLayout()).add(feedbackLabel),
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        feedbackScrollPane.setBorder(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Initialize input fields and buttons for particle and wall addition
        mainPanel.add(createParticleInputPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        mainPanel.add(createWallInputPanel());

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.NORTH);
        add(feedbackScrollPane, BorderLayout.SOUTH); // Add feedback at the bottom of the window
    }

    private JPanel createParticleInputPanel() {
        JPanel particlePanel = new JPanel(new BorderLayout());
        JPanel particleInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel particleButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JLabel particleLabel = new JLabel("Add particles");
        particleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Add some padding to the label

        JTextField numberInput = new JTextField("1", 5);
        JTextField xInput = new JTextField(5);
        JTextField yInput = new JTextField(5);
        JTextField angleInput = new JTextField(5);
        JTextField velocityInput = new JTextField(5);
        JButton addButton = new JButton("Add");

        particleInputPanel.add(new JLabel("No. of Particles:"));
        particleInputPanel.add(numberInput);
        particleInputPanel.add(new JLabel("X:"));
        particleInputPanel.add(xInput);
        particleInputPanel.add(new JLabel("Y:"));
        particleInputPanel.add(yInput);
        particleInputPanel.add(new JLabel("Angle:"));
        particleInputPanel.add(angleInput);
        particleInputPanel.add(new JLabel("Velocity:"));
        particleInputPanel.add(velocityInput);

        particleButtonPanel.add(addButton);

        particlePanel.add(particleLabel, BorderLayout.NORTH);
        particlePanel.add(particleInputPanel, BorderLayout.CENTER);
        particlePanel.add(particleButtonPanel, BorderLayout.EAST);

        addButton.addActionListener(e -> {
            try {
                int x = Integer.parseInt(xInput.getText());
                int y = Integer.parseInt(yInput.getText());
                double angle = Math.toRadians(Double.parseDouble(angleInput.getText()));
                double velocity = Double.parseDouble(velocityInput.getText());
                int number = Integer.parseInt(numberInput.getText());
        
                // Define an ActionListener for the Timer that will add particles
                ActionListener taskPerformer = new ActionListener() {
                    private int count = 0; // Counter to track the number of particles added
        
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if (count < number) {
                            threadManager.addParticle(x, y, angle, velocity);
                            feedbackLabel.setText(String.format("%d particles added with position (%d, %d), angle: %.1f degrees, velocity: %.1f pixels/second", count + 1, x, y, Math.toDegrees(angle), velocity));
                            count++;
                        } else {
                            ((Timer)evt.getSource()).stop(); // Stop the timer
                        }
                    }
                };
        
                // Create a new Timer that calls the ActionListener every 100 milliseconds
                Timer timer = new Timer(100, taskPerformer);
                timer.setInitialDelay(0); // Optional: Start without delay for the first execution
                timer.start();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        

        return particlePanel;
    }

    private JPanel createWallInputPanel() {
        JPanel wallPanel = new JPanel(new BorderLayout());
        JPanel wallInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel wallButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JLabel wallLabel = new JLabel("Add walls");
        wallLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Add some padding to the label

        JTextField x1Input = new JTextField(5);
        JTextField y1Input = new JTextField(5);
        JTextField x2Input = new JTextField(5);
        JTextField y2Input = new JTextField(5);
        JButton addWallButton = new JButton("Add");

        wallInputPanel.add(new JLabel("X1:"));
        wallInputPanel.add(x1Input);
        wallInputPanel.add(new JLabel("Y1:"));
        wallInputPanel.add(y1Input);
        wallInputPanel.add(new JLabel("X2:"));
        wallInputPanel.add(x2Input);
        wallInputPanel.add(new JLabel("Y2:"));
        wallInputPanel.add(y2Input);

        wallButtonPanel.add(addWallButton);

        wallPanel.add(wallLabel, BorderLayout.NORTH);
        wallPanel.add(wallInputPanel, BorderLayout.CENTER);
        wallPanel.add(wallButtonPanel, BorderLayout.EAST);

        addWallButton.addActionListener(e -> {
            try {
                int x1 = Integer.parseInt(x1Input.getText());
                int y1 = Integer.parseInt(y1Input.getText());
                int x2 = Integer.parseInt(x2Input.getText());
                int y2 = Integer.parseInt(y2Input.getText());

                // Assuming ParticleController has a method addWall to add walls
                wallController.addWall(x1, y1, x2, y2);

                feedbackLabel.setText(String.format("Wall added between (%d, %d) and (%d, %d)", x1, y1, x2, y2));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return wallPanel;
    }
}
