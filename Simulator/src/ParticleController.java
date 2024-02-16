import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParticleController {
    private List<Particle> particles = new CopyOnWriteArrayList<>();

    public ParticleController() {
    }

    public ParticleController(List<Particle> particles) {
        this.particles = new CopyOnWriteArrayList<>(particles);
    }    

    public void addParticle(int x, int y, double angle, double velocity) {
        particles.add(new Particle(x, y, Math.cos(angle) * velocity, Math.sin(angle) * velocity));
    }

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    public void updateParticles(double deltaTime, int canvasWidth, int canvasHeight, List<Wall> walls) {
        for (Particle particle : particles) {
            // Now passing deltaTime along with the other required arguments
            particle.update(deltaTime, canvasWidth, canvasHeight, walls);
        }
    }

    public List<Particle> getParticles(){
        return particles;
    }

    public void drawParticles(Graphics g, int canvasHeight) {
        for (Particle particle : particles) {
            particle.draw(g, canvasHeight);
        }
    }
}
