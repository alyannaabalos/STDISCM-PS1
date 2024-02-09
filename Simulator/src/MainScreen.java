import javax.swing.*;
import java.awt.*;

public class MainScreen extends JFrame {
    public MainScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Physics Particle Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new ParticleSimulatorPanel());

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainScreen ms = new MainScreen();
            ms.setVisible(true);
        });
    }
}
