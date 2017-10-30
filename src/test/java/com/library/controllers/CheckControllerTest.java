package com.library.controllers;

import com.library.TestConstants;
import com.library.dao.services.BookService;
import com.library.dao.services.UserService;
import com.library.model.Book;
import com.library.model.BookBuilder;
import com.library.model.User;
import com.library.model.UserBuilder;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CheckControllerTest {
    private MockMvc mockMvc;

    @Mock(name = "bookService")
    private BookService bookServiceMock;
    @Mock(name = "userService")
    private UserService userServiceMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new CheckController(userServiceMock, bookServiceMock)).build();
    }

    @Test
    public void checkOldPasswordTest() throws Exception {
        String hash = DigestUtils.sha256Hex(TestConstants.PASSWORD);
        User user = new UserBuilder()
                .userId(TestConstants.USER_ID)
                .login(TestConstants.LOGIN)
                .password(hash)
                .build();
        when(userServiceMock.getById(TestConstants.USER_ID)).thenReturn(user);
        mockMvc.perform(post("/check/pass/{userId}", TestConstants.USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"pass\":\"" + hash + "\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/check/pass/{userId}", TestConstants.USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"pass\":\"" + DigestUtils.sha256Hex("pass") + "\"}"))
                .andExpect(status().isBadRequest());
        verify(userServiceMock, times(2)).getById(TestConstants.USER_ID);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    public void checkUniqueLoginTest() throws Exception {
        User user = new UserBuilder()
                .userId(TestConstants.USER_ID)
                .login(TestConstants.LOGIN)
                .password(TestConstants.PASSWORD)
                .build();
        when(userServiceMock.getByLogin(TestConstants.LOGIN)).thenReturn(user);
        mockMvc.perform(post("/check/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"" + TestConstants.LOGIN + "\"}"))
            .andExpect(status().isBadRequest());

        when(userServiceMock.getByLogin(TestConstants.LOGIN)).thenReturn(null);
        mockMvc.perform(post("/check/login/{userId}", TestConstants.USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"" + TestConstants.LOGIN + "\"}"))
                .andExpect(status().isOk());
        verify(userServiceMock, times(2)).getByLogin(TestConstants.LOGIN);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    public void checkUniqueIsbnTest() throws Exception {
        Book book = new BookBuilder()
                .bookId(TestConstants.BOOK_ID)
                .isbn(TestConstants.ISBN)
                .author(TestConstants.AUTHOR)
                .title(TestConstants.TITLE)
                .takenBy(null)
                .build();
        when(bookServiceMock.getByIsbn(TestConstants.ISBN)).thenReturn(book);
        mockMvc.perform(post("/check/isbn")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"" + TestConstants.ISBN + "\"}"))
                .andExpect(status().isBadRequest());

        when(bookServiceMock.getByIsbn(TestConstants.ISBN)).thenReturn(null);
        mockMvc.perform(post("/check/isbn/{bookId}", TestConstants.BOOK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"" + TestConstants.ISBN + "\"}"))
                .andExpect(status().isOk());
        verify(bookServiceMock, times(2)).getByIsbn(TestConstants.ISBN);
        verifyNoMoreInteractions(bookServiceMock);
    }

}
