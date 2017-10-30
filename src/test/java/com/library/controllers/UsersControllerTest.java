package com.library.controllers;

import com.library.TestConstants;
import com.library.dao.services.UserService;
import com.library.model.User;
import com.library.model.UserBuilder;
import org.apache.commons.codec.digest.DigestUtils;
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
public class UsersControllerTest {
    private MockMvc mockMvc;

    @Mock(name = "userService")
    private UserService userServiceMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new UsersController(userServiceMock))
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
    public void getUserListTest() throws Exception {
        User first = new UserBuilder()
                .userId(1L)
                .login("user1")
                .password(TestConstants.PASSWORD)
                .build();
        User second = new UserBuilder()
                .userId(2L)
                .login("user2")
                .password(TestConstants.PASSWORD)
                .build();
        when(userServiceMock.getAll()).thenReturn(Arrays.asList(first, second));
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userList", hasSize(2)))
                .andExpect(model().attribute("userList", hasItem(
                        allOf(
                                hasProperty("userId", is(1L)),
                                hasProperty("login", is("user1")),
                                hasProperty("password", is(TestConstants.PASSWORD))
                        )
                )))
                .andExpect(model().attribute("userList", hasItem(
                        allOf(
                                hasProperty("userId", is(2L)),
                                hasProperty("login", is("user2")),
                                hasProperty("password", is(TestConstants.PASSWORD))
                        )
                )));
        verify(userServiceMock, times(1)).getAll();
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    public void deleteUserTest() throws Exception {
        when(userServiceMock.delete(TestConstants.USER_ID)).thenReturn(true);
        mockMvc.perform(post("/user/delete")
                        .param("userId", TestConstants.USER_ID.toString()))
                .andExpect(status().isOk());
        verify(userServiceMock, times(1)).delete(TestConstants.USER_ID);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    public void getAddUserModalTest() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("userForm"))
                .andExpect(model().attribute("user", allOf(
                            hasProperty("userId", nullValue()),
                            hasProperty("login", isEmptyOrNullString()),
                            hasProperty("password", isEmptyOrNullString())
                )));
        verifyZeroInteractions(userServiceMock);
    }

    @Test
    public void getEditUserModalTest() throws Exception {
        User user = new UserBuilder()
                .userId(TestConstants.USER_ID)
                .login(TestConstants.LOGIN)
                .password(TestConstants.PASSWORD)
                .build();
        when(userServiceMock.getById(TestConstants.USER_ID)).thenReturn(user);
        mockMvc.perform(get("/user/{userId}", TestConstants.USER_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("userForm"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("userId", is(TestConstants.USER_ID)),
                        hasProperty("login", is(TestConstants.LOGIN)),
                        hasProperty("password", is(TestConstants.PASSWORD))
                )));
        verify(userServiceMock, times(1)).getById(TestConstants.USER_ID);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    public void saveNewUserTest() throws Exception {
        User created = new UserBuilder()
                .userId(null)
                .login(TestConstants.LOGIN)
                .password(TestConstants.PASSWORD)
                .build();
        when(userServiceMock.create(created)).thenReturn(true);
        mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("login", TestConstants.LOGIN)
                    .param("password", TestConstants.PASSWORD))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users"));
        ArgumentCaptor<User> formObjectArgument = ArgumentCaptor.forClass(User.class);
        verify(userServiceMock, times(1)).create(formObjectArgument.capture());
        verifyNoMoreInteractions(userServiceMock);
        User formObject = formObjectArgument.getValue();
        assertNull(formObject.getUserId());
        assertThat(formObject.getLogin(), is(TestConstants.LOGIN));
        assertThat(formObject.getPassword(), is(DigestUtils.sha256Hex(TestConstants.PASSWORD)));
    }

    @Test
    public void saveEditedUserTest() throws Exception {
        User edited = new UserBuilder()
                .userId(TestConstants.USER_ID)
                .login(TestConstants.LOGIN)
                .password(TestConstants.PASSWORD)
                .build();
        when(userServiceMock.update(edited)).thenReturn(true);
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userId", TestConstants.USER_ID.toString())
                .param("login", TestConstants.LOGIN)
                .param("password", TestConstants.PASSWORD))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users"));
        ArgumentCaptor<User> formObjectArgument = ArgumentCaptor.forClass(User.class);
        verify(userServiceMock, times(1)).update(formObjectArgument.capture());
        verifyNoMoreInteractions(userServiceMock);
        User formObject = formObjectArgument.getValue();
        assertThat(formObject.getUserId(), is(TestConstants.USER_ID));
        assertThat(formObject.getLogin(), is(TestConstants.LOGIN));
        assertThat(formObject.getPassword(), is(DigestUtils.sha256Hex(TestConstants.PASSWORD)));
    }
}