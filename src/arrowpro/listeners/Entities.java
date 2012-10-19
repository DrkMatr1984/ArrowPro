package arrowpro.listeners;

import arrowpro.ArrowPro;
import arrowpro.arrow.ArrowType;
import arrowpro.arrow.ProArrow;
import arrowpro.lightning.ProLightning;

import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityLiving;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Handles all entity events for the plugin,
 * 
 * @author Justin Stauch
 * @since October 9, 2012
 */
public class Entities implements Listener {
    private ArrowPro plugin;
    
    public Entities(ArrowPro plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.isCancelled()) {
                return;
            }
            if (event.getProjectile() instanceof ProArrow) {
                return;
            }
            if (!(event.getProjectile() instanceof CraftArrow)) {
                return;
            }
            Player player = (Player) event.getEntity();
            ArrowType type = plugin.getArrow(player, player.getInventory().getHeldItemSlot(), true);
            if (!type.canPay(((CraftPlayer) player).getHandle().inventory)) {
                player.sendMessage(ChatColor.RED + "You can't afford this arrow.");
                event.setCancelled(true);
                return;
            }
            player.getInventory().removeItem(type.getCost());
            EntityArrow eArrow = ((CraftArrow) event.getProjectile()).getHandle();
            ProArrow arrow = new ProArrow(eArrow.world, type.newAction(), (EntityLiving) eArrow.shooter, event.getForce());
            eArrow.world.addEntity(arrow);
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getLevel(((CraftPlayer) event.getPlayer()).getHandle());
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)) {
            return;
        }
        EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
        if (ev.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
            if (((CraftEntity) ev.getDamager()).getHandle() instanceof ProArrow) {
                if (ev.getEntity().isDead() && ((ProArrow) ((CraftEntity) ev.getDamager()).getHandle()).getShooter().getBukkitEntity() instanceof Player) {
                    plugin.killedEntity((Player) ((ProArrow) ((CraftEntity) ev.getDamager()).getHandle()).getShooter().getBukkitEntity(), ((CraftEntity) event.getEntity()).getHandle());
                }
            }
        }
        if (ev.getCause().equals(DamageCause.LIGHTNING)) {
            if (ev.getDamager() instanceof ProLightning) {
                if (ev.getEntity().isDead() && ((ProArrow) ((ProLightning) ev.getDamager()).getArrow()).getShooter().getBukkitEntity() instanceof Player) {
                    plugin.killedEntity((Player) ((ProArrow) ((ProLightning) ev.getDamager()).getArrow()).getShooter().getBukkitEntity(), ((CraftEntity) event.getEntity()).getHandle());
                }
            }
        }
    }
}