package com.xiaoju.ecom.huayu.javaSerializable;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by zhaohuayu on 16/9/18.
 */
public class SubReqClientHandler extends ChannelInboundHandlerAdapter {

    public SubReqClientHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i=0;i<10;i++) {
            ctx.write(subReq(i));
        }
        ctx.flush();
    }

    private SubscribeReq subReq(long i) {
        SubscribeReq req = new SubscribeReq();
        req.setAddress("华龙苑南里");
        req.setPhoneNumber("110");
        req.setProductName("Netty 权威指南");
        req.setSubReqId(i);
        req.setUserName("zhaohuayu");
        return  req;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Receive server response:[" + msg + "]");
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
