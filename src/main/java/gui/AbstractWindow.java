package gui;

import client.Client;

import java.util.ResourceBundle;

public abstract class AbstractWindow {
    protected Client client;
    protected ResourceBundle bundle;
    protected final static short WIDTH = 1500;
    protected final static short HEIGHT = 600;

    protected AbstractWindow() {
        this.client = new Client("localhost", 2205, false);
        this.bundle = ResourceBundle.getBundle("locale");
    }
}
