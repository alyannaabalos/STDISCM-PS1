import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Point2D;
import static java.lang.Math.*;

public class Particle {
    double x, y;
    double velocity; 
    double angle; // Angle in degrees

    public Particle(double x, double y, double velocity, double angle) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.angle = angle;
    }

    public void update(double deltaTime, int canvasWidth, int canvasHeight, List<Wall> walls) {
        move(deltaTime);
        checkCanvasBordersCollision(canvasWidth, canvasHeight);
        checkWallCollisions(walls, deltaTime);
    }

    private void move(double deltaTime) {
        x += cos(toRadians(angle)) * velocity * deltaTime;
        y += sin(toRadians(angle)) * velocity * deltaTime;
    }

    private void checkCanvasBordersCollision(int canvasWidth, int canvasHeight) {
        if (x < 0 || x > canvasWidth) {
            angle = 180 - angle;
        }
        if (y < 0 || y > canvasHeight) {
            angle = -angle;
        }
        normalizeAngle();
    }

    private void checkWallCollisions(List<Wall> walls, double deltaTime) {
        for (Wall wall : walls) {
            double toi = calculateTOI(wall);
            if (toi >= 0 && toi <= 1) {
                // Calculate the velocity components based on angle and velocity
                double vx = cos(toRadians(angle)) * velocity;
                double vy = sin(toRadians(angle)) * velocity;

                // Move to collision point
                x += vx * toi;
                y += vy * toi;

                List<Wall> nearestWalls = getNearestWalls(this, walls);

                if (!nearestWalls.isEmpty() && nearestWalls.size() >= 2) {
                    vx *= -1;
                    vy *= -1;
                } else {
                    // Reflect the particle's velocity using the wall's normal
                    Point2D.Double wallVector = new Point2D.Double(wall.x2 - wall.x1, wall.y2 - wall.y1);
                    Point2D.Double normal = new Point2D.Double(-wallVector.y, wallVector.x);
                    double length = Math.sqrt(normal.x * normal.x + normal.y * normal.y);
                    normal.x /= length;
                    normal.y /= length;

                    double dotProduct = vx * normal.x + vy * normal.y;
                    vx -= 2 * dotProduct * normal.x;
                    vy -= 2 * dotProduct * normal.y;

                    // Update the angle based on the new velocity vector
                    angle = toDegrees(atan2(vy, vx));
                }  

                // Adjust position slightly to prevent sticking to the wall
                x += vx * deltaTime * (1 - toi);
                y += vy * deltaTime * (1 - toi);

                // Ensure the angle remains normalized
                normalizeAngle();
            }
        }
    }
    
    double calculateTOI(Wall wall) {
        // Calculate velocity components based on angle and velocity
        double vx = Math.cos(Math.toRadians(angle)) * velocity;
        double vy = Math.sin(Math.toRadians(angle)) * velocity;
    
        double wx = wall.x2 - wall.x1;
        double wy = wall.y2 - wall.y1;
    
        // Normal vector to the wall
        double nx = -wy;
        double ny = wx;
    
        // Vector from wall's start point to particle's current position
        double px = x - wall.x1;
        double py = y - wall.y1;
    
        // Calculate dot products for TOI formula
        double numerator = nx * px + ny * py;
        double denominator = nx * vx + ny * vy;
    
        if (denominator == 0) return -1; // Parallel movement, no collision
    
        double t = -numerator / denominator;
    
        if (t < 0 || t > 1) return -1; // Collision not within current update cycle
    
        // Calculate potential collision point
        double collisionX = x + vx * t;
        double collisionY = y + vy * t;
        double wallDot = wx * wx + wy * wy;
    
        // Check if collision point is within the wall segment
        double collisionDot = (collisionX - wall.x1) * wx + (collisionY - wall.y1) * wy;
    
        if (collisionDot < 0 || collisionDot > wallDot) return -1; // Collision point not on wall
    
        return t;
    }
    

    List<Wall> getNearestWalls(Particle particle, List<Wall> walls) {
        List<Wall> nearestWalls = new ArrayList<>();
        
        // Calculate the potential movement vector components
        double potentialVx = Math.cos(Math.toRadians(particle.angle)) * particle.velocity;
        double potentialVy = Math.sin(Math.toRadians(particle.angle)) * particle.velocity;
        
        for (Wall wall : walls) {
            // Simplistic proximity check based on the particle's current position and potential movement
            boolean isNearX = Math.abs(wall.x1 - particle.x) <= Math.abs(potentialVx) || Math.abs(wall.x2 - particle.x) <= Math.abs(potentialVx);
            boolean isNearY = Math.abs(wall.y1 - particle.y) <= Math.abs(potentialVy) || Math.abs(wall.y2 - particle.y) <= Math.abs(potentialVy);
            
            if (isNearX && isNearY) {
                nearestWalls.add(wall);
            }
        }
        return nearestWalls;
    }

    private void normalizeAngle() {
        angle %= 360;
        if (angle < 0) {
            angle += 360;
        }
    }

    public void draw(Graphics g, int canvasHeight) {
        int invertedY = canvasHeight - (int)y - 5;
        g.fillOval((int)x - 5, invertedY, 10, 10);
    }
}
