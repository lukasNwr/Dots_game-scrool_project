package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.JPA.CommentServiceJPA;
import sk.tuke.gamestudio.service.JPA.RatingServiceJPA;
import sk.tuke.gamestudio.service.JPA.ScoreServiceJPA;
import sk.tuke.gamestudio.service.JPA.UserServiceJPA;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.UserService;


@SpringBootApplication
@EntityScan({"sk.tuke.gamestudio.entity"})
public class GameStudioServer {
	public static void main(String[] args) {
		SpringApplication.run(GameStudioServer.class, args);
	}

	@Bean (name = "scoreServiceServer")
	public ScoreService scoreService() {return new ScoreServiceJPA();}

	@Bean (name = "commentServiceServer")
	public CommentService commentService() {return new CommentServiceJPA();}

	@Bean (name = "ratingServiceServer")
	public RatingService ratingService() {return new RatingServiceJPA();}

	@Bean
	public UserService userService() {return new UserServiceJPA();}
}
