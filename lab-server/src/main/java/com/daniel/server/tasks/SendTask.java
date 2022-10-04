package com.daniel.server.tasks;

import com.daniel.common.network.Response;
import com.daniel.server.connection.ResponseSender;

import java.net.SocketAddress;
import java.util.concurrent.RecursiveTask;

public class SendTask extends RecursiveTask<Void> {

    private final ResponseSender responseSender;
    private final Response response;
    private final SocketAddress socketAddress;

    public SendTask(ResponseSender responseSender, Response response, SocketAddress socketAddress) {
        this.responseSender = responseSender;
        this.response = response;
        this.socketAddress = socketAddress;
    }

    @Override
    protected Void compute() {
        responseSender.sendResponse(response, socketAddress);
        return null;
    }
}
