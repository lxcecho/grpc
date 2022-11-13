package io.netty.service;

import io.grpc.stub.StreamObserver;
import org.netty.proto.*;

import java.util.UUID;

/**
 * @author lxcecho 909231497@qq.com
 * @since 22:31 12-11-2022
 */
public class PersonServiceImpl extends PersonServiceGrpc.PersonServiceImplBase {

    @Override
    public void getRealNameByUsername(MyRequest request, StreamObserver<MyResponse> responseObserver) {
        System.out.println("Received From Client: " + request.getUsername());

        responseObserver.onNext(MyResponse.newBuilder().setRealName("LXCECHO").build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPersonByAge(PersonRequest request, StreamObserver<PersonResponse> responseObserver) {
        System.out.println("Received From Client: " + request.getAge());

        responseObserver.onNext(PersonResponse.newBuilder().setName("LXCECHO").setAge(20).setAddress("GuangXi").build());
        responseObserver.onNext(PersonResponse.newBuilder().setName("LXCECHO2").setAge(20).setAddress("GuangXi2").build());
        responseObserver.onNext(PersonResponse.newBuilder().setName("LXCECHO3").setAge(20).setAddress("GuangXi3").build());
        responseObserver.onNext(PersonResponse.newBuilder().setName("LXCECHO4").setAge(20).setAddress("GuangXi4").build());

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<PersonRequest> getPersonWrapperByAge(StreamObserver<PersonResponseList> responseObserver) {
        return new StreamObserver<PersonRequest>() {
            @Override
            public void onNext(PersonRequest value) {
                System.out.println("onNext: " + value.getAge());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                PersonResponse lxcecho = PersonResponse.newBuilder().setName("LXCECHO").setAge(20).setAddress("GuangXi").build();
                PersonResponse song = PersonResponse.newBuilder().setName("宋江").setAge(40).setAddress("Liangshan").build();
                PersonResponse wu = PersonResponse.newBuilder().setName("吴用").setAge(35).setAddress("Liangshan").build();

                PersonResponseList personResponseList = PersonResponseList.newBuilder().addPersonResponse(lxcecho).addPersonResponse(song).addPersonResponse(wu).build();
                responseObserver.onNext(personResponseList);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<StreamRequest> biTalk(StreamObserver<StreamResponse> responseObserver) {
        return new StreamObserver<StreamRequest>() {
            @Override
            public void onNext(StreamRequest value) {
                System.out.println("request info: " + value.getRequestInfo());
                responseObserver.onNext(StreamResponse.newBuilder().setResponseInfo(UUID.randomUUID().toString()).build());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("完成");
                responseObserver.onCompleted();
            }
        };
    }
}
