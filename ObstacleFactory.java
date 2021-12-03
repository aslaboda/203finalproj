import processing.core.PImage;

import java.util.List;

public abstract class ObstacleFactory {
    public static Obstacle create(String id, Point pos, List<PImage> images){
        return new Obstacle(id, pos, images);
    }

}
