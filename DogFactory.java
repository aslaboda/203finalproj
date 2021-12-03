import processing.core.PImage;

import java.util.List;

public interface DogFactory
{
    public Dog createEntity(String id, Point position, int difficulty, ImageStore imageStore);


}
