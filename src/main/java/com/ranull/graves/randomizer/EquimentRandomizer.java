package com.ranull.graves.randomizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EquimentRandomizer {
    private Plugin plugin;

    private static String ARMOR_TYPE[]  = {"DIAMOND", "IRON", "GOLDEN", "LEATHER", "DEFAULT"};
    private static String ARMOR_NAME[]  = {"BOOTS", "LEGGINGS", "CHESTPLATE", "HELMET"};
    private static Material WEAPON[]    = {};
    private int armorTotalWeight = 100;
    private int armorTypeWeight[];
    private double armorEnchantChance[];
    public EquimentRandomizer(Plugin plugin){
        this.plugin = plugin;
        armorTypeWeight = new int[]{
            plugin.getConfig().getInt("settings.armorTypeWeight.diamond"),
            plugin.getConfig().getInt("settings.armorTypeWeight.iron"),
            plugin.getConfig().getInt("settings.armorTypeWeight.golden"),
            plugin.getConfig().getInt("settings.armorTypeWeight.leather"),
            plugin.getConfig().getInt("settings.armorTypeWeight.default")
        };
        armorEnchantChance = new double[]{
            plugin.getConfig().getDouble("settings.armorEnchantentChance.enchantChance"),
            plugin.getConfig().getDouble("settings.armorEnchantentChance.protection"),
            plugin.getConfig().getDouble("settings.armorEnchantentChance.protectionFire"),
            plugin.getConfig().getDouble("settings.armorEnchantentChance.protectionProjectile"),
            plugin.getConfig().getDouble("settings.armorEnchantentChance.thorns"),
            plugin.getConfig().getDouble("settings.armorEnchantentChance.durability")
        };

        int i = 0;
        for( Map<?, ?> weapon : plugin.getConfig().getMapList("setting.weapon") ){
            Map.Entry<?, ?> entry = weapon.entrySet().iterator().next();
            WEAPON[i] = Material.getMaterial( Objects.toString( entry.getKey() )  );
            
        }
    }
    public Armor nextArmor(){

        ItemStack equipments[] = new ItemStack[4];

        int type = chance( armorTypeWeight );
        if( type == 4 )
            return new Armor( Material.AIR, equipments) ;
        
        String armorType = ARMOR_TYPE[ type ];
        for(int i = 0, size = equipments.length; i < size; i += 1){
            equipments[i] = new ItemStack( Material.getMaterial( String.join("_", armorType, ARMOR_NAME[ i ] ) ) );
            equipments[i].addEnchantments( getArmorEnchantent( armorEnchantChance ) );
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
    private int chance( int ... values ){
        int target = new Random().nextInt( armorTotalWeight );
        int count = 0, nowSum = 0;
        for (int i : values) {
            if( target < (nowSum += i)  ){
                return count;
            }
            count += 1;
        }
        return 0;
    }
    private Map<Enchantment, Integer> getArmorEnchantent( double ... values ){
        Random random = new Random();
        int level = 0;
        Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();

        if( random.nextDouble() >= values[0] ){

            if( random.nextDouble() <  values[0] && (level = random.nextInt( 5 )) != 0 )
                enchants.put( Enchantment.PROTECTION_ENVIRONMENTAL , level );
        
            if( random.nextDouble() <  values[1] && (level = random.nextInt( 5 )) != 0 )
                enchants.put( Enchantment.PROTECTION_PROJECTILE , level );
            
            if( random.nextDouble() <  values[2] && (level = random.nextInt( 5 )) != 0 )
                enchants.put( Enchantment.PROTECTION_FIRE , level );

            if( random.nextDouble() <  values[3] && (level = random.nextInt( 4 )) != 0 )
                enchants.put( Enchantment.THORNS , level );
            
            if( random.nextDouble() <  values[4] && (level = random.nextInt( 4 )) != 0 )
                enchants.put( Enchantment.DURABILITY , level );
        }


        return enchants;
    }
}
