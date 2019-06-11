package sk.tuke.gamestudio.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.game.dots.novorolnik.webui.WebUI;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.UserService;
import sk.tuke.gamestudio.service.exceptions.CommentException;
import sk.tuke.gamestudio.service.exceptions.RatingException;

import java.util.Date;
import java.util.List;

//http://localhost:8080/dots-novorolnik
@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping(value = {"/dots-novorolnik", "/gamestudio_lukas_novorolnik/dots-novorolnik"})
public class DotsNovorolnikController {

    private final WebUI webUI;
    private User loggedUser;

    private final ScoreService scoreService;
    private final RatingService ratingService;
    private final CommentService commentService;
    private final UserService userService;


    public DotsNovorolnikController(ScoreService scoreService,
                                    RatingService ratingService,
                                    CommentService commentService,
                                    UserService userService) {
        this.scoreService = scoreService;
        this.ratingService = ratingService;
        this.commentService = commentService;
        this.userService = userService;
        this.webUI = new WebUI(scoreService, ratingService);

    }

    @RequestMapping
    public String dots(@RequestParam(value = "command", required = false) String command,
                        @RequestParam(value = "row", required = false) String row,
                        @RequestParam(value = "column", required = false) String column,
                        Model model) {

        // if required, add additional code, e.g. to check provided parameters for null
        if (command == null) {
            command = ((row != null) && (column != null)) ? "mark" :  "new";
        }

        webUI.processCommand(command, row, column);
        model.addAttribute("webUI", webUI);
        model.addAttribute("scores", scoreService.getBestScores("dots-novorolnik"));

        List<Comment> comments = null;
        try {
            comments = commentService.getComments("dots-novorolnik");
        }catch (CommentException e){
            System.out.println(e.getCause());
        }
        model.addAttribute("comments", comments);

//        int avgRating = 0;
//        try {
//            avgRating = ratingService.getAverageRating("dots-novorolnik");
//        }catch (RatingException e){
//            System.out.println(e.getCause());
//        }
//        model.addAttribute("avgRating", avgRating);

        return "dots-novorolnik"; //same name as the template
    }

    @RequestMapping("/username")
    public String username(@RequestParam(value = "name", required = false) String name, Model model) {
        if(name == null) System.out.println("username = null");

        webUI.processUsername(name);
        model.addAttribute("webUI", webUI);
        return "dots-novorolnik";
    }

    @RequestMapping(value = "/comment")
    public String comment(Comment comment, Model model) {
        model.addAttribute("webUI", webUI);
        comment.setGame("dots-novorolnik");
        comment.setPlayer(webUI.getUserName());
        comment.setCommentedOn(new Date());
        try {
            commentService.addComment(comment);
        }catch (CommentException e){
            System.out.println(e.getCause());
        }
        updatePage(model);
        return "dots-novorolnik";
    }

    @RequestMapping("/rate")
    public String rate(Rating rating, Model model) {
        model.addAttribute("webUI", webUI);
        if(rating.getRating() < 0 || rating.getRating() > 10) {
            return "dots-novorolnik";
        }
        rating.setGame("dots-novorolnik");
        rating.setPlayer(webUI.getUserName());
        rating.setRatedon(new Date());
        try {
            ratingService.setRating(rating);
        }catch (RatingException e){
            System.out.println(e.getCause());
        }
        updatePage(model);
        return "dots-novorolnik";
    }

    @RequestMapping("/user")
    public String user(Model model) {
        return "dots-novorolnik_login";
    }

    @RequestMapping("/login")
    public String login(User user, Model model) {
        user = userService.login(user.getUsername(), user.getPassword());
        loggedUser = user;
        return "dots-novorolnik_login";
    }

    @RequestMapping("/register")
    public String register(User user, Model model) {
        user = userService.register(user.getUsername(), user.getPassword());
        loggedUser = user;
        return "dots-novorolnik_login";
    }

//    @RequestMapping("/logout")
//    public String logout() {
//        loggedUser = null;
//        return"index";
//    }

//    @RequestMapping({"", "/index"})
//    public String index() {
//        return "index";
//    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void updatePage(Model model) {
        List<Comment> comments = null;
        try {
            comments = commentService.getComments("dots-novorolnik");
        }catch (CommentException e){
            System.out.println(e.getCause());
        }
        model.addAttribute("comments", comments);
        model.addAttribute("scores", scoreService.getBestScores("dots-novorolnik"));
    }

    public boolean isUserLogged() {
        return loggedUser != null;
    }

}
