package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import sk.tuke.gamestudio.game.dots.novorolnik.consoleUI.ConsoleUI;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.REST.CommentServiceRestClient;
import sk.tuke.gamestudio.service.REST.RatingServiceRestClient;
import sk.tuke.gamestudio.service.REST.ScoreServiceRestClient;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;


@Configuration
@SpringBootApplication
@ComponentScan(basePackages = { "sk.tuke.gamestudio" },
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "sk.tuke.gamestudio.server.*"))
public class SpringClient {
    public static void main(String[] args) {
        //SpringApplication.run(SpringClient.class, args);
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runner(sk.tuke.gamestudio.game.dots.novorolnik.consoleUI.ConsoleUI ui) {
        return (args) -> ui.play();
    }

    @Bean
    public sk.tuke.gamestudio.game.dots.novorolnik.consoleUI.ConsoleUI dotsNovorolnikConsoleUI(sk.tuke.gamestudio.game.dots.novorolnik.core.Field field) {
        return new ConsoleUI(field);
    }

    @Bean
    public sk.tuke.gamestudio.game.dots.novorolnik.core.Field dotsNovorolnikField() {
        return new sk.tuke.gamestudio.game.dots.novorolnik.core.Field(5, 5, 3);
    }

    @Bean
    public ScoreService scoreService() {
        //return new ScoreServiceJPA();
        return  new ScoreServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        //return new CommentServiceJPA();
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        //return new RatingServiceJPA();
        return new RatingServiceRestClient();
    }
}
