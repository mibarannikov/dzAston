import dto.CommentDto;
import dto.PostDto;
import dto.UserDto;
import entity.User;
import org.junit.Before;
import org.junit.Test;
import repository.UserRepository;
import service.CommentService;
import service.PostService;
import service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRep;
    private CommentService commentService;
    private PostService postService;

    @Before
    public void setUp() {
        userRep = mock(UserRepository.class);
        commentService = mock(CommentService.class);
        postService = mock(PostService.class);
        userService = new UserService(userRep, commentService, postService);
    }

    @Test
    public void testGetUser() {
        User user = new User();
        when(userRep.find(1)).thenReturn(user);
        when(commentService.findByUser(1)).thenReturn(Arrays.asList(new CommentDto()));
        when(postService.findByUser(1)).thenReturn(Arrays.asList(new PostDto()));

        UserDto userDto = userService.getUser(1);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertNotNull(userDto.getComments());
        assertNotNull(userDto.getPosts());
        // Другие проверки, если необходимо
    }

    @Test
    public void testCreateUser() {
        UserDto userDto = new UserDto();
        userService.creatUser(userDto);
        verify(userRep).create(any(User.class));
    }

    @Test
    public void testEditUser() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        User user = new User();
        user.setId(1);
        when(userRep.find(1)).thenReturn(user);
        userService.editeUser(userDto);
        verify(userRep).edit(any(User.class));
    }

    @Test
    public void testRemoveUser() {
        UserDto userDto = new UserDto();
        userService.removeUser(userDto);
        verify(userRep).remove(any(User.class));
    }

    @Test
    public void testGetAllUsers() {
        when(userRep.findAll()).thenReturn(Arrays.asList(new User()));
        List<UserDto> userDtos = userService.getAllUser();

        assertNotNull(userDtos);
        assertFalse(userDtos.isEmpty());
        // Другие проверки, если необходимо
    }
}
