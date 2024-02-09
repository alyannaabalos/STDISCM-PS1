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
                Point2D.Double collisionPoint = getCollisionPoint(wall);
                if (collisionPoint != null) {
                    // Calculate the reflection
                    Point2D.Double wallVector = new Point2D.Double(wall.x2 - wall.x1, wall.y2 - wall.y1);
                    Point2D.Double normal = new Point2D.Double(-wallVector.y, wallVector.x);
                    double length = Math.sqrt(normal.x * normal.x + normal.y * normal.y);
                    normal.x /= length;
                    normal.y /= length;
    
                    double dotProduct = 2 * (vx * normal.x + vy * normal.y);
                    vx -= dotProduct * normal.x;
                    vy -= dotProduct * normal.y;
    
                    // Correct the position
                    x = collisionPoint.x;
                    y = collisionPoint.y;
                }
            }
        }
    
        Point2D.Double getCollisionPoint(Wall wall) {
            // Ray-casting approach
            double dx = vx;
            double dy = vy;
            double fx = x - dx;
            double fy = y - dy;
    
            double wallDx = wall.x2 - wall.x1;
            double wallDy = wall.y2 - wall.y1;
    
            double denom = wallDy * dx - wallDx * dy;
            if (denom == 0) {
                return null; // No collision
            }
    
            double ua = (wallDx * (fy - wall.y1) - wallDy * (fx - wall.x1)) / denom;
            if (ua < 0 || ua > 1) {
                return null; // Collision but not within the current movement
            }
    
            double ub = (dx * (fy - wall.y1) - dy * (fx - wall.x1)) / denom;
            if (ub < 0 || ub > 1) {
                return null; // The particle would never collide with the wall segment
            }
    
            // The collision point
            return new Point2D.Double(fx + ua * dx, fy + ua * dy);
        }

        void draw(Graphics g) {
            if (particleImage != null) {
                int width = 50; // Set width to match the oval's width
                int height = 50; // Set height to match the oval's height
                g.drawImage(particleImage, (int)x - width / 2, (int)y - height / 2, width, height, null);
            }
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
