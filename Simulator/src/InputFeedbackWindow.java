import javax.swing.*;
import java.awt.*;

public class InputFeedbackWindow extends JFrame {
    public InputFeedbackWindow(ParticleController particleController) {
        setTitle("Particle Controls");
        setSize(700, 100); // Adjust the size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Adjust as needed or remove

        JPanel inputPanel = new JPanel(new FlowLayout()); // Ensure this uses FlowLayout for consistent component alignment
        JLabel feedbackLabel = new JLabel(" ");
        feedbackLabel.setHorizontalAlignment(JLabel.CENTER); // Set the label's text to be centered

        JPanel feedbackPanel = new JPanel(new FlowLayout());
        feedbackPanel.add(feedbackLabel);

        JScrollPane feedbackScrollPane = new JScrollPane(feedbackPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        feedbackScrollPane.setBorder(null);

        JTextField xInput = new JTextField(5);
        JTextField yInput = new JTextField(5);
        JTextField angleInput = new JTextField(5);
        JTextField velocityInput = new JTextField(5);
        JTextField numberInput = new JTextField("1", 5); // Set default value to 1 for number of particles
        JButton addButton = new JButton("Add");

        inputPanel.add(new JLabel("No. of Particles:")); // Label for the number of particles
        inputPanel.add(numberInput); // Add the input field to the panel
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
                int number = Integer.parseInt(numberInput.getText()); // Get the number of particles to add

                for (int i = 0; i < number; i++) { // Loop to add the specified number of particles
                    particleController.addParticle(x, y, angle, velocity);
                }

                feedbackLabel.setText(String.format("%d particles added with position (%d, %d), angle: %.1f degrees, velocity: %.1f pixels/second", number, x, y, Math.toDegrees(angle), velocity));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(feedbackScrollPane, BorderLayout.CENTER);
    }
}
