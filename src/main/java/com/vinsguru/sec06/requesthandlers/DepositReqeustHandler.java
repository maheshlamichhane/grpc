package com.vinsguru.sec06.requesthandlers;

import com.vinsguru.models.sec06.AccountBalance;
import com.vinsguru.models.sec06.DepositRequest;
import com.vinsguru.sec06.repository.AccountRepository;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DepositReqeustHandler implements StreamObserver<DepositRequest> {
    private static final Logger logger = LoggerFactory.getLogger(DepositReqeustHandler.class);
    private final StreamObserver<AccountBalance> responseObserver;
    private int accountNumber;

    public DepositReqeustHandler(StreamObserver<AccountBalance> responseObserver){
        this.responseObserver = responseObserver;
    }


    @Override
    public void onNext(DepositRequest depositRequest) {
        switch (depositRequest.getRequestCase()){
            case ACCOUNT_NUMBER -> this.accountNumber = depositRequest.getAccountNumber();
            case MONEY -> AccountRepository.addAmount(this.accountNumber,depositRequest.getMoney().getAmount());
        }
    }

    @Override
    public void onError(Throwable throwable) {
        logger.info("error occured");
    }

    @Override
    public void onCompleted() {
        var accountBalance = AccountBalance.newBuilder()
                .setAccountNumber(this.accountNumber)
                .setBalance(AccountRepository.getBalance(this.accountNumber))
                .build();
        this.responseObserver.onNext(accountBalance);
        this.responseObserver.onCompleted();
    }
}
