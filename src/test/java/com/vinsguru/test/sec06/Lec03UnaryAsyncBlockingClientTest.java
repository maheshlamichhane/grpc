package com.vinsguru.test.sec06;

import com.google.protobuf.Empty;
import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec06.BalanceCheckRequest;
import com.vinsguru.models.sec06.BankServiceGrpc;
import com.vinsguru.sec06.BankService;
import com.vinsguru.test.common.AbstractChannelTest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec03UnaryAsyncBlockingClientTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec03UnaryAsyncBlockingClientTest.class);

    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected BankServiceGrpc.BankServiceFutureStub blockingStub;

    @BeforeAll
    public void setup(){
        grpcServer.start();
        blockingStub = BankServiceGrpc.newFutureStub(channel);
    }

    @Test
    public void getBalanceTest() throws InterruptedException, ExecutionException {
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();
        var future = this.blockingStub.getAccountBalance(request);
        logger.info("aync blocking balance={}",future.get());
        logger.info("end #################S");
    }

    @Test
    public void getAllBalanceTest() throws ExecutionException, InterruptedException {
        var future = this.blockingStub.getAllAccounts(Empty.getDefaultInstance());
        logger.info("All accounts:{}",future.get());
        logger.info("end #################S");
        Assertions.assertEquals(10,future.get().getAccountsCount());
    }

    @AfterAll
    public void stop(){
        grpcServer.stop();
    }
}
