package arrowpro.arrow;

import net.minecraft.server.Entity;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * A Fire Arrow is set on fire upon being shot.
 *
 * @author Justin Stauch
 * @since August 30, 2012
 */
public class FireArrow extends ProArrowAction {
    
    
    @Override
    public void shoot(ProArrow arrow) {
        arrow.setOnFire(300);
    }
    
    @Override
    public void inGround(ProArrow arrow) {
        Location loco = new Location(arrow.world.getWorld(), arrow.locX, arrow.locY, arrow.locZ);
        loco.getBlock().setType(Material.FIRE);
    }
}