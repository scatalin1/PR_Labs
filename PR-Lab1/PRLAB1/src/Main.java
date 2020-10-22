import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static String getJSON(String link, String header) {
        HttpURLConnection c = null;
        try {
            String url = "http://localhost:5000";
            URL u = new URL(url + link);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("X-Access-Token", header);
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String string = getJSON("/register", "");
        JSONObject json = new JSONObject(string);
        String value = json.getString("access_token");

        String homeLink = getJSON("/home", value);

        Traverser traverser = new Traverser("/home", value);
        traverser.run();
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        while (!Traverser.getLinks().getRoutesLinks().isEmpty()) {
            while (!Traverser.getLinks().getRoutesLinks().isEmpty()) {
                Traverser traverser1 = new Traverser(Traverser.getLinks().getRoutesLinks().element(), value);
                executorService.execute(traverser1);
                Traverser.getLinks().getRoutesLinks().remove();
            }

            TimeUnit.MILLISECONDS.sleep(6100);
        }
        executorService.shutdown();
        try {
                        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                    } catch (InterruptedException e) {
                    }

        Traverser.parse();
        MainServer.run();
    }
}

