package com.library.controllers;

import com.library.TestConstants;
import com.library.dao.services.BookService;
import com.library.dao.services.UserService;
import com.library.model.Book;
import com.library.model.BookBuilder;
import com.library.model.User;
import com.library.model.UserBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import java.util.Arrays;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class BooksControllerTest {
    private MockMvc mockMvc;

    @Mock(name = "bookService")
    private BookService bookServiceMock;
    @Mock(name = "userService")
    private UserService userServiceMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new BooksController(bookServiceMock, userServiceMock))
                .setViewResolvers(viewResolver())
                .build();
    }

    private ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Test
    public void getBookListTest() throws Exception {
        Book first = new BookBuilder()
                .bookId(1L)
                .isbn(TestConstants.ISBN)
                .author(TestConstants.AUTHOR)
                .title(TestConstants.TITLE)
                .takenBy(null)
                .build();
        Book second = new BookBuilder()
                .bookId(2L)
                .isbn(TestConstants.ISBN)
                .author(TestConstants.AUTHOR)
                .title(TestConstants.TITLE)
                .takenBy(null)
                .build();
        when(bookServiceMock.getPartition(0, "author", "asc")).thenReturn(Arrays.asList(first, second));
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookList", hasSize(2)))
                .andExpect(model().attribute("bookList", hasItem(
                        allOf(
                                hasProperty("bookId", is(1L)),
                                hasProperty("isbn", is(TestConstants.ISBN)),
                                hasProperty("author", is(TestConstants.AUTHOR)),
                                hasProperty("title", is(TestConstants.TITLE)),
                                hasProperty("takenBy", nullValue())
                        )
                )))
                .andExpect(model().attribute("bookList", hasItem(
                        allOf(
                                hasProperty("bookId", is(2L)),
                                hasProperty("isbn", is(TestConstants.ISBN)),
                                hasProperty("author", is(TestConstants.AUTHOR)),
                                hasProperty("title", is(TestConstants.TITLE)),
                                hasProperty("takenBy", nullValue())
                        )
                )));
        verify(bookServiceMock, times(1)).getPartition(0, "author", "asc");
        verifyNoMoreInteractions(bookServiceMock);
    }

    @Test
    public void renderBooksTableTest() throws Exception {
        Book first = new BookBuilder()
                .bookId(1L)
                .isbn(TestConstants.ISBN)
                .author(TestConstants.AUTHOR)
                .title(TestConstants.TITLE)
                .takenBy(null)
                .build();
        Book second = new BookBuilder()
                .bookId(2L)
                .isbn(TestConstants.ISBN)
                .author(TestConstants.AUTHOR)
                .title(TestConstants.TITLE)
                .takenBy(null)
                .build();
        when(bookServiceMock.getPartition(0, "title", "desc")).thenReturn(Arrays.asList(first, second));
        mockMvc.perform(get("/books/table")
                        .param("quantity", Integer.toString(0))
                        .param("column", "title")
                        .param("asc", Boolean.toString(false)))
                .andExpect(status().isOk())
                .andExpect(view().name("booksTable"))
                .andExpect(model().attribute("bookList", hasSize(2)))
                .andExpect(model().attribute("bookList", hasItem(
                        allOf(
                                hasProperty("bookId", is(1L)),
                                hasProperty("isbn", is(TestConstants.ISBN)),
                                hasProperty("author", is(TestConstants.AUTHOR)),
                                hasProperty("title", is(TestConstants.TITLE)),
                                hasProperty("takenBy", nullValue())
                        )
                )))
                .andExpect(model().attribute("bookList", hasItem(
                        allOf(
                                hasProperty("bookId", is(2L)),
                                hasProperty("isbn", is(TestConstants.ISBN)),
                                hasProperty("author", is(TestConstants.AUTHOR)),
                                hasProperty("title", is(TestConstants.TITLE)),
                                hasProperty("takenBy", nullValue())
                        )
                )));
        verify(bookServiceMock, times(1)).getPartition(0, "title", "desc");
        verifyNoMoreInteractions(bookServiceMock);
    }

    @Test
    public void deleteBookTest() throws Exception {
        when(bookServiceMock.delete(TestConstants.BOOK_ID)).thenReturn(true);
        mockMvc.perform(post("/book/delete")
                .param("bookId", TestConstants.BOOK_ID.toString()))
                .andExpect(status().isOk());
        verify(bookServiceMock, times(1)).delete(TestConstants.BOOK_ID);
        verifyNoMoreInteractions(bookServiceMock);
    }

    @Test
    public void takeBookTest() throws Exception {
        User user = new UserBuilder()
                .userId(TestConstants.USER_ID)
                .login(TestConstants.LOGIN)
                .password(TestConstants.PASSWORD)
                .build();
        Book book = new BookBuilder()
                .bookId(TestConstants.BOOK_ID)
                .isbn(TestConstants.ISBN)
                .author(TestConstants.AUTHOR)
                .title(TestConstants.TITLE)
                .takenBy(null)
                .build();
        when(bookServiceMock.getById(TestConstants.BOOK_ID)).thenReturn(book);
        when(userServiceMock.getByLogin(TestConstants.LOGIN)).thenReturn(user);
        when(bookServiceMock.update(book)).thenReturn(true);
        mockMvc.perform(post("/book/take")
                .param("bookId", TestConstants.BOOK_ID.toString())
                .param("login", TestConstants.LOGIN))
                .andExpect(status().isOk())
                .andExpect(view().name("booksTable"))
                .andExpect(model().attribute("bookList", hasSize(1)))
                .andExpect(model().attribute("bookList", hasItem(
                        allOf(
                                hasProperty("bookId", is(TestConstants.BOOK_ID)),
                                hasProperty("isbn", is(TestConstants.ISBN)),
                                hasProperty("author", is(TestConstants.AUTHOR)),
                                hasProperty("title", is(TestConstants.TITLE)),
                                hasProperty("takenBy", is(user))
                        )
                )));
        verify(bookServiceMock, times(1)).getById(TestConstants.BOOK_ID);
        verify(userServiceMock, times(1)).getByLogin(TestConstants.LOGIN);
        verifyNoMoreInteractions(userServiceMock);
        verify(bookServiceMock, times(1)).update(book);
        verifyNoMoreInteractions(bookServiceMock);
    }

    @Test
    public void giveBackBookTest() throws Exception {
        User user = new UserBuilder()
                .userId(TestConstants.USER_ID)
                .login(TestConstants.LOGIN)
                .password(TestConstants.PASSWORD)
                .build();
        Book book = new BookBuilder()
                .bookId(TestConstants.BOOK_ID)
                .isbn(TestConstants.ISBN)
                .author(TestConstants.AUTHOR)
                .title(TestConstants.TITLE)
                .takenBy(user)
                .build();
        when(bookServiceMock.getById(TestConstants.BOOK_ID)).thenReturn(book);
        when(bookServiceMock.update(book)).thenReturn(true);
        mockMvc.perform(post("/book/give")
                .param("bookId", TestConstants.BOOK_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("booksTable"))
                .andExpect(model().attribute("bookList", hasSize(1)))
                .andExpect(model().attribute("bookList", hasItem(
                        allOf(
                                hasProperty("bookId", is(TestConstants.BOOK_ID)),
                                hasProperty("isbn", is(TestConstants.ISBN)),
                                hasProperty("author", is(TestConstants.AUTHOR)),
                                hasProperty("title", is(TestConstants.TITLE)),
                                hasProperty("takenBy", nullValue())
                        )
                )));
        verify(bookServiceMock, times(1)).getById(TestConstants.BOOK_ID);
        verify(bookServiceMock, times(1)).update(book);
        verifyNoMoreInteractions(bookServiceMock);
    }

    @Test
    public void getBooksQuantityTest() throws Exception {
        Integer quantity = 20;
        when(bookServiceMock.getBooksQuantity()).thenReturn(quantity);
        mockMvc.perform(get("/books/quantity"))
                .andExpect(status().isOk())
                .andExpect(content().string(quantity.toString()));
        verify(bookServiceMock, times(1)).getBooksQuantity();
        verifyNoMoreInteractions(bookServiceMock);
    }

    @Test
    public void getAddBookModalTest() throws Exception {
        mockMvc.perform(get("/book/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookForm"))
                .andExpect(model().attribute("book", allOf(
                        hasProperty("bookId", nullValue()),
                        hasProperty("isbn", isEmptyOrNullString()),
                        hasProperty("author", isEmptyOrNullString()),
                        hasProperty("title", isEmptyOrNullString()),
                        hasProperty("takenBy", nullValue())
                )));
        verifyZeroInteractions(bookServiceMock);
    }

    @Test
    public void getEditBookModalTest() throws Exception {
        Book book = new BookBuilder()
                .bookId(TestConstants.BOOK_ID)
                .isbn(TestConstants.ISBN)
                .author(TestConstants.AUTHOR)
                .title(TestConstants.TITLE)
                .takenBy(null)
                .build();
        when(bookServiceMock.getById(TestConstants.BOOK_ID)).thenReturn(book);
        mockMvc.perform(get("/book/{bookId}", TestConstants.BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("bookForm"))
                .andExpect(model().attribute("book", allOf(
                        hasProperty("bookId", is(TestConstants.BOOK_ID)),
                        hasProperty("isbn", is(TestConstants.ISBN)),
                        hasProperty("author", is(TestConstants.AUTHOR)),
                        hasProperty("title", is(TestConstants.TITLE)),
                        hasProperty("takenBy", nullValue())
                )));
        verify(bookServiceMock, times(1)).getById(TestConstants.BOOK_ID);
        verifyNoMoreInteractions(bookServiceMock);
    }

    @Test
    public void saveNewBookTest() throws Exception {
        Book created = new BookBuilder()
                .bookId(null)
                .isbn(TestConstants.ISBN)
                .author(TestConstants.AUTHOR)
                .title(TestConstants.TITLE)
                .takenBy(null)
                .build();
        when(bookServiceMock.getPartition(0, "author", "asc")).thenReturn(Arrays.asList(created, created));
        when(bookServiceMock.create(created)).thenReturn(true);
        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", TestConstants.ISBN)
                .param("author", TestConstants.AUTHOR)
                .param("title", TestConstants.TITLE))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));
        ArgumentCaptor<Book> formObjectArgument = ArgumentCaptor.forClass(Book.class);
        verify(bookServiceMock, times(1)).create(formObjectArgument.capture());
        verify(bookServiceMock, times(1)).getPartition(0, "author", "asc");
        verifyNoMoreInteractions(bookServiceMock);
        Book formObject = formObjectArgument.getValue();
        assertNull(formObject.getBookId());
        assertThat(formObject.getIsbn(), is(TestConstants.ISBN));
        assertThat(formObject.getAuthor(), is(TestConstants.AUTHOR));
        assertThat(formObject.getTitle(), is(TestConstants.TITLE));
    }

    @Test
    public void saveEditedBookTest() throws Exception {
        Book edited = new BookBuilder()
                .bookId(TestConstants.BOOK_ID)
                .isbn(TestConstants.ISBN)
                .author(TestConstants.AUTHOR)
                .title(TestConstants.TITLE)
                .takenBy(null)
                .build();
        when(bookServiceMock.getPartition(0, "author", "asc")).thenReturn(Arrays.asList(edited, edited));
        when(bookServiceMock.update(edited)).thenReturn(true);
        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("bookId", TestConstants.BOOK_ID.toString())
                .param("isbn", TestConstants.ISBN)
                .param("author", TestConstants.AUTHOR)
                .param("title", TestConstants.TITLE))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));
        ArgumentCaptor<Book> formObjectArgument = ArgumentCaptor.forClass(Book.class);
        verify(bookServiceMock, times(1)).update(formObjectArgument.capture());
        verify(bookServiceMock, times(1)).getPartition(0, "author", "asc");
        verifyNoMoreInteractions(bookServiceMock);
        Book formObject = formObjectArgument.getValue();
        assertThat(formObject.getBookId(), is(TestConstants.BOOK_ID));
        assertThat(formObject.getIsbn(), is(TestConstants.ISBN));
        assertThat(formObject.getAuthor(), is(TestConstants.AUTHOR));
        assertThat(formObject.getTitle(), is(TestConstants.TITLE));
    }
}