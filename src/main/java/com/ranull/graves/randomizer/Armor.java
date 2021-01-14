package com.ranull.graves.randomizer;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class Armor {
    final private Material armorMaterial;
    final private ItemStack[] armor;
    public Armor( Material ArmorType, ItemStack[] armor ){
        this.armorMaterial  = ArmorType;
        this.armor      = armor;
    }
    public ItemStack[] getArmor() {
        return armor;
    }
    public Material getArmorType() {
        return armorMaterial;
    }
    public void setWearingEntity( LivingEntity entity ){
        entity.getEquipment().setArmorContents( armor );
    }
    public void setDropChance( LivingEntity entity, float [] value ){
        if( armorMaterial == Material.DIAMOND ){
            entity.getEquipment().setChestplateDropChance( value[0] );
            entity.getEquipment().setBootsDropChance( value[0] );
            entity.getEquipment().setLeggingsDropChance( value[0] );
            entity.getEquipment().setHelmetDropChance( value[0] );
        }
        else if( armorMaterial == Material.IRON_INGOT ){
            entity.getEquipment().setChestplateDropChance( value[1] );
            entity.getEquipment().setBootsDropChance( value[1] );
            entity.getEquipment().setLeggingsDropChance( value[1] );
            entity.getEquipment().setHelmetDropChance( value[1] );
        }
        else if( armorMaterial == Material.GOLD_INGOT ){
            entity.getEquipment().setChestplateDropChance( value[2] );
            entity.getEquipment().setBootsDropChance( value[2] );
            entity.getEquipment().setLeggingsDropChance( value[2] );
            entity.getEquipment().setHelmetDropChance( value[2] );
        }
        else if( armorMaterial == Material.LEATHER ){
            entity.getEquipment().setChestplateDropChance( value[3] );
            entity.getEquipment().setBootsDropChance( value[3] );
            entity.getEquipment().setLeggingsDropChance( value[3] );
            entity.getEquipment().setHelmetDropChance( value[3] );
        }
    }
}
