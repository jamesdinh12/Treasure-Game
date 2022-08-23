import bagel.Image;
import bagel.util.Point;

public class Bullet extends Entity{

    private final int energyChange = -3;
    private final int stepSize = 25;

    public Bullet(double x, double y, Image image) {
        super(x, y, image);
    }

    public int getStepSize(){
        return stepSize;
    }

    @Override
    public void affectsEnergy(Player player) {
        player.setEnergy(player.getEnergy() + this.energyChange);
    }
}