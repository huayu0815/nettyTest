package com.xiaoju.ecom.huayu.javaSerializable;

import io.netty.channel.*;

/**
 * Created by zhaohuayu on 16/9/9.
 */
@ChannelHandler.Sharable
public class SubReqServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReq req = (SubscribeReq)msg;
        if ("zhaohuayu".equalsIgnoreCase(req.getUserName())) {
            System.out.println("Service accept client subscribe req:[" + req.toString() + "]");
            //ctx.writeAndFlush(resp(req.getSubReqId())) ;
            /**
             * channel或是channelPipeline调用write,会从handler列表的最底层调用
             * 是ChannelHandlerContext,会调用下一个handler进行处理
             * ChannelHandlerContext是线程安全的,并且可以缓存起来进行后续调用
             */
            Channel channel = ctx.channel() ;
            channel.writeAndFlush(resp(req.getSubReqId())) ;
        }
    }

    private SubscribeResp resp(long subReqID) {
        SubscribeResp resp = new SubscribeResp() ;
        resp.setSubReqID(subReqID);
        resp.setRespCode(0);
        resp.setDesc("success");
        return resp;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close(); //发生异常,关闭链路
    }
}
