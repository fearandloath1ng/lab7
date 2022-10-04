package com.daniel.client.managers;

import com.daniel.client.connection.RequestFactory;
import com.daniel.client.connection.RequestSender;
import com.daniel.client.connection.ResponseReceiver;
import com.daniel.client.util.InputScanner;
import com.daniel.common.network.Response;
import com.daniel.common.network.ResponseType;
import com.daniel.common.person.Person;
import com.daniel.common.person.User;
import com.daniel.common.util.FileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.InputMismatchException;
import java.util.Scanner;

import static com.daniel.client.util.MD5Password.encryptPassword;

public class ClientCommandManager {

    private final FileManager fileManager;
    private final DatagramSocket socket;
    private final SocketAddress address;
    private final InputScanner inputScanner;

    public ClientCommandManager(DatagramSocket socket, SocketAddress address, FileManager fileManager) {
        this.fileManager = fileManager;
        this.socket = socket;
        this.address = address;
        inputScanner = new InputScanner();
    }

    public void execute(String[] line) throws IOException {
        RequestSender requestSender = new RequestSender(address, socket);
        if (AuthManager.getUser() == null && !(line[0].equals("login") || line[0].equals("register"))) {
            System.err.println("Необходима авторизация, для этого введите login/register username password");
            return;
        }
        switch (line[0]) {
            case "exit":
                exit();

            case "execute_script":
                if (line.length < 2) {
                    System.err.println("Необходимо указать путь до файла");
                } else {
                    executeScript(line[1]);
                }
                return;

            case "remove_greater_key":
            case "remove_key":
                if (line.length < 2) {
                    System.err.println("Команда требует аргумент");
                    return;
                } else {
                    try {
                        requestSender.sendRequest(RequestFactory.createArgRequest(line[0], Integer.parseInt(line[1])));
                    } catch (NumberFormatException e) {
                        System.err.println("Аргумент должен быть целым числом");
                        return;
                    }
                }
                break;

            case "remove_greater":
                Person p = inputScanner.scanPerson();
                requestSender.sendRequest(RequestFactory.createObjectRequest(line[0], p));
                break;

            case "insert":
            case "replace_if_lower":
            case "update":
                if (line.length < 2) {
                    System.err.println("Команда требует аргумент");
                    return;
                } else {
                    p = inputScanner.scanPerson();
                    try {
                        p.setUser(AuthManager.getUser());
                        requestSender.sendRequest(RequestFactory.createArgObjectRequest(line[0], Integer.parseInt(line[1]), p));
                    } catch (NumberFormatException e) {
                        System.err.println("Аргумент должен быть целым числом");
                        return;
                    }
                }
                break;
            case "login" :
            case "register":
                if (line.length < 3) {
                    System.err.println("Команда требует аргумент");
                    return;
                } else {
                    requestSender.sendRequest(RequestFactory.createAuthRequest(line[0], line[1], encryptPassword(line[2])));
                }
                break;

            default:
                requestSender.sendRequest(RequestFactory.createSimpleRequest(line[0]));
                break;

        }
        ResponseReceiver responseReceiver = new ResponseReceiver(socket);
        Response response = responseReceiver.receiveResponse();
        if (response.getType() == ResponseType.ERROR_RESPONSE) {
            System.err.println(response.getContent());
        } else if (response.getType() == ResponseType.OK_RESPONSE) {
            System.out.println(response.getContent());
        } else if (response.getType() == ResponseType.AUTH_ERROR) {
            System.err.println(response.getContent());
        } else if (response.getType() == ResponseType.AUTH_SUCCESS) {
            String content = response.getContent();
            String[] args = content.split(" ");
            AuthManager.setUser(new User(args[1], args[2], Integer.parseInt(args[0])));
            System.out.println("Success login");
        }
    }

    private void executeScript(String filePath) {
        try {
            File script = new File(filePath);
            fileManager.checkFile(script);
            Scanner scanner = new Scanner(new FileInputStream(script));
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                if (!"".equals(s)) {
                    String[] line = s.trim().split(" ");
                    execute(line);
                }
            }
        } catch (IOException | SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    private void exit() {
        System.exit(0);
    }
}
