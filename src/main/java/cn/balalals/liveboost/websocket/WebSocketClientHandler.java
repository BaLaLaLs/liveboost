package cn.balalals.liveboost.websocket;

import cn.balalals.liveboost.bean.DanMuMessage;
import cn.balalals.liveboost.bean.EntryRoomMessage;
import cn.balalals.liveboost.util.BilibiliMsgSplit;
import cn.balalals.liveboost.util.Zlib;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import handler.DanMuMsgHandler;
import handler.EntryRoomHandler;
import handler.PopularHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;

import static cn.balalals.liveboost.BiliBiliConstants.*;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private final WebSocketClientHandshaker handShaker;
    private ChannelPromise handshakeFuture;
    public WebSocketClientHandler(WebSocketClientHandshaker handShaker) {
        this.handShaker = handShaker;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handShaker.handshake(ctx.channel());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handShaker.isHandshakeComplete()) {
            handShaker.finishHandshake(ch, (FullHttpResponse) msg);
            Logger.getLogger("WebSocketClientHandler").info("WebSocket Client connected!");
            handshakeFuture.setSuccess();
            return;
        }
        if (msg instanceof FullHttpResponse) {
            final FullHttpResponse response = (FullHttpResponse) msg;
            throw new Exception("Unexpected FullHttpResponse (getStatus=" + response.status() + ", content="
                    + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        final WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof BinaryWebSocketFrame) {
            ByteBuf data = frame.content();
            int protocol = data.getShort(PROTOCOL_VERSION_OFFSET);
            System.out.println("protocol:"+ protocol);
            switch (protocol) {
                case JSON_PROTOCOL_VERSION:
                    return;
                case POPULAR_PROTOCOL_VERSION:
                    //handPopularMessage(data);
                    return;
                case BUFFER_PROTOCOL_VERSION:
                    handBufferMessage(data);
                    return;
                default:
            }
        }
    }

    /**
     * 0x2 客户端请求人气值
     * 0x3 服务端返回人气值
     * 0x5 弹幕消息、礼物等等
     * 0x7 客户端请求连接
     * 0x8 服务端允许连接
     * @param data
     * @throws Exception
     */
    private void handBufferMessage(ByteBuf data) throws Exception {
        int packetLength = data.getInt(PACKET_LENGTH_OFFSET);
        int operation = data.getInt(OPERATION_OFFSET);
        System.out.println("operation:"+operation);
        if (operation == POPULAR_OPERATION) {
            new PopularHandler().handler(data.getInt(BODY_OFFSET));
            return;
        }

        if (operation == MESSAGE_OPERATION) {
            byte[] uncompressedData = new byte[packetLength - BODY_OFFSET];
            data.getBytes(BODY_OFFSET, uncompressedData);
            byte[] decompressData = Zlib.decompress(uncompressedData);
            byte[] msgBytes = Arrays.copyOfRange(decompressData, BODY_OFFSET, decompressData.length);
            String[] message = BilibiliMsgSplit.split(IOUtils.toString(msgBytes, StandardCharsets.UTF_8.toString()));
            for (String msg : message) {
                handStringMessage(msg);
            }
        }
    }
    private void handStringMessage(String message) {
        System.out.println("handStringMessage: "+ message);
        JSONObject jsonObject = JSON.parseObject(message);
        String cmd = jsonObject.getString("cmd");
        switch (cmd) {
            case "INTERACT_WORD":
                EntryRoomMessage entryRoomMessage = new EntryRoomMessage();
                entryRoomMessage.setUname(jsonObject.getJSONObject("data").getString("uname"));
                new EntryRoomHandler().handler(entryRoomMessage);
            case "DANMU_MSG":
                JSONArray info = jsonObject.getJSONArray("info");
                DanMuMessage danMuMessage = new DanMuMessage();
                danMuMessage.setMsg(info.getString(1));
                danMuMessage.setUname(info.getJSONArray(2).getString(1));
                new DanMuMsgHandler().handler(danMuMessage);
            default:
        }
    }
    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }
}
