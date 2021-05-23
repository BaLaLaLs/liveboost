package handler;

import cn.balalals.liveboost.LiveBoost;
import cn.balalals.liveboost.bean.EntryRoomMessage;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class EntryRoomHandler implements IMessageHandler<EntryRoomMessage>{

    @Override
    public void handler(EntryRoomMessage msg) {
        LiveBoost.PLUGIN.getServer().broadcastMessage("[直播信息] 欢迎"+ msg.getUname() + "进入直播间！");
        LiveBoost.PLUGIN.getServer().getWorld("world").getPlayers().forEach(p -> {
            Location location = p.getLocation();
            location.add(getRandomNumber(10,4),getRandomNumber(11,5),0);
            p.getWorld().spawnEntity(location, EntityType.ZOMBIE).setCustomName(msg.getUname());
        });
        System.out.println("EntryRoomHandler:" + msg);
    }
    private int getRandomNumber(Integer max, Integer min) {
        return min + (int)(Math.random() * (max-min+1));
    }
}
