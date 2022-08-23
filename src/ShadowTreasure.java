import bagel.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShadowTreasure extends AbstractGame implements FileWriteable{

    private int counter;

    private final Font font = new Font("res/font/DejaVuSans-Bold.ttf", 20);

    private final Image background = new Image("res/images/background.png");

    ArrayList<Zombie> zombies = new ArrayList<>();
    ArrayList<Sandwich> sandwiches = new ArrayList<>();
    Player player;
    Treasure treasure;
    Bullet bullet = null;

    private final int interactionDist = 50;

    private final int energyMinimum = 3;

    // for rounding double number; use this to print the location of the player
    private static DecimalFormat df = new DecimalFormat("0.00");

    public static void printInfo(double x, double y, int e) {
        System.out.println(df.format(x) + "," + df.format(y) + "," + e);
    }

    public ShadowTreasure() throws IOException {
        this.loadEnvironment("res/IO/environment.csv");
        // Add code to initialize other attributes as needed
        //Initialised entities
        for (int row = 1; row <= 3; row++) {
            zombies.add(new Zombie(Double.parseDouble(cellHolder.get(row)[1]),
                    Double.parseDouble(cellHolder.get(row)[2]), new Image("res/images/zombie.png")));
        }

        for (int row = 5; row <= 7; row++) {
            sandwiches.add(new Sandwich(Double.parseDouble(cellHolder.get(row)[1]),
                    Double.parseDouble(cellHolder.get(row)[2]), new Image("res/images/sandwich.png")));
        }
        player = new Player(Double.parseDouble(cellHolder.get(0)[1]),
                Double.parseDouble(cellHolder.get(0)[2]), new Image("res/images/player.png"),
                Integer.parseInt(cellHolder.get(0)[3]));

        treasure = new Treasure(Double.parseDouble(cellHolder.get(4)[1]),
                Double.parseDouble(cellHolder.get(4)[2]), new Image("res/images/treasure.png"));

    }

    private List<String[]> cellHolder;

    private void loadEnvironment(String filename) throws IOException {
        // Code here to read from the file and set up the environment
        cellHolder = this.readCSV(filename);
    }

    private List<String[]> readCSV(String filename) throws IOException {
        List<String[]> cells = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("[^a-zA-Z0-9,]", "");
                String[] values = line.split(",");
                cells.add(values);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return cells;
    }
    //writer
    @Override
    public void writeToFile(BufferedWriter writer) throws IOException{
        String result = String.format("%d,success!",player.getEnergy());
        writer.write(result);
        writer.newLine();
        writer.flush();
    }

    public void writeToFile(BufferedWriter writer,Entity entity) throws IOException {
        String result = String.format("%f,%f",entity.getEntityPos().x,entity.getEntityPos().y);
        writer.write(result);
        writer.newLine();
        writer.flush();
    }

    private BufferedWriter outputWriter = new BufferedWriter(new FileWriter("res/IO/output.csv"));
    private BufferedWriter stdoutWriter = new BufferedWriter(new FileWriter("res/IO/stdout.csv"));

    public boolean anyExist(ArrayList<? extends Entity> entities) {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).doesExist()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Performs a state update.
     */
    @Override
    public void update(Input input) {
        // Logic to update the game, as per specification must go here

        //set frame rate
        if (counter < 10) {
            counter++;
        } else {
            counter = 0;

            printInfo(player.getEntityPos().x,player.getEntityPos().y, player.getEnergy());


            if (bullet != null) {
                try {
                    writeToFile(outputWriter,bullet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Situations to end the game
            if ((player.closeness(treasure) < interactionDist)
                    || ((player.getEnergy() < energyMinimum) && anyExist(zombies)
                    && !anyExist(sandwiches))
            ) {
                //Victory!
                if (player.closeness(treasure) < interactionDist) {
                    try {
                        writeToFile(stdoutWriter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(player.getEnergy() + ",success!");
                }

                Window.close();

            } else {
                //player interacts with sandwiches
                for (int i = 0; i < sandwiches.size(); i++) {
                    if (player.closeness(sandwiches.get(i)) < interactionDist && sandwiches.get(i).doesExist()) {
                        sandwiches.get(i).affectsEnergy(player);
                        sandwiches.get(i).setExists(false);
                    }
                }
            }

            // Spawn bullet
            for (int i = 0; i < zombies.size(); i++) {
                if (player.closeness(zombies.get(i)) < zombies.get(i).getShootingRange()
                        && bullet == null && zombies.get(i).doesExist()) {
                    bullet = new Bullet(player.getEntityPos().x, player.getEntityPos().y,
                            new Image("res/images/shot.png"));
                    bullet.setExists(true);
                    bullet.affectsEnergy(player);
                    break;
                }
            }
            //update bullet position
            if (bullet != null) {
                bullet.changeDirection(bullet.closestEntity(zombies), bullet.getStepSize());
                if (bullet.closeness(bullet.closestEntity(zombies)) < zombies.get(0).getDeadDist()) {
                    bullet.closestEntity(zombies).setExists(false);
                    bullet.setExists(false);
                    bullet = null;
                }
            }

            //setting playing movement
            if (!anyExist(zombies)) {
                player.changeDirection(treasure, player.getStepSize());

            } else if (player.getEnergy() >= 3) {
                player.changeDirection(player.closestEntity(zombies), player.getStepSize());

            } else {
                player.changeDirection(player.closestEntity(sandwiches), player.getStepSize());
            }

        }


            //draw entities as needed

            background.drawFromTopLeft(0, 0);

            font.drawString("energy: " + "[" + player.getEnergy() + "]", 20, 760);

            player.drawImage();

            treasure.drawImage();

            for (
                    int i = 0; i < sandwiches.size(); i++) {
                if (sandwiches.get(i).doesExist()) {
                    sandwiches.get(i).drawImage();
                }
            }
            for (
                    int i = 0; i < zombies.size(); i++) {
                if (zombies.get(i).doesExist()) {
                    zombies.get(i).drawImage();
                }
            }

            if (bullet != null) {
                bullet.drawImage();
            }

    }

        /**
         * The entry point for the program.
         */

    public static void main(String[] args) throws IOException {
        ShadowTreasure game = new ShadowTreasure();
        game.run();
    }
}

