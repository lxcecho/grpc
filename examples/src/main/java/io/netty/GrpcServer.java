package io.netty;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.netty.service.PersonServiceImpl;

/**
 * @author lxcecho 909231497@qq.com
 * @since 22:50 12-11-2022
 */
public class GrpcServer {

    private Server server;

    private void start() throws Exception {
        this.server = ServerBuilder.forPort(8090).addService(new PersonServiceImpl()).build().start();
        System.out.println("Server Started!");

        /**
         * 触发方式：
         * 1. 程序正常退出
         * 2. 用户中断
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Close JVM");
            GrpcServer.this.stop();
        }));
        System.out.println("END");
    }

    private void stop() {
        if (null != this.server) {
            this.server.shutdown();
        }
    }

    private void awaitTermination() throws Exception {
        if (null != this.server) {
            this.server.awaitTermination();
            // 超过 3s server 端退出
//            this.server.awaitTermination(3, TimeUnit.SECONDS);
        }
    }

    public static void main(String[] args) throws Exception {
        GrpcServer grpcServer = new GrpcServer();

        grpcServer.start();
        grpcServer.awaitTermination();
    }

}
