package com.vinsguru.test.sec08;


import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec08.AccountBalance;
import com.vinsguru.models.sec08.BalanceCheckRequest;
import com.vinsguru.models.sec08.BankServiceGrpc;
import com.vinsguru.sec08.BankService;
import com.vinsguru.test.common.AbstractChannelTest;
import com.vinsguru.test.common.ResponseObserver;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec01UnarySyncInputValidationTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec01UnarySyncInputValidationTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void syncBlockingInputValidationTest(){

        var ex = Assertions.assertThrows(StatusRuntimeException.class,() -> {
            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(12)
                    .build();
            this.blockingStub.getAccountBalance(request);
        });

        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT,ex.getStatus().getCode());
    }



    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }

}
