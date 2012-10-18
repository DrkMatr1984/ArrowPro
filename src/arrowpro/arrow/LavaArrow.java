package arrowpro.arrow;

import net.minecraft.server.Entity;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Places lava where it hits.
 * 
 * @author Justin Stauch
 * @since August 30, 2012
 */
public class LavaArrow extends ProArrowAction {
private boolean hasPlaced = false;
    
    @Override
    public void inGround(ProArrow arrow) {
        if (!hasPlaced) {
            Location loco = new Location(arrow.world.getWorld(), arrow.locX, arrow.locY, arrow.locZ);
            loco.getBlock().setType(Material.LAVA);
            hasPlaced = true;
            arrow.die();
        }
    }

    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if (!hasPlaced) {
            Location loco = new Location(entity.world.getWorld(), entity.locX, entity.locY, entity.locZ);
            loco.getBlock().setType(Material.LAVA);
            hasPlaced = true;
            arrow.die();
        }
    }
}
