import processing.core.PImage;
import java.util.List;
import java.util.Optional;

/*
This class is a child class of Entity, IDs, Biotic, AnimatedBiotic. The Crab Class contains logic expected of a Crab for program etiquette.
 */

public class Mouse extends AnimatedBiotic {

    // Fields:
    protected static final String QUAKE_KEY = "quake";

    // Constructor:
    public Mouse(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images, PathingStrategy pathingStrategy) {
        super(id, position, images, actionPeriod, animationPeriod, pathingStrategy);
    }

    // Methods:
    public void executeActivity(EventScheduler eventScheduler, MovingEntity entity, WorldModel world, ImageStore imageStore) {
        Optional<Entity> mouseTarget = entity.getPosition().findNearest(world, Cheese.class);
        long nextPeriod = entity.getActionPeriod();
        if (mouseTarget.isPresent()) {
            Point tgtPos = mouseTarget.get().getPosition();
            if (moveToMouse(entity, world, mouseTarget.get(), eventScheduler)) {
                Quake quake = new Quake(tgtPos, imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += entity.getActionPeriod(); // no longer need to cast entity to biotic
                quake.scheduleActions(world, imageStore, eventScheduler);
            }
        }
        eventScheduler.scheduleEvent(entity, new Activity(entity, world, imageStore), nextPeriod);

    }

    public static boolean moveToMouse(Entity crab, WorldModel world, Entity target, EventScheduler scheduler) {
        if (crab.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = nextPositionMouse(world, crab, target.getPosition());

            if (!crab.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(crab, nextPos);
            }
            return false;
        }
    }

    public static Point nextPositionMouse(WorldModel world, Entity entity, Point destPos) {

        List<Point> path;
        path = ((Mouse)entity).getPathingStrategy().computePath(entity.getPosition(), destPos,
                p ->  world.withinBounds(p) && !(world.getOccupant(p).isPresent() && !(world.getOccupant(p).get() instanceof Cheese)),
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
