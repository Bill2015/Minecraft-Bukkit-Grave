package com.ranull.graves.randomizer;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.plugin.Plugin;

public class EquimentRandomizer {
    private Plugin plugin;

    private static String ARMOR_MATERIAL[]  = {"DIAMOND", "IRON", "GOLDEN", "LEATHER", "DEFAULT"};
    private static String ARMOR_NAME[]  = {"BOOTS", "LEGGINGS", "CHESTPLATE", "HELMET"};
    private int armorTotalWeight = 0;
    private int armorMaterialWeight[];
    private double armorEnchantChance;
    private EquipEnchantment armorEnchant[];

    private static String WEAPON_MATERIAL[]  = { "DIAMOND", "IRON", "GOLDEN", "STONE", "WOODEN"};
    private static String WEAPON_NAME[]  = {"AXE", "SWORD", "SHOVEL", "DEFAULT"};
    private int weaponMaterialTotalWeight = 0;
    private int weaponTypeTotalWeight = 0;
    private int weaponMaterialWeight[];
    private int weaponTypeWeight[];
    private double weaponEnchantChance;
    private EquipEnchantment weaponEnchant[];

    private Random random;

    public EquimentRandomizer(Plugin plugin){
        this.plugin = plugin;
        this.random = new Random();
        FileConfiguration config = plugin.getConfig();
        
        armorMaterialWeight = new int[]{
            config.getInt("settings.armor.materialWeight.diamond"),
            config.getInt("settings.armor.materialWeight.iron"),
            config.getInt("settings.armor.materialWeight.golden"),
            config.getInt("settings.armor.materialWeight.leather"),
            config.getInt("settings.armor.materialWeight.default")

        };
        armorEnchantChance = config.getDouble("settings.armor.enchantentChance.enchantChance");
        armorEnchant = new EquipEnchantment[]{
            new EquipEnchantment( Enchantment.PROTECTION_ENVIRONMENTAL, 4, config.getDouble("settings.armor.enchantentChance.protection") ),
            new EquipEnchantment( Enchantment.PROTECTION_FIRE,          4, config.getDouble("settings.armor.enchantentChance.protectionFire") ),
            new EquipEnchantment( Enchantment.PROTECTION_EXPLOSIONS,    4, config.getDouble("settings.armor.enchantentChance.protectionProjectile") ),
            new EquipEnchantment( Enchantment.THORNS,                   3, config.getDouble("settings.armor.enchantentChance.thorns") ),
            new EquipEnchantment( Enchantment.DURABILITY,               2, config.getDouble("settings.armor.enchantentChance.durability") )
        };

        for(int i = 0; i < armorMaterialWeight.length; i++){
            this.armorTotalWeight += armorMaterialWeight[i];
        }
        //--------------------------------------------------------------------
        weaponMaterialWeight = new int[]{
            config.getInt("settings.weapon.materialWeight.diamond"),
            config.getInt("settings.weapon.materialWeight.iron"),
            config.getInt("settings.weapon.materialWeight.golden"),
            config.getInt("settings.weapon.materialWeight.stone"),
            config.getInt("settings.weapon.materialWeight.wooden")
        };
        weaponTypeWeight = new int[]{
            config.getInt("settings.weapon.typeWeight.axe"),
            config.getInt("settings.weapon.typeWeight.sword"),
            config.getInt("settings.weapon.typeWeight.shovel"),
            config.getInt("settings.weapon.typeWeight.default")

        };
        weaponEnchantChance = config.getDouble("settings.weapon.enchantentChance.enchantChance");
        weaponEnchant = new EquipEnchantment[]{
            new EquipEnchantment( Enchantment.DAMAGE_ALL,       5, config.getDouble("settings.weapon.enchantentChance.damage") ),
            new EquipEnchantment( Enchantment.DIG_SPEED,        3, config.getDouble("settings.weapon.enchantentChance.digSpeed") ),
            new EquipEnchantment( Enchantment.LOOT_BONUS_MOBS,  3, config.getDouble("settings.weapon.enchantentChance.looting") ),
            new EquipEnchantment( Enchantment.KNOCKBACK,        2, config.getDouble("settings.weapon.enchantentChance.knockback") ),
            new EquipEnchantment( Enchantment.FIRE_ASPECT,      2, config.getDouble("settings.weapon.enchantentChance.fire") ),
            new EquipEnchantment( Enchantment.DURABILITY,       2, config.getDouble("settings.weapon.enchantentChance.durability") ),
        };

        for(int i = 0; i < weaponMaterialWeight.length; i++){
            this.weaponMaterialTotalWeight += weaponMaterialWeight[i];
        }
        for(int i = 0; i < weaponTypeWeight.length; i++){
            this.weaponTypeTotalWeight += weaponTypeWeight[i];
        }
    }

    private int nextGaussian(int min, int max){
        double temp = Math.abs( random.nextGaussian() * 0.5 );
        return Math.min( (int)Math.ceil( temp * max + min ), max);
    }

    private int chance( int totalWeight,  int [] values ){
        int target = random.nextInt( totalWeight );
        int count = 0, nowSum = 0;
        for (int i : values) {
            if( target < (nowSum += i)  ){
                return count;
            }
            count += 1;
        }
        return values.length - 1;
    }

    public Armor nextArmor(){

        ItemStack equipments[] = new ItemStack[4];

        int type = chance( armorTotalWeight, armorMaterialWeight );
        if( type == (armorMaterialWeight.length - 1) )
            return new Armor( Material.AIR, equipments) ;
        
        String armorType = ARMOR_MATERIAL[ type ];
        for(int i = 0, size = equipments.length; i < size; i += 1){
            equipments[i] = new ItemStack( Material.getMaterial( String.join("_", armorType, ARMOR_NAME[ i ] ) ) );
            int itemQuality = 0;
            for (EquipEnchantment enchant : armorEnchant ) {
                if( random.nextDouble() <= armorEnchantChance ){
                    if( random.nextDouble() <= enchant.getChance() ){
                        int lvl = nextGaussian(0, enchant.getMaxLevel() );
                        try {
                            equipments[i].addEnchantment( enchant.getEnchantment(), lvl );
                            itemQuality += lvl;
                        } catch (IllegalArgumentException e) {
                            continue;
                        }
                    }
                }
            }
            increaseReapirCount( equipments[i], itemQuality * 100 );
        }


        switch ( armorType ) {
            case "DIAMOND": 
                return new Armor( Material.DIAMOND, equipments) ;
            case "IRON": 
                return new Armor( Material.IRON_INGOT, equipments) ;
            case "GOLDEN":
                return new Armor( Material.GOLD_INGOT, equipments) ;       
            case "LEATHER":
                return new Armor( Material.LEATHER, equipments) ;    
            default:
                return new Armor( Material.AIR, equipments) ;
        }
    }


    private void increaseReapirCount(ItemStack item, int x) {
        if (x == 0) 
            return;
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Repairable) {
            Repairable r = (Repairable) meta;
            int count = r.getRepairCost() + x;
            if (count < 0) count = 0;
            r.setRepairCost(count);
            item.setItemMeta(meta);
        }
    }
     
    public Weapon nextWeapon(){
        ItemStack weapon = null;

        int weaponType = chance( weaponTypeTotalWeight, weaponTypeWeight );
        if( weaponType == ( weaponTypeWeight.length - 1 ) )
            return new Weapon( Material.AIR, weapon);

        int material = chance( weaponMaterialTotalWeight, weaponMaterialWeight );
        String weaponMaterial = WEAPON_MATERIAL[ material ];

        weapon = new ItemStack( Material.getMaterial( String.join("_", weaponMaterial, WEAPON_NAME[ weaponType ] ) ) );
        int itemQuality = 0;
        for (EquipEnchantment enchant : weaponEnchant) {
            if( random.nextDouble() <= weaponEnchantChance ){
                if( random.nextDouble() <= enchant.getChance() ){
                    int lvl = nextGaussian(0, enchant.getMaxLevel() );
                    try {
                        weapon.addEnchantment( enchant.getEnchantment(), lvl );
                        itemQuality += lvl;
                    } catch (IllegalArgumentException e) {
                        continue;
                    }
                }
            }
        }
        increaseReapirCount( weapon, itemQuality * 100 );
        
        
        switch ( weaponMaterial ) {
            case "DIAMOND": 
                return new Weapon( Material.DIAMOND, weapon) ;
            case "IRON": 
                return new Weapon( Material.IRON_INGOT, weapon) ;
            case "GOLDEN":
                return new Weapon( Material.GOLD_INGOT, weapon) ;       
            case "STONE":
                return new Weapon( Material.STONE, weapon) ;    
            case "WOODEN":
                return new Weapon( Material.OAK_PLANKS, weapon) ;    
            default:
                return new Weapon( Material.AIR, weapon) ;
        }
    }
}

class EquipEnchantment{
    private Enchantment enchantment;
    private double chance;
    private int maxLevel;
    public EquipEnchantment(Enchantment enchantment, int maxLevel, double chance){
        this.enchantment = enchantment;
        this.maxLevel   = maxLevel;
        this.chance     = chance;
    }
    public double getChance() {
        return chance;
    }
    public Enchantment getEnchantment() {
        return enchantment;
    }
    public int getMaxLevel() {
        return maxLevel;
    }
}
