package com.mimacom.ai.mock.spring.controller;

import com.mimacom.ai.mock.exception.UnkownErrorException;
import com.mimacom.ai.mock.exception.WrongUserPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class PasswordUpdateController {

    private static final Logger log = LoggerFactory.getLogger(PasswordUpdateController.class);

    private static final String API_KEY = "95kgjvb90tg..bm505..d.23l590ymgl50423cdWERf3";

    private static final String USER_PASSWORD = "test";


    @RequestMapping(value = "/mock/password-update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@RequestParam("api_key") String apiKey, @RequestParam("user") String user, @RequestParam("password") String password, @RequestParam("new_password") String newPassword) {
        if(log.isDebugEnabled()) {
            log.debug("entering updatePassword in MOCK");
            log.debug("api_key: " + apiKey);
            log.debug("user: " + user);
            log.debug("password: " + password);
            log.debug("new_Password: " + newPassword);
        }
        if(!checkApiKey(apiKey)) {
            log.debug("Wrong api key: returning 400");
            throw new UnkownErrorException();
        }
        else if(!checkUserPassword(user, password)) {
            log.debug("Wrong password. returning 403");
            throw new WrongUserPasswordException();
        }

        updatePassword(user, newPassword);
//        updatePassword(user, "");

    }

    private void updatePassword(String user, String password) {
        //do nothing
    }

    private boolean checkUserPassword(String user, String oldPassword) {
        return USER_PASSWORD.equals(oldPassword);
    }

    private boolean checkApiKey(String apiKey) {
        return API_KEY.equals(apiKey);
    }


}
