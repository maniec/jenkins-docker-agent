package net.maniec.app;

public class App {

    private final String message = "Hello World!";

    public App() {
    }

    public static void main(String[] args) {
        System.out.println(new App().getMessage());
    }

    private String getMessage() {
        return message;
    }

}