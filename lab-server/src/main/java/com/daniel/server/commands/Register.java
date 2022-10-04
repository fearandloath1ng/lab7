package com.daniel.server.commands;

import com.daniel.common.network.Request;
import com.daniel.common.network.Response;
import com.daniel.common.person.User;
import com.daniel.server.connection.ResponseFactory;
import com.daniel.server.database.UserDao;

public class Register {

    public Response execute(Request request) {
        if (UserDao.createUser(request.getUser()).contains("successfully")) {
            User userFromDB = UserDao.getUser(request.getUser());
            return ResponseFactory.createAuthSuccessResponse(userFromDB.getId() + " " + userFromDB.getName() + " " + userFromDB.getPassword());
        }
        return ResponseFactory.createErrorResponse("user with this name already exists!");
    }
}
