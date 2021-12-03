public class CrazyDogFactory implements DogFactory
{
    public Dog createEntity(String id, Point position, int difficulty, ImageStore imageStore)
    {
        return new Dog(id, position, 1500, 6, imageStore.getImageList("greendog"), new SingleStepPathingStrategy());
    }

}
