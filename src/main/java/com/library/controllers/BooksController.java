package com.library.controllers;

import com.library.dao.services.BookService;
import com.library.dao.services.UserService;
import com.library.model.Book;
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
public class BooksController {
    private final Logger logger = LoggerFactory.getLogger(BooksController.class);
    private UserService userService;
    private BookService bookService;

    @Autowired
    public BooksController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public ModelMap getBookList(ModelMap modelMap) {
        List<Book> bookList = new ArrayList<>();
        try {
            bookList = bookService.getPartition(0, "author","asc");
        }
        catch (DataAccessException ex) {
            logger.error(String.format("Error on getting books list"), ex);
        }
        if (!modelMap.containsAttribute("bookList"))
            modelMap.addAttribute("bookList", bookList);
        else {
            modelMap.put("bookList", bookList);
        }
        return modelMap;
    }

    @RequestMapping(value = "/books/table", method = RequestMethod.GET)
    public ModelAndView renderBooksTable(@RequestParam(value = "quantity") Integer quantity,
                                         @RequestParam(value = "column") String column,
                                         @RequestParam(value = "asc") Boolean asc,
                                         ModelMap modelMap) {
        List<Book> bookList = new ArrayList<>();
        try {
            bookList = bookService.getPartition(quantity, column, (asc) ? ("asc") : ("desc"));
        }
        catch (DataAccessException ex) {
            logger.error(String.format(("Error on getting books list"), ex));
        }
        if (bookList.isEmpty())
            return null;
        if (!modelMap.containsAttribute("bookList"))
            modelMap.addAttribute("bookList", bookList);
        else {
            modelMap.put("bookList", bookList);
        }
        return new ModelAndView("booksTable", modelMap);
    }

    @RequestMapping(value = "/book/delete", method = RequestMethod.POST)
    public ResponseEntity deleteBook(@RequestParam(value = "bookId") Long bookId) {
        if (bookId != null) {
            try {
                bookService.delete(bookId);
                logger.info(String.format("Book with ISBN = '%s' was deleted", bookId));
                return new ResponseEntity(HttpStatus.OK);
            }
            catch (DataAccessException ex) {
                logger.error(String.format("Error on deleting book with ISBN = '%s'", bookService.getById(bookId).getIsbn()), ex);
            }
        }
        return new ResponseEntity("Error on deleting!", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/book/take", method = RequestMethod.POST)
    public ModelAndView takeBook(@RequestParam(value = "bookId") Long bookId,
                                 @RequestParam(value = "login") String login,
                                 ModelMap modelMap) {
        Book book = bookService.getById(bookId);
        try {
            book.setTakenBy(userService.getByLogin(login));
            bookService.update(book);
            logger.info(String.format("Book with ISBN = '%s' was taken", book.getIsbn()));
        }
        catch (DataAccessException ex) {
            logger.error(String.format("Error on taking book with ISBN = '%s'", book.getIsbn()), ex);
        }
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        if (!modelMap.containsAttribute("bookList"))
            modelMap.addAttribute("bookList", bookList);
        else {
            modelMap.put("bookList", bookList);
        }
        return new ModelAndView("booksTable", modelMap);
    }

    @RequestMapping(value = "/book/give", method = RequestMethod.POST)
    public ModelAndView giveBackBook(@RequestParam(value = "bookId") Long bookId, ModelMap modelMap) {
        Book book = bookService.getById(bookId);
        try {
            book.setTakenBy(null);
            bookService.update(book);
            List<Book> bookList = new ArrayList<>();
            bookList.add(book);
            if (!modelMap.containsAttribute("bookList"))
                modelMap.addAttribute("bookList", bookList);
            logger.info(String.format("Book with ISBN = '%s' was given back", book.getIsbn()));
        }
        catch (DataAccessException ex) {
            logger.error(String.format("Error on giving back book with ISBN = '%s'", book.getIsbn()), ex);
        }
        return new ModelAndView("booksTable", modelMap);
    }

    @RequestMapping(value = "/books/quantity", method = RequestMethod.GET)
    public ResponseEntity getBooksQuantity() {
       Integer quantity = bookService.getBooksQuantity();
       return new ResponseEntity(quantity.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = {"/book/add", "/book/{bookId}"}, method = RequestMethod.GET)
    public ModelAndView getBookModal(@PathVariable Optional<Long> bookId) {
        Book book;
        if (bookId.isPresent())
            book = bookService.getById(bookId.get());
        else
            book = new Book();
        return new ModelAndView("bookForm", "book", book);
    }

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public String saveBook(@ModelAttribute Book book) {
        try {
            if (book != null) {
                if(book.getBookId() != null) {
                    bookService.update(book);
                    logger.info(String.format("Book with ISBN = '%s' was updated", book.getIsbn()));
                }
                else {
                    bookService.create(book);
                    logger.info(String.format("Book with ISBN = '%s' was added", book.getIsbn()));
                }
            }
        }
        catch(DataAccessException ex) {
            logger.error("Error during saving data", ex);
        }
        List<Book> bookList = new ArrayList<>();
        try {
            bookList = bookService.getPartition(0, "author", "asc");
        }
        catch (DataAccessException ex) {
            logger.error(String.format("Error on getting books list"), ex);
        }
        return "redirect:/books";
    }
}