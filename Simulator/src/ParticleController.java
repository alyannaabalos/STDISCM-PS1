import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ParticleController {
    private final List<Particle> particles = new ArrayList<>();

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
                x = Math.max(5, Math.min(x, canvasWidth - 5));
            }

            if (y <= 5 || y >= canvasHeight - 5) {
                vy *= -1;
                y = Math.max(5, Math.min(y, canvasHeight - 5));
            }
        }

        void draw(Graphics g) {
            g.fillOval((int) x - 5, (int) y - 5, 10, 10);
        }
    }
}
