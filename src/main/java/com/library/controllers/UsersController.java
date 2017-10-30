package com.library.controllers;

import com.library.dao.services.UserService;
import com.library.model.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {
    private final Logger logger = LoggerFactory.getLogger(UsersController.class);
    private UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelMap getUserList(ModelMap modelMap) {
        List<User> userList = new ArrayList<>();
        try {
            userList = userService.getAll();
        }
        catch (DataAccessException ex) {
            logger.error(String.format("Error on getting users list"), ex);
        }
        if (!modelMap.containsAttribute("userList"))
            modelMap.addAttribute("userList", userList);
        else {
            modelMap.put("userList", userList);
        }
        return modelMap;
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public ResponseEntity deleteUser(@RequestParam(value = "userId") Long userId) {
        if (userId != null) {
            try {
                userService.delete(userId);
                logger.info(String.format("User with ID = '%s' was deleted", userId));
                return new ResponseEntity(HttpStatus.OK);
            }
            catch (DataAccessException ex) {
                logger.error(String.format("Error on deleting user with ID = '%s'", userId));
            }
        }
        return new ResponseEntity("Error on deleting!", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = {"/user/add", "/user/{userId}"}, method = RequestMethod.GET)
    public ModelAndView getUserModal(@PathVariable Optional<Long> userId) {
        User user;
        if (userId.isPresent())
            user = userService.getById(userId.get());
        else
            user = new User();
        return new ModelAndView("userForm", "user", user);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute User user) {
        try {
            if (user != null) {
                if(user.getUserId() != null) {
                    if(user.getPassword() != "") {
                        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
                        userService.update(user);
                    }
                    else {
                        userService.updateLogin(user.getUserId(), user.getLogin());
                    }
                    logger.info(String.format("User '%s' was updated", user.getLogin()));
                }
                else {
                    user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
                    userService.create(user);
                    logger.info(String.format("User '%s' was added", user.getLogin()));
                }
            }
        }
        catch(DataAccessException ex) {
            logger.error("Error during saving data", ex);
        }
        return "redirect:/users";
    }
}
