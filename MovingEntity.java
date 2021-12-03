import processing.core.PImage;
import java.util.List;
import java.util.Random;

/*
Biotic is an Abstract Class
Hierarchical tree:  Entity -> IDs -> Biotic
 */

abstract class MovingEntity extends Entity {

    // Fields:
    protected int actionPeriod;
    protected static final Random rand = new Random();

    // Constructor:
    public MovingEntity(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    // Abstract Methods:
    public abstract void executeActivity(EventScheduler eventScheduler, MovingEntity entity, WorldModel world, ImageStore imageStore);

    // Concrete Methods:
    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler) {
        eventScheduler.scheduleEvent(this, new Activity(this, world, imageStore), getActionPeriod());
    }

    // Accessors:
    public int getActionPeriod() {
        return actionPeriod;
    }
}
