import javax.swing.*;
import java.awt.*;

public class MainScreen extends JFrame {
    private final ParticleSimulatorPanel particleSimulatorPanel;

    public MainScreen() {
        particleSimulatorPanel = new ParticleSimulatorPanel();
        initUI();
    }

    private void initUI() {
        setTitle("Physics Particle Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(particleSimulatorPanel);
        pack(); // Adjust the frame size based on its contents
        setLocationRelativeTo(null); // Center the window on screen
        setResizable(false); // Optional: prevent resizing
    }

    public ParticleController getParticleController() {
        return particleSimulatorPanel.getParticleController();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MainScreen ms = new MainScreen();
            ms.setVisible(true);
            new InputFeedbackWindow(ms.getParticleController()).setVisible(true); // Create and show the input and feedback window
        });
    }
}
