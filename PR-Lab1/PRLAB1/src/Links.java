import java.util.LinkedList;
import java.util.Queue;

public class Links {

    public static Queue<String> routesLinks = new LinkedList<>();

    public static Queue<String> getRoutesLinks() {
        return routesLinks;
    }

    public static void setRoutesLinks(String s) {
        Links.routesLinks.add(s);
    }

}
