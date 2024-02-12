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

    public WallController getWallController() {
        return particleSimulatorPanel.getWallController();
    }

    public ParticleSimulatorPanel getParticleSimulatorPanel() {
        return particleSimulatorPanel;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MainScreen ms = new MainScreen();
            ms.setVisible(true);
            ParticleSimulatorPanel particleSimulatorPanel = ms.getParticleSimulatorPanel();
            if (particleSimulatorPanel != null) {
                new InputFeedbackWindow(particleSimulatorPanel.getDynamicThreadManager(), ms.getWallController()).setVisible(true);
            } else {
                System.out.println("ParticleSimulatorPanel is null.");
            }
        });
    }    
}
