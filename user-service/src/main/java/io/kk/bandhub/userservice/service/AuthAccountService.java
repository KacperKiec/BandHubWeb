package io.kk.bandhub.userservice.service;

import io.kk.bandhub.userservice.dto.AccountAuthDTO;

public interface AuthAccountService {
    AccountAuthDTO findToAuth(String email);
    Boolean validatePassword(Integer userId, String password);
    void changePassword(Integer userId, String password);
    void remindPassword(String email);
}
