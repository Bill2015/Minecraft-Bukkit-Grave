package com.ranull.graves.randomizer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Armor {
    final private Material ArmorType;
    final private ItemStack[] armor;
    public Armor( Material ArmorType, ItemStack[] armor ){
        this.ArmorType  = ArmorType;
        this.armor      = armor;
    }
    public ItemStack[] getArmor() {
        return armor;
    }
    public Material getArmorType() {
        return ArmorType;
    }
}
