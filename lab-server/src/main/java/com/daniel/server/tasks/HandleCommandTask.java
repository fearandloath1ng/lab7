package com.daniel.server.tasks;

import com.daniel.common.network.Request;
import com.daniel.common.network.Response;
import com.daniel.server.managers.CommandManager;

import java.util.concurrent.RecursiveTask;

public class HandleCommandTask extends RecursiveTask<Response> {

    private final CommandManager commandManager;
    private final Request request;

    public HandleCommandTask(CommandManager commandManager, Request req) {
        this.commandManager = commandManager;
        this.request = req;
    }

    @Override
    protected Response compute() {
        return commandManager.execute(request);
    }
}
