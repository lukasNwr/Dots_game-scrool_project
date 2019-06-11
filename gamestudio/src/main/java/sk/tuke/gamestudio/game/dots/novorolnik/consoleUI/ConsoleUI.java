package sk.tuke.gamestudio.game.dots.novorolnik.consoleUI;


import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.dots.novorolnik.core.Field;
import sk.tuke.gamestudio.game.dots.novorolnik.core.GameState;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.exceptions.CommentException;
import sk.tuke.gamestudio.service.exceptions.RatingException;
import sk.tuke.gamestudio.service.exceptions.ScoreException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    private Field field;
    private int scoreValue = 0;

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private CommentService commentService;

    final Pattern INPUT_PATTERN = Pattern.compile("(M|R)([A-Z])([1-9][0-9]*)");
    BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

    public ConsoleUI(Field field) {
        this.field = field;
    }

    private String readLine() {
        try {
            return buffer.readLine();
        } catch (IOException e) {
            System.err.println("Error while reading input, please try again!");
            return "";
        }
    }

    private void show() {
        System.out.println("Moves: " + field.getMoves());
        System.out.println("Score: " + scoreValue);
        //show header
        System.out.printf("%3s", "");
        for (int column = 0; column < field.getColCount(); column++) {
            System.out.printf("%3s", column + 1);
        }
        System.out.println();
        //print body
        System.out.println(field);

    }

    private void handleInput() {
        System.out.println("Enter input (M - mark, R - reset) A1 (tile A1): ");
        String line = readLine();

        Matcher m = INPUT_PATTERN.matcher(line);
        if (m.matches()) {
            int row = m.group(2).charAt(0) - 65;
            int col = Integer.parseInt(m.group(3)) - 1;
            if (m.group(1).equals("M")) {
                field.markTile(row, col);
            } else if (m.group(1).equals("R")) {
                field.resetTile(row, col);
            }
        } else if (line.equals("X")) {
            System.exit(0);
        } else if (line.equals("Y")) {
            field.finish();
            scoreValue = field.getScoreValue();
            field.resetMarkedTile();
        } else System.out.println("You have entered wrong input, try again!");
    }

    public void play() {

        while (field.getGameState() == GameState.PLAYING) {
            show();
            handleInput();
        }
        if (field.getGameState() == GameState.WON) {
            System.out.println("You have WON!");
        } else if (field.getGameState() == GameState.LOST) {
            System.out.println("You have LOST!");
        } else if (field.getGameState() == GameState.END) {
            System.out.println("You have no more moves, end of the Game!");
        }

        Scanner scanner = new Scanner(System.in);

        //SCORE
        Date date = new Date();
        System.out.println("Enter your name: ");
        String name = scanner.next(Pattern.compile("[a-zA-Z]+"));
        System.out.println("Your score is: " + scoreValue);
        try {
            scoreService.addScore(new Score(
                    "dots-novorolnik", name, scoreValue, date
            ));
            System.out.println("Your score was added to database");
            //System.out.println(scoreValue);
        } catch (ScoreException e) {
            System.err.println(e.getMessage());
        }


        //RATING
        String ratingInput;
        System.out.println("Please enter rating for this game(1-10):");
        try {
            scanner = new Scanner(System.in);
            ratingInput = scanner.next(Pattern.compile("([1-9]|10)"));
        } catch (InputMismatchException e) {
            System.out.println("Wrong rating! Try again");
            scanner = new Scanner(System.in);
            ratingInput = scanner.next(Pattern.compile("([1-9]|10)"));
        }
        try {
            ratingService.setRating(new Rating(
                    name, "dots", Integer.parseInt(ratingInput), date
            ));
            System.out.println("You have successfully rated the game, thank you!");
        } catch (RatingException e) {
            System.out.println(e.getMessage());
        }
        int avgRating = 0;
        try {
            avgRating = ratingService.getAverageRating("dots-novorolnik");
        }catch (RatingException e){
            System.out.println(e.getCause());
        }

        //COMMENT
        System.out.println("Do you want to add comment? Y/N");
        String line = readLine();
        if (line.equals("Y")) {
            try {
                String commentInput = scanner.next();
                commentService.addComment(new Comment(
                        name, "dots-novorolnik", commentInput, date
                ));
            } catch (CommentException e) {
                System.out.println(e.getMessage());
            }
        } else System.exit(0);

        System.out.println("BEST SCORES");

        List<Score> bestScores = scoreService.getBestScores("dots-novorolnik");
        for(Score score : bestScores) {
            System.out.println("Player: " + score.getPlayer() + " Score: " + score.getPoints());
        }
    }
}
