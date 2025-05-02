package lii.autotexttprocessor.exception;

public class ProcessingException {

    private String message;
    private Throwable cause;

    public ProcessingException(String message) {
        this.message = message;
    }

    public ProcessingException(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return "ProcessingException{" +
                "message='" + message + '\'' +
                ", cause=" + cause +
                '}';
    }
}
