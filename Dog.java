import processing.core.PImage;

import java.util.List;
import java.util.Optional;

/*
This class is a child class of Entity, IDs, Biotic, AnimatedBiotic. The Crab Class contains logic expected of a Crab for program etiquette.
 */

public class Dog extends AnimatedBiotic {

    // Constructor:
    public Dog(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images, PathingStrategy pathingStrategy) {
        super(id, position, images, actionPeriod, animationPeriod, pathingStrategy);
    }

    // Methods:
    public void executeActivity(EventScheduler eventScheduler, MovingEntity entity, WorldModel world, ImageStore imageStore) {
        Optional<Entity> dogTarget = entity.getPosition().findNearest(world, Cat.class);
        long nextPeriod = entity.getActionPeriod();
        if (dogTarget.isPresent()) {
            Point tgtPos = dogTarget.get().getPosition();
            if (moveToDog(entity, world, dogTarget.get(), eventScheduler)) {
                Quake quake = new Quake(tgtPos, imageStore.getImageList(WorldModel.QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += entity.getActionPeriod(); // no longer need to cast entity to biotic
                quake.scheduleActions(world, imageStore, eventScheduler);
            }
        }

        eventScheduler.scheduleEvent(entity, new Activity(entity, world, imageStore), nextPeriod);

    }

    public static boolean moveToDog(Entity dog, WorldModel world, Entity target, EventScheduler scheduler) {
        if (dog.getPosition().adjacent(target.getPosition())) {
             world.removeEntity(target); // THIS is the consequence of getting caught by the dog
             scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = nextPositionDog(world, dog, target.getPosition());

            if (!dog.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(dog, nextPos);
            }
            return false;
        }
    }

    public static Point nextPositionDog(WorldModel world, Entity entity, Point destPos) {

        List<Point> path;
        path = ((Dog)entity).getPathingStrategy().computePath(entity.getPosition(), destPos,
                p ->  world.withinBounds(p) && !(world.getOccupant(p).isPresent() /*&& !(world.getOccupant(p).get() instanceof Cheese)*/),
                (p1, p2) -> Point.neighbors(p1, p2),
                PathingStrategy.CARDINAL_NEIGHBORS);

        Point newPos;

        if (path.size() != 0) {
            newPos = path.get(0);
        }
        else {
            newPos = entity.getPosition();
        }
        return newPos;

    }

}
