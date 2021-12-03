import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import processing.core.*;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
        extends PApplet
{
    // Timer Action Period:
    private static final int TIMER_ACTION_PERIOD = 150;
    private static final Random world_event_rand = new Random();

    public static int difficulty = 0;


    // View Window Size & World Size:
    private static final int VIEW_WIDTH =1300;
            //= 640;
    private static final int VIEW_HEIGHT =900;
                    //= 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int WORLD_WIDTH_SCALE = 1;
    private static final int WORLD_HEIGHT_SCALE = 1;

    // ???????????
    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    // Image Information:
    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    // Load File Name:
    private static final String LOAD_FILE_NAME = "world.sav";

    // Flag & Scale Speeds (Only used on cmd line?):
    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private static double timeScale = 1.0;
    private static boolean game_start = false;
    private static boolean game_over = false;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private long next_time;

    public Cat player;

    public void settings()
    {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS, createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
        //this.player = CatFactory.create(WorldModel.CAT_ID, new Point(2, 2), imageStore.getImageList(WorldModel.CAT_ID));
        //this.world.addEntity(this.player);
        //world.addPlayer(player);

    }

    public void draw() {
        if (!game_start){
            background(200);
            textSize(30);
            fill(0, 102, 153);
            text("Welcome to Cat and Mouse!", 300, 300);
            text("Choose difficulty to start playing", 300, 350);

            fill(0, 255, 0);
            square(400, 400, 75);
            text("Easy: ", 300, 450);

            fill(0, 0, 255);
            square(450, 500, 75);
            text("Medium: ", 300, 550);

            fill(255, 0, 0);
            square(400, 600, 75);
            text("Hard: ", 300, 650);

//            fill(0, 102, 153, 51);

        }
        else if (!game_over){
            long time = System.currentTimeMillis();
            if (time >= next_time ) {
                this.scheduler.updateOnTime(time);
                next_time = time + TIMER_ACTION_PERIOD;

            }
            view.drawViewport();

            if (world.isOver()){
                game_over = true;
            }

        }
        if (game_over){
            background(200);
            textSize(30);
            fill(0, 102, 153);
            text("Game over!", 300, 300);
        }

    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;
            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
//            WorldView.shiftView(view, dx, dy);
            // move cat
            world.moveEntity(WorldModel.player, new Point(WorldModel.player.getPosition().x + dx, WorldModel.player.getPosition().y + dy));
        }
    }


    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME, imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public void mousePressed()
    {
        if (!game_start) {
            if (mouseX >= 400 && mouseX <= 475 && mouseY >= 400 && mouseY <= 475) {
                world.setDifficulty(1);
                game_start = true;
            }
            if (mouseX >= 450 && mouseX <= 525 && mouseY >= 500 && mouseY <= 575) {
                world.setDifficulty(2);
                game_start = true;
            }
            if (mouseX >= 400 && mouseX <= 475 && mouseY >= 600 && mouseY <= 675) {
                world.setDifficulty(3);
                game_start = true;
            }
        }
        else{
            //world changing event
            worldEvent();
        }
    }

    public void worldEvent() {
        {
            Point pressed = convertMouseCursorToPoint(mouseX, mouseY);

            if (!world.isOccupied(pressed)) // If this spot is available, create cheese
            {
                Cheese cheese = new Cheese(WorldModel.CHEESE_ID_PREFIX + 100, pressed, WorldModel.CHEESE_CORRUPT_MIN + world_event_rand.nextInt(WorldModel.CHEESE_CORRUPT_MAX - WorldModel.CHEESE_CORRUPT_MIN), imageStore.getImageList(WorldModel.CHEESE_KEY));
                world.addEntity(cheese);
                cheese.scheduleActions(world, imageStore, this.scheduler); } // If the spot is not available, do not create cheese

            redraw();

        }
    }

    private Point convertMouseCursorToPoint(int x, int y)
    {
        return new Point(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }


    /* THIS IS THE WORLDCHANGING EVENT IN ASTAR (Creates & destroys the rocks) APPLY THIS METHOD!!!
       public void mousePressed()
   {
      Point pressed = mouseToPoint(mouseX, mouseY);

     if (grid[pressed.y][pressed.x] == GridValues.OBSTACLE)
        grid[pressed.y][pressed.x] = GridValues.BACKGROUND;
     else if (grid[pressed.y][pressed.x] == GridValues.BACKGROUND)
        grid[pressed.y][pressed.x] = GridValues.OBSTACLE;

     redraw();

   }
     */

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private static void loadImages(String filename, ImageStore imageStore, PApplet screen) {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(WorldModel world, String filename, ImageStore imageStore) {
        try {
            Scanner in = new Scanner(new File(filename));
            world.load(in, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        for (Entity entity : world.getEntities()) {
            //Only start actions for entities that include action (not those with just animations)
            if (entity instanceof MovingEntity)
                ((MovingEntity)entity).scheduleActions(world, imageStore, scheduler);
        }
    }

    public static void parseCommandLine(String [] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }


    // Main
    public static void main(String [] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
