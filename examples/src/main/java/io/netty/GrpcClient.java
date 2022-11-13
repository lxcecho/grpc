package io.netty;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.netty.proto.*;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author lxcecho 909231497@qq.com
 * @since 23:05 12-11-2022
 */
public class GrpcClient {

    public static void main(String[] args) {

        System.out.println("---------CASE 1------------");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8090).usePlaintext().build();
        PersonServiceGrpc.PersonServiceBlockingStub personServiceBlockingStub = PersonServiceGrpc.newBlockingStub(channel);

        MyResponse response = personServiceBlockingStub.getRealNameByUsername(MyRequest.newBuilder().setUsername("AZAKI").build());
        System.out.println("RealName: " + response.getRealName());

        System.out.println("---------CASE 2------------");

        Iterator<PersonResponse> iterator = personServiceBlockingStub.getPersonByAge(PersonRequest.newBuilder().setAge(20).build());
        while (iterator.hasNext()) {
            PersonResponse resp = iterator.next();
            System.out.println(resp.getName() + " " + resp.getAge() + " " + resp.getAddress());
        }
        System.out.println("END");

        System.out.println("---------CASE 3------------");

        StreamObserver<PersonResponseList> personResponseListStreamObserver = new StreamObserver<PersonResponseList>() {
            @Override
            public void onNext(PersonResponseList value) {
                value.getPersonResponseList().forEach(response -> {
                    System.out.println(response.getName() + " " + response.getAge() + " " + response.getAddress());
                });
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Client onCompleted");
            }
        };

        // 异步调用
        PersonServiceGrpc.PersonServiceStub personServiceStub = PersonServiceGrpc.newStub(channel);

        StreamObserver<PersonRequest> personRequestStreamObserver = personServiceStub.getPersonWrapperByAge(personResponseListStreamObserver);

        personRequestStreamObserver.onNext(PersonRequest.newBuilder().setAge(11).build());
        personRequestStreamObserver.onNext(PersonRequest.newBuilder().setAge(12).build());
        personRequestStreamObserver.onNext(PersonRequest.newBuilder().setAge(13).build());
        personRequestStreamObserver.onCompleted();

        System.out.println("---------CASE 4------------");

        StreamObserver<StreamRequest> streamRequestStreamObserver = personServiceStub.biTalk(new StreamObserver<StreamResponse>() {
            @Override
            public void onNext(StreamResponse value) {
                System.out.println(value.getResponseInfo());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }
        });

        for (int i = 0; i < 10; i++) {
            streamRequestStreamObserver.onNext(StreamRequest.newBuilder().setRequestInfo(LocalDateTime.now().toString()).build());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
