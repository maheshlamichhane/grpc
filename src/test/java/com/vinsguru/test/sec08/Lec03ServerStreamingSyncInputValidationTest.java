package com.vinsguru.test.sec08;


import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec08.BankServiceGrpc;
import com.vinsguru.models.sec08.WithdrawRequest;
import com.vinsguru.sec08.BankService;
import com.vinsguru.test.common.AbstractChannelTest;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec03ServerStreamingSyncInputValidationTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec03ServerStreamingSyncInputValidationTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @ParameterizedTest
    @MethodSource("testdata")
    public void syncBlockingInputValidationTest(WithdrawRequest request,Status.Code code){

        var ex = Assertions.assertThrows(StatusRuntimeException.class,() -> {
            var response = this.blockingStub.withdraw(request).hasNext();
        });
        Assertions.assertEquals(code,ex.getStatus().getCode());
    }

    private Stream<Arguments> testdata(){
        return Stream.of(
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(11).setAmount(10).build(),Status.Code.INVALID_ARGUMENT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(17).build(),Status.Code.INVALID_ARGUMENT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(120).build(),Status.Code.FAILED_PRECONDITION)
        );
    }



    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }

}
