package io.grpc.examples.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.proto.DataInfo;

/**
 * @author lxcecho 909231497@qq.com
 * @since ${TIME} ${DAY}-${MONTH}-${YEAR}
 */
public class ProtoBufTest {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        DataInfo.Student student = DataInfo.Student.newBuilder().setName("lxcecho").setAddress("GuangXi Qinzhou").setAge(18).build();

        byte[] student2ByteArray = student.toByteArray();

        DataInfo.Student student2 = DataInfo.Student.parseFrom(student2ByteArray);

        System.out.println(student2.getName() + " " + student2.getAge() + " " + student2.getAddress());

    }
}
