package arrowpro.arrow;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Places a torch where it hits.
 * 
 * @author Justin Stauch
 * @since August 30, 2012
 */
public class TorchArrow extends ProArrowAction {
    private boolean placedTorch = false;

    @Override
    public void inGround(ProArrow arrow) {
        if (!placedTorch) {
            Location loco = new Location(arrow.world.getWorld(), arrow.locX, arrow.locY, arrow.locZ);
            loco.getBlock().setType(Material.TORCH);
            placedTorch = true;
            arrow.die();
        }
    }
}