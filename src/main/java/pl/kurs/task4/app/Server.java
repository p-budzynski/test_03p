package pl.kurs.task4.app;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import static java.lang.System.out;

public class Server {
    private static final int PORT = 12345;
    private static final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private static final List<PrintWriter> consumers = Collections.synchronizedList(new ArrayList<>());
    private static final Queue<String> backlogMessages = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            out.println("Server started on port: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String clientType = in.readLine();

                if ("publisher".equalsIgnoreCase(clientType)) {
                    handlePublisher(in, out);
                } else if ("consumer".equalsIgnoreCase(clientType)) {
                    handleConsumer(out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void handlePublisher(BufferedReader in, PrintWriter out) {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (consumers.isEmpty()) {
                    backlogMessages.offer(message);
                } else {
                    messageQueue.put(message);
                }
                out.println("Message: " + message);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    private static void handleConsumer(PrintWriter out) {
        try {
            while (!backlogMessages.isEmpty()) {
                String message = backlogMessages.poll();
                out.println(message);
            }
            synchronized (consumers) {
                consumers.add(out);
            }
            while (true) {
                String message = messageQueue.take();
                broadcastMessage(message);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            synchronized (consumers) {
                consumers.remove(out);
            }
        }
    }

    private static void broadcastMessage(String message) {
        synchronized (consumers) {
            if (consumers.isEmpty()) {
                backlogMessages.offer(message);
            } else {
                for (Iterator<PrintWriter> it = consumers.iterator(); it.hasNext(); ) {
                    PrintWriter consumer = it.next();
                    consumer.println(message);
                    if (consumer.checkError()) {
                        it.remove();
                    }
                }
            }
        }
    }
}
