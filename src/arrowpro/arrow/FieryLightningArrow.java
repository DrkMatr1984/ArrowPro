package arrowpro.arrow;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityLightning;

/**
 * Strikes lightning and sets fire where it hits.
 * 
 * @author Justin Stauch
 * @since August 30, 2012
 */
public class FieryLightningArrow extends ProArrowAction {
    private boolean hasStruck = false;
    
    @Override
    public void shoot(ProArrow arrow) {
        arrow.setOnFire(300);
    }

    @Override
    public void inGround(ProArrow arrow) {
        if(!hasStruck) {
            EntityLightning light = new EntityLightning(arrow.world, arrow.locX, arrow.locY, arrow.locZ, true);
            arrow.world.strikeLightning(light);
            hasStruck = true;
            arrow.die();
        }
    }

    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if(!hasStruck) {
            EntityLightning light = new EntityLightning(entity.world, entity.locX, entity.locY, entity.locZ, true);
            arrow.world.strikeLightning(light);
            hasStruck = true;
            arrow.die();
        }
    }
}