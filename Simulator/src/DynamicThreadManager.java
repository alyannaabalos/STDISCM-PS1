import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.awt.*;

public class DynamicThreadManager {
    private List<ParticleProcessor> processors = new ArrayList<>();
    private ExecutorService executorService = Executors.newCachedThreadPool(); // Manages threads
    private int canvasWidth, canvasHeight;
    private WallController wallController;
    private int targetFPS = 60;
    private int roundRobinIndex = 0;
    private int particleSize = 0;
    private int optimalParticlesPerThread = 2000;

    public DynamicThreadManager(WallController wallController) {
        this.wallController = wallController;
    }

    public void setCanvasSize(int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        addProcessor();
    }

    private void addProcessor() {
        System.err.println(String.format("Width: %d Height: %d", canvasWidth, canvasHeight));
        ParticleProcessor processor = new ParticleProcessor(canvasWidth, canvasHeight, wallController);
        processors.add(processor);
        executorService.execute(processor); // Start the processor in a new thread
    }

    public void checkAndAdjustFPS(int currentFPS) {
        // Example pseudocode
        if (shouldAddThread(currentFPS)) {
            System.err.println(String.format("Number of particles at this time: %d", particleSize));
            addProcessor();
        }
    }
    
    private boolean shouldAddThread(int currentFPS) {
        return (currentFPS < targetFPS || 
        (particleSize / processors.size() > optimalParticlesPerThread)) && 
        processors.size() < Runtime.getRuntime().availableProcessors() &&
        particleSize != 0;
    }

    public void addParticle(Particle particle) {
        if (processors.isEmpty()) {
            addProcessor(); 
        }

        particleSize += 1;

        // Add particle in round-robin fashion
        ParticleProcessor selectedProcessor = processors.get(roundRobinIndex);
        selectedProcessor.addParticle(particle);
        roundRobinIndex = (roundRobinIndex + 1) % processors.size();
    }

    public void addParticle(int x, int y, double angle, double velocity) {
        if (processors.isEmpty()) {
            addProcessor(); // Ensure there is at least one processor
        }

        particleSize += 1;

        // Add particle in round-robin fashion
        ParticleProcessor selectedProcessor = processors.get(roundRobinIndex);
        selectedProcessor.addParticle(new Particle(x, y, Math.cos(angle) * velocity, Math.sin(angle) * velocity));
        roundRobinIndex = (roundRobinIndex + 1) % processors.size();
    }

    public void updateParticles() {
        for (ParticleProcessor processor : processors) {
            executorService.submit(processor::run);
        }
    }
     
    public void drawParticles(Graphics g) {
        for (ParticleProcessor processor : processors) {
            processor.getParticleController().drawParticles(g);
        }
    }    

    public int getParticleSize(){
        return particleSize;
    }

    // Implement redistribution logic here, based on your needs
}
