package arrowpro.lightning;

import arrowpro.arrow.ProArrow;
import net.minecraft.server.EntityLightning;
import net.minecraft.server.World;

/**
 * Same as the EntityLightning but it holds an arrow as the one that called it so that the arrow can be tracked and made responsible for the death.
 * 
 * @author Justin Stauch
 * @since October 18, 2012
 * @see EntityLightning
 */
public class ProLightning extends EntityLightning {
    private ProArrow arrow;
    
    public ProLightning(World world, ProArrow arrow, double d0, double d1, double d2) {
        this(world, arrow, d0, d1, d2, false);
    }
    
    public ProLightning(World world, ProArrow arrow, double d0, double d1, double d2, boolean isEffect) {
        super(world, d0, d1, d2, isEffect);
        this.arrow = arrow;
    }
    
    /**
     * Gets the arrow that caused this lightning.
     * 
     * @return The arrow that caused this lightning.
     */
    public ProArrow getArrow() {
        return arrow;
    }
}