package net.edge.test.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.edge.util.TextUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 6-7-2017.
 */
public final class FakeClient {
    private static final BigInteger RSA_MODULUS = new BigInteger("94306533927366675756465748344550949689550982334568289470527341681445613288505954291473168510012417401156971344988779343797488043615702971738296505168869556915772193568338164756326915583511871429998053169912492097791139829802309908513249248934714848531624001166946082342750924060600795950241816621880914628143");
    private static final BigInteger RSA_EXPONENT = new BigInteger("65537");
    
    private static AtomicInteger count = new AtomicInteger();
    
    public static void main(String[] args) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        SecureRandom random = new SecureRandom();

        Bootstrap b = new Bootstrap(); // (1)
        b.group(workerGroup); // (2)
        b.channel(NioSocketChannel.class); // (3)
        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("handler", new ByteToMessageDecoder() {
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        cause.printStackTrace();
                    }

                    private boolean loggedIn = false;

                    @Override
                    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
                        if (!loggedIn) {
                            ByteBuf rsa = ctx.alloc().buffer();
                            
                            // rsa block prefix
                            rsa.writeByte(10);

                            // session keys
                            rsa.writeInt(random.nextInt());
                            rsa.writeInt(random.nextInt());
                            rsa.writeInt(random.nextInt());
                            rsa.writeInt(random.nextInt());

                            //rsa.writeInt(0); // uid
                            //rsa.putCString("Bot" + count.get());
                            rsa.putCString("123456");

                            byte[] rsaBytes = new byte[rsa.readableBytes()];
                            rsa.readBytes(rsaBytes);
    
                            ByteBuf payload = ctx.alloc().buffer();

                            payload.writeByte(rsaBytes.length); // rsa block size
                            payload.writeBytes(rsaBytes);
    
                            ByteBuf out = ctx.alloc().buffer();
                            //out.writeByte(16); // connection type
                            out.writeByte(payload.readableBytes()); // payload size
                            out.writeBytes(payload);

                            ctx.writeAndFlush(out, ctx.voidPromise());
                            loggedIn = true;
                            count.incrementAndGet();
                        } else {
                            int pktId = in.readUnsignedByte();
                            System.out.println("received pkt id: " + pktId);
                        }
                    }
                });
            }
        });

        for (int i = 0; i < 400; i++) {
            // Start the client.
            System.out.println("connecting bot " + i);
            Channel f = b.connect("127.0.0.1", 43594).sync().channel(); //(5)
            ByteBuf buffer = f.alloc().buffer();
            buffer.writeShort(37);
            buffer.writeInt(37);
            buffer.writeLong(encryptName("Bot" + count.get()));
            f.writeAndFlush(buffer, f.voidPromise());
        }
    }
    
    /**
     * Encrypts a long value.
     */
    public static long encryptName(String name) {
        long l = 0L;
        for(int i = 0; i < name.length() && i <= 12; i++) {
            char c = name.charAt(i);
            l *= 37L;
            if(c >= 'A' && c <= 'Z')
                l += (1 + c) - 65;
            else if(c >= 'a' && c <= 'z')
                l += (1 + c) - 97;
            else if(c >= '0' && c <= '9')
                l += (27 + c) - 48;
        }
        while(l % 37L == 0L && l != 0L)
            l /= 37L;
        return l;
    }
}