import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost",8081)){
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter printString = new PrintWriter(socket.getOutputStream(),true);

            Scanner scanner = new Scanner(System.in);
            String string;
            String response;
            do{
                System.out.println("\nType the command: \n");
                string = scanner.nextLine();

                printString.println(string);
                if(!string.equals("exit")){
                    response = reader.readLine();
                    System.out.println(response);
                }
            } while (!string.equals("exit"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
