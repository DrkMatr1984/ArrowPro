package arrowpro.arrow;

import net.minecraft.server.Entity;

/**
 * This arrow makes a fiery explosion when it hits something.
 * 
 * @author Justin Stauch
 * @since August 30, 2012
 */
public class FieryTNTArrow extends ProArrowAction {
    private float power;
    private boolean hasExploded = false;
    
    public FieryTNTArrow(float power) {
        this.power = power;
    }
    
    @Override
    public void shoot(ProArrow arrow) {
        arrow.setOnFire(300);
    }

    @Override
    public void inGround(ProArrow arrow) {
        if (!hasExploded) {
            arrow.world.createExplosion(arrow, arrow.locX, arrow.locY, arrow.locZ, power, true, arrow.world.getGameRules().getBoolean("mobGriefing"));
            hasExploded = true;
            arrow.die();
        }
    }

    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if (!hasExploded) {
            arrow.world.createExplosion(arrow, entity.locX, entity.locY, entity.locZ, power, true, arrow.world.getGameRules().getBoolean("mobGriefing"));
            hasExploded = true;
            arrow.die();
        }
    }
}