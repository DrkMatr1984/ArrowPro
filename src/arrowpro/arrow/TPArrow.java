package arrowpro.arrow;

import net.minecraft.server.Entity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Teleports the shooter to the place where the arrow hits.
 * 
 * @author Justin Stauch
 * @since August 30, 2012
 */
public class TPArrow extends ProArrowAction {
    private boolean hasTeleported = false;

    @Override
    public void inGround(ProArrow arrow) {
        if (!hasTeleported) {
            Location loco = new Location(arrow.world.getWorld(), arrow.locX, arrow.locY, arrow.locZ);
            arrow.shooter.getBukkitEntity().teleport(loco);
            hasTeleported = true;
            arrow.die();
        }
    }

    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if (!hasTeleported) {
            Location loco = new Location(arrow.world.getWorld(), arrow.locX, arrow.locY, arrow.locZ);
            arrow.shooter.getBukkitEntity().teleport(loco);
            hasTeleported = true;
            arrow.die();
        }
    }
}
