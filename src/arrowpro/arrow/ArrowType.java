package arrowpro.arrow;

import arrowpro.ArrowPro;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.PlayerInventory;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

/**
 * Used to track what arrows are equipped by players.
 *
 * @author Justin Stauch
 * @since September 1, 2012
 */
public enum ArrowType implements Serializable {

    NORMAL_ARROW("Arrow", NormalArrow.class, "Works identically to the vanilla minecraft arrow."),
    BLIND_ARROW("Blind Arrow", BlindArrow.class, "Applies the blind effect on the entity that it hits."),
    CONFUSION_ARROW("Confusion Arrow", ConfusionArrow.class, "Applies the confusion effect on the entity that it hits."),
    CREEPER_ARROW("Creeper Arrow", CreeperArrow.class, "Spawns a Creeper where it hits."),
    EXPLOSIVE_LIGHTNING_ARROW("Explosive Lightning Arrow", ExplosiveLightningArrow.class, "Strikes lightning and explodes where it hits.", ArrowPro.getPlugin().getConfig().contains("Explosive Lightning Arrow.Power") ? ArrowPro.getPlugin().getConfig().getInt("Explosive Lightning Arrow.Power") : 0),
    FIERY_EXPLOSIVE_LIGHTNING_ARROW("Fiery Explosive Lightning Arrow", FieryExplosiveLightningArrow.class, "Strikes lightning and makes a fiery explosion where it hits.", ArrowPro.getPlugin().getConfig().contains("Fiery Explosive Lightning Arrow.Power") ? ArrowPro.getPlugin().getConfig().getInt("Fiery Explosive Lightning Arrow.Power") : 0),
    FIERY_TNT_ARROW("Fiery TNT Arrow", FieryTNTArrow.class, "Makes a fiery explosion where it hits.", ArrowPro.getPlugin().getConfig().contains("Fiery TNT Arrow.Power") ? ArrowPro.getPlugin().getConfig().getInt("Fiery TNT Arrow.Power") : 0),
    FIRE_ARROW("Fire Arrow", FireArrow.class, "It is an arrow that catches fire."),
    IRON_GOLEM_ARROW("Iron Golem Arrow", IronGolemArrow.class, "Spawns an Iron Golem where it hits."),
    LAVA_ARROW("Lava Arrow", LavaArrow.class, "Places lava where it hits."),
    LIGHTNING_ARROW("Lightning Arrow", LightningArrow.class, "Strikes lightning where it hits."),
    POISON_ARROW("Poison Arrow", PoisonArrow.class, "Applies the poison effect on the entity it hits."),
    REDSTONE_TORCH_ARROW("Redstone Torch Arrow", PoisonArrow.class, "Places a redstone torch where it hits."),
    SKELLETON_ARROW("Skeleton Arrow", SkeletonArrow.class, "Spawns a skeleton where it hits."),
    SLOW_ARROW("Slow Arrow", SlowArrow.class, "Applies the slowness effect on the entity it hits."),
    SNOWMAN_ARROW("Snowman Arrow", SnowmanArrow.class, "Spawns a snowman where it lands."),
    TNT_ARROW("TNT Arrow", TNTArrow.class, "Explodes where it hits.", ArrowPro.getPlugin().getConfig().contains("TNT Arrow.Power") ? ArrowPro.getPlugin().getConfig().getInt("TNT Arrow.Power") : 0),
    TP_ARROW("TP Arrow", TPArrow.class, "Teleports the shooter to where the arrow lands."),
    TORCH_ARROW("Torch Arrow", TorchArrow.class, "Places a torch where it lands."),
    TREE_ARROW("Tree Arrow", TreeArrow.class, "Generates a tree of a random type where it lands."),
    TRIPLE_ARROW("Triple Arrow", TripleArrow.class, "Shoots three arrows at once."),
    WATER_ARROW("Water Arrow", WaterArrow.class, "Places Water where it lands"),
    WEAKNESS_ARROW("Weakness Arrow", WeaknessArrow.class, "Applies the weakness effect on the entity that it hits."),
    ZOMBIE_ARROW("Zombie Arrow", ZombieArrow.class, "Spawns a Zombie where it lands.");
    private final Class<? extends ProArrowAction> type;
    private final String name;
    private final String description;
    private String[] costAsString, bowCostAsString;
    private ItemStack[] cost, bowCost;
    private int level;
    private final float power;
    private static HashMap<String, ArrowType> nameMap = new HashMap<String, ArrowType>();
    private static HashMap<String, ArrowType> abbrvMap = new HashMap<String, ArrowType>();
    private static HashMap<Class<? extends ProArrowAction>, ArrowType> classMap = new HashMap<Class<? extends ProArrowAction>, ArrowType>();

    private ArrowType(String name, Class<? extends ProArrowAction> type, String description, float power) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.power = power;
    }

    private ArrowType(String name, Class<? extends ProArrowAction> type, String description) {
        this(name, type, description, 0);
    }

    private void addToMaps() {
        if (nameMap == null) {
            nameMap = new HashMap<String, ArrowType>();
        }
        if (classMap == null) {
            classMap = new HashMap<Class<? extends ProArrowAction>, ArrowType>();
        }
        if (abbrvMap == null) {
            abbrvMap = new HashMap<String, ArrowType>();
        }
        nameMap.put(this.name.toLowerCase(), this);
        abbrvMap.put(abbreviate(this.name).toLowerCase(), this);
        classMap.put(this.type, this);
    }

    /**
     * Abbreviates the given word by removing the word arrow from it and if it
     * contains spaces, taking the first letter of each word and putting it
     * together.
     *
     * @param word The word to abbreviate.
     * @return The abbreviated word.
     */
    public static String abbreviate(String word) {
        String abbr = word.toLowerCase();
        abbr = abbr.replace(" arrow", "");
        if (!abbr.contains(" ")) {
            return abbr;
        }
        String[] words = abbr.split(" ");//All the words in the word that was given except for arrow which has been removed.
        String realAbbr = "";
        for (String part : words) {
            realAbbr += part.charAt(0);
        }
        return realAbbr;
    }

    public void setCost(ItemStack[] cost) {
        this.cost = cost;
        costAsString = new String[cost.length];
        for (int x = 0; x < cost.length; x++) {
            costAsString[x] = cost[x].getType().toString() + ", " + cost[x].getAmount();
        }
    }

    public void setBowCost(ItemStack[] cost) {
        bowCost = cost;
        bowCostAsString = new String[cost.length];
        for (int x = 0; x < cost.length; x++) {
            bowCostAsString[x] = cost[x].getType().toString() + ", " + cost[x].getAmount();
        }
    }

    public String[] getCostAsString() {
        return costAsString;
    }

    public String[] getBowCostAsString() {
        return bowCostAsString;
    }

    public ItemStack[] getCost() {
        return cost;
    }

    public ItemStack[] getBowCost() {
        return bowCost;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getFullDescription() {
        String[] descript = new String[(isExplosive() ? 7 : 6) + costAsString.length + bowCostAsString.length];
        descript[0] = (String) (ChatColor.BLUE + "" + ChatColor.BOLD + getName());
        descript[1] = (String) (ChatColor.AQUA + "Level: " + getLevel());
        int expl = 0;//0 if it the arrow isn't explosive and one if it is to push everything down in the list.
        if (isExplosive()) {
            descript[2] = (String) (ChatColor.AQUA + "Power: " + getPower());
            expl = 1;
        }
        descript[2 + expl] = (String) (ChatColor.GREEN + getDescription());
        descript[3 + expl] = ChatColor.YELLOW + "Bow Cost:";
        for (int x = 4 + expl; x < bowCostAsString.length + 4 + expl; x++) {
            descript[x] = ChatColor.WHITE + "    " + "- " + bowCostAsString[x - 4 - expl];
        }
        descript[4 + expl + bowCostAsString.length] = ChatColor.YELLOW + "Cost:";
        for (int x = 5 + expl + bowCostAsString.length; x < descript.length - 1; x++) {
            descript[x] = ChatColor.WHITE + "    " + "- " + costAsString[x - bowCostAsString.length - 5 - expl];
        }
        descript[descript.length - 1] = ChatColor.RED + "-----------------------------------------------------";
        return descript;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public Class<? extends ProArrowAction> getArrowClass() {
        return type;
    }

    public boolean isExplosive() {
        return this.equals(TNT_ARROW) || this.equals(EXPLOSIVE_LIGHTNING_ARROW) || this.equals(FIERY_EXPLOSIVE_LIGHTNING_ARROW) || this.equals(FIERY_TNT_ARROW);
    }

    public float getPower() {
        return power;
    }
    
    /**
     * Creates a new instance of this ArrowType's class.
     * 
     * @return The new instance.
     */
    public ProArrowAction newAction() {
        try {
            if (isExplosive()) {
                return getArrowClass().getConstructor(float.class).newInstance(power);
            }
            else {
                return getArrowClass().getConstructor().newInstance();
            }
        } catch (NoSuchMethodException ex) {
            ArrowPro.getPlugin().getLogger().log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            ArrowPro.getPlugin().getLogger().log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            ArrowPro.getPlugin().getLogger().log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            ArrowPro.getPlugin().getLogger().log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            ArrowPro.getPlugin().getLogger().log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            ArrowPro.getPlugin().getLogger().log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Determines if the inventory contains the required items to shoot this bow.
     * 
     * Goes through every required stack and subtracts the amount from the total required of the item.
     * 
     * @param inventory The inventory to check.
     * @return If it contains the proper items.
     */
    public boolean canPayBow(PlayerInventory inventory) {
        boolean contains = true;
        net.minecraft.server.ItemStack[] contents = inventory.getContents();
        for (org.bukkit.inventory.ItemStack stack : getBowCost()) {
            int stillToFind = stack.getAmount();
            for (net.minecraft.server.ItemStack stac : contents) {
                if (stac == null) {
                    continue;
                }
                if (stac.id == stack.getTypeId()) {
                    stillToFind -= stac.count;
                }
                if (stillToFind <= 0) {
                    break;
                }
            }
            contains = contains && stillToFind <= 0;
        }
        return contains;
    }
    
    /**
     * Determines if the inventory contains the required items to shoot this bow.
     * 
     * Goes through every required stack and subtracts the amount from the total required of the item.
     * 
     * @param inventory The inventory to check.
     * @return If it contains the proper items.
     */
    public boolean canPay(PlayerInventory inventory) {
        boolean contains = true;
        net.minecraft.server.ItemStack[] contents = inventory.getContents();
        for (org.bukkit.inventory.ItemStack stack : getCost()) {
            int stillToFind = stack.getAmount();
            for (net.minecraft.server.ItemStack stac : contents) {
                if (stac == null) {
                    continue;
                }
                if (stac.id == stack.getTypeId()) {
                    stillToFind -= stac.count;
                }
                if (stillToFind <= 0) {
                    break;
                }
            }
            contains = contains && (stillToFind <= 0 ? true : false);
        }
        return contains;
    }

    public static ArrowType fromName(String name) {
        return nameMap.get(name.toLowerCase());
    }

    public static ArrowType fromAbbreviation(String abbrv) {
        return abbrvMap.get(abbrv.toLowerCase());
    }

    public static ArrowType fromClass(Class<? extends ProArrowAction> type) {
        return classMap.get(type);
    }

    public static boolean isAnArrow(String arrow) {
        return nameMap.containsKey(arrow.toLowerCase()) || abbrvMap.containsKey(arrow.toLowerCase());
    }
    
    public static void updateMaps() {
        for(ArrowType type : values()) {
            type.addToMaps();
        }
    }
}