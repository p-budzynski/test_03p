package pl.kurs.task4.app;

import java.io.*;
import java.net.*;

public class Consumer {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("consumer");

            System.out.println("Waiting for messages...");

            while (true) {
                String message = in.readLine();
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
