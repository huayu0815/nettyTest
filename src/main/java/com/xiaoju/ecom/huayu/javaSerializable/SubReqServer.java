package com.xiaoju.ecom.huayu.javaSerializable;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by zhaohuayu on 16/9/9.
 */
public class SubReqServer {

    public void bind(int port) throws Exception {
        //配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();//接收客户端的tcp链接
        EventLoopGroup workerGroup = new NioEventLoopGroup();//处理I/O操作或定时task

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch) {
                            /**
                             * 基于event,顺序调用。分为InBound Event(从流获取数据)和OutBound Event(往流写入数据).
                             * addLast()讲handler放到List中并且顺序调用。如果下面的第1和第3调换位置就会报错
                             * java.lang.ClassCastException: io.netty.buffer.SimpleLeakAwareByteBuf cannot be cast to com.xiaoju.ecom.huayu.javaSerializable.SubscribeReq
                             * 默认冲流获取数据为SimpleLeakAwareByteBuf,第3步放的InBoundHandler处理的是SubscribeReq。
                             * 所以必须先经过第1步放的ObjectDecoder讲流解码为SubscribeReq
                             * 同时,对于OutBound Event,按照添加的Handler逆向顺序处理。所以,如果1,2调换位置是没有问题的,但是
                             * 如果2,3调换位置,对于demo的client请求,server可以获取数据,但是3进行写的对象数据,没有经过ObjectEncode,
                             * 写的数据client的ObjectDecode匹配不上,也就获取不到数据.但是,如果使用channel或是pipeLine进行写,会从最底层
                             * handler写起,就没有这个问题
                             */
                            ch.pipeline().addLast(new ObjectDecoder(1024*1024,
                                    ClassResolvers.weakCachingConcurrentResolver(
                                            this.getClass().getClassLoader())));

                            ch.pipeline().addLast(new ObjectEncoder()) ;


                            ch.pipeline().addLast(new SubReqServerHandler()) ;//多个handle之间串行执行,避免锁问题

                        }
                    });
            //绑定端口,同步等待成功
            ChannelFuture f = b.bind(port).sync();

            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();

        } finally {
            //优雅退出,释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {

            }
        }

        new SubReqServer().bind(port);
    }
}
