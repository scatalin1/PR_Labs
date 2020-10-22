import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class XmlToJson {

    public static String convert(String string){
        String str = "";
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(string);
            String jsonString = xmlJSONObj.toString(4);
            while(jsonString.charAt(0) != '[')
                jsonString = jsonString.substring(1);
            while (jsonString.charAt(jsonString.length()-1)!=']')
                jsonString = jsonString.substring(0,jsonString.length()-2);
            int i = 2;
            while (i<jsonString.length()){
                if(jsonString.charAt(i) == '\n' && jsonString.charAt(i-2)!='}'){
                    jsonString = jsonString.substring(0,i) + jsonString.substring(i+1);
                }
                i++;
            }
            return jsonString;
        } catch (JSONException je) {
            System.out.println(je.toString());
        }
        return str;
    }
}
