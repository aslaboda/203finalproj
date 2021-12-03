public class EasyDogFactory implements DogFactory
{
    public Dog createEntity(String id, Point position, int difficulty, ImageStore imageStore)
    {
        return new Dog(id, position, 500, 6, imageStore.getImageList("bluedog"), new SingleStepPathingStrategy());
    }

}
