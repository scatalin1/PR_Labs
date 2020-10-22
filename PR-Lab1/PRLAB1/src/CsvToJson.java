import org.json.JSONObject;

public class CsvToJson {

    public static String convert(String string) throws Exception {
        String strings[] = string.split("\\r?\\n");
        String title[] = strings[0].split("[,]", 0);
        String strJson = "[";
        for (int i = 1; i < strings.length; i++){
            JSONObject jsonObject = new JSONObject();
            String new_strings[] = strings[i].split("[,]", 0);
            for(int j = 0; j < title.length; j++){
                jsonObject.put(title[j],new_strings[j]);
            }
            strJson += jsonObject.toString() + ",\n";
        }
        strJson = strJson.substring(0, strJson.length()-2) + "]";
        return strJson;
    }
}