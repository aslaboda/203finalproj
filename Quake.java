import processing.core.PImage;

import java.util.List;

/*
This class is a child class of Entity, IDs, Biotic, AnimatedBiotic. The Quake Class contains logic expected of a Quake for program etiquette.
 */

public class Quake extends AnimatedBiotic {

    // Fields:
    protected static final String QUAKE_ID = "quake";
    protected static final int QUAKE_ANIMATION_PERIOD = 100;
    protected static final int QUAKE_ACTION_PERIOD = 1100;
    protected static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    protected static final PathingStrategy QUAKE_PATHING_STRATEGY = new AStarPathingStrategy();

    // Construction:
    public Quake(Point position, List<PImage> images) {
        super(QUAKE_ID, position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD, QUAKE_PATHING_STRATEGY);
    }

    // Methods:
    public void executeActivity(EventScheduler eventScheduler, MovingEntity entity, WorldModel world, ImageStore imageStore) {
        eventScheduler.unscheduleAllEvents(entity);
        world.removeEntity(entity);
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler) {
        eventScheduler.scheduleEvent(this, new Activity(this, world, imageStore), QUAKE_ACTION_PERIOD);
        eventScheduler.scheduleEvent(this, new Animation(this, QUAKE_ANIMATION_REPEAT_COUNT), QUAKE_ANIMATION_PERIOD);
    }

}
