package arrowpro.arrow;

import arrowpro.lightning.ProLightning;

import net.minecraft.server.Entity;

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
            ProLightning light = new ProLightning(arrow.world, arrow, arrow.locX, arrow.locY, arrow.locZ);
            arrow.world.strikeLightning(light);
            hasStruck = true;
            arrow.die();
        }
    }

    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if(!hasStruck) {
            ProLightning light = new ProLightning(entity.world, arrow, entity.locX, entity.locY, entity.locZ);
            arrow.world.strikeLightning(light);
            hasStruck = true;
            arrow.die();
        }
    }    
}