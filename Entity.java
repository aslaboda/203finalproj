import java.util.List;

import processing.core.PImage;

/* Entity is an abstract parent class that is an overall superclass for all entities in our world. */


abstract class Entity
{
   // Fields:
   protected int imageIndex;
   protected int resourceCount;
   protected List<PImage> images;
   protected Point position;
   protected String id;

   // Constructor:
   protected Entity(String id, Point position, List<PImage> images) {
      this.position = position;
      this.images = images;
      this.id = id;
   }

    // Accessors & Setters:
   public Point getPosition() {
      return this.position;
   }

   public void setPosition(Point position) {this.position = position;}

   public List<PImage> getImages() {
      return this.images;
   }

   public int getImageIndex() {
      return this.imageIndex;
   }

   public int getResourceCount() {
      return resourceCount;
   }

   public void setResourceCount(int resourceCount) {
      this.resourceCount = resourceCount;
   }

   public void setImageIndex(int imageIndex) {
      this.imageIndex = imageIndex;
   }

   public String getId() {
      return this.id;
   }

}
