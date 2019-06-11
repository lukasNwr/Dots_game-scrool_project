package sk.tuke.gamestudio.service.exceptions;

public class RatingException extends Exception {
    public RatingException(String message) {
        super(message);
    }

    public RatingException(String message, Throwable cause) {
        super(message, cause);
    }
}
