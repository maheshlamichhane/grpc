package com.vinsguru.test.sec08;


import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec08.BankServiceGrpc;
import com.vinsguru.models.sec08.Money;
import com.vinsguru.models.sec08.WithdrawRequest;
import com.vinsguru.sec08.BankService;
import com.vinsguru.test.common.AbstractChannelTest;
import com.vinsguru.test.common.ResponseObserver;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec04ServerStreamingAsyncInputValidationTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec04ServerStreamingAsyncInputValidationTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected BankServiceGrpc.BankServiceStub blockingStub;

    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newStub(channel);
    }

    @ParameterizedTest
    @MethodSource("testdata")
    public void asyncInputValidationTest(WithdrawRequest request,Status.Code code){

       var observer = ResponseObserver.<Money>create();
       this.blockingStub.withdraw(request,observer);
       observer.await();


       Assertions.assertTrue(observer.getItems().isEmpty());
       Assertions.assertNotNull(observer.getThrowable());
        Assertions.assertEquals(code,((StatusRuntimeException) observer.getThrowable()).getStatus().getCode());
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
