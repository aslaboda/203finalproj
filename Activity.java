
/*
Activity: Contains the logic for how entities act
 */

public class Activity extends Action
{

    // Fields:
    protected WorldModel world;
    protected ImageStore imageStore;

    // Constructor:
    public Activity(MovingEntity entity, WorldModel world, ImageStore imageStore)
    {
        super(entity);
        this.world = world;
        this.imageStore = imageStore;
    }

    // Concrete Methods:
    public void executeAction(EventScheduler eventScheduler)
    {
        entity.executeActivity(eventScheduler, entity, world, imageStore);
    }

}
