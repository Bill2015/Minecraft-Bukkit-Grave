package com.ranull.graves.listeners;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Media;

import com.ranull.graves.Graves;
import com.ranull.graves.randomizer.Armor;
import com.ranull.graves.randomizer.EquimentRandomizer;
import com.ranull.graves.randomizer.Weapon;

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
    private final EquimentRandomizer equipRandomizer;
    private final float[] armorDropChance;
    private final float[] weaponDropChance;

    private final float creeperExplsionRadius;
    public EntityEvent(Graves plugin) {
        this.plugin = plugin;
        this.equipRandomizer = new EquimentRandomizer( plugin );
        this.creeperExplsionRadius = plugin.getConfig().getInt( "settings.creeperExplosionRadiusMutiple" );
        this.armorDropChance = new float[]{
            (float)plugin.getConfig().getDouble("settings.armor.dropChance.diamond"),
            (float)plugin.getConfig().getDouble("settings.armor.dropChance.iron"),
            (float)plugin.getConfig().getDouble("settings.armor.dropChance.golden"),
            (float)plugin.getConfig().getDouble("settings.armor.dropChance.leather")
        };
        this.weaponDropChance = new float[]{
            (float)plugin.getConfig().getDouble("settings.weapon.dropChance.diamond"),
            (float)plugin.getConfig().getDouble("settings.weapon.dropChance.iron"),
            (float)plugin.getConfig().getDouble("settings.weapon.dropChance.golden"),
            (float)plugin.getConfig().getDouble("settings.weapon.dropChance.stone"),
            (float)plugin.getConfig().getDouble("settings.weapon.dropChance.wooden")
        };
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onZombieSpawn( CreatureSpawnEvent event ){
        if (event.isCancelled()) 
            return;
        if( event.getEntityType() == EntityType.ZOMBIE ){                
            Zombie zombie = (Zombie)event.getEntity();

            if( zombie.getCustomName() == null || zombie.getCustomName().equals("") ){

                Armor armor = equipRandomizer.nextArmor();
                armor.setWearingEntity( zombie );
                armor.setDropChance( zombie , armorDropChance );
                Weapon weapon = equipRandomizer.nextWeapon();
                weapon.setWearingEntity( zombie );
                weapon.setDropChance( zombie , weaponDropChance );

            }

        } 
    }

    @EventHandler
    public void onEntityExplosion( EntityExplodeEvent event ){
        if (event.isCancelled()) 
            return;

        if( event.getEntity() instanceof Creeper ){
            Creeper creeper = (Creeper)event.getEntity();
            int radius = (int)(creeper.getExplosionRadius() * creeperExplsionRadius);
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
