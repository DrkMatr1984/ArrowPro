package arrowpro;

import arrowpro.arrow.ArrowType;
import arrowpro.equations.StatSolver;
import arrowpro.equations.exponential.EMD;
import arrowpro.equations.exponential.EWD;
import arrowpro.equations.exponential.Exponential;
import arrowpro.equations.quadratic.QMD;
import arrowpro.equations.quadratic.QWD;
import arrowpro.equations.quadratic.Quadratic;
import arrowpro.listeners.Commands;
import arrowpro.listeners.Entities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mobstats.MobStats;
import mobstats.entities.StatsEntity;

import net.minecraft.server.Entity;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is the main class of ArrowPro.
 * 
 * It handles all initialization and basic functions needed by the rest of the plugin.
 * 
 * @author Justin Stauch
 * @since December 19, 2011
 * 
 * copyright 2011© Justin Stauch, All Rights Reserved
 */
public class ArrowPro extends JavaPlugin {
    private HashMap<String, HashMap<Integer, ArrowType>> arrows = new HashMap<String, HashMap<Integer, ArrowType>>();
    private HashMap<String, Integer> levels = new HashMap<String, Integer>();
    private HashMap<String, Double> exp = new HashMap<String, Double>();
    private ArrayList<ArrowType> disabled = new ArrayList<ArrowType>();
    private StatSolver levelEquat, mobExpDrop;
    private double entityKillExp, playerKillExp, entityHitExp, playerHitExp;
    private boolean useMobStats = false;
    private static ArrowPro plugin;

    /**
     * Handles when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        plugin = null;
        
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        File arrow = new File(getDataFolder(), "arrows.data");
        if (!arrow.exists()) {
            try {
                arrow.createNewFile();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, null, ex);
            }
        }
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(arrow));
            out.writeObject(arrows);
        }
        catch (IOException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, null, ex);
            }
        }
        File level = new File(getDataFolder(), "levels.data");
        if (!level.exists()) {
            try {
                level.createNewFile();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, null, ex);
            }
        }
        try {
            out = new ObjectOutputStream(new FileOutputStream(level));
            out.writeObject(levels);
        }
        catch (IOException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, null, ex);
            }
        }
        File xp = new File(getDataFolder(), "exp.data");
        if (!xp.exists()) {
            try {
                xp.createNewFile();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, null, ex);
            }
        }
        try {
            out = new ObjectOutputStream(new FileOutputStream(xp));
            out.writeObject(exp);
        }
        catch (IOException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Called when the plugin is enabled. Creates recipes and reads the config.
     */
    @Override
    public void onEnable() {
        plugin = this;
        
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        
        File config = new File(getDataFolder(), "config.yml");
        if (!config.exists()) {
            this.saveDefaultConfig();
        }
        
        ArrowType.updateMaps();
        setupArrows();
        loadArrowInfo();
        levelEquat = getEquation("Player Levels");
        entityKillExp = getConfig().getDouble("Experience.Entity.Kill");
        playerKillExp = getConfig().getDouble("Experience.Player.Kill");
        entityHitExp = getConfig().getDouble("Experience.Entity.Hit");
        playerHitExp = getConfig().getDouble("Experience.Player.Hit");
        loadLevels();
        loadExp();
        loadArrows();
        
        if (getConfig().contains("Experience.Entity.Kill Equation")) {
            mobExpDrop = getEquation("Experience.Entity.Kill Equation");
            useMobStats = true;
        }
        
        useMobStats = useMobStats && isMobStatsLoaded();
        
        getCommand("ArrowPro").setExecutor(new Commands(this));
        getServer().getPluginManager().registerEvents(new Entities(this), this);
        
        plugin = this;
    }
    
    public static ArrowPro getPlugin() {
        return plugin;
    }
    
    public boolean isMobStatsLoaded() {
        Plugin[] plugins = getServer().getPluginManager().getPlugins();
        int found = 0;
        for (Plugin plug : plugins) {
            for (String bef : getDescription().getLoadBefore()) {
                if (plug.getDescription().getName().equalsIgnoreCase(bef)) {
                    found++;
                    break;
                }
            }
        }
        return found >= getDescription().getLoadBefore().size();
    }
    
    /**
     * Gets the level of the given Player.
     * 
     * @param player The Player to check the level for.
     * @return The Player's level.
     */
    public int getLevel(Entity entity) {
        if (entity.getBukkitEntity() instanceof Player) {
            Player player = (Player) entity.getBukkitEntity();
            if (levels == null) {
                levels = new HashMap<String, Integer>();
            }
            if (!levels.containsKey(player.getName())) {
                levels.put(player.getName(), 0);
            }
            if (exp == null) {
                exp = new HashMap<String, Double>();
            }
            if (!exp.containsKey(player.getName())) {
                exp.put(player.getName(), 0D);
            }
            checkLevel(player);
            return levels.get(player.getName());
        }
        if (entity instanceof StatsEntity) {
            return ((StatsEntity) entity).getLevel();
        }
        return 1;
    }
    
    public double getExp(Player player) {
        if (exp == null) {
            exp = new HashMap<String, Double>();
        }
        if (!exp.containsKey(player.getName())) {
            exp.put(player.getName(), 0D);
        }
        return exp.get(player.getName());
    }
    
    public double getExpForLevelUp(Player player) {
        return levelEquat.solve(getLevel(((CraftPlayer) player).getHandle()), 0);
    }
    
    /**
     * Changes exp because the given player killed an entity.
     * 
     * @param player The player that killed the entity.
     */
    public void killedEntity(Player player, Entity killed) {
        if (useMobStats) {
            if (killed instanceof StatsEntity) {
                exp.put(player.getName(), exp.get(player.getName()) + mobExpDrop.solve(((StatsEntity) killed).getLevel(), 1));
            }
        }
        else {
            exp.put(player.getName(), exp.get(player.getName()) + entityKillExp);
        }
        checkLevel(player);
    }
    
    /**
     * Changes exp because the given player killed an entity.
     * 
     * @param player The player that killed the entity.
     */
    public void killedPlayer(Player player) {
        exp.put(player.getName(), exp.get(player.getName()) + playerKillExp);
        checkLevel(player);
    }
    
    /**
     * Changes exp because the given player killed an entity.
     * 
     * @param player The player that killed the entity.
     */
    public void hitEntity(Player player) {
        exp.put(player.getName(), exp.get(player.getName()) + entityHitExp);
        checkLevel(player);
    }
    
    /**
     * Changes exp because the given player killed an entity.
     * 
     * @param player The player that killed the entity.
     */
    public void hitPlayer(Player player) {
        exp.put(player.getName(), exp.get(player.getName()) + playerHitExp);
        checkLevel(player);
    }
    
    /**
     * Checks to see what level the Player should be based on experience and the current level.
     * 
     * @param player The player to check for.
     */
    public void checkLevel(Player player) {
        if (!exp.containsKey(player.getName())) {
            exp.put(player.getName(), 0D);
        }
        if (!levels.containsKey(player.getName())) {
            levels.put(player.getName(), 0);
        }
        double xp = exp.get(player.getName());
        double levelUp = levelEquat.solve(levels.get(player.getName()), 1);
        if (xp >= levelUp) {//Player leveled up
            int old = levels.put(player.getName(), levels.get(player.getName()) + 1);
            exp.put(player.getName(), xp - levelUp);
            checkLevel(player);//Checks the levels until it is sure that the player is the right level, in case the player goes up multiple levels.
            player.sendMessage(ChatColor.GREEN + "You have leveled up from a level " + old + " to a level " + levels.get(player.getName()) + "!");
        }
        if (xp < 0) {//Player's level is too high for the amount of exp that it has and must be sent down levels.
            int old = levels.put(player.getName(), levels.get(player.getName()) - 1);
            levelUp = levelEquat.solve(levels.get(player.getName()), xp);
            exp.put(player.getName(), levelUp + xp);
            checkLevel(player);//Checks the levels until it is sure that the player is the right level, in case the player goes down multiple levels.
            player.sendMessage(ChatColor.RED + "You have leveled down from a level " + old + " to a level " + levels.get(player.getName()) + ".");
        }
    }
    
    /**
     * Checks if an Arrow may not be used.
     * 
     * @param type The ArrowType of the Arrow to check.
     * @return If the given ArrowType is disabled.
     */
    public boolean isDisabled(ArrowType type) {
        return disabled.contains(type);
    }
    
    /**
     * Sets the arrow that the given Player will use when holding the bow in the given slot.
     * 
     * @param player The player to set the slot for.
     * @param slot The slot to change for.
     * @param type The type of arrow to use in the given slot.
     */
    public void setArrow(Player player, int slot, ArrowType type) {
        if (!arrows.containsKey(player.getName())) {
            arrows.put(player.getName(), new HashMap<Integer, ArrowType>());
        }
        arrows.get(player.getName()).put(slot, type);
    }
    
    /**
     * Gets the type of bow that the Player has in the given inventory slot.
     * 
     * @param player The player to check for.
     * @param slot The slot to check in.
     * @param convert If the type of Arrow should be converted to normal.
     * @return The type of bow in that slot.
     */
    public ArrowType getArrow(Player player, int slot, boolean convert) {
        if (!arrows.containsKey(player.getName())) {
            arrows.put(player.getName(), new HashMap<Integer, ArrowType>());
        }
        if (!arrows.get(player.getName()).containsKey(slot)) {
            arrows.get(player.getName()).put(slot, convert ? ArrowType.NORMAL_ARROW : null);
        }
        if (arrows.get(player.getName()).get(slot) != null) {
            if (arrows.get(player.getName()).get(slot).getLevel() > getLevel(((CraftPlayer) player).getHandle())) {
                arrows.get(player.getName()).put(slot, convert && !arrows.get(player.getName()).get(slot).equals(ArrowType.NORMAL_ARROW) ? ArrowType.NORMAL_ARROW : null);
            }
        }
        if (isDisabled(arrows.get(player.getName()).get(slot))) {
            arrows.get(player.getName()).put(slot, convert && !arrows.get(player.getName()).get(slot).equals(ArrowType.NORMAL_ARROW) ? ArrowType.NORMAL_ARROW : null);
        }
        return arrows.get(player.getName()).get(slot);
    }
    
    /**
     * Loads the settings for arrows out of the config file.
     */
    private void setupArrows() {
        for (ArrowType type : ArrowType.values()) {
            if (!getConfig().contains(type.getName())) {
                disabled.add(type);
                continue;
            }
            if (!getConfig().contains(type.getName() + ".Cost")) {
                type.setCost(new ItemStack[0]);
            }
            else {
                List<String> cost = getConfig().getStringList(type.getName() + ".Cost");
                ItemStack[] itemCost = new ItemStack[cost.size()];
                for (int x = 0; x < cost.size(); x++) {
                    String[] pieces = cost.get(x).split(", ");
                    if (pieces.length < 2) {
                        continue;
                    }
                    int id = Integer.parseInt(pieces[0]);
                    int amount = Integer.parseInt(pieces[1]);
                    itemCost[x] = new ItemStack(id, amount);
                }
                itemCost = removeEmpties(itemCost);
                type.setCost(itemCost);
            }
            if (!getConfig().contains(type.getName() + ".Bow Cost")) {
                type.setBowCost(new ItemStack[0]);
            }
            else {
                List<String> bowCost = getConfig().getStringList(type.getName() + ".Bow Cost");
                ItemStack[] itemBowCost = new ItemStack[bowCost.size()];
                for (int x = 0; x < bowCost.size(); x++) {
                    String[] pieces = bowCost.get(x).split(", ");
                    if (pieces.length < 2) {
                        continue;
                    }
                    int id = Integer.parseInt(pieces[0]);
                    int amount = Integer.parseInt(pieces[1]);
                    itemBowCost[x] = new ItemStack(id, amount);
                }
                itemBowCost = removeEmpties(itemBowCost);
                type.setBowCost(itemBowCost);
            }
            if (getConfig().contains(type.getName() + ".Level")) {
                type.setLevel(getConfig().getInt(type.getName() + ".Level"));
            }
            else {
                type.setLevel(0);
            }
            try {
                type.getArrowClass().getMethod("setSpeedEquation", StatSolver.class).invoke(type.getClass(), getEquation(type.getName() + ".Equations.Speed"));
                type.getArrowClass().getMethod("setDamageEquation", StatSolver.class).invoke(type.getClass(), getEquation(type.getName() + ".Equations.Damage"));
                if (type.equals(ArrowType.BLIND_ARROW) || type.equals(ArrowType.CONFUSION_ARROW) || type.equals(ArrowType.POISON_ARROW) || type.equals(ArrowType.SLOW_ARROW) || type.equals(ArrowType.WEAKNESS_ARROW)) {
                    type.getArrowClass().getMethod("setDurationEquation", StatSolver.class).invoke(type.getClass(), getEquation(type.getName() + ".Equations.Duration"));
                    type.getArrowClass().getMethod("setAmplificationEquation", StatSolver.class).invoke(type.getClass(), getEquation(type.getName() + ".Equations.Amplification"));
                }
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace(System.out);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace(System.out);
            } catch (InvocationTargetException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }
    
    /**
     * Removes any part of the array that is null.
     * 
     * @param obj The array to cleanup.
     * @return The clean array.
     */
    private ItemStack[] removeEmpties(ItemStack[] obj) {
        int size = 0;
        for (Object ob : obj) {
            if (ob != null) {
                size++;
            }
        }
        ItemStack[] clean = new ItemStack[size];
        for (int y = 0; y < clean.length; y++) {
            for (int x = 0; x < obj.length; x++) {
                if (obj[x] != null) {
                    clean[y] = obj[x];
                    obj[x] = null;
                    break;
                }
            }
        }
        return clean;
    }
    
    /**
     * Creates an equation out of the information for a certain path in the config. So far only quadratic equations are supported.
     * 
     * @param path The path to look for values at in the config.
     * @return The equation from the given path.
     */
    private StatSolver getEquation(String path) {
        if (!getConfig().contains(path)) {
            return new QMD(0, 0, 1, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        }
        if (getConfig().getString(path + ".type").equalsIgnoreCase("quadratic")) {
            double a, b, c, max, min;
            a = getConfig().getDouble(path + ".a");
            b = getConfig().getDouble(path + ".b");
            c = getConfig().getDouble(path + ".c");
            if (getConfig().contains(path + ".max")) {
                max = getConfig().getDouble(path + ".max");
            }
            else {
                max = Double.POSITIVE_INFINITY;
            }
            if (getConfig().contains(path + "min")) {
                min = getConfig().getDouble(path + ".min");
            }
            else {
                min = Double.NEGATIVE_INFINITY;
            }
            return new Quadratic(a, b, c, max, min);
        }
        if (getConfig().getString(path + ".type").equalsIgnoreCase("QWD")) {
            double a, b, c, max, min;
            boolean aDef, bDef, cDef;
            String hold;
            hold = getConfig().getString(path + ".a");
            if (hold.contains("d")) {
                aDef = true;
                hold = hold.replaceAll("d", "");
                System.out.println(hold);
            }
            else aDef = false;
            a = Double.parseDouble(hold);
            hold = getConfig().getString(path + ".b");
            if (hold.contains("d")) {
                bDef = true;
                hold = hold.replaceAll("d", "");
            }
            else bDef = false;
            b = Double.parseDouble(hold);
            hold = getConfig().getString(path + ".c");
            if (hold.contains("d")) {
                cDef = true;
                hold = hold.replaceAll("d", "");
            }
            else cDef = false;
            c = Double.parseDouble(hold);
            if (getConfig().contains(path + ".max")) {
                max = getConfig().getDouble(path + ".max");
            }
            else {
                max = Double.POSITIVE_INFINITY;
            }
            if (getConfig().contains(path + "min")) {
                min = getConfig().getDouble(path + ".min");
            }
            else {
                min = Double.NEGATIVE_INFINITY;
            }
            return new QWD(a, b, c, max, min, aDef, bDef, cDef);
        }
        if (getConfig().getString(path + ".type").equalsIgnoreCase("QMD")) {
            double a, b, c, max, min;
            a = getConfig().getDouble(path + ".a");
            b = getConfig().getDouble(path + ".b");
            c = getConfig().getDouble(path + ".c");
            if (getConfig().contains(path + ".max")) {
                max = getConfig().getDouble(path + ".max");
            }
            else {
                max = Double.POSITIVE_INFINITY;
            }
            if (getConfig().contains(path + "min")) {
                min = getConfig().getDouble(path + ".min");
            }
            else {
                min = Double.NEGATIVE_INFINITY;
            }
            return new QMD(a, b, c, max, min);
        }
        if (getConfig().getString(path + ".type").equalsIgnoreCase("exponential")) {
            double a, b, c, d, f, max, min;
            a = getConfig().getDouble(path + ".a");
            b = getConfig().getDouble(path + ".b");
            c = getConfig().getDouble(path + ".c");
            d = getConfig().getDouble(path + ".d");
            f = getConfig().getDouble(path + ".f");
            if (getConfig().contains(path + ".max")) {
                max = getConfig().getDouble(path + ".max");
            }
            else {
                max = Double.POSITIVE_INFINITY;
            }
            if (getConfig().contains(path + "min")) {
                min = getConfig().getDouble(path + ".min");
            }
            else {
                min = Double.NEGATIVE_INFINITY;
            }
            return new Exponential(a, b, c, d, f, max, min);
        }
        if (getConfig().getString(path + ".type").equalsIgnoreCase("EWD")) {
            double a, b, c, d, f, max, min;
            boolean aDef, bDef, cDef, dDef, fDef;
            String hold;
            hold = getConfig().getString(path + ".a");
            if (hold.contains("d")) {
                aDef = true;
                hold = hold.replaceAll("d", "");
            }
            else aDef = false;
            a = Double.parseDouble(hold);
            hold = getConfig().getString(path + ".b");
            if (hold.contains("d")) {
                bDef = true;
                hold = hold.replaceAll("d", "");
            }
            else bDef = false;
            b = Double.parseDouble(hold);
            hold = getConfig().getString(path + ".c");
            if (hold.contains("d")) {
                cDef = true;
                hold = hold.replaceAll("d", "");
            }
            else cDef = false;
            c = Double.parseDouble(hold);
            hold = getConfig().getString(path + ".d");
            if (hold.contains("d")) {
                dDef = true;
                hold = hold.replaceAll("d", "");
            }
            else dDef = false;
            d = Double.parseDouble(hold);
            hold = getConfig().getString(path + ".f");
            if (hold.contains("d")) {
                fDef = true;
                hold = hold.replaceAll("d", "");
            }
            else fDef = false;
            f = Double.parseDouble(hold);
            if (getConfig().contains(path + ".max")) {
                max = getConfig().getDouble(path + ".max");
            }
            else {
                max = Double.POSITIVE_INFINITY;
            }
            if (getConfig().contains(path + "min")) {
                min = getConfig().getDouble(path + ".min");
            }
            else {
                min = Double.NEGATIVE_INFINITY;
            }
            return new EWD(a, b, c, d, f, max, min, aDef, bDef, cDef, dDef, fDef);
        }
        if (getConfig().getString(path + ".type").equalsIgnoreCase("EMD")) {
            double a, b, c, d, f, max, min;
            a = getConfig().getDouble(path + ".a");
            b = getConfig().getDouble(path + ".b");
            c = getConfig().getDouble(path + ".c");
            d = getConfig().getDouble(path + ".d");
            f = getConfig().getDouble(path + ".f");
            if (getConfig().contains(path + ".max")) {
                max = getConfig().getDouble(path + ".max");
            }
            else {
                max = Double.POSITIVE_INFINITY;
            }
            if (getConfig().contains(path + "min")) {
                min = getConfig().getDouble(path + ".min");
            }
            else {
                min = Double.NEGATIVE_INFINITY;
            }
            return new EMD(a, b, c, d, f, max, min);
        }
        return new QMD(0, 0, 1, 1, 1);//Just makes it so that it isn't modified, it creates this function: ⨍(x) = 1 and then returns ⨍(x)(the default value).
    }
    
    /**
     * Loads all the information amount the arrows into a file for arrow information called "Arrow_Information.yml" it is just a copy of all the full arrow descriptions.
     */
    private void loadArrowInfo() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        File configFile = new File(getDataFolder(), "config.yml");
        File infoFile = new File(getDataFolder(), "info.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
        }
        if (!infoFile.exists()) {
            try {
                infoFile.createNewFile();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, null, ex);
            }
        }
        else if (configFile.lastModified() < infoFile.lastModified()) {
            return;
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(infoFile));
            out.write("This is not a config file, only a file containing information about each arrow.");
            out.newLine();
            out.write("For configuration, open config.yml.");
            out.newLine();
            out.write("When the config gets changed, this file changes on the next reload or startup.");
            out.newLine();
            out.write("Any arrow excluded from the config file will be excluded here.");
            out.newLine();
            out.newLine();
            for (ArrowType arrow : ArrowType.values()) {
                if (isDisabled(arrow)) {
                    continue;
                }
                for (String item : arrow.getFullDescription()) {
                    String line = item;
                    for (ChatColor col : ChatColor.values()) {
                        line = line.replace(col.toString(), "");
                    }
                    out.write(line);
                    out.newLine();
                }
            }
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Loads the player arrow information from the disk and checks the information.
     */
    private void loadArrows() {
        File arrow = new File(getDataFolder(), "arrows.data");
        if (!arrow.exists()) {
            arrows = new HashMap<String, HashMap<Integer, ArrowType>>();
            return;
        }
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(arrow));
            arrows = (HashMap<String, HashMap<Integer, ArrowType>>) in.readObject();
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, null, ex);
            }
        }
        for (Player player : getServer().getOnlinePlayers()) {
            for (int slot = 0; slot < player.getInventory().getSize(); slot++) {
                getArrow(player, slot, true);
            }
        }
    }
    
    /**
     * Loads the exp of the players from the disk and then checks to make sure that each player is the right level.
     */
    private void loadExp() {
        File xp = new File(getDataFolder(), "exp.data");
        if (!xp.exists()) {
            exp = new HashMap<String, Double>();
        }
        else {
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(new FileInputStream(xp));
                exp = (HashMap<String, Double>) in.readObject();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                getLogger().log(Level.SEVERE, null, ex);
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    getLogger().log(Level.SEVERE, null, ex);
                }
            }
        }
        for (Player player : getServer().getOnlinePlayers()) {
            checkLevel(player);
        }
    }
    
    /**
     * Loads the levels of the players from the disk.
     */
    private void loadLevels() {
        File level = new File(getDataFolder(), "levels.data");
        if (!level.exists()) {
            levels = new HashMap<String, Integer>();
            return;
        }
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(level));
            exp = (HashMap<String, Double>) in.readObject();
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, null, ex);
            }
        }
    }
}