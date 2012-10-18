package arrowpro.arrow;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityTypes;
import org.bukkit.entity.EntityType;

/**
 * Spawns an Iron Golem where it hits.
 * 
 * @author Justin Stauch
 * @since August 31, 2012
 */
public class IronGolemArrow extends ProArrowAction {
    private boolean hasSpawned = false;
    
    @Override
    public void inGround(ProArrow arrow) {
        if (!hasSpawned) {
            Entity ironGolem = EntityTypes.createEntityByName(EntityType.IRON_GOLEM.getName(), arrow.world);
            ironGolem.setPosition(arrow.locX, arrow.locY, arrow.locZ);
            arrow.world.addEntity(ironGolem);
            hasSpawned = true;
            arrow.die();
        }
    }
    
    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if (!hasSpawned) {
            Entity ironGolem = EntityTypes.createEntityByName(EntityType.IRON_GOLEM.getName(), arrow.world);
            ironGolem.setPosition(arrow.locX, arrow.locY, arrow.locZ);
            arrow.world.addEntity(ironGolem);
            hasSpawned = true;
            arrow.die();
        }
    }
}