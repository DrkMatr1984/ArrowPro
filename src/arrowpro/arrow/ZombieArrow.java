package arrowpro.arrow;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityTypes;
import org.bukkit.entity.EntityType;

/**
 * Spawns a Zombie where the arrow hits.
 * 
 * @author Justin Stauch
 * @since August 31
 */
public class ZombieArrow extends ProArrowAction {
    private boolean hasSpawned = false;
    
    @Override
    public void inGround(ProArrow arrow) {
        if (!hasSpawned) {
            Entity zombie = EntityTypes.createEntityByName(EntityType.ZOMBIE.getName(), arrow.world);
            zombie.setPosition(arrow.locX, arrow.locY, arrow.locZ);
            arrow.world.addEntity(zombie);
            hasSpawned = true;
            arrow.die();
        }
    }
    
    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if (!hasSpawned) {
            Entity zombie = EntityTypes.createEntityByName(EntityType.ZOMBIE.getName(), arrow.world);
            zombie.setPosition(arrow.locX, arrow.locY, arrow.locZ);
            arrow.world.addEntity(zombie);
            hasSpawned = true;
            arrow.die();
        }
    }
}