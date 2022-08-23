import bagel.Image;
import bagel.util.Point;


public class Player extends Entity{
    private int stepSize = 10;
    private int energy;

    public Player(double x, double y, Image image, int energy){
        super(x,y,image);
        this.energy = energy;
    }

    public int getEnergy(){
        return energy;
    }

    public void setEnergy(int newEnergy){
        this.energy = newEnergy;
    }

    public int getStepSize(){
        return stepSize;
    }

}

