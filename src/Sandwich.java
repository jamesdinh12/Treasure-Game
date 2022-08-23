import bagel.Image;

public class Sandwich extends Entity{

    private final int interactionDist = 50;
    private final int energyChange = 5;

    public Sandwich(double x, double y, Image image) {
        super(x, y, image);
    }

    @Override
    public void affectsEnergy(Player player) {
        player.setEnergy(player.getEnergy() + this.energyChange);
    }
}