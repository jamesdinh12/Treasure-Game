import bagel.Image;
import bagel.util.Point;

public class Zombie extends Entity{

    private int energyChange = -3;
    public final int deadDist = 25;
    private final int shootingRange = 150;

    public Zombie(double x, double y, Image image) {
        super(x, y, image);
    }


    public int getShootingRange() {
        return shootingRange;
    }

    public int getDeadDist(){
        return deadDist;
    }

    @Override
    public void affectsEnergy(Player player) {
        player.setEnergy(player.getEnergy() + this.energyChange);
    }
}
