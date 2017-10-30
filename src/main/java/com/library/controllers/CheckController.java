package com.library.controllers;

import com.library.dao.services.BookService;
import com.library.model.Book;
import com.library.model.User;
import com.library.dao.services.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
public class CheckController {
    private final Logger logger = LoggerFactory.getLogger(CheckController.class);
    private UserService userService;
    private BookService bookService;
    @Autowired
    public CheckController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @RequestMapping(value = "/check/pass/{userId}",
                    method = RequestMethod.POST,
                    consumes = "application/json")
    public ResponseEntity checkOldPassword(@PathVariable Long userId,
                                           @RequestBody String body) {
        try {
            JSONObject jsonObject = new JSONObject(body.toString());
            String password = jsonObject.getString("pass");
            if (!password.equals(userService.getById(userId).getPassword())) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (JSONException ex) {
            logger.error("Error parsing JSON request string", ex);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = {"/check/login", "/check/login/{userId}"},
                    method = RequestMethod.POST,
                    consumes = "application/json")
    public ResponseEntity checkUniqueLogin(@PathVariable Optional<Long> userId,
                                           @RequestBody String body) {
        try {
            JSONObject jsonObject = new JSONObject(body.toString());
            String login = jsonObject.getString("login");
            User user = userService.getByLogin(login);
            if (user != null) {
                if (!userId.isPresent()) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
                else if (user.getUserId() != userId.get()) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            }
        } catch (JSONException ex) {
            logger.error("Error parsing JSON request string", ex);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = {"/check/isbn", "/check/isbn/{bookId}"},
            method = RequestMethod.POST,
            consumes = "application/json")
    public ResponseEntity checkUniqueIsbn(@PathVariable Optional<Long> bookId,
                                          @RequestBody String body) {
        try {
            JSONObject jsonObject = new JSONObject(body.toString());
            String isbn = jsonObject.getString("isbn");
            Book book = bookService.getByIsbn(isbn);
            if (book != null) {
                if (!bookId.isPresent()) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
                else if (book.getBookId() != bookId.get()) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            }
        } catch (JSONException ex) {
            logger.error("Error parsing JSON request string", ex);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
