package arrowpro.arrow;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityTypes;
import org.bukkit.entity.EntityType;

/**
 * Spawns a Creeper where it hits.
 * 
 * @author Justin Stauch
 * @since August 31, 2012
 */
public class CreeperArrow extends ProArrowAction {
    private boolean hasSpawned = false;
    
    @Override
    public void inGround(ProArrow arrow) {
        if (!hasSpawned) {
            Entity creeper = EntityTypes.createEntityByName(EntityType.CREEPER.getName(), arrow.world);
            creeper.setPosition(arrow.locX, arrow.locY, arrow.locZ);
            arrow.world.addEntity(creeper);
            hasSpawned = true;
            arrow.die();
        }
    }
    
    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if (!hasSpawned) {
            Entity creeper = EntityTypes.createEntityByName(EntityType.CREEPER.getName(), arrow.world);
            creeper.setPosition(arrow.locX, arrow.locY, arrow.locZ);
            arrow.world.addEntity(creeper);
            hasSpawned = true;
            arrow.die();
        }
    }
}