package com.daniel.server.commands;

import com.daniel.common.network.Request;
import com.daniel.common.network.Response;
import com.daniel.common.person.User;
import com.daniel.server.connection.ResponseFactory;
import com.daniel.server.database.UserDao;

public class Login {

    public Response execute(Request request) {
        User dbUser = UserDao.getUser(request.getUser());
        if (dbUser != null && dbUser.getPassword().equals(request.getUser().getPassword())) {
            return ResponseFactory.createAuthSuccessResponse(dbUser.getId() + " " + dbUser.getName() + " " + dbUser.getPassword());
        }
        return ResponseFactory.createAuthErrorResponse("invalid user");
    }
}
