package arrowpro.arrow;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityLightning;

/**
 * Explodes and strikes lightning where it hits.
 * 
 * @author Justin Stauch
 * @since August 30, 2012
 */
public class ExplosiveLightningArrow extends ProArrowAction {
    private boolean hasGone = false;
    private float power;
    
    public ExplosiveLightningArrow(float power) {
        this.power = power;
    }
    
    @Override
    public void inGround(ProArrow arrow) {
        if (!hasGone) {
            EntityLightning light = new EntityLightning(arrow.world, arrow.locX, arrow.locY, arrow.locZ);
            arrow.world.strikeLightning(light);
            arrow.world.createExplosion(arrow, arrow.locX, arrow.locY, arrow.locZ, power, false);
            hasGone = true;
            arrow.die();
        }
    }

    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if (!hasGone) {
            EntityLightning light = new EntityLightning(entity.world, entity.locX, entity.locY, entity.locZ);
            arrow.world.strikeLightning(light);
            arrow.world.createExplosion(arrow, arrow.locX, arrow.locY, arrow.locZ, power, false);
            hasGone = true;
            arrow.die();
        }
    }
}