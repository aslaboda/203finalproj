import processing.core.PImage;

import java.util.List;

public class HardDogFactory implements DogFactory
{
    public Dog createEntity(String id, Point position, int difficulty, ImageStore imageStore)
    {
        return new Dog(id, position, 100, 6, imageStore.getImageList("reddog"), new AStarPathingStrategy());
    }

}
