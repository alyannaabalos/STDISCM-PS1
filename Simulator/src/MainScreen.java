import javax.swing.*;
import java.awt.Dimension;


public class MainScreen extends JFrame {
    public MainScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Physics Particle Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        ParticleSimulatorPanel particleSimulatorPanel = new ParticleSimulatorPanel();
        add(particleSimulatorPanel);

        pack();

        // Manual adjustment to ensure the JFrame is large enough
        setSize(new Dimension(1280 + getInsets().left + getInsets().right, 
                              720 + getInsets().top + getInsets().bottom + 150));
        
        setLocationRelativeTo(null); // Center the window on screen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainScreen ms = new MainScreen();
            ms.setVisible(true);
        });
    }
}
