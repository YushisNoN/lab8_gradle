package utils.console;

import java.io.InputStream;
import java.util.Scanner;



public class ConsoleHandler implements Console {

    private Scanner inputScanner;
    private InputStream currentInputSource;

    public ConsoleHandler() {
        this.inputScanner = new Scanner(System.in);
    }
    public ConsoleHandler(InputStream inputStream) {
        this.currentInputSource = inputStream;
        this.inputScanner = new Scanner(inputStream);
    }

    public void setInputSource(InputStream inputStream) {
        this.currentInputSource = inputStream;
        this.inputScanner = new Scanner(inputStream);
    }

    @Override
    public String getInputString() {

        String tmp = this.inputScanner.nextLine();
        System.out.println(tmp);
        return tmp;
    }

    public Scanner getInputStream() {
        return this.inputScanner;
    }

    @Override
    public void printString(String outputString) {
        System.out.print(outputString);
    }

    @Override
    public String toString() {
        return "Console";
    }

    @Override
    public void printStringln(String outputString) {
        System.out.println(outputString);
    }
}
