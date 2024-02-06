import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;

public class MainScreen {

    public static void main(String[] args) {
        // Create the JFrame.
        JFrame frame = new JFrame("Physics Particle Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JPanel.
        JPanel panel = new JPanel();
        
        // Set the background color of the panel to pink.
        panel.setBackground(Color.PINK);
        
        // Add the panel to the frame's content pane.
        frame.getContentPane().add(panel);

        // Set the size of the frame to 1280x720 pixels.
        frame.setSize(1280, 720);

        // This positions the JFrame to the center of the screen.
        frame.setLocationRelativeTo(null);

        // Make the frame visible.
        frame.setVisible(true);
    }
}
