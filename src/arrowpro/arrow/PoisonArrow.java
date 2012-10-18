package arrowpro.arrow;

import net.minecraft.server.MobEffectList;

/**
 * Poisons entities upon hitting them.
 * 
 * @author Justin Stauch
 * @since September 1, 2012
 */
public class PoisonArrow extends EffectArrow {
    
    @Override
    public MobEffectList getEffect() {
        return MobEffectList.POISON;
    }
}