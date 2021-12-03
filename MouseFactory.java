import processing.core.PImage;

import java.util.List;

public abstract class MouseFactory {
    public static Mouse create(/*String id, Point pos, List<PImage> images*/String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images, PathingStrategy pathingStrategy){
        return new Mouse(id, position, actionPeriod,  animationPeriod, images,  pathingStrategy/*id, pos, images*/);
    }
}
