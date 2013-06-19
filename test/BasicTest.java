import org.junit.*;

import java.util.*;
import play.test.*;
import models.*;
public class BasicTest extends UnitTest {
	
	@Before
	public void setUp(){
		Fixtures.deleteDatabase();
	}
	
    @Test
    public void createAndRetRetrieveUser() {
        //create new user and save it
    	new User("bob@gmail.com","Bob Minta","123456").save();
    	
    	//retrieve user
    	User bob = User.find("byEmail","bob@gmail.com").first();
    	
    	//tests
    	assertNotNull(bob);
    	assertEquals("Bob Minta", bob.fullname);
    }
    
    @Test
    public void tryConnectAUser(){
    	new User("bob@gmail.com","Bob Minta","secret").save();
    	
    	assertNotNull(User.connect("bob@gmail.com", "secret"));
    	assertNull(User.connect("bob@gmail.com", "badpasssord"));
    	assertNull(User.connect("tom@gmail.com", "secret"));
    }
    
    @Test
    public void createPost(){
    	//creating user
    	User bob = new User("bob@gmail.com","Bob Minta","secrete").save();
    	
    	//creating new post
    	new Post(bob,"My First Post","Content is king").save();
    	
    	//tests
    	assertEquals(1, Post.count());
    	List<Post> bobPost = Post.find("byAuthor", bob).fetch();
    	assertEquals(1,bobPost.size());
    	Post firstPost = bobPost.get(0);
    	assertEquals(bob, firstPost.author);
    	assertEquals("My First Post", firstPost.title);
    	assertEquals("Content is king",firstPost.content);
    	//assertNotNull(firstPost.postedAt);
    }
    
    @Test
    public void createComment(){
    	//create user, post and comment
    	User bob = new User("bob@gmail.com","Bob Minta","secret").save();
    	Post bobPost = new Post(bob,"My Second Post","I am proud of myself").save();
    	new Comment(bobPost,"Albert Fiati","You are doing a great job").save();
    	
    	//tests
    	assertEquals(1,Comment.count());
    	assertNotNull(Comment.find("byAuthor","Albert Fiati"));
    	List<Comment> bobPostComments = Comment.find("byPost", bobPost).fetch();
    	Comment firstComment = bobPostComments.get(0);
    	assertEquals("You are doing a great job", firstComment.content);
    	assertEquals("Albert Fiati",firstComment.author);
    	//assertNotNull(firstComment.postedAt);
    }
}