import javax.swing.*;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.EventQueue;

public class MainScreen extends JFrame {
    public MainScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Physics Particle Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        ParticleSimulatorPanel particleSimulatorPanel = new ParticleSimulatorPanel();
        add(particleSimulatorPanel);

        pack(); // Adjust the frame size based on its contents

        // After packing, check if the frame size needs adjustment
        Dimension screenSize = getSize();
        Insets insets = getInsets();
        int minWidth = 100 + insets.left + insets.right;
        int minHeight = 440 + insets.top + insets.bottom; // Adjusted for extra components
        
        if (screenSize.width < minWidth || screenSize.height < minHeight) {
            setSize(Math.max(minWidth, screenSize.width), Math.max(minHeight, screenSize.height));
        }

        setLocationRelativeTo(null); // Center the window on screen
        setResizable(false); // Optional: prevent resizing
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MainScreen ms = new MainScreen();
            ms.setVisible(true);
        });
    }
}
