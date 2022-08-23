import bagel.*;
import bagel.util.Point;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Entity implements affectsEnergy{

    private Point entityPos;
    private Image image;
    private boolean exists;

    public Entity(double xPos, double yPos, Image image) {
        this.entityPos = new Point(xPos, yPos);
        this.image = image;
        this.exists = true;
    }

    public Point getEntityPos() {
        return entityPos;
    }

    public void setEntityPos(Point point) {
        this.entityPos = point;
    }

    public Image getImage() {
        return image;
    }

    public void drawImage() {
        this.getImage().draw(getEntityPos().x, getEntityPos().y);
    }

    public boolean doesExist() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public double closeness(Entity entity) {
        return this.getEntityPos().distanceTo(entity.getEntityPos());
    }

    public Entity closestEntity(ArrayList<? extends Entity> entities) {
        Entity closestEntity = entities.get(0);
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).doesExist()) {
                if (entities.get(i).getEntityPos().distanceTo(this.getEntityPos())
                        < this.getEntityPos().distanceTo(closestEntity.getEntityPos())) {
                    closestEntity = entities.get(i);
                }
            }
        }
        return closestEntity;
    }

    public void changeDirection(Entity entity, double stepSize) {
        double distance = this.getEntityPos().distanceTo(entity.getEntityPos());
        double xDirection = (this.getEntityPos().x - entity.getEntityPos().x) / distance;
        double yDirection = (this.getEntityPos().y - entity.getEntityPos().y) / distance;
        this.setEntityPos(new Point(this.getEntityPos().x - stepSize * xDirection,
                this.getEntityPos().y - stepSize * yDirection));
    }


    @Override
    public void affectsEnergy(Player player) {
    }
}
