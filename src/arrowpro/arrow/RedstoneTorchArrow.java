package arrowpro.arrow;

import net.minecraft.server.Entity;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Places a RedstoneTorch where it hits.
 * 
 * @author Justin Stauch
 * @since August 30, 2012
 */
public class RedstoneTorchArrow extends ProArrowAction {
private boolean placedTorch = false;
    
    @Override
    public void inGround(ProArrow arrow) {
        if (!placedTorch) {
            Location loco = new Location(arrow.world.getWorld(), arrow.locX, arrow.locY, arrow.locZ);
            loco.getBlock().setType(Material.REDSTONE_TORCH_ON);
            placedTorch = true;
            arrow.die();
        }
    }
}