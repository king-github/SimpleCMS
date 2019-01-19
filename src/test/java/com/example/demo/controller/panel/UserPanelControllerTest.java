package com.example.demo.controller.panel;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.error.NotFoundException;
import com.example.demo.form.panel.UserForm;
import com.example.demo.form.panel.UserFormUserConverter;
import com.example.demo.helper.FormHelper;
import com.example.demo.helper.FormHelperFactory;
import com.example.demo.helper.OrderModeHelper;
import com.example.demo.helper.PageSizeHelper;
import com.example.demo.services.UserService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration
public class UserPanelControllerTest {


    @Mock
    private UserService userServiceMock;

    @Mock
    private OrderModeHelper sortModeHelperMock;

    @Mock
    private PageSizeHelper pageSizeHelperMock;

    @Mock
    private FormHelperFactory formHelperFactoryMock;

    @Mock
    private UserFormUserConverter userFormUserConverterMock;

    @Mock
    private Model modelMock;

    @Mock
    private User userMock;
    @Mock
    private List<Role> rolesMock;

    @Mock
    private Page<User> pageMock;

    @Mock
    private Pageable pageableMock;

    @Mock
    private FormHelper formHelper;

    private PageRequest paramPagerMock;

    private PageRequest pagerMock;

    UserPanelController userPanelController;

    @Mock
    private UserForm userFormMock;
    @Mock
    private BindingResult bindingResultMock;
    @Mock
    private RedirectAttributes redirectAttributes;

    private List<User.UserStatus> allStatuses = User.UserStatus.getAllStatuses();

    @Before
    public void setUp() throws Exception {

        userPanelController = new UserPanelController();

        ReflectionTestUtils.setField(userPanelController, "userService", userServiceMock);
        ReflectionTestUtils.setField(userPanelController, "sortModeHelper", sortModeHelperMock);
        ReflectionTestUtils.setField(userPanelController, "pageSizeHelper", pageSizeHelperMock);
        ReflectionTestUtils.setField(userPanelController, "formHelperFactory", formHelperFactoryMock);
        ReflectionTestUtils.setField(userPanelController, "userFormUserConverter", userFormUserConverterMock);

        pagerMock = PageRequest.of(1,10,Sort.by("name"));
    }

    @Test
    public void whenIndex_ThenShowPageOfUsers() {

        paramPagerMock = PageRequest.of(2,12,Sort.by("id"));

        when(userServiceMock.getAllUsers(any(Pageable.class))).thenReturn(pageMock);

        String result = userPanelController.index(modelMock, paramPagerMock, pagerMock);

        assertEquals("panel/user/index", result);
        verify(userServiceMock, times(1)).getAllUsers(paramPagerMock);
    }

    @Test
    public void givenCorrectId_whenEdit_ThenShowEditForm() {

        Long id = 123L;

        when(userServiceMock.findUserById(id)).thenReturn(Optional.of(userMock));
        when(userServiceMock.getAllRoles()).thenReturn(rolesMock);
        when(formHelperFactoryMock.makeErrorFormHelper(anyMap())).thenReturn(formHelper);

        String result = userPanelController.edit(modelMock, id);

        assertEquals("panel/user/edit", result);
        verify(userServiceMock, times(1)).findUserById(id);
        verify(userServiceMock, times(1)).getAllRoles();

        verify(modelMock, times(1)).addAttribute(eq("roles"), eq(rolesMock));
        verify(modelMock, times(1)).addAttribute(eq("form"), eq(formHelper));
        verify(modelMock, times(1)).addAttribute(eq("title"), anyString());
        verify(modelMock, times(1)).addAttribute(eq("statuses"), eq(allStatuses));

    }

    @Test(expected = NotFoundException.class)
    public void givenNoExisedId_whenEdit_ThenThrowException() {

        Long id = 666L;

        when(userServiceMock.findUserById(id)).thenReturn(Optional.empty());

        userPanelController.edit(modelMock, id);
    }

    @Test
    public void givenCorrectForm_whenSave_thenSaveAndShowForm() {

        Long id = 123L;

        when(bindingResultMock.hasErrors()).thenReturn(false);
        when(userServiceMock.findUserById(id)).thenReturn(Optional.of(userMock));
        when(userServiceMock.save(any())).thenReturn(userMock);
        when(userMock.getUsername()).thenReturn("user");
        when(userFormUserConverterMock.toUser(any(), any())).thenReturn(userMock);
        when(userFormMock.getSubmit()).thenReturn("stay");

        String result = userPanelController.save(modelMock, id, userFormMock,
                bindingResultMock, redirectAttributes, pagerMock);

        assertEquals("panel/user/edit", result);
    }

    @Test
    public void givenCorrectForm_whenSaveAndBack_thenSaveAndShowList() {

        Long id = 123L;

        when(bindingResultMock.hasErrors()).thenReturn(false);
        when(userServiceMock.findUserById(id)).thenReturn(Optional.of(userMock));
        when(userServiceMock.save(any())).thenReturn(userMock);
        when(userMock.getUsername()).thenReturn("user");
        when(userFormUserConverterMock.toUser(any(), any())).thenReturn(userMock);
        when(userFormMock.getSubmit()).thenReturn("back");

        String result = userPanelController.save(modelMock, id, userFormMock,
                bindingResultMock, redirectAttributes, pagerMock);

        assertThat(result, Matchers.startsWith("redirect:/panel/user"));
    }

    @Test
    public void givenInvalidForm_whenSave_thenNotSaveAndBackToForm() {

        Long id = 123L;

        when(bindingResultMock.hasErrors()).thenReturn(true);

        String result = userPanelController.save(modelMock, id, userFormMock,
                bindingResultMock, redirectAttributes, pagerMock);

        assertEquals("panel/user/edit", result);
        verify(userServiceMock, never()).save(any());
        verify(formHelperFactoryMock, times(1)).makeErrorFormHelper(bindingResultMock);
    }

}