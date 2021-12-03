import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Cat extends MovingEntity {
    public Cat(String id, Point pos, List<PImage> images){super(id, pos, images, 0);} // Action Period is a dud variable! Remember!

    public void move(int dx, int dy){
        this.position.x += dx;
        this.position.y += dy;
    }

    public void executeActivity(EventScheduler eventScheduler, MovingEntity entity, WorldModel world, ImageStore imageStore) {
        Optional<Entity> catTarget = entity.getPosition().findNearest(world, Mouse.class);
        long nextPeriod = entity.getActionPeriod();
        if (catTarget.isPresent()) {
            Point tgtPos = catTarget.get().getPosition();
            if (moveToCat(entity, world, catTarget.get(), eventScheduler)) {
                Quake quake = new Quake(tgtPos, imageStore.getImageList(WorldModel.QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += entity.getActionPeriod(); // no longer need to cast entity to biotic
                quake.scheduleActions(world, imageStore, eventScheduler);
            }
        }
        // Make Quake
        eventScheduler.scheduleEvent(entity, new Activity(entity, world, imageStore), nextPeriod);
    }

    public static boolean moveToCat(Entity crab, WorldModel world, Entity target, EventScheduler scheduler) {
        if (crab.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            return false;
        }
    }

}
