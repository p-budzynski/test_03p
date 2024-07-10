package pl.kurs.task4.app;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Publisher {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            out.println("publisher");
            System.out.println("Enter your nickname: ");
            String nickName = scanner.nextLine();
            System.out.println("Hello " + nickName + ", enter messages to send. Type 'exit' to quit.");

            String message;
            while (true) {
                System.out.print("Message: ");
                message = scanner.nextLine();
                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }
                out.println(nickName + ": " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
