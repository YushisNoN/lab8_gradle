package org.example;

import gui.main.AuthorizationWindow;

public class Main {
    public static final Thread mainThread = Thread.currentThread();
    public static void main(String[] args) {

        AuthorizationWindow.launch(AuthorizationWindow.class, args);

    }
}


