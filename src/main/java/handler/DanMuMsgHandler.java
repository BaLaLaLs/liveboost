package handler;

import cn.balalals.liveboost.LiveBoost;
import cn.balalals.liveboost.bean.DanMuMessage;

public class DanMuMsgHandler implements IMessageHandler<DanMuMessage> {
    @Override
    public void handler(DanMuMessage msg) {
        LiveBoost.PLUGIN.getServer().broadcastMessage(String.format("<%s> %s",msg.getUname(),msg.getMsg()));
    }
}
