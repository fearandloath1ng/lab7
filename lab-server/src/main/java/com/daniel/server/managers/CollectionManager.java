package com.daniel.server.managers;

import com.daniel.common.network.Request;
import com.daniel.common.network.Response;
import com.daniel.common.util.FileManager;
import com.daniel.server.connection.RequestReceiver;
import com.daniel.server.connection.ResponseSender;
import com.daniel.server.connection.ServerConnectionManager;
import com.daniel.server.tasks.HandleCommandTask;
import com.daniel.server.tasks.SendTask;
import com.daniel.server.util.CollectionHandler;
import com.daniel.server.util.ServerCommandsHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class CollectionManager {

    private final String path;
    private final Integer port;
    private CommandManager commandManager;
    private CollectionHandler collectionHandler;

    private RequestReceiver requestReceiver;
    private ServerConnectionManager connectionManager;
    private ResponseSender responseSender;
    private Executor executor = Executors.newCachedThreadPool();

    public CollectionManager(String filePath, String port) {
        path = filePath;
        this.port = Integer.parseInt(port);
    }

    /**
     * Метод загружает коллекцию из файла и активирует интерактивное приложение
     */
    public void run() {
        if (path == null) {
            System.out.println("Путь к файлу должен передаваться через переменную окружения!");
            return;
        }
        FileManager fileManager = new FileManager(path);
        try {
            collectionHandler = new CollectionHandler();
        } catch (SecurityException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        connectionManager = new ServerConnectionManager();
        try {
            DatagramChannel channel = connectionManager.openConnection(port);
            requestReceiver = new RequestReceiver(channel);
            responseSender = new ResponseSender(channel);
        } catch (IOException e) {
            System.err.println("Ошибка открытия соединения");
            return;
        }
        commandManager = new CommandManager(collectionHandler, fileManager);
        readInput();
    }

    /**
     * Метод реализует чтение команд с консоли
     */
    private void readInput() {
        new Thread(new ServerCommandsHandler(commandManager)).start();
        System.out.println("Сервер запущен");
        ForkJoinPool commandExecutor = new ForkJoinPool();
        ForkJoinPool senderExecutor = new ForkJoinPool();
        executor.execute(() -> {
            try {
                while (true) {
                    Request request = requestReceiver.receiveRequest();
                    System.out.println("Получен запрос " + request);
                    Response response = commandExecutor.invoke(new HandleCommandTask(commandManager, request));
                    senderExecutor.execute(new SendTask(responseSender, response, requestReceiver.getAddress()));
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Ошибка при получении запроса");
            }
        });

    }

}
