import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Server extends Thread{
    private Socket socket;

    public Server(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

            while (true){
                String readString = input.readLine();

                if(readString.startsWith("SelectColumn")){
                    int i=0;
                    while (readString.charAt(i)!=' '){
                        i++;
                    }
                    String s = readString.substring(i+1);
                    int j=0;
//        System.out.println(s);
                    while (j<Traverser.title.size() && !Traverser.title.get(j).equals(s)) {
                        j++;
                    }
                    if(j == Traverser.title.size())
                        output.println("There is no such column");
                    s = "";
                    for(i =0; i<Traverser.arrayList.size();i++){
                        if(Traverser.arrayList.get(i)[j]!=null){
                            s+=Traverser.arrayList.get(i)[j] + "  ";
                        }
                    }
                    output.println(s);
                }
                else if (readString.startsWith("SelectRow")){
                    int i=0;
                    while (readString.charAt(i)!=' '){
                        i++;
                    }
                    String s = readString.substring(i+1);
                    i = Integer.parseInt(s);
                    s = "";
                    for(int j = 0; j < Traverser.arrayList.get(i).length; j++){
                        if(Traverser.arrayList.get(i)[j] != null){
                            s += Traverser.arrayList.get(i)[j] + "  ";
                        }
                    }
                    if(s.equals(""))
                        output.println("No such row");
                    output.println(s);
                }
                else if (readString.startsWith("SelectCell")){
                    int i=0; int k=0;
                    while (readString.charAt(i)!=' '){
                        i++;
                    }
                    k = i+1;
                    i++;
                    while (readString.charAt(i) != ' ') {
                        i++;
                    }
                    String row = readString.substring(k,i);
                    String column = readString.substring(i+1);
                    int j=0;
                    i = Integer.parseInt(row);
                    while (j<Traverser.title.size() && !Traverser.title.get(j).equals(column)) {
                        j++;
                    }
                    if(j == Traverser.title.size())
                        output.println( "There is no such column");
                    String s = Traverser.arrayList.get(i)[j];
                    if(s.equals(""))
                        output.println( "Cell is null");
                    output.println(s);
                }
                else if (readString.equals("exit")){
                    break;
                }
                else if (readString.equals("")){
                    output.println("Enter the command");
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                socket.close();
            } catch (IOException e){
            }
        }
    }
}