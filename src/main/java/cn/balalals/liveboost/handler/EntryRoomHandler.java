package cn.balalals.liveboost.handler;

import cn.balalals.liveboost.LiveBoost;
import cn.balalals.liveboost.bean.EntryRoomMessage;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import static cn.balalals.liveboost.util.Utils.getRandomNumber;

public class EntryRoomHandler implements IMessageHandler<EntryRoomMessage>{

    @Override
    public void handler(EntryRoomMessage msg) {
        LiveBoost.PLUGIN.getServer().broadcastMessage("[直播信息] 欢迎"+ msg.getUname() + "进入直播间！");
        LiveBoost.PLUGIN.getServer().getWorld("world").getPlayers().forEach(p -> {
            Location location = p.getLocation();
            location.add(getRandomNumber(-5,5),getRandomNumber(-6,6),0);
            p.getWorld().spawnEntity(location, EntityType.ZOMBIE).setCustomName(msg.getUname());
        });
        System.out.println("EntryRoomHandler:" + msg);
    }

}
