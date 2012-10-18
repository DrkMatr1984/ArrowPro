package arrowpro.arrow;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityLightning;

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
            EntityLightning light = new EntityLightning(arrow.world, arrow.locX, arrow.locY, arrow.locZ, true);
            arrow.world.strikeLightning(light);
            arrow.world.createExplosion(arrow, arrow.locX, arrow.locY, arrow.locZ, power, true);
            hasGone = true;
            arrow.die();
        }
    }

    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if (!hasGone) {
            EntityLightning light = new EntityLightning(entity.world, entity.locX, entity.locY, entity.locZ, true);
            arrow.world.strikeLightning(light);
            arrow.world.createExplosion(arrow, arrow.locX, arrow.locY, arrow.locZ, power, true);
            hasGone = true;
            arrow.die();
        }
    }
}