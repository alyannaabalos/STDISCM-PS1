import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.awt.*;

public class DynamicThreadManager {
    private List<ParticleProcessor> processors = new CopyOnWriteArrayList<>();
    private ExecutorService executorService = Executors.newCachedThreadPool(); // Manages threads
    private int canvasWidth, canvasHeight;
    private WallController wallController;
    private int particleSize = 0;

    private int targetFPS = 60;
    private int roundRobinIndex = 0;
    
    private long lastAverageProcessingTime = 0;
    private List<Long> processingTimesHistory = new ArrayList<>();
    private static final int PROCESSING_TIME_HISTORY_SIZE = 20; 

    private int lastParticleSizeAtThreadAddition = 0;

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
        lastParticleSizeAtThreadAddition = particleSize;
    }

    private void addProcessor(List<Particle> particles) {
        System.err.println(String.format("Width: %d Height: %d", canvasWidth, canvasHeight));
        ParticleProcessor processor = new ParticleProcessor(canvasWidth, canvasHeight, wallController, particles);
        processors.add(processor);
        executorService.execute(processor); 
        lastParticleSizeAtThreadAddition = particleSize;
    }

    public void checkAndAdjustFPS() {
        int count = 1;
        if (shouldAddThread()) {
            redistributeParticles();

            for (ParticleProcessor processor : processors) {
                System.err.println("AFTER:");
                System.err.println(String.format("thread #: %d particles: %d", count, processor.getParticleController().getParticles().size()));

                count += 1;
            }
        }
    }
    
    private boolean shouldAddThread() {
        boolean processingTimeIncreasing = false;
        if (!processingTimesHistory.isEmpty() &&
            particleSize >= 1000 ) {                // for warm-up
            long currentAverageProcessingTime = processingTimesHistory.get(processingTimesHistory.size() - 1);
            processingTimeIncreasing = currentAverageProcessingTime > lastAverageProcessingTime;
        }

        boolean significantParticleIncrease = particleSize >= lastParticleSizeAtThreadAddition * 1.10;

        return processingTimeIncreasing &&
            processors.size() < Runtime.getRuntime().availableProcessors() &&
            particleSize != 0 && significantParticleIncrease;
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

    private void redistributeParticles() {
        int processorSize = processors.size();
        List<Particle> newParticles = new ArrayList<>();
        int count = 1;
    
        for (ParticleProcessor processor : processors) {
            System.err.println("BEFORE:");
            System.err.println(String.format("thread #: %d particles: %d", count, processor.getParticleController().getParticles().size()));

            List<Particle> allParticles = processor.getParticleController().getParticles();
            int popCount = allParticles.size() / (processorSize + 1); 
            
            List<Particle> particles = popItems(allParticles, popCount);
            newParticles.addAll(particles);

            count += 1;
        }
    
        addProcessor(newParticles);
    }

    public static List<Particle> popItems(List<Particle> list, int numberOfItemsToPop) {
        List<Particle> poppedItems = new ArrayList<>();
        int size = list.size();
        int endIndex = Math.min(size, numberOfItemsToPop);
        for (int i = 0; i < endIndex; i++) {
            poppedItems.add(list.remove(0));
        }
        return poppedItems;
    }

    public void updateProcessingTimes() {
        long totalProcessingTime = 0;
        for (ParticleProcessor processor : processors) {
            totalProcessingTime += processor.getLastProcessingTime();
        }
        long currentAverageProcessingTime = totalProcessingTime / processors.size();
    
        // Update the history
        processingTimesHistory.add(currentAverageProcessingTime);
        if (processingTimesHistory.size() > PROCESSING_TIME_HISTORY_SIZE) {
            processingTimesHistory.remove(0); // Keep the list size fixed
        }
    
        // Update last average processing time based on history
        lastAverageProcessingTime = processingTimesHistory.stream().mapToLong(Long::longValue).sum() / processingTimesHistory.size();
    }
    
}
