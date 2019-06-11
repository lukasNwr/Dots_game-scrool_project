package sk.tuke.gamestudio.game.dots.novorolnik;

import sk.tuke.gamestudio.game.dots.novorolnik.consoleUI.ConsoleUI;
import sk.tuke.gamestudio.game.dots.novorolnik.core.Field;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Dots {
    public static void main(String[] args) {

        System.out.println("Welcome to the Dots game, please choose size of game field (4 - 10)");
        Scanner scanner;
        String size;
        try {
            scanner = new Scanner(System.in);
            size = scanner.next(Pattern.compile("(([4-9])|(10))"));
        }
        catch (InputMismatchException e){
            System.out.println("Wrong size! Try again");
            scanner = new Scanner(System.in);
            size = scanner.next(Pattern.compile("(([4-9])|(10))"));
        }

        Field field = new Field(Integer.parseInt(size), Integer.parseInt(size), 5);
        ConsoleUI ui = new ConsoleUI(field);
        ui.play();
    }

}
