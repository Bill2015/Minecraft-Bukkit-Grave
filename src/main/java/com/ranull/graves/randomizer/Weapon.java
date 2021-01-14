package com.ranull.graves.randomizer;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class Weapon {
    final private Material weaponMaterial;
    final private ItemStack weapon;
    public Weapon( Material weaponType, ItemStack weapon ){
        this.weaponMaterial     = weaponType;
        this.weapon             = weapon;
    }
    public ItemStack getWeapon() {
        return weapon;
    }
    public Material getWeaponType() {
        return weaponMaterial;
    }
    public void setWearingEntity( LivingEntity entity ){
        entity.getEquipment().setItemInMainHand( weapon );
    }
    public void setDropChance( LivingEntity entity, float [] value ){
        if( weaponMaterial == Material.DIAMOND ){
            entity.getEquipment().setItemInMainHandDropChance( value[0] );
        }
        else if( weaponMaterial == Material.IRON_INGOT ){
            entity.getEquipment().setItemInMainHandDropChance( value[1] );
        }
        else if( weaponMaterial == Material.GOLD_INGOT ){
            entity.getEquipment().setItemInMainHandDropChance( value[2] );
        }
        else if( weaponMaterial == Material.STONE ){
            entity.getEquipment().setItemInMainHandDropChance( value[3] );
        }  
        else if( weaponMaterial == Material.OAK_PLANKS ){
            entity.getEquipment().setItemInMainHandDropChance( value[4] );
        }
    }
}
