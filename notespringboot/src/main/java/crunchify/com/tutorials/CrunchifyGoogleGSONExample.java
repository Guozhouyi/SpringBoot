
package crunchify.com.tutorials;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.internal.LinkedTreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * @author Crunchify.com
 * Gson() -> fromJson() to deserializes the specified Json into an object of the specified class
 */

public class CrunchifyGoogleGSONExample {

    public static void main(String[] args) {
        JSONArray array = readFileContent();
        convertJSONArraytoArrayList(array);
    }

    private static void convertJSONArraytoArrayList(JSONArray array) {

        // Use method fromJson() to deserializes the specified Json into an object
        // of the specified class
        //final ArrayList<?> jsonArray = new Gson().fromJson(array.toString(), ArrayList.class);
        final List<LinkedTreeMap<String,String>> jsonArray = new Gson().fromJson("[{\"Impala\":\"config.fieldmappingname_kudu\"}]", List.class);
        log("\nArrayList: " + jsonArray);

    }

    private static JSONArray readFileContent() {
        JSONArray crunchifyArray = new JSONArray();
        String lineFromFile;

        try (BufferedReader bufferReader = new BufferedReader(new FileReader("D:\\Work\\notespringboot\\src\\main\\resources\\crunchify-gson.txt"))) {

            while ((lineFromFile = bufferReader.readLine()) != null) {
                if (lineFromFile != null && !lineFromFile.isEmpty()) {
                    JSONObject crunchifyObject = new JSONObject();
                    log("Line: ==>" + lineFromFile);

                    // escape any blank space between tokens
                    String[] split = lineFromFile.split("\\s+");
                    crunchifyObject.put("companyName", split[0]);
                    crunchifyObject.put("address", split[1]);
                    crunchifyObject.put("description", split[2]);
                    crunchifyArray.put(crunchifyObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nJSONArray: " + crunchifyArray.toString());
        return crunchifyArray;

    }

    private static void log(Object string) {
        System.out.println(string);
    }
}