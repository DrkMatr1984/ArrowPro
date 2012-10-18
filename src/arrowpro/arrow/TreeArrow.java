package arrowpro.arrow;

import java.util.Random;
import net.minecraft.server.Entity;
import org.bukkit.Location;
import org.bukkit.TreeType;

/**
 * Creates a tree where the arrow hits.
 * 
 * @author Justin Stauch
 * @since August 30, 2012
 */
public class TreeArrow extends ProArrowAction {
    boolean hasMadeTree = false;
    
    @Override
    public void inGround(ProArrow arrow) {
        if (!hasMadeTree) {
            TreeType type;
            Random random = new Random();
            switch (random.nextInt(11)) {
                case 0:
                    type = TreeType.BIG_TREE;
                    break;
                case 1:
                    type = TreeType.BIRCH;
                    break;
                case 2:
                    type = TreeType.BROWN_MUSHROOM;
                    break;
                case 3:
                    type = TreeType.JUNGLE;
                    break;
                case 4:
                    type = TreeType.JUNGLE_BUSH;
                    break;
                case 5:
                    type = TreeType.REDWOOD;
                    break;
                case 6:
                    type = TreeType.RED_MUSHROOM;
                    break;
                case 7:
                    type = TreeType.SMALL_JUNGLE;
                    break;
                case 8:
                    type = TreeType.SWAMP;
                    break;
                case 9:
                    type = TreeType.TALL_REDWOOD;
                    break;
                case 10:
                    type = TreeType.TREE;
                    break;
                default:
                    type = TreeType.TREE;
                    break;
            }
            arrow.world.getWorld().generateTree(new Location(arrow.world.getWorld(), arrow.locX, arrow.locY, arrow.locZ), type);
            hasMadeTree = true;
            arrow.die();
        }
    }

    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if (!hasMadeTree) {
            TreeType type;
            Random random = new Random();
            switch (random.nextInt(11)) {
                case 0:
                    type = TreeType.BIG_TREE;
                    break;
                case 1:
                    type = TreeType.BIRCH;
                    break;
                case 2:
                    type = TreeType.BROWN_MUSHROOM;
                    break;
                case 3:
                    type = TreeType.JUNGLE;
                    break;
                case 4:
                    type = TreeType.JUNGLE_BUSH;
                    break;
                case 5:
                    type = TreeType.REDWOOD;
                    break;
                case 6:
                    type = TreeType.RED_MUSHROOM;
                    break;
                case 7:
                    type = TreeType.SMALL_JUNGLE;
                    break;
                case 8:
                    type = TreeType.SWAMP;
                    break;
                case 9:
                    type = TreeType.TALL_REDWOOD;
                    break;
                case 10:
                    type = TreeType.TREE;
                    break;
                default:
                    type = TreeType.TREE;
                    break;
            }
            arrow.world.getWorld().generateTree(new Location(entity.world.getWorld(), entity.locX, entity.locY, entity.locZ), type);
            hasMadeTree = true;
            arrow.die();
        }
    }
    
}
