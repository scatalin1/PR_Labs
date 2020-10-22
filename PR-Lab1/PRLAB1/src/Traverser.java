import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Traverser implements Runnable {
    private String header;
    private String link_data;
    private static Links links = new Links();

    public static ArrayList<String> data = new ArrayList<>();
    public static ArrayList<String> mime_type = new ArrayList<>();
    public static ArrayList<String> title = new ArrayList<>();
    public static ArrayList<String[]> arrayList = new ArrayList<>();

    public Traverser(String data, String header) {
        this.header = header;
        this.link_data = data;
    }

    @Override
    public void run() {
        try {
            String str = Main.getJSON(link_data, header);
            JSONObject dataJSONObject = new JSONObject(str);
            if (dataJSONObject.has("data")){
                String data_from_link = dataJSONObject.getString("data");
                data.add(data_from_link);
                if(dataJSONObject.has("mime_type")){
                    String type_from_link = dataJSONObject.getString("mime_type");
                    mime_type.add(type_from_link);
                }
                else {
                    mime_type.add("json");
                }
            }
            if (dataJSONObject.has("link")) {
                String newData = dataJSONObject.getString("link");
                JSONObject newDataJSONObject = new JSONObject(newData);
                for (int i = newDataJSONObject.names().length() - 1; i >= 0; i--) {
                    String value = newDataJSONObject.get(newDataJSONObject.names().getString(i)).toString();
                    links.setRoutesLinks(value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void print(){
        int i = 0;
        while (i<title.size()){
            System.out.print(title.get(i) + "   ");
            i++;
        }
        System.out.println();
        i = 0; int j;
        while (i<arrayList.size()){
            j = 0;
            while (j<title.size()){
                System.out.print(arrayList.get(i)[j] + "     ");
                j++;
            }
            System.out.println();
            i++;
        }
    }

    public static Links getLinks() {
        return links;
    }

    public static void convertToJson() throws Exception {
        int i = 0;
        while (i < mime_type.size()){
            if (mime_type.get(i).contains("csv")){
                String string = data.get(i);
                String content = CsvToJson.convert(string);
                data.remove(i);
                mime_type.remove(i);
                data.add(content);
                mime_type.add("json");
            }
            else if (mime_type.get(i).contains("xml")){
                String string = data.get(i);
                String content = XmlToJson.convert(string);
                data.remove(i);
                mime_type.remove(i);
                data.add(content);
                mime_type.add("json");
            }
            else if (mime_type.get(i).contains("yaml")){
                String string = data.get(i);
                String content = YamlToJson.convert(string);
                data.remove(i);
                mime_type.remove(i);
                data.add(content);
                mime_type.add("json");
            }
            else i++;
        }
    }
    public static String readFile() throws IOException {
        File file = new File("data.json");
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    public static void parse() throws Exception {
        convertToJson();
        for (int i = 0; i < Traverser.data.size(); i++) {
            String string = Traverser.data.get(i);
            int j = 0;
                while (string.charAt(0) != '{') {
                    string = string.substring(1);
                }
                while (string.charAt(string.length()-1)!='}'){
                    string = string.substring(0,string.length()-2);
                }
        List<String> lines = Arrays.asList(string.split("},", 0));
                while (j<lines.size()){
                    List<String> cells = Arrays.asList(lines.get(j).split(",", 0));
                    List<String> values = new ArrayList<>(), types = new ArrayList<>();
                    for(int k = 0; k < cells.size(); k++){
                        String[] cell = cells.get(k).split(":",0);
                        int index = 0; String value = "", type = "";
                        while (index<cell[0].length()){
                            if(cell[0].charAt(index)!='"' && cell[0].charAt(index)!='{' && cell[0].charAt(index)!='\n' && cell[0].charAt(index)!=' '){
                                type += cell[0].charAt(index);
                            }
                            index++;
                        }
                        types.add(type);
                        index = 0;
                        while (index<cell[1].length()){
                            if(cell[1].charAt(index)!='"' && cell[1].charAt(index)!='}' && cell[1].charAt(index)!=' '){
                                value += cell[1].charAt(index);
                            }
                            index++;
                        }
                        values.add(value);
                    }
                    String[] line = new String[100];
                    if(types.contains("email") && title.contains("email")){
                        String current_email = values.get(types.indexOf("email"));
                        int indexTitle = title.indexOf("email");
                        if(title.get(indexTitle) != null) {
                            int m;
                            for (m = 0; m < arrayList.size(); m++) {
                                if (arrayList.get(m)[indexTitle]!=null && arrayList.get(m)[indexTitle].equals(current_email))
                                    break;
                            }
                            if (m == arrayList.size()) {
                                for (int k = 0; k < types.size(); k++) {
                                    int index;
                                    if (!title.contains(types.get(k))) {
                                        title.add(types.get(k));
                                    }
                                    index = title.indexOf(types.get(k));
                                    line[index] = values.get(k);
                                }
                                arrayList.add(line);
                            } else {
                                for (int k = 0; k < types.size(); k++) {
                                    int index;
                                    if (!title.contains(types.get(k))) {
                                        title.add(types.get(k));
                                    }
                                    index = title.indexOf(types.get(k));
                                    arrayList.get(m)[index] = values.get(k);
                                }
                            }
                        }
                        else{
                            for(int k = 0; k < types.size(); k++){
                                int index;
                                if(!title.contains(types.get(k))){
                                    title.add(types.get(k));
                                }
                                index = title.indexOf(types.get(k));
                                line[index] = values.get(k);
                            }
                            arrayList.add(line);
                        }
                    }
                    else{
                        for(int k = 0; k < types.size(); k++){
                            int index;
                            if(!title.contains(types.get(k))){
                                title.add(types.get(k));
                            }
                            index = title.indexOf(types.get(k));
                            line[index] = values.get(k);
                        }
                        arrayList.add(line);
                    }
                    j++;
                }
        }
        print();
    }
}
