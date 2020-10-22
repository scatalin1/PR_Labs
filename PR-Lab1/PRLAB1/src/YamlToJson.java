import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlToJson {

    public static String convert(String string) throws JsonProcessingException {
        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        Object obj = yamlReader.readValue(string, Object.class);

        ObjectMapper jsonWriter = new ObjectMapper();
        String str = jsonWriter.writeValueAsString(obj);
        int i = 0;
        while(i < str.length()){
            if (str.charAt(i) == '}' && str.charAt(i+1) != ']'){
                str = str.substring(0,i+2) + "\n" + str.substring(i+3);
            }
            i++;
        }
        return str;
    }
}
