package arrowpro.arrow;

import net.minecraft.server.MobEffectList;

/**
 * Applies the confusion effect to the entity it hits.
 * 
 * @author Justin Stauch
 * @since September 1, 2012
 */
public class ConfusionArrow extends EffectArrow {
    
    @Override
    public MobEffectList getEffect() {
        return MobEffectList.CONFUSION;
    }
}