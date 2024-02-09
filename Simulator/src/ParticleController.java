import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParticleController {
    private final List<Particle> particles = new ArrayList<>();
    private BufferedImage particleImage; // Image for particle

    public ParticleController() {
        // Load the particle image from a file
        try {
            particleImage = ImageIO.read(new File("particle.png")); // Adjust the file path as necessary
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addParticle(int x, int y, double angle, double velocity) {
        particles.add(new Particle(x, y, Math.cos(angle) * velocity, Math.sin(angle) * velocity));
    }

    public void updateParticles(int canvasWidth, int canvasHeight) {
        for (Particle particle : particles) {
            particle.update(canvasWidth, canvasHeight);
        }
    }

    public void drawParticles(Graphics g) {
        for (Particle particle : particles) {
            particle.draw(g);
        }
    }

    private class Particle {
        double x, y, vx, vy;

        public Particle(int x, int y, double vx, double vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        void update(int canvasWidth, int canvasHeight) {
            x += vx;
            y += vy;

            if (x <= 5 || x >= canvasWidth - 5) {
                vx *= -1;
            }

            if (y <= 5 || y >= canvasHeight - 5) {
                vy *= -1;
            }
        }

        void draw(Graphics g) {
            if (particleImage != null) {
                int width = 50; // Set width to match the oval's width
                int height = 50; // Set height to match the oval's height
                g.drawImage(particleImage, (int)x - width / 2, (int)y - height / 2, width, height, null);
            }
        }
    }
}
