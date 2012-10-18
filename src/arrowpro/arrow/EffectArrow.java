package arrowpro.arrow;

import arrowpro.ArrowPro;
import arrowpro.equations.StatSolver;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import org.bukkit.entity.Player;

/**
 * Extended by all arrows that plays an effect and just give it their effect.
 * 
 * @author Justin Stauch
 * @since September 1, 2012
 */
public abstract class EffectArrow extends ProArrowAction {
    private static StatSolver duration, amplification;
    
    public abstract MobEffectList getEffect();
    
    @Override
    public void entityHit(ProArrow arrow, Entity entity) {
        if (entity instanceof EntityLiving) {
            int level = 1;
            if (arrow.shooter instanceof Player) {
                level = ArrowPro.getPlugin().getLevel(arrow.shooter);
            }
            ((EntityLiving) entity).addEffect(new MobEffect(getEffect().getId(), getDuration(level), getAmplification(level)));
            arrow.die();
        }
    }
    
    public static void setDurationEquation(StatSolver equation) {
        duration = equation;
    }
    
    public static void setAmplificationEquation(StatSolver equation) {
        amplification = equation;
    }
    
    public int getDuration(int level) {
        return (int) duration.solve(level, 0);
    }
    
    public int getAmplification(int level) {
        return (int) amplification.solve(level, 0);
    }
}