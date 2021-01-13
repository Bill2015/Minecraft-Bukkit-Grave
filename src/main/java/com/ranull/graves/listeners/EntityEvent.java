package com.ranull.graves.listeners;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Media;

import com.ranull.graves.Graves;
import com.ranull.graves.randomizer.Armor;
import com.ranull.graves.randomizer.EquimentRandomizer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

// Just Make creeper destroy the torch when explsion
public class EntityEvent implements Listener{
    private Graves plugin;
    private EquimentRandomizer armorRandomizer;
    private float[] armorDropChance;
    public EntityEvent(Graves plugin) {
        this.plugin = plugin;
        this.armorRandomizer = new EquimentRandomizer( plugin );
        this.armorDropChance = new float[]{
            (float)plugin.getConfig().getDouble("setting.armorDropChance.diamond"),
            (float)plugin.getConfig().getDouble("setting.armorDropChance.iron"),
            (float)plugin.getConfig().getDouble("setting.armorDropChance.golden"),
            (float)plugin.getConfig().getDouble("setting.armorDropChance.leather"),
        };
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onZombieSpawn( CreatureSpawnEvent event ){
        if (event.isCancelled()) 
            return;
        if( event.getEntityType() == EntityType.ZOMBIE ){
            Zombie zombie = (Zombie)event.getEntity();

            Armor armor = armorRandomizer.nextArmor();
            zombie.getEquipment().setArmorContents( armor.getArmor() );
            if( armor.getArmorType() == Material.DIAMOND ){
                zombie.getEquipment().setChestplateDropChance( armorDropChance[0] );
                zombie.getEquipment().setBootsDropChance( armorDropChance[0] );
                zombie.getEquipment().setLeggingsDropChance( armorDropChance[0] );
                zombie.getEquipment().setHelmetDropChance( armorDropChance[0] );
            }
            else if( armor.getArmorType() == Material.IRON_INGOT ){
                zombie.getEquipment().setChestplateDropChance( armorDropChance[1] );
                zombie.getEquipment().setBootsDropChance( armorDropChance[1] );
                zombie.getEquipment().setLeggingsDropChance( armorDropChance[1] );
                zombie.getEquipment().setHelmetDropChance( armorDropChance[1] );
            }
            else if( armor.getArmorType() == Material.GOLD_INGOT ){
                zombie.getEquipment().setChestplateDropChance( armorDropChance[2] );
                zombie.getEquipment().setBootsDropChance( armorDropChance[2] );
                zombie.getEquipment().setLeggingsDropChance( armorDropChance[2] );
                zombie.getEquipment().setHelmetDropChance( armorDropChance[2] );
            }
            else if( armor.getArmorType() == Material.LEATHER ){
                zombie.getEquipment().setChestplateDropChance( armorDropChance[3] );
                zombie.getEquipment().setBootsDropChance( armorDropChance[3] );
                zombie.getEquipment().setLeggingsDropChance( armorDropChance[3] );
                zombie.getEquipment().setHelmetDropChance( armorDropChance[3] );
            }

        } 
    }

    @EventHandler
    public void onEntityExplosion( EntityExplodeEvent event ){
        if (event.isCancelled()) 
            return;

        if( event.getEntity() instanceof Creeper ){
            Creeper creeper = (Creeper)event.getEntity();
            int radius = plugin.getConfig().getInt( "settings.creeperExplosionRadius" );
            List<Block> blockList = EntityEvent.getNearbyBlocks( creeper.getLocation(), radius );

            for (Block block : blockList) {
                Material material = block.getType();
                if( material == Material.TORCH ||  material == Material.WALL_TORCH || 
                    material == Material.SOUL_TORCH || material == Material.SOUL_WALL_TORCH  ){
                        
                    block.breakNaturally();
                }
            }
        }
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                   blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
}
