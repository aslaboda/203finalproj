import processing.core.PImage;

import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{
   public static final int difficulty = 1;
   public static DogFactory dog_factory= new EasyDogFactory();
   public static DogFactory world_event_dog_factory= new CrazyDogFactory();

   public static final String CAT_ID = "cat";
   // CHANGE THE CRAB Variables Below TO MOUSE? Delete later if not used
   protected static final int CRAB_PERIOD_SCALE = 4;
   // protected static final String CRAB_KEY = "crab";
   protected static final String CRAB_ID_SUFFIX = " -- crab";
   protected static final int CRAB_ANIMATION_MIN = 50;
   protected static final int CRAB_ANIMATION_MAX = 150;
   // Fields:
   protected static final String QUAKE_KEY = "quake";
   // Fields:
   private static final int PROPERTY_KEY = 0;
   private static final String BGND_KEY = "background";
   private static final int BGND_ID = 1;
   private static final int BGND_ROW = 3;
   private static final int BGND_COL = 2;
   private static final int BGND_NUM_PROPERTIES = 4;

   private static final String CAT_KEY = "cat";

   public static final String DOG_KEY = "dog";
   private static final int DOG_NUM_PROPERTIES = 4;
   public static final int DOG_ID = 1;
   private static final int DOG_ROW = 3;
   private static final int DOG_COL = 2;
   private static final int DOG_ACTION_PERIOD = 100; // 5
   private static final PathingStrategy DOG_PATHING_STRATEGY = new AStarPathingStrategy();       

   public static final String MOUSE_KEY = "mouse";
   private static final int MOUSE_NUM_PROPERTIES = 4;
   private static final int MOUSE_ID = 1;
   private static final int MOUSE_ROW = 3;
   private static final int MOUSE_COL = 2;
   private static final int MOUSE_ACTION_PERIOD = 5;
   private static final PathingStrategy MOUSE_PATHING_STRATEGY = new AStarPathingStrategy();


   protected static final int CHEESE_CORRUPT_MAX = 30000;
   protected static final String CHEESE_KEY = "cheese";
   protected static final String CHEESE_ID_PREFIX = "fish -- ";
   protected static final int CHEESE_CORRUPT_MIN = 20000;

   private static final int CHEESE_ID = 1;
   private static final int CHEESE_ACTION_PERIOD = 4;
   private static final int CHEESE_ROW = 3;
   private static final int CHEESE_COL = 2;
   private static final int CHEESE_NUM_PROPERTIES = 5;
   private static final int CHEESE_REACH = 1;

   private static final String CHEESE_SPAWNER_KEY = "cheeseSpawner";
   private static final int CHEESE_SPAWNER_ID = 1;
   private static final int CHEESE_SPAWNER_ACTION_PERIOD = 4;
   private static final int CHEESE_SPAWNER_ROW = 3;
   private static final int CHEESE_SPAWNER_COL = 2;
   private static final int CHEESE_SPAWNER_NUM_PROPERTIES = 5;


   private static final String SGRASS_KEY = "seaGrass";
   private static final int SGRASS_ID = 1;
   private static final int SGRASS_ACTION_PERIOD = 4;
   private static final int SGRASS_ROW = 3;
   private static final int SGRASS_COL = 2;
   private static final int SGRASS_NUM_PROPERTIES = 5;

   private static final String ATLANTIS_KEY = "atlantis";
   private static final int ATLANTIS_ID = 1;
   private static final int ATLANTIS_ROW = 3;
   private static final int ATLANTIS_COL = 2;
   private static final int ATLANTIS_NUM_PROPERTIES = 4;


   private static final String FISH_KEY = "fish";
   private static final int FISH_ID = 1;
   private static final int FISH_ACTION_PERIOD = 4;
   private static final int FISH_ROW = 3;
   private static final int FISH_COL = 2;
   private static final int FISH_NUM_PROPERTIES = 5;
   private static final int FISH_REACH = 1;

   private static final String OBSTACLE_KEY = "obstacle";
   private static final int OBSTACLE_ID = 1;
   private static final int OBSTACLE_ROW = 3;
   private static final int OBSTACLE_COL = 2;
   private static final int OBSTACLE_NUM_PROPERTIES = 4;

   private static final String OCTO_KEY = "octo";
   private static final int OCTO_ID = 1;
   private static final int OCTO_ACTION_PERIOD = 5;
   private static final int OCTO_ANIMATION_PERIOD = 6;
   private static final int OCTO_ROW = 3;
   private static final int OCTO_COL = 2;
   private static final int OCTO_LIMIT = 4;
   private static final int OCTO_NUM_PROPERTIES = 7;

   public static Cat player;



   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;
   private Set<Entity> players;

   // Constructors:
   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.setNumRows(numRows);
      this.setNumCols(numCols);
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.setEntities(new HashSet<>());
      this.setPlayers(new HashSet<>());


      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   // Methods:
   private void setBackgroundCell(Point pos, Background background) {
      this.background[pos.y][pos.x] = background;
   }

   private Background getBackgroundCell(Point pos) {
      return this.background[pos.y][pos.x];
   }

   private void setOccupancyCell(Point pos, Entity entity) {
      this.occupancy[pos.y][pos.x] = entity;
   }

   public void setBackground(Point pos, Background background) {
      if (this.withinBounds(pos)) {
         this.setBackgroundCell(pos, background);
      }
   }

   public Optional<Entity> getOccupant(Point pos) {
      if (this.isOccupied(pos)) {
         return Optional.of(this.getOccupancyCell(pos));
      }
      else {
         return Optional.empty();
      }
   }

   private Entity getOccupancyCell(Point pos) {
      return this.occupancy[pos.y][pos.x];
   }

   public Optional<PImage> getBackgroundImage(Point pos) {
      if (this.withinBounds(pos)) {
         return Optional.of(Background.getCurrentImage(this.getBackgroundCell(pos)));
      }
      else {
         return Optional.empty();
      }
   }

   private void removeEntityAt(Point pos) {
      if (this.withinBounds(pos) && this.getOccupancyCell(pos) != null) {
         Entity entity = this.getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         this.getEntities().remove(entity);
         this.setOccupancyCell(pos, null);
      }
   }

   public void removeEntity(Entity entity) {
      this.removeEntityAt(entity.getPosition());
   }

   /*
         Assumes that there is no entity currently occupying the
         intended destination cell.
      */
   public void addEntity(Entity entity) {
      if (this.withinBounds(entity.getPosition())) {
         this.setOccupancyCell(entity.getPosition(), entity);
         this.getEntities().add(entity);
      }
   }
   public void addPlayer(Entity entity) {
      if (this.withinBounds(entity.getPosition())) {
         this.setOccupancyCell(entity.getPosition(), entity);
         this.getPlayers().add(entity);
      }
   }

   public void moveEntity(Entity entity, Point pos) {
      Point oldPos = entity.getPosition();
      if (this.withinBounds(pos) && !pos.equals(oldPos) && !this.isOccupied(pos)) {
         this.setOccupancyCell(oldPos, null);
         this.removeEntityAt(pos);
         this.setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }


   private void tryAddEntity(Entity entity) {
      if (this.isOccupied(entity.getPosition())) {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }
      this.addEntity(entity);
   }

   public boolean withinBounds(Point pos) {
      return pos.y >= 0 && pos.y < numRows && pos.x >= 0 && pos.x < numCols;
   }

   public boolean isOccupied(Point pos) {
      return this.withinBounds(pos) && this.getOccupancyCell(pos) != null;
   }

   private boolean parseBackground(String [] properties, ImageStore imageStore) {
      if (properties.length == BGND_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]), Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         // bgnd id = 1
         this.setBackground(pt, new Background(id, imageStore.getImageList(id)));
      }
      return properties.length == BGND_NUM_PROPERTIES;
   }





   //
//   private boolean parseOcto(String [] properties, ImageStore imageStore) {
//      if (properties.length == OCTO_NUM_PROPERTIES) {
//         Point pt = new Point(Integer.parseInt(properties[OCTO_COL]),
//            Integer.parseInt(properties[OCTO_ROW]));
//         OctoNotFull entity = new OctoNotFull(properties[OCTO_ID], Integer.parseInt(properties[OCTO_LIMIT]), pt, Integer.parseInt(properties[OCTO_ACTION_PERIOD]), Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]), imageStore.getImageList(OCTO_KEY));
//
//         this.tryAddEntity(entity);
//      }
//
//      return properties.length == OCTO_NUM_PROPERTIES;
//   }

//
//   private boolean parseFish(String [] properties, ImageStore imageStore) {
//      if (properties.length == FISH_NUM_PROPERTIES) {
//         Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
//            Integer.parseInt(properties[FISH_ROW]));
//         Fish entity = new Fish(properties[FISH_ID],
//            pt, Integer.parseInt(properties[FISH_ACTION_PERIOD]),
//            imageStore.getImageList(FISH_KEY));
//         this.tryAddEntity(entity);
//      }
//
//      return properties.length == FISH_NUM_PROPERTIES;
//   }
//
//   private boolean parseAtlantis(String [] properties, ImageStore imageStore) {
//      if (properties.length == ATLANTIS_NUM_PROPERTIES) {
//         Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]),
//            Integer.parseInt(properties[ATLANTIS_ROW]));
//         Atlantis entity = new Atlantis(properties[ATLANTIS_ID], pt, imageStore.getImageList(ATLANTIS_KEY));
//         this.tryAddEntity(entity);
//      }
//      return properties.length == ATLANTIS_NUM_PROPERTIES;
//   }
//
//   private boolean parseSgrass(String [] properties, ImageStore imageStore) {
//      if (properties.length == SGRASS_NUM_PROPERTIES) {
//         Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
//            Integer.parseInt(properties[SGRASS_ROW]));
//         SGrass entity = new SGrass(properties[SGRASS_ID], pt, Integer.parseInt(properties[SGRASS_ACTION_PERIOD]), imageStore.getImageList(SGRASS_KEY));
//         this.tryAddEntity(entity);
//      }
//
//      return properties.length == SGRASS_NUM_PROPERTIES;
//   }

   public boolean processLine(String line, ImageStore imageStore) {
      String[] properties = line.split("\\s");
      if (properties.length > 0) {
         switch (properties[PROPERTY_KEY]) {
            case BGND_KEY:
               return this.parseBackground(properties, imageStore);
            case OBSTACLE_KEY:
               return this.parseObstacle(properties, imageStore);
            case MOUSE_KEY:
               return this.parseMouse(properties, imageStore);
            case CHEESE_KEY:
               return this.parseCheese(properties, imageStore);
            case CHEESE_SPAWNER_KEY:
               return this.parseCheeseSpawner(properties, imageStore);
            case CAT_KEY:
                return this.parseCat(properties, imageStore);
            case DOG_KEY:
                return this.parseDog(properties, imageStore);
            //    case SGRASS_KEY:
            //   return this.parseSgrass(properties, imageStore);

         }
      }

      return false;
   }
   private boolean parseObstacle(String [] properties, ImageStore imageStore) {
      if (properties.length == OBSTACLE_NUM_PROPERTIES) {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Obstacle entity = ObstacleFactory.create(properties[OBSTACLE_ID], pt, imageStore.getImageList(OBSTACLE_KEY));
//                 new Obstacle(properties[OBSTACLE_ID],
//                 pt, imageStore.getImageList(OBSTACLE_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }
   // CatFactory.create(CAT_ID, new Point(2, 2), imageStore.getImageList(CAT_ID));   













      private boolean parseCat(String [] properties, ImageStore imageStore) {
      if (properties.length == CHEESE_SPAWNER_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[CHEESE_SPAWNER_COL]),
            Integer.parseInt(properties[CHEESE_SPAWNER_ROW]));
         Cat entity = CatFactory.create(CAT_ID, pt, imageStore.getImageList(CAT_ID));
         this.player = entity;
         this.tryAddEntity(entity);
      }

      return properties.length == CHEESE_SPAWNER_NUM_PROPERTIES;
   }

   private boolean parseDog(String [] properties, ImageStore imageStore){
      if (properties.length == DOG_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[DOG_COL]),
                 Integer.parseInt(properties[DOG_ROW]));
         Dog dog = dog_factory.createEntity(properties[DOG_ID], pt, this.difficulty, imageStore);
         this.tryAddEntity(dog);
      }
      return properties.length == DOG_NUM_PROPERTIES;
   }

   private boolean parseMouse(String [] properties, ImageStore imageStore){
      if (properties.length == MOUSE_NUM_PROPERTIES){
         Point pt = new Point(Integer.parseInt(properties[MOUSE_COL]),
                 Integer.parseInt(properties[MOUSE_ROW]));
         // REFERENCE: //         OctoNotFull entity = new OctoNotFull(properties[OCTO_ID], Integer.parseInt(properties[OCTO_LIMIT]), pt, Integer.parseInt(properties[OCTO_ACTION_PERIOD]), Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]), imageStore.getImageList(OCTO_KEY));
         // String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images, PathingStrategy pathingStrategy
         Mouse mouse = MouseFactory.create(properties[MOUSE_ID], pt,  MOUSE_ACTION_PERIOD, OCTO_ANIMATION_PERIOD, imageStore.getImageList(MOUSE_KEY), MOUSE_PATHING_STRATEGY);
         this.tryAddEntity(mouse);
      }
      return properties.length == MOUSE_NUM_PROPERTIES;
   }

      private boolean parseCheeseSpawner(String [] properties, ImageStore imageStore) {
      if (properties.length == CHEESE_SPAWNER_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[CHEESE_SPAWNER_COL]),
            Integer.parseInt(properties[CHEESE_SPAWNER_ROW]));
         CheeseSpawner entity = new CheeseSpawner(properties[CHEESE_SPAWNER_ID], pt, Integer.parseInt(properties[CHEESE_SPAWNER_ACTION_PERIOD]), imageStore.getImageList(CHEESE_SPAWNER_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == CHEESE_SPAWNER_NUM_PROPERTIES;
   }

      private boolean parseCheese(String [] properties, ImageStore imageStore) {
      if (properties.length == CHEESE_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[CHEESE_COL]),
            Integer.parseInt(properties[CHEESE_ROW]));
         Cheese entity = new Cheese(properties[CHEESE_ID],
            pt, Integer.parseInt(properties[CHEESE_ACTION_PERIOD]),
            imageStore.getImageList(CHEESE_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == CHEESE_NUM_PROPERTIES;
   }
//
//   public boolean transformNotFull(/*Entity*/Octo octo, EventScheduler scheduler, ImageStore imageStore) {
//      if (octo.getResourceCount() >= octo.getResourceLimit()) {
//         OctoFull octoFull = new OctoFull(octo.getId(), octo.getResourceLimit(), octo.getPosition(), octo.getActionPeriod(), octo.getAnimationPeriod(), octo.getImages());
//
//
//         this.removeEntity(octo);
//         scheduler.unscheduleAllEvents(octo);
//
//         this.addEntity(octoFull);
//         octoFull.scheduleActions(this, imageStore, scheduler);
//
//         return true;
//      }
//
//      return false;
//   }
//
//   public void transformFull(/*Entity*/Octo entity, EventScheduler scheduler, ImageStore imageStore) {
//      OctoNotFull octo = new OctoNotFull(entity.getId(), entity.getResourceLimit(), entity.getPosition(), entity.getActionPeriod(), entity.getAnimationPeriod(), entity.getImages());
//
//
//      this.removeEntity(entity);
//      scheduler.unscheduleAllEvents(entity);
//
//      this.addEntity(octo);
//      octo.scheduleActions(this, imageStore, scheduler);
//   }
//
//   public boolean moveToNotFull(Entity octo, Entity target, EventScheduler scheduler) {
//      if (octo.getPosition().adjacent(target.getPosition())) {
//         octo.setResourceCount(octo.getResourceCount() + 1);
//         this.removeEntity(target);
//         scheduler.unscheduleAllEvents(target);
//
//         return true;
//      }
//      else {
//         Point nextPos = Octo.nextPositionOcto(this, octo, target.getPosition());
//
//
//         if (!octo.getPosition().equals(nextPos)) {
//            Optional<Entity> occupant = this.getOccupant(nextPos);
//            if (occupant.isPresent()) {
//               scheduler.unscheduleAllEvents(occupant.get());
//            }
//
//            this.moveEntity(octo, nextPos);
//         }
//         return false;
//      }
//   }
//
//   public boolean moveToFull(Entity octo, Entity target, EventScheduler scheduler) {
//      if (octo.getPosition().adjacent(target.getPosition())) {
//         return true;
//      }
//      else {
//         Point nextPos = Octo.nextPositionOcto(this, octo, target.getPosition());
//
//
//         if (!octo.getPosition().equals(nextPos)) {
//            Optional<Entity> occupant = this.getOccupant(nextPos);
//            if (occupant.isPresent()) {
//               scheduler.unscheduleAllEvents(occupant.get());
//            }
//
//            this.moveEntity(octo, nextPos);
//         }
//         return false;
//      }
//   }

   public void load(Scanner in, ImageStore imageStore) {
      int lineNumber = 0;
      while (in.hasNextLine()) {
         try {
            if (!this.processLine(in.nextLine(), imageStore)) {
               System.err.println(String.format("invalid entry on line %d",
                  lineNumber));
            }
         }
         catch (NumberFormatException e) {
            System.err.println(String.format("invalid entry on line %d",
               lineNumber));
         }
         catch (IllegalArgumentException e) {
            System.err.println(String.format("issue on line %d: %s",
               lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   public Optional<Point> findOpenAround(Point pos) {
      for (int dy = -FISH_REACH; dy <= FISH_REACH; dy++) {
         for (int dx = -FISH_REACH; dx <= FISH_REACH; dx++) {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (this.withinBounds(newPt) && !this.isOccupied(newPt)) {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   // Student Accessors:
   public int getNumRows() {
      return this.numRows;
   }

   public int getNumCols() {
      return this.numCols;
   }

   public Set<Entity> getEntities() {
      return this.entities;
   }
   public Set<Entity> getPlayers(){return this.players;}

   public void setNumRows(int numRows) {
      this.numRows = numRows;
   }

   public void setNumCols(int numCols) {
      this.numCols = numCols;
   }

   public void setEntities(Set<Entity> entities) {
      this.entities = entities;
   }
   public void setPlayers(Set<Entity> players){this.players = players;}
}
