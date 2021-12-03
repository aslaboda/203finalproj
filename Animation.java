/*
This class is a child class of Action. The Animation Class contains logic expected for Animations for program etiquette.
 */

public class Animation extends Action{

    // Fields:
    protected int repeatCount;

    // Constructor:
    public Animation(MovingEntity entity, int repeatCount) {
        super(entity);
        this.repeatCount = repeatCount;
    }

    // Concrete Methods:
    public void executeAction(EventScheduler eventScheduler) {
        ImageStore.nextImage(entity);
        if (repeatCount != 1) {
            eventScheduler.scheduleEvent(entity, new Animation(entity, Math.max(repeatCount - 1, 0)), ((AnimatedBiotic)entity).getAnimationPeriod());
        }
    }
}
