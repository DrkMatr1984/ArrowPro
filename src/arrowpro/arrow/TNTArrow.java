package arrowpro.arrow;

import net.minecraft.server.Entity;
import org.bukkit.craftbukkit.event.CraftEventFactory;

/**
 * An arrow that blows up as soon as it hits something.
 * 
 * @author Justin Stauch
 * @since August 30, 2012
 */
public class TNTArrow extends ProArrowAction {
    private float power;
    private boolean hasExploded = false;
    
    public TNTArrow(float power) {
        this.power = power;
    }

    @Override
    public void inGround(ProArrow arrow) {
        if (!hasExploded) {
            arrow.world.createExplosion(arrow, arrow.locX, arrow.locY, arrow.locZ, power, false);
            hasExploded = true;
            arrow.die();
        }
    }

    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if (!hasExploded) {
            arrow.world.createExplosion(arrow, entity.locX, entity.locY, entity.locZ, power, false);
            hasExploded = true;
            arrow.die();
        }
    }
}