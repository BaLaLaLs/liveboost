package cn.balalals.liveboost.handler;

import cn.balalals.liveboost.LiveBoost;
import cn.balalals.liveboost.bean.GiftMessage;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import static cn.balalals.liveboost.util.Utils.getRandomNumber;

public class GiftMsgHandler implements IMessageHandler<GiftMessage>{
    @Override
    public void handler(GiftMessage msg) {
        StringBuffer subMsg = new StringBuffer();
        if(msg.getGiftName().equals("辣条")) {
            LiveBoost.PLUGIN.getServer().getWorld("world").getPlayers().forEach(p -> {
                Location location = p.getLocation();
                location.add(getRandomNumber(-5,5),getRandomNumber(-6,6),0);
                p.getWorld().spawnEntity(location, EntityType.CREEPER).setCustomName(msg.getUname());
                subMsg.append("苦力怕觉得很赞！");
            });
        }
        LiveBoost.PLUGIN.getServer().broadcastMessage(String.format("[直播信息] 谢谢"+ msg.getUname() + "的辣条！%s",subMsg));
    }
}
