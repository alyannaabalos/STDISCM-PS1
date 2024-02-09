import javax.swing.*;
import java.awt.*;

public class InputFeedbackWindow extends JFrame {
    public InputFeedbackWindow(ParticleController particleController) {
        setTitle("Particle Controls");
        setSize(600, 100); // Adjust the size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Adjust as needed or remove

        JPanel inputPanel = new JPanel(new FlowLayout()); // Ensure this uses FlowLayout for consistent component alignment
        JLabel feedbackLabel = new JLabel(" ");
        feedbackLabel.setHorizontalAlignment(JLabel.CENTER); // Set the label's text to be centered

        // Use a panel with FlowLayout for the label to ensure it centers within its container
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
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(feedbackScrollPane, BorderLayout.CENTER); // Now the feedbackScrollPane contains the centered feedbackPanel
    }
}
