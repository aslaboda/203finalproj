import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

/*
WorldView ideally mostly controls drawing the current part of the whole world
that we can see based on the viewport
*/

final class WorldView
{
   // Fields:
   private PApplet screen;
   private WorldModel world;
   private int tileWidth;
   private int tileHeight;
   private Viewport viewport;

   // Constructor:
   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world, int tileWidth, int tileHeight) {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }

//   // Methods:
//    public static void shiftView(WorldView view, int colDelta, int rowDelta) {
//       int newCol = Functions.clamp(view.viewport.col + colDelta, 0, view.world.getNumCols() - view.viewport.numCols);
//       int newRow = Functions.clamp(view.viewport.row + rowDelta, 0, view.world.getNumRows() - view.viewport.numRows);
//       view.viewport.shift(newCol, newRow);
//    }

    public void drawViewport() {
      this.drawBackground();
      this.drawEntities();
      this.drawPlayers();
   }

   public void drawEntities() {
      for (Entity entity : this.world.getEntities()) {
         Point pos = entity.getPosition();
         if (this.viewport.contains(pos)) {
            Point viewPoint = this.viewport.worldToViewport(pos.x, pos.y);
            this.screen.image(Background.getCurrentImage(entity),
               viewPoint.x * this.tileWidth, viewPoint.y * this.tileHeight);
         }
      }
   }

   public void drawBackground() {
      for (int row = 0; row < this.viewport.numRows; row++) {
         for (int col = 0; col < this.viewport.numCols; col++) {
            Point worldPoint = this.viewport.viewportToWorld(col, row);
            Optional<PImage> image = this.world.getBackgroundImage(worldPoint);
            if (image.isPresent()) {
               this.screen.image(image.get(), col * this.tileWidth,
                  row * this.tileHeight);
            }
         }
      }
   }

   public void drawPlayers(){
      for (Entity entity : this.world.getPlayers()) {
         Point pos = entity.getPosition();
         if (this.viewport.contains(pos)) {
            Point viewPoint = this.viewport.worldToViewport(pos.x, pos.y);
            this.screen.image(Background.getCurrentImage(entity),
                    viewPoint.x * this.tileWidth, viewPoint.y * this.tileHeight);
         }
      }

   }
}
