import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class InputFeedbackWindow extends JFrame {
    private DynamicThreadManager threadManager;
    private WallController wallController;
    private JLabel feedbackLabel;
    private List<String> feedbackMessages = new ArrayList<>();

    public InputFeedbackWindow(DynamicThreadManager threadManager, WallController wallController) {
        this.wallController = wallController;
        this.threadManager = threadManager;
        setTitle("Particle Controls");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(750, 400); 

        feedbackLabel = new JLabel("<html></html>");
        feedbackLabel.setHorizontalAlignment(JLabel.CENTER);

        JScrollPane feedbackScrollPane = new JScrollPane(feedbackLabel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        feedbackScrollPane.setPreferredSize(new Dimension(700, 150));
        feedbackScrollPane.setBorder(null);

        JLabel feedbackTitle = new JLabel("Output");
        feedbackTitle.setHorizontalAlignment(JLabel.CENTER);
        feedbackTitle.setBorder(new EmptyBorder(10, 0, 10, 0)); 
        feedbackTitle.setOpaque(true); 
        feedbackTitle.setBackground(Color.WHITE);

        JPanel feedbackPanel = new JPanel(new BorderLayout());
        feedbackPanel.add(feedbackTitle, BorderLayout.NORTH); // Add title at the top
        feedbackPanel.add(feedbackScrollPane, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(createParticleInputPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createWallInputPanel());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.NORTH);
        getContentPane().add(feedbackPanel, BorderLayout.CENTER); 

        pack();
        setLocationRelativeTo(null);
    }


    private JPanel createParticleInputPanel() {
        JPanel particlePanel = new JPanel(new BorderLayout());
        JPanel particleInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel particleButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JLabel particleLabel = new JLabel("Add particles");
        particleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

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
                if(x > 1280 || y > 720 || x < 0 || y < 0) {
                    JOptionPane.showMessageDialog(this, "X value must be between 0 and 1280 and Y value must be between 0 and 720.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double angle = Math.toRadians(Double.parseDouble(angleInput.getText()));
                double velocity = Double.parseDouble(velocityInput.getText());
                int number = Integer.parseInt(numberInput.getText());
                
                String initialFeedback = "Adding " + number + " particles...";
                feedbackMessages.add(initialFeedback);
                int feedbackIndex = feedbackMessages.size() - 1;
                updateFeedbackDisplay();

                ActionListener taskPerformer = new ActionListener() {
                    private int count = 0;

                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if (count < number) {
                            threadManager.addParticle(x, y, angle, velocity);
                            feedbackMessages.set(feedbackIndex, String.format("%d of %d particles added with position (%d, %d), angle: %.1f degrees, velocity: %.1f pixels/second", count + 1, number, x, y, Math.toDegrees(angle), velocity));
                            updateFeedbackDisplay();
                            count++;
                        } else {
                            feedbackMessages.set(feedbackIndex, String.format("%d particles added with position (%d, %d), angle: %.1f degrees, velocity: %.1f pixels/second", number, x, y, Math.toDegrees(angle), velocity));
                            updateFeedbackDisplay();
                            ((Timer)evt.getSource()).stop();
                        }
                    }
                };

                Timer timer = new Timer(100, taskPerformer);
                timer.setInitialDelay(0);
                timer.start();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return particlePanel;
    }

    private void updateFeedbackDisplay() {
        StringBuilder feedbackHtml = new StringBuilder("<html>");
        // Iterate through the feedbackMessages list in reverse order to add the latest message at the top
        for (int i = feedbackMessages.size() - 1; i >= 0; i--) {
            feedbackHtml.append(feedbackMessages.get(i)).append("<br>");
        }
        feedbackHtml.append("</html>");
        feedbackLabel.setText(feedbackHtml.toString());
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

            if(x1 > 1280 || y1 > 720 || x2 > 1280 || y2 > 720 || x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0) {
                JOptionPane.showMessageDialog(this, "X value must be between 0 and 1280 and Y value must be between 0 and 720.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            wallController.addWall(x1, y1, x2, y2);

            String wallFeedback = String.format("Wall added between (%d, %d) and (%d, %d)", x1, y1, x2, y2);
            feedbackMessages.add(wallFeedback);
            updateFeedbackDisplay();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    return wallPanel;
}

}
