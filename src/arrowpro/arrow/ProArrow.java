package arrowpro.arrow;

import arrowpro.ArrowPro;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.Vec3D;

import net.minecraft.server.World;

import org.bukkit.entity.Player;

/**
 * Represents an Arrow that can be moded.
 * 
 * The code is a copy of EntityArrow but with some modifications in it that allow me to use it to my needs
 * This class holds an implementation of ProArrowAction that is called upon to run all of the methods when needed. This class is used only to get the game to recognize the arrows.
 * 
 * @author Justin Stauch
 * @since August 30, 2012
 */
public class ProArrow extends EntityArrow {
    protected ProArrowAction arrow;
    protected float speed;
    private boolean inGround;
    private int as = 0;
    
    public ProArrow(World world) {
        super(world);
        arrow = new NormalArrow();
    }
    
    public ProArrow(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
        arrow = new NormalArrow();
    }
    
    public ProArrow(World world, EntityLiving entityliving, EntityLiving entityliving1, float f0, float f1) {
        super (world, entityliving, entityliving1, f0, f1);
        arrow = new NormalArrow();
    }
    
    public ProArrow(World world, EntityLiving entityliving, float f) {
        super(world, entityliving, f);
        arrow = new NormalArrow();
    }
    
    public ProArrow(World world, ProArrowAction arrow) {
        super(world);
        this.arrow = arrow;
    }
    
    public ProArrow(World world, ProArrowAction arrow, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
        this.arrow = arrow;
    }
    
    public ProArrow(World world, ProArrowAction arrow, EntityLiving entityliving, EntityLiving entityliving1, float f, float f1) {
        super(world);
        this.shooter = entityliving;
        if (entityliving instanceof EntityHuman) {
            this.fromPlayer = 1;
        }
        speed = f;
        this.arrow = arrow;
        this.locY = entityliving.locY + (double) entityliving.getHeadHeight() - 0.10000000149011612D;
        double d0 = entityliving1.locX - entityliving.locX;
        double d1 = entityliving1.locY + (double) entityliving1.getHeadHeight() - 0.699999988079071D - this.locY;
        double d2 = entityliving1.locZ - entityliving.locZ;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);

        if (d3 >= 1.0E-7D) {
            float f2 = (float) (Math.atan2(d2, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
            float f3 = (float) (-(Math.atan2(d1, d3) * 180.0D / 3.1415927410125732D));
            double d4 = d0 / d3;
            double d5 = d2 / d3;

            this.setPositionRotation(entityliving.locX + d4, this.locY, entityliving.locZ + d5, f2, f3);
            this.height = 0.0F;
            float f4 = (float) d3 * 0.2F;

            if (shooter instanceof Player) {
                int level = ArrowPro.getPlugin().getLevel(shooter);
                this.b(arrow.getDamage(level, this.c()));
                speed = arrow.getSpeed(level, speed);
            }
            
            this.shoot(d0, d1 + (double) f4, d2, speed, f1);
        }
    }
    
    public ProArrow(World world, ProArrowAction arrow, EntityLiving entityliving, float f) {
        super(world);
        this.shooter = entityliving;
        if (entityliving instanceof EntityHuman) {
            this.fromPlayer = 1;
        }
        speed = f;
        this.arrow = arrow;
        this.a(0.5F, 0.5F);
        this.setPositionRotation(entityliving.locX, entityliving.locY + (double) entityliving.getHeadHeight(), entityliving.locZ, entityliving.yaw, entityliving.pitch);
        this.locX -= (double) (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.locY -= 0.10000000149011612D;
        this.locZ -= (double) (MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.height = 0.0F;
        this.motX = (double) (-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F));
        this.motZ = (double) (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F));
        this.motY = (double) (-MathHelper.sin(this.pitch / 180.0F * 3.1415927F));
        if (shooter instanceof Player) {
            int level = ArrowPro.getPlugin().getLevel(shooter);
            this.b(arrow.getDamage(level, this.c()));
            speed = arrow.getSpeed(level, speed);
        }
        this.shoot(this.motX, this.motY, this.motZ, speed * 1.5F, 1.0F);
    }
    
    @Override
    public void shoot(double d0, double d1, double d2, float f, float f1) {
        super.shoot(d0, d1, d2, f, f1);
        arrow.shoot(this);
    }
    
    @Override
    public void j_() {
        updateFields();
        super.j_();
        if (inGround) {
            arrow.inGround(this);
        }
        else {
            this.as += 1;
            Vec3D vec3d = this.world.getVec3DPool().create(this.locX, this.locY, this.locZ);
            Vec3D vec3d1 = this.world.getVec3DPool().create(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
            MovingObjectPosition movingobjectposition = this.world.rayTrace(vec3d, vec3d1, false, true);

            vec3d = this.world.getVec3DPool().create(this.locX, this.locY, this.locZ);
            vec3d1 = this.world.getVec3DPool().create(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
            if (movingobjectposition != null) {
                vec3d1 = this.world.getVec3DPool().create(movingobjectposition.pos.c, movingobjectposition.pos.d, movingobjectposition.pos.e);
            }

            Entity entity = null;
            List list = this.world.getEntities(this, this.boundingBox.a(this.motX, this.motY, this.motZ).grow(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Entity entity1 = (Entity)iterator.next();

                if ((entity1.L()) && ((entity1 != this.shooter) || (this.as >= 5))) {
                    float f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.boundingBox.grow(f1, f1, f1);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.a(vec3d, vec3d1);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3d.distanceSquared(movingobjectposition1.pos);

                        if ((d1 < d0) || (d0 == 0.0D)) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
            
            if (movingobjectposition != null) {
                if (movingobjectposition.entity != null) {
                    arrow.entityHit(this, movingobjectposition.entity);
                    if (shooter.getBukkitEntity() instanceof Player) {
                        if (movingobjectposition.entity.getBukkitEntity() instanceof Player) {
                            if (movingobjectposition.entity.dead) {
                                ArrowPro.getPlugin().killedPlayer((Player) shooter.getBukkitEntity());
                            }
                            else {
                                ArrowPro.getPlugin().hitPlayer((Player) shooter.getBukkitEntity());
                            }
                        }
                        else {
                            if (movingobjectposition.entity.dead) {
                                ArrowPro.getPlugin().killedEntity((Player) shooter.getBukkitEntity(), movingobjectposition.entity);
                            }
                            else {
                                ArrowPro.getPlugin().hitEntity((Player) shooter.getBukkitEntity());
                            }
                        }
                    }
                }
            }
        }
        updateFields();
    }
    
    /**
     * Gets the Entity that shot this arrow.
     * 
     * @return The entity that shot this arrow.
     */
    public Entity getShooter() {
        return shooter;
    }
    
    /**
     * Calls the method a (float, float) from the super type.
     * 
     * @param f
     * @param f1 
     */
    protected void eh(float f, float f1) {
        this.a(f, f1);
    }
    
    /**
     * Reads the private fields from the superclass that are needed in this class.
     */
    private void updateFields() {
        try {
            EntityArrow arr = this;
            Field ground = EntityArrow.class.getDeclaredField("inGround");
            Field and = EntityArrow.class.getDeclaredField("as");
            Field dama = EntityArrow.class.getDeclaredField("damage");
            ground.setAccessible(true);
            and.setAccessible(true);
            dama.setAccessible(true);
            inGround = ground.getBoolean(arr);
            as = and.getInt(arr);
            double defDam = dama.getDouble(arr);
            dama.setDouble(arr, arrow.getDamage(ArrowPro.getPlugin().getLevel(shooter), defDam));
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace(System.out);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace(System.out);
        }
    }
}