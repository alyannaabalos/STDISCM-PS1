import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParticleController {
    private final List<Particle> particles = new ArrayList<>();
    private final List<Wall> walls = new ArrayList<>(); // List to store walls
    private BufferedImage particleImage; // Image for particle

    public ParticleController() {
        // Load the particle image from a file
        try {
            particleImage = ImageIO.read(getClass().getResourceAsStream("/particle.png"));
            // particleImage = ImageIO.read(new File("particle.png")); // Adjust the file path as necessary
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addParticle(int x, int y, double angle, double velocity) {
        particles.add(new Particle(x, y, Math.cos(angle) * velocity, Math.sin(angle) * velocity));
        
    }

    public void addWall(int x1, int y1, int x2, int y2) {
        walls.add(new Wall(x1, y1, x2, y2));
    }

    public void updateParticles(int canvasWidth, int canvasHeight) {
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

    public void drawWalls(Graphics g) {
        for (Wall wall : walls) {
            wall.draw(g);
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

        void checkWallCollisions(List<Wall> walls) {
            for (Wall wall : walls) {
                double toi = calculateTOI(wall);
                if (toi >= 0 && toi <= 1) {
                    // Move to collision point
                    x += vx * toi;
                    y += vy * toi;
    
                    // Reflect the particle's velocity
                    Point2D.Double wallVector = new Point2D.Double(wall.x2 - wall.x1, wall.y2 - wall.y1);
                    Point2D.Double normal = new Point2D.Double(-wallVector.y, wallVector.x);
                    double length = Math.sqrt(normal.x * normal.x + normal.y * normal.y);
                    normal.x /= length;
                    normal.y /= length;
    
                    double dotProduct = vx * normal.x + vy * normal.y;
                    vx -= 2 * dotProduct * normal.x;
                    vy -= 2 * dotProduct * normal.y;
    
                    // Adjust position to prevent sticking
                    x += vx * (1 - toi);
                    y += vy * (1 - toi);
                }
            }
        }
        
        double calculateTOI(Wall wall) {
            // Define wall vector
            double wx = wall.x2 - wall.x1;
            double wy = wall.y2 - wall.y1;
    
            // Normal vector to the wall (right-hand normal)
            double nx = -wy;
            double ny = wx;
    
            double ux = vx;
            double uy = vy;
    
            // Vector from wall start to particle position
            double px = x - wall.x1;
            double py = y - wall.y1;
    
            // Need to solve for t where dot((P + tU) - W, N) = 0
            // t = dot(W - P, N) / dot(U, N)
            double numerator = nx * px + ny * py;
            double denominator = nx * ux + ny * uy;
    
            if (denominator == 0) return -1;
    
            double t = -numerator / denominator;
    
            if (t < 0 || t > 1) return -1;
    
            double collisionX = x + ux * t;
            double collisionY = y + uy * t;
            double wallDot = wx * wx + wy * wy; 
            double collisionDot = (collisionX - wall.x1) * wx + (collisionY - wall.y1) * wy; // Dot product of wall vector and vector from wall start to collision point
    
            // The collision point must be between 0 and wallDot to be on the wall segment
            if (collisionDot < 0 || collisionDot > wallDot) return -1;
    
            return t;
        }

        void draw(Graphics g) {
            // if (particleImage != null) {
            //     int width = 50; // Set width to match the oval's width
            //     int height = 50; // Set height to match the oval's height
            //     g.drawImage(particleImage, (int)x - width / 2, (int)y - height / 2, width, height, null);
            // }

            g.fillOval((int)x - 5, (int)y - 5, 10, 10);
        }
    }

    private class Wall {
        int x1, y1, x2, y2;

        public Wall(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        void draw(Graphics g) {
            g.drawLine(x1, y1, x2, y2);
        }
    }
}
