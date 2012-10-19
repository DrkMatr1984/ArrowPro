package arrowpro.arrow;

import arrowpro.lightning.ProLightning;

import net.minecraft.server.Entity;

/**
 * These strike lightning and make a fiery explosion where they hit.
 * 
 * @author Justin Stauch
 * @since August 30, 2012
 */
public class FieryExplosiveLightningArrow extends ProArrowAction {
    private boolean hasGone = false;
    private float power;
    
    public FieryExplosiveLightningArrow(float power) {
        this.power = power;
    }
    
    @Override
    public void inGround(ProArrow arrow) {
        if (!hasGone) {
            ProLightning light = new ProLightning(arrow.world, arrow, arrow.locX, arrow.locY, arrow.locZ);
            arrow.world.strikeLightning(light);
            arrow.world.createExplosion(arrow, arrow.locX, arrow.locY, arrow.locZ, power, true);
            hasGone = true;
            arrow.die();
        }
    }

    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if (!hasGone) {
            ProLightning light = new ProLightning(entity.world, arrow, entity.locX, entity.locY, entity.locZ);
            arrow.world.strikeLightning(light);
            arrow.world.createExplosion(arrow, arrow.locX, arrow.locY, arrow.locZ, power, true);
            hasGone = true;
            arrow.die();
        }
    }
}