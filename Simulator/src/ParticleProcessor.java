import java.util.*;

public class ParticleProcessor implements Runnable {
    private ParticleController particleController;
    private WallController wallController;
    private int canvasWidth, canvasHeight;
    private long lastProcessingTime = 0;

    public ParticleProcessor(int canvasWidth, int canvasHeight, WallController wallController) {
        this.particleController = new ParticleController();
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        this.wallController = wallController;
    }

    public ParticleProcessor(int canvasWidth, int canvasHeight, WallController wallController, List<Particle> particles) {
        this.particleController = new ParticleController(particles);
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        this.wallController = wallController;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        List<Wall> walls = wallController.getWall();
        particleController.updateParticles(canvasWidth, canvasHeight, walls);

        long endTime = System.currentTimeMillis();
        lastProcessingTime = endTime - startTime;
    }

    public void addParticle(Particle particle) {
        particleController.addParticle(particle);
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public long getLastProcessingTime() {
        return lastProcessingTime;
    }
}
