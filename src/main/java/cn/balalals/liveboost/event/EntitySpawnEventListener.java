package cn.balalals.liveboost.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawnEventListener implements Listener {
    @EventHandler
    public void handlerEvent(EntitySpawnEvent event) {
        System.out.println("EntitySpawnEventListener：" + event.getEntity().getCustomName());
        System.out.println("EntitySpawnEventListener: " + event.getEntity().getName());
    }

}
