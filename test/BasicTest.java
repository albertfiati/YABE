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
    	new Comment(bobPost,"Julliet Joyce","Interesting post").save();
    	
    	//tests
    	assertEquals(2,Comment.count());
    	assertNotNull(Comment.find("byAuthor","Albert Fiati"));
    	List<Comment> bobPostComments = Comment.find("byPost", bobPost).fetch();
    	Comment firstComment = bobPostComments.get(0);
    	Comment secondComment = bobPostComments.get(1);
    	assertEquals("You are doing a great job", firstComment.content);
    	assertEquals("Interesting post",secondComment.content);
    	//assertNotNull(firstComment.postedAt);
    }
    
    @Test
    public void useTheCommentsRelation(){
    	User bob = new User("bob@gmail.com","Bob Minta","secret").save();
    	Post bobPost = new Post(bob,"Ghana Leaks","Ghana dey Bii K3k3").save();
    	
    	bobPost.addComment("Helen Odame", "This is great news");
    	bobPost.addComment("Mansah Ghana","Which part of Ghana dey biiii?");
    	
    	assertEquals(1,Post.count());
    	assertEquals(1,User.count());
    	assertEquals(2, Comment.count());
    	
    	bobPost = Post.find("byAuthor", bob).first();
    	assertNotNull(bobPost);
    	assertEquals(2, bobPost.comments.size());
    	assertEquals("Helen Odame", bobPost.comments.get(0).author);
    	
    	bobPost.delete();
    	
    	assertEquals(0, Post.count());
    	assertEquals(1,User.count());
    	assertEquals(0, Comment.count());
    	
    }
}