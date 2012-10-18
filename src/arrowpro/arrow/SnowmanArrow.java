package arrowpro.arrow;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityTypes;
import org.bukkit.entity.EntityType;

/**
 * Spawns a Snowman where the arrow hits.
 * 
 * @author Justin Stauch
 * @since August 31, 2012
 */
public class SnowmanArrow extends ProArrowAction {
    private boolean hasSpawned = false;
    
    @Override
    public void inGround(ProArrow arrow) {
        if (!hasSpawned) {
            Entity snowman = EntityTypes.createEntityByName(EntityType.SNOWMAN.getName(), arrow.world);
            snowman.setPosition(arrow.locX, arrow.locY, arrow.locZ);
            arrow.world.addEntity(snowman);
            hasSpawned = true;
            arrow.die();
        }
    }
    
    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if (!hasSpawned) {
            Entity snowman = EntityTypes.createEntityByName(EntityType.SNOWMAN.getName(), arrow.world);
            snowman.setPosition(arrow.locX, arrow.locY, arrow.locZ);
            arrow.world.addEntity(snowman);
            hasSpawned = true;
            arrow.die();
        }
    }
}