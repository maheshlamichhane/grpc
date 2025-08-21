package com.vinsguru.sec06;

import com.vinsguru.models.sec06.AccountBalance;
import com.vinsguru.models.sec06.BalanceCheckRequest;
import com.vinsguru.models.sec06.BankServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GrpcClient {

    private static final Logger log = LoggerFactory.getLogger(GrpcClient.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        var channel = ManagedChannelBuilder.forAddress("localhost",6565)
                .usePlaintext()
                .build();

        // for synchronous
//        var stub = BankServiceGrpc.newBlockingStub(channel);

         // for non blocking asynchronous
        var stub = BankServiceGrpc.newStub(channel);

//        // for blocking asynchronous
//        var stub = BankServiceGrpc.newFutureStub(channel);


        stub.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(2).build(), new StreamObserver<AccountBalance>() {
            @Override
            public void onNext(AccountBalance accountBalance) {
                log.info("{}",accountBalance);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
            }
        });

//        stub.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(2).build());
        log.info("{}","done");
        Thread.sleep(Duration.ofSeconds(1));

    }
}
