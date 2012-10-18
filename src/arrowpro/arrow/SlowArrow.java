package arrowpro.arrow;

import net.minecraft.server.MobEffectList;

/**
 * Applies the slower movement effect to the entity it hits.
 * 
 * @author Justin Stauch
 * @since September 1, 2012
 */
public class SlowArrow extends EffectArrow{

    @Override
    public MobEffectList getEffect() {
        return MobEffectList.SLOWER_MOVEMENT;
    }
}