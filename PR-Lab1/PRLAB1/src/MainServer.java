import java.io.IOException;
import java.net.ServerSocket;

public class MainServer {
    public static void run(){
        try (ServerSocket serverSocket = new ServerSocket(8081)){
            while(true){
                new Server(serverSocket.accept()).start();
            }
        } catch (IOException e){
        }
    }
}
