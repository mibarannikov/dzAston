import dto.CommentDto;
import dto.PostDto;
import dto.UserDto;
import entity.Comment;
import entity.Post;
import entity.User;
import org.junit.Test;
import repository.CommentRepository;
import repository.PostRepository;
import repository.UserRepository;
import service.CommentService;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommentServiceTest {
    private final PostRepository postRep = mock(PostRepository.class);
    private final UserRepository userRep = mock(UserRepository.class);
    private final CommentRepository commentRep = mock(CommentRepository.class);

    private final CommentService commentService = new CommentService(postRep, userRep, commentRep);


    @Test
    public void testGetComment() {
        User user = new User();
        user.setId(1);
        Post post = new Post();
        post.setId(1);
        post.setUser(user);
        // Создание фиктивного комментария
        Comment comment = new Comment();
        comment.setId(1);
        comment.setUser(user);
        comment.setPost(post);
        when(commentRep.find(1)).thenReturn(comment);

        CommentDto commentDto = commentService.getComment(1);

        assertNotNull(commentDto);
        assertEquals(comment.getId(), commentDto.getId());
        // Другие проверки, если необходимо
    }

    @Test
    public void testCreateComment() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");
        commentDto.setUser(new UserDto(1, "username", "John"));
        commentDto.setPost(new PostDto(1, "Test post"));

        when(userRep.find(1)).thenReturn(new User());
        when(postRep.find(1)).thenReturn(new Post());

        commentService.creatComment(commentDto);

        // Проверки, что методы создания были вызваны с правильными параметрами
        verify(userRep, times(1)).find(1);
        verify(postRep, times(1)).find(1);
        verify(commentRep, times(1)).create(any(Comment.class));
    }

    @Test
    public void testEditComment() {
        // Создание фиктивного комментария
        Comment comment = new Comment();
        comment.setId(1);
        comment.setText("Old Comment");

        // Создание фиктивного CommentDto
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("New Comment");

        // Настройка поведения заглушки
        when(commentRep.find(1)).thenReturn(comment);

        // Вызов тестируемого метода
        commentService.editeComment(commentDto);

        // Проверки
        verify(commentRep).edit(comment); // Проверяем, что метод edit вызывался с корректным комментарием
        assertEquals("New Comment", comment.getText()); // Проверяем, что текст комментария изменился
    }

    @Test
    public void testEditCommentWithNullId() {
        // Создание фиктивного CommentDto с null id
        CommentDto commentDto = new CommentDto();
        commentDto.setId(null);

        // Вызов тестируемого метода
        commentService.editeComment(commentDto);

        // Убеждаемся, что метод edit не вызывается при null id
        verify(commentRep, never()).edit(any(Comment.class));
    }

    @Test
    public void testRemoveComment() {
        // Создание фиктивного комментария
        Comment comment = new Comment();
        comment.setId(1);

        // Создание фиктивного CommentDto
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);

        // Настройка поведения заглушки
        when(commentRep.find(1)).thenReturn(comment);

        // Вызов тестируемого метода
        commentService.removeComment(commentDto);

        // Проверки
        verify(commentRep).remove(comment); // Проверяем, что метод remove вызывался с корректным комментарием
    }

    @Test
    public void testFindByUser() {
        // Создание фиктивного пользователя
        User user = new User();
        user.setId(1);
        Post post = new Post();
        post.setId(1);
        post.setUser(user);
        // Создание фиктивного комментария
        Comment comment = new Comment();
        comment.setId(1);
        comment.setUser(user);
        comment.setPost(post);

        // Настройка поведения заглушек
        when(userRep.find(1)).thenReturn(user);
        when(commentRep.findAllByUser(user)).thenReturn(Arrays.asList(comment));

        // Вызов тестируемого метода
        List<CommentDto> result = commentService.findByUser(1);

        // Проверки
        assertNotNull(result);
        assertEquals(1, result.size());

        CommentDto resultCommentDto = result.get(0);
        assertEquals(comment.getId(), resultCommentDto.getId());
        assertEquals(user.getId(), resultCommentDto.getUser().getId());
        // Другие проверки, если необходимо
    }

    @Test
    public void testFindByUserWithNonexistentUser() {
        // Настройка поведения заглушки для случая, когда пользователя не существует
        when(userRep.find(1)).thenReturn(null);

        // Вызов тестируемого метода
        List<CommentDto> result = commentService.findByUser(1);

        // Проверки
        assertNotNull(result);
        assertTrue(result.isEmpty()); // Ожидаем пустой результат, так как пользователя не существует
    }

    @Test
    public void testFindByPost() {
        // Создание фиктивного поста
        User user = new User();
        user.setId(1);
        Post post = new Post();
        post.setId(1);
        post.setUser(user);
        // Создание фиктивного комментария
        Comment comment = new Comment();
        comment.setId(1);
        comment.setUser(user);
        comment.setPost(post);

        // Настройка поведения заглушек
        when(postRep.find(1)).thenReturn(post);
        when(commentRep.findAllByPost(post)).thenReturn(Arrays.asList(comment));

        // Вызов тестируемого метода
        List<CommentDto> result = commentService.findByPost(1);

        // Проверки
        assertNotNull(result);
        assertEquals(1, result.size());

        CommentDto resultCommentDto = result.get(0);
        assertEquals(comment.getId(), resultCommentDto.getId());
        assertEquals(post.getId(), resultCommentDto.getPost().getId());
        // Другие проверки, если необходимо
    }

    @Test
    public void testFindByPostWithNonexistentPost() {
        // Настройка поведения заглушки для случая, когда поста не существует
        when(postRep.find(1)).thenReturn(null);

        // Вызов тестируемого метода
        List<CommentDto> result = commentService.findByPost(1);

        // Проверки
        assertNotNull(result);
        assertTrue(result.isEmpty()); // Ожидаем пустой результат, так как поста не существует
    }

}
