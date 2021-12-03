import processing.core.PImage;

import java.util.List;

public abstract class CatFactory {
    public static Cat create(String id, Point pos, List<PImage> images){
        return new Cat(id, pos, images);
    }
}
