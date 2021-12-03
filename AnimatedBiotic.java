import processing.core.PImage;

import java.util.List;

/*
AnimatedBiotic is an Abstract Class
Hierarchical tree:  Entity -> IDs -> Biotic -> AnimatedBiotic
 */

abstract class AnimatedBiotic extends MovingEntity
{

    // Fields:
    protected int animationPeriod;
    public PathingStrategy pathingStrategy;

    // Constructor:
    public AnimatedBiotic(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, PathingStrategy pathingStrategy)
    {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
        this.pathingStrategy = pathingStrategy;
    }

    // Concrete Methods:
    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler)
    {
        eventScheduler.scheduleEvent(this, new Activity(this, world, imageStore), getActionPeriod());
        eventScheduler.scheduleEvent(this, new Animation(this, 0), getAnimationPeriod());
    }

    // Accessors:
    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public PathingStrategy getPathingStrategy() { return this.pathingStrategy; }
}
