import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado ao chat! Digite suas mensagens:");

            // Thread para receber mensagens
            new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Enviar mensagens do teclado
            String message;
            while ((message = console.readLine()) != null) {
                out.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


