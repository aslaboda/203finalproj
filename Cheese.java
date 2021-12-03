import processing.core.PImage;

import java.util.List;

/*
This class is a child class of Entity, IDs, Biotic. The Fish Class contains logic expected of a Fish for program etiquette.
 */

public class Cheese extends MovingEntity {

    public Cheese(String id, Point position, int actionPeriod, List<PImage> images) {
        super(id, position, images, actionPeriod);
    }

    public void executeActivity(EventScheduler eventScheduler, MovingEntity entity, WorldModel world, ImageStore imageStore) {
        world.removeEntity(entity);
        eventScheduler.unscheduleAllEvents(entity);

    }


}
