package com.vinsguru.sec06.requesthandlers;

import com.vinsguru.models.sec06.*;
import com.vinsguru.sec06.repository.AccountRepository;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferReqeustHandler implements StreamObserver<TransferRequest> {
    private static final Logger logger = LoggerFactory.getLogger(TransferReqeustHandler.class);
    private final StreamObserver<TransferResponse> responseObserver;

    public TransferReqeustHandler(StreamObserver<TransferResponse> responseObserver){
        this.responseObserver = responseObserver;
    }


    @Override
    public void onNext(TransferRequest transferRequest) {
       var status = this.transfer(transferRequest);
           var response = TransferResponse.newBuilder()
                   .setFromAccount(this.toAccountBalance(transferRequest.getFromAccount()))
                   .setToAccount(this.toAccountBalance(transferRequest.getToAccount()))
                   .setStatus(status)
                   .build();

           this.responseObserver.onNext(response);


    }

    @Override
    public void onError(Throwable throwable) {
        logger.info("error occured");
    }

    @Override
    public void onCompleted() {
        logger.info("transfer requeset stream completed");
        this.responseObserver.onCompleted();
    }

    private TransferStatus transfer(TransferRequest request){
        var amount = request.getAmount();
        var fromAccount = request.getFromAccount();
        var toAccount = request.getToAccount();
        var status = TransferStatus.REJECTED;
        if(AccountRepository.getBalance(fromAccount) >= amount && (fromAccount != toAccount)){
            AccountRepository.deductAmount(fromAccount,amount);
            AccountRepository.addAmount(toAccount,amount);
            status = TransferStatus.COMPLETED;
        }
        return status;
    }

    private AccountBalance toAccountBalance(int accountNumber){
        return AccountBalance.newBuilder()
                .setAccountNumber(accountNumber)
                .setBalance(AccountRepository.getBalance(accountNumber))
                .build();
    }
}
