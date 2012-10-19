package arrowpro.listeners;

import arrowpro.ArrowPro;
import arrowpro.arrow.ArrowType;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Executes the commands for the plugin.
 * 
 * @author Justin Stauch
 * @since October 2, 2012
 */
public class Commands implements CommandExecutor {
    
    private ArrowPro plugin;
    
    public Commands(ArrowPro plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commander, String[] args) {
        if (!(commander.equalsIgnoreCase("ArrowPro") || commander.equalsIgnoreCase("ap"))) {
            return false;
        }
        if (args[0].equalsIgnoreCase("change")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "You must include your bow type.");
                return true;
            }
            if (sender instanceof CraftPlayer) {
                if (!ArrowType.isAnArrow(args[1])) {
                    sender.sendMessage(ChatColor.RED + "There is no arrow type called " + args[1] + ".");
                    return true;
                }
                if (plugin.isDisabled(ArrowType.fromName(args[1])) || plugin.isDisabled(ArrowType.fromAbbreviation(args[1]))) {
                    sender.sendMessage(ChatColor.RED + "This type of arrow is not allowed.");
                    return true;
                }
                CraftPlayer player = (CraftPlayer) sender;
                ArrowType type = ArrowType.fromName(args[1]) == null ? ArrowType.fromAbbreviation(args[1].toLowerCase()) : ArrowType.fromName(args[1]);
                if (!player.hasPermission("arrowpro." + ArrowType.abbreviate(type.getName())) && !player.hasPermission("arrowpro.*") && !player.hasPermission("*")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission.");
                    return true;
                }
                if (plugin.getLevel(player.getHandle()) < type.getLevel()) {
                    sender.sendMessage(ChatColor.RED + "Your level is too low.");
                    sender.sendMessage(ChatColor.RED + "You are a level " + ChatColor.WHITE +  plugin.getLevel(player.getHandle()) + ChatColor.RED + ". You need to be a level " + ChatColor.WHITE + ArrowType.fromName(args[1]).getLevel() + ChatColor.RED + " to use this type.");
                    return true;
                }
                if (!type.canPayBow(((CraftPlayer) player).getHandle().inventory)) {
                    sender.sendMessage(ChatColor.RED + "You can't afford this.");
                    return true;
                }
                plugin.setArrow(player, player.getInventory().getHeldItemSlot(), type);
                player.getInventory().removeItem(type.getBowCost());
                sender.sendMessage(ChatColor.GOLD + "Changed bow so that it shoots arrows of type " + type.getName() + ".");
                return true;
            }
            else {
                sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
                return true;
            }
        }
        if (args[0].equals("arrows")) {
            String message = "";
            String outOfReachMessage = "";//Holds the names of any not disabled arrows that require a higher level than that of the sender.
            boolean outOfReach = false;//If there is any arrows that are not disabled but require a level higher than that of the sender so that they may be displayed later.
            ArrowType[] types = ArrowType.values();
            for (ArrowType type : types) {
                if (plugin.isDisabled(type)) {
                    continue;
                }
                if (sender instanceof CraftPlayer) {
                    if (plugin.getLevel(((CraftPlayer) sender).getHandle()) < type.getLevel()) {
                        outOfReach = true;
                        outOfReachMessage += (outOfReachMessage.equalsIgnoreCase("") ? "" : ", ") + type.getName().replace(" Arrow", "");
                        continue;
                    }
                }
                message += (message.equalsIgnoreCase("") ? "" : ", ") + type.getName().replace(" Arrow", "");
            }
            sender.sendMessage(sender instanceof Player ? ChatColor.GREEN + "All arrows available to you:" : ChatColor.GREEN + "All enabled arrows:");
            sender.sendMessage(message);
            if (outOfReach) {
                sender.sendMessage(ChatColor.RED + "All arrows that require you to be a higher level:");
                sender.sendMessage(outOfReachMessage);
            }
            sender.sendMessage(ChatColor.YELLOW + "For more detailed information type /ap then the name of the arrow.");
            sender.sendMessage(ChatColor.YELLOW + "For arrows with more than one word in its name, type the first letter in each word.");
            return true;
        }
        if (ArrowType.isAnArrow(args[0])) {
            if (plugin.isDisabled(ArrowType.fromName(args[0])) || plugin.isDisabled(ArrowType.fromAbbreviation(args[0]))) {
                sender.sendMessage(ChatColor.RED + "This type of arrow is not allowed.");
                return true;
            }
            if (sender instanceof Player) {
                CraftPlayer player = (CraftPlayer) sender;
                ArrowType type = ArrowType.fromName(args[0]) == null ? ArrowType.fromAbbreviation(args[0].toLowerCase()) : ArrowType.fromName(args[0]);
                if (!player.hasPermission("arrowpro." + ArrowType.abbreviate(type.getName())) && !player.hasPermission("arrowpro.*") && !player.hasPermission("*")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission.");
                    return true;
                }
            }
            sender.sendMessage(ArrowType.fromAbbreviation(args[0]).getFullDescription());
            return true;
        }
        if (args[0].equalsIgnoreCase("") || args.length < 1) {
            sender.sendMessage(ChatColor.YELLOW + "ArrowPro help");
            sender.sendMessage(ChatColor.BLUE + "Type /ap arrows for a list of all arrows.");
            sender.sendMessage(ChatColor.BLUE + "Type /ap then an arrow type for more inforation on that arrow.");
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.BLUE + "Type /ap change then an arrow type while holding a bow to change the bow to that type.");
                sender.sendMessage(ChatColor.BLUE + "Type /ap me to get your level, experience, and current arrow.");
            }
        }
        if (args[0].equalsIgnoreCase("me")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a player");
                return true;
            }
            Player player = (Player) sender;
            sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + player.getDisplayName());
            sender.sendMessage(ChatColor.YELLOW + "Archery Level: " + ChatColor.WHITE + plugin.getLevel(((CraftPlayer) player).getHandle()));
            sender.sendMessage(ChatColor.YELLOW + "Experience: " + ChatColor.WHITE + plugin.getExp(player) + ChatColor.YELLOW + " of " + ChatColor.WHITE + plugin.getExpForLevelUp(player));
            if (plugin.getArrow(player, player.getInventory().getHeldItemSlot(), false) != null) {
                sender.sendMessage(ChatColor.YELLOW + "Current Arrow: " + ChatColor.WHITE + plugin.getArrow(player, player.getInventory().getHeldItemSlot(), false).getName());
            }
            return true;
        }
        return true;
    }
}