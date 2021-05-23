package cn.balalals.liveboost.event;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakEventListener implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent blockBreakEvent) {
        blockBreakEvent.getBlock().setType(Material.AIR);
        blockBreakEvent.setDropItems(false);

    }
}
