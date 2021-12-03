import processing.core.PImage;

import java.util.List;

public class NormalDogFactory implements DogFactory
{
    public Dog createEntity(String id, Point position, int difficulty, ImageStore imageStore)
    {
        return new Dog(id, position, 300, 6, imageStore.getImageList("yellowdog"), new AStarPathingStrategy());
    }

}
