package com.vinsguru.test.sec11;

import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec10.BalanceCheckRequest;
import com.vinsguru.models.sec10.BankServiceGrpc;
import com.vinsguru.sec10.DeadlineBankService;
import com.vinsguru.test.common.AbstractChannelTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01GzipCallOptionsTest extends AbstractChannelTest {

    private static final Logger log  = LoggerFactory.getLogger(Lec01GzipCallOptionsTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;


    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }


    @Test
    public void gzipDemo(){
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();
        var response = blockingStub
                .withCompression("gzip")
                .getAccountBalance(request);
        log.info("{}",response);
    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }
}
