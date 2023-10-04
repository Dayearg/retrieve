package edu.njucm.retrieve.services;

import edu.njucm.retrieve.model.User;

public interface UserService {
    int login(User user);

    int register(User user);

}
