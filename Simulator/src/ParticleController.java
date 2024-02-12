import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class ParticleController {
    private List<Particle> particles = new ArrayList<>();

    public ParticleController() {
    }

    public ParticleController(List<Particle> particles) {
        this.particles = particles;
    }    

    public void addParticle(int x, int y, double angle, double velocity) {
        particles.add(new Particle(x, y, Math.cos(angle) * velocity, Math.sin(angle) * velocity));
    }

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    public void updateParticles(int canvasWidth, int canvasHeight, List<Wall> walls) {
        for (Particle particle : particles) {
            particle.update(canvasWidth, canvasHeight);
            particle.checkWallCollisions(walls);
        }
    }

    public void drawParticles(Graphics g) {
        for (Particle particle : particles) {
            particle.draw(g);
        }
    }
}
