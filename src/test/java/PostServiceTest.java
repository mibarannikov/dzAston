import dto.CommentDto;
import dto.PostDto;
import dto.UserDto;
import entity.Post;
import entity.User;
import org.junit.Before;
import org.junit.Test;
import repository.PostRepository;
import repository.UserRepository;
import service.CommentService;
import service.PostService;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostServiceTest {

    private PostService postService;
    private PostRepository postRep;
    private UserRepository userRep;
    private CommentService commentService;

    @Before
    public void setUp() {
        postRep = mock(PostRepository.class);
        userRep = mock(UserRepository.class);
        commentService = mock(CommentService.class);
        postService = new PostService(postRep, userRep, commentService);
    }

    @Test
    public void testGetPost() {
        User user = new User();
        user.setId(1);
        Post post = new Post();
        post.setId(1);
        post.setUser(user);
        when(postRep.find(1)).thenReturn(post);
        when(commentService.findByPost(1)).thenReturn(Arrays.asList(new CommentDto()));

        PostDto postDto = postService.getPost(1);

        assertNotNull(postDto);
        assertEquals(post.getId(), postDto.getId());
        assertNotNull(postDto.getComments());
        // Другие проверки, если необходимо
    }

    @Test
    public void testCreatePost() {
        PostDto postDto = new PostDto();
        postDto.setId(1);
        UserDto userDto = new UserDto();
        userDto.setId(1);
        postDto.setUser(userDto);
        postService.creatPost(postDto);
        verify(postRep).create(any(Post.class));
    }

    @Test
    public void testEditPost() {
        PostDto postDto = new PostDto();
        postDto.setId(1);
        UserDto userDto = new UserDto();
        userDto.setId(1);
        Post post = new Post();
        post.setId(1);
        when(postRep.find(1)).thenReturn(post);
        postDto.setUser(userDto);
        postService.editePost(postDto);
        verify(postRep).edit(any(Post.class));
    }

    @Test
    public void testRemovePost() {
        PostDto postDto = new PostDto();
        postService.removePost(postDto);
        verify(postRep).remove(any(Post.class));
    }

    @Test
    public void testFindByUser() {
        when(postRep.findAllByUser(any(User.class))).thenReturn(Arrays.asList(new Post()));
        List<PostDto> postDtos = postService.findByUser(1);

        assertNotNull(postDtos);
    }

    @Test
    public void testGetAllPosts() {
        User user = new User();
        user.setId(1);
        Post post = new Post();
        post.setId(1);
        post.setUser(user);
        when(postRep.findAll()).thenReturn(Arrays.asList(post));
        List<PostDto> postDtos = postService.getAllPost();

        assertNotNull(postDtos);
        assertFalse(postDtos.isEmpty());
        // Другие проверки, если необходимо
    }
}
