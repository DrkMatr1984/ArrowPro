package arrowpro.arrow;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityLightning;

/**
 * Lightning Arrows strike lightning where they hit.
 * 
 * @author Justin Stauch
 * @since August 30, 2012
 */
public class LightningArrow extends ProArrowAction {
    private boolean hasStruck = false;

    @Override
    public void inGround(ProArrow arrow) {
        if(!hasStruck) {
            EntityLightning light = new EntityLightning(arrow.world, arrow.locX, arrow.locY, arrow.locZ);
            arrow.world.strikeLightning(light);
            hasStruck = true;
            arrow.die();
        }
    }

    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if(!hasStruck) {
            EntityLightning light = new EntityLightning(entity.world, entity.locX, entity.locY, entity.locZ);
            arrow.world.strikeLightning(light);
            hasStruck = true;
            arrow.die();
        }
    }    
}