/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arrowpro.arrow;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityTypes;
import org.bukkit.entity.EntityType;

/**
 * Spawns a Skeleton where the arrow hits.
 * 
 * @author Justin Stauch
 * @since August 31, 2012
 */
public class SkeletonArrow extends ProArrowAction {
    private boolean hasSpawned = false;
    
    @Override
    public void inGround(ProArrow arrow) {
        if (!hasSpawned) {
            Entity skeleton = EntityTypes.createEntityByName(EntityType.SKELETON.getName(), arrow.world);
            skeleton.setPosition(arrow.locX, arrow.locY, arrow.locZ);
            arrow.world.addEntity(skeleton);
            hasSpawned = true;
            arrow.die();
        }
    }
    
    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if (!hasSpawned) {
            Entity skeleton = EntityTypes.createEntityByName(EntityType.SKELETON.getName(), arrow.world);
            skeleton.setPosition(arrow.locX, arrow.locY, arrow.locZ);
            arrow.world.addEntity(skeleton);
            hasSpawned = true;
            arrow.die();
        }
    }
}