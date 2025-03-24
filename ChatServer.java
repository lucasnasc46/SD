import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer{
    private static final int PORT = 12345;
    private static Set<PrintWriter> clients = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Servidor de chat iniciado...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            while (true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket);

                new ClientHandler(clientSocket).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread{
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket){
            this.socket=socket;
        }

        public void run(){
            try{
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                synchronized (clients){
                    clients.add(out);
                }

                String message;
                while ((message = in.readLine()) !=null) {
                    System.out.println("Recebido: " + message);
                    broadcast(message);
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally{
                try{
                    socket.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
                synchronized (clients){
                    clients.remove(out);
                }
            }
        }

        private void broadcast (String message){
            synchronized(clients){
                for(PrintWriter client : clients){
                    client.println(message);
                }
            }
        }
    }
}
