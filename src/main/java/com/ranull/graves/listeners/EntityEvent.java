package com.ranull.graves.listeners;

import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Media;

import com.ranull.graves.Graves;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

// Just Make creeper destroy the torch when explsion
public class EntityEvent implements Listener{
    private Graves plugin;

    public EntityEvent(Graves plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityExplosion( EntityExplodeEvent event ){
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
