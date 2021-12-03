import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/*
This class deals with point logic on the board
 */

final class Point {

   // Fields:
   public int x;
   public int y;

   // Constructor:
   public Point(int x, int y) {
      this.x = x;
      this.y = y;
   }

   // Methods:
   private int distanceSquared(Point p2) {
      int deltaX = this.x - p2.x;
      int deltaY = this.y - p2.y;
      return deltaX * deltaX + deltaY * deltaY;
   }

   public boolean adjacent(Point p2) {
      return (this.x == p2.x && Math.abs(this.y - p2.y) == 1) ||
              (this.y == p2.y && Math.abs(this.x - p2.x) == 1);
   }

   private Optional<Entity> nearestEntity(List<Entity> entities) {
      if (entities.isEmpty()) {
         return Optional.empty();
      }

      else {
         Entity nearest = entities.get(0);
         int nearestDistance = nearest.getPosition().distanceSquared(this);
         for (Entity other : entities) {
            int otherDistance = other.getPosition().distanceSquared(this);
            if (otherDistance < nearestDistance) {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }
         return Optional.of(nearest);
      }
   }

   public Optional<Entity> findNearest(WorldModel world, Class kind) {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : world.getEntities()) {
         if (kind.isInstance(entity)) {
            ofType.add(entity);
         }

      }
      return this.nearestEntity(ofType);
   }

   public static boolean neighbors(Point p1, Point p2)
   {
      return p1.x+1 == p2.x && p1.y == p2.y ||
              p1.x-1 == p2.x && p1.y == p2.y ||
              p1.x == p2.x && p1.y+1 == p2.y ||
              p1.x == p2.x && p1.y-1 == p2.y;
   }

   public String toString() {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other) {
      return other instanceof Point && ((Point) other).x == this.x && ((Point) other).y == this.y;
   }



   public int hashCode() {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

}
