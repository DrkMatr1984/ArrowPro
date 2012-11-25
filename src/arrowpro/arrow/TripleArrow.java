package arrowpro.arrow;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.MathHelper;

/**
 * Shoots three arrows at one time.
 * 
 * @author Justin Stauch
 * @since August 31, 2012
 */
public class TripleArrow extends ProArrowAction {
    
    @Override
    public void shoot(ProArrow arrow) {
        ProArrow arrow1 = new ProArrow(arrow.world, new NormalArrow());
        ProArrow arrow2 = new ProArrow(arrow.world, new NormalArrow());
        arrow1.shooter = arrow.shooter;
        arrow2.shooter = arrow.shooter;
        if (arrow.shooter instanceof EntityHuman) {
            arrow1.fromPlayer = 1;
        }
        arrow1.eh(0.5F, 0.5F);
        float yaw1 = arrow.shooter.yaw + 16F;
        yaw1 = yaw1 > 180 ? yaw1 - 360 : yaw1;
        yaw1 = yaw1 < -180 ? yaw1 + 360 : yaw1;
        arrow1.setPositionRotation(arrow.locX, arrow.locY, arrow.locZ, yaw1, arrow.shooter.pitch);
        arrow1.locX -= (double) (MathHelper.cos(arrow1.yaw / 180.0F * 3.1415927F) * 0.16F);
        arrow1.locY -= 0.10000000149011612D;
        arrow1.locZ -= (double) (MathHelper.sin(arrow1.yaw / 180.0F * 3.1415927F) * 0.16F);
        arrow1.setPosition(arrow1.locX, arrow1.locY, arrow1.locZ);
        arrow1.height = 0.0F;
        arrow1.motX = (double) (-MathHelper.sin(arrow1.yaw / 180.0F * 3.1415927F) * MathHelper.cos(arrow1.pitch / 180.0F * 3.1415927F));
        arrow1.motZ = (double) (MathHelper.cos(arrow1.yaw / 180.0F * 3.1415927F) * MathHelper.cos(arrow1.pitch / 180.0F * 3.1415927F));
        arrow1.motY = (double) (-MathHelper.sin(arrow1.pitch / 180.0F * 3.1415927F));
        arrow2.arrow = new NormalArrow();
        arrow2.eh(0.5F, 0.5F);
        float yaw2 = arrow.shooter.yaw - 22.5F;
        yaw2 = yaw2 > 180 ? yaw2 - 360 : yaw2;
        yaw2 = yaw2 < -180 ? yaw2 + 360 : yaw2;
        arrow2.setPositionRotation(arrow.locX, arrow.locY, arrow.locZ, yaw2, arrow.shooter.pitch);
        arrow2.locX -= (double) (MathHelper.cos(arrow2.yaw / 180.0F * 3.1415927F) * 0.16F);
        arrow2.locY -= 0.10000000149011612D;
        arrow2.locZ -= (double) (MathHelper.sin(arrow2.yaw / 180.0F * 3.1415927F) * 0.16F);
        arrow2.setPosition(arrow2.locX, arrow2.locY, arrow2.locZ);
        arrow2.height = 0.0F;
        arrow2.motX = (double) (-MathHelper.sin(arrow2.yaw / 180.0F * 3.1415927F) * MathHelper.cos(arrow2.pitch / 180.0F * 3.1415927F));
        arrow2.motZ = (double) (MathHelper.cos(arrow2.yaw / 180.0F * 3.1415927F) * MathHelper.cos(arrow2.pitch / 180.0F * 3.1415927F));
        arrow2.motY = (double) (-MathHelper.sin(arrow2.pitch / 180.0F * 3.1415927F));
        arrow1.b(arrow.c());
        arrow2.b(arrow.c());
        arrow.world.addEntity(arrow1);
        arrow.world.addEntity(arrow2);
        arrow1.shoot(arrow1.motX, arrow1.motY, arrow1.motZ, arrow.speed * 1.5F, 1.0F);
        arrow2.shoot(arrow2.motX, arrow2.motY, arrow2.motZ, arrow.speed * 1.5F, 1.0F);
    }
}