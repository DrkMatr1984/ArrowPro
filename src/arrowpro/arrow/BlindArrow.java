package arrowpro.arrow;

import net.minecraft.server.MobEffectList;

/**
 * Applies the blindness effect to the entity it hits.
 * 
 * @author Justin Stauch
 * @since September 1, 2012
 */
public class BlindArrow extends EffectArrow {
    
    @Override
    public MobEffectList getEffect() {
        return MobEffectList.BLINDNESS;
    }
}