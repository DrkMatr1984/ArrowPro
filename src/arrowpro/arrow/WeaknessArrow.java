package arrowpro.arrow;

import net.minecraft.server.MobEffectList;

/**
 * Applies the weakness effect to the entity it hits.
 * 
 * @author Justin Stauch
 * @since September 1, 2012
 */
public class WeaknessArrow extends EffectArrow {

    @Override
    public MobEffectList getEffect() {
        return MobEffectList.WEAKNESS;
    }
}