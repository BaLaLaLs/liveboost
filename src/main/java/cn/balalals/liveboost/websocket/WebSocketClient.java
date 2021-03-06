package cn.balalals.liveboost.websocket;

import cn.balalals.liveboost.util.BiliBiliRoomId;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.EmptyHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static cn.balalals.liveboost.BiliBiliConstants.*;

public class WebSocketClient {

    public static final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor();
    private final Integer roomId;
    private Channel channel;
    private ScheduledFuture<?> heartScheduledFuture;

    public WebSocketClient(Integer roomId) {
        this.roomId = roomId;
    }
    public void open() throws SSLException, InterruptedException {
        String uri = "wss://broadcastlv.chat.bilibili.com:443/sub";
        open(uri);
    }
    public void open(String uriStr) throws SSLException, InterruptedException {
        URI uri = URI.create(uriStr);
        Bootstrap bootstrap = new Bootstrap();
        SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        EventLoopGroup group = new NioEventLoopGroup();
        WebSocketClientHandshaker handShaker = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13,
                null, false, EmptyHttpHeaders.INSTANCE);
        WebSocketClientHandler handler = new WebSocketClientHandler(handShaker);

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipe = ch.pipeline();
                        pipe.addLast(
                                sslCtx.newHandler(ch.alloc(), uri.getHost(), uri.getPort()),
                                new HttpClientCodec(),
                                new HttpObjectAggregator(8192),
                                handler
                        );
                    }
                });

        channel = bootstrap.connect(uri.getHost(), uri.getPort()).sync().channel();
        handler.handshakeFuture().sync();
        // ????????????
        final int realRoomId = BiliBiliRoomId.getRealRoomId(roomId);
        if (realRoomId == -1) {
            Logger.getLogger("FUCKPLUGIN:"+"????????????????????????????????????????????????????????????????????????");
        } else {
            Logger.getLogger("FUCKPLUGIN:"+"??????????????????????????????????????????");
        }
        byte[] message = String.format("{\"roomid\": %d}", realRoomId).getBytes(StandardCharsets.UTF_8);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(HEADER_LENGTH + message.length);
        buf.writeShort(HEADER_LENGTH);
        buf.writeShort(BUFFER_PROTOCOL_VERSION);
        buf.writeInt(ENTER_ROOM_OPERATION);
        buf.writeInt(SEQUENCE_ID);
        buf.writeBytes(message);
        sendMessage(buf);

        heartScheduledFuture = SERVICE.scheduleAtFixedRate(() -> sendMessage(getHeartBeat()),
                30 * 1000, 30 * 1000, TimeUnit.MILLISECONDS);
    }

    public void close() throws InterruptedException {
        channel.writeAndFlush(new CloseWebSocketFrame());
        channel.closeFuture().sync();
        heartScheduledFuture.cancel(true);
    }

    public void sendMessage(final ByteBuf binaryData) {
        channel.writeAndFlush(new BinaryWebSocketFrame(binaryData));
    }
    public ByteBuf getHeartBeat() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(HEADER_LENGTH);
        buf.writeShort(HEADER_LENGTH);
        buf.writeShort(BUFFER_PROTOCOL_VERSION);
        buf.writeInt(HEART_BEAT_OPERATION);
        buf.writeInt(SEQUENCE_ID);
        return buf;
    }
}
