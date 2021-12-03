import processing.core.PImage;
import java.util.Optional;
import java.util.List;

/*
This class is a child class of Entity, IDs, Biotic. The SGrass Class contains logic expected of a Quake for program etiquette.
 */

public class CheeseSpawner extends MovingEntity {

    // Constructor:
    public CheeseSpawner(String id, Point position, int actionPeriod, List<PImage> images) {
        super(id, position, images, actionPeriod);
    }

    // Methods:
    public void executeActivity(EventScheduler eventScheduler, MovingEntity entity, WorldModel world, ImageStore imageStore) {
        Optional<Point> openPt = world.findOpenAround(entity.getPosition());

        if (openPt.isPresent()) {
            Cheese cheese = new Cheese(WorldModel.CHEESE_ID_PREFIX + entity.getId(), openPt.get(), WorldModel.CHEESE_CORRUPT_MIN + rand.nextInt(WorldModel.CHEESE_CORRUPT_MAX - WorldModel.CHEESE_CORRUPT_MIN), imageStore.getImageList(WorldModel.CHEESE_KEY));
            world.addEntity(cheese);
            cheese.scheduleActions(world, imageStore, eventScheduler);
        }

        eventScheduler.scheduleEvent(entity, new Activity(entity, world, imageStore), entity.getActionPeriod());
    }

}
