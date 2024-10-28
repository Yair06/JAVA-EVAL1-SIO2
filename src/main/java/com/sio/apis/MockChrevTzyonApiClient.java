package com.sio.apis;

import com.sio.models.Target;
import com.sio.models.Position;
import com.sio.tools.ConfigManager;
import com.sio.tools.HttpRequestBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.http.HttpResponse;

import java.io.IOException;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;

import java.util.ArrayList;

public class MockChrevTzyonApiClient {
    private final ConfigManager cm = new ConfigManager();
    private final JSONParser parser = new JSONParser();

    /**
     * Get all targets from the API
     * @return ArrayList<JSONObject>
     */
    public ArrayList<JSONObject> getTargets() {

        try {
            HttpResponse<String> response = HttpRequestBuilder.get(cm.getProperty("api.url") + "/targets");
            JSONObject jsonItem = (JSONObject) parser.parse(response.body());
            ArrayList<JSONObject> targets = new ArrayList<>();
            JSONArray jsonTargets = (JSONArray) jsonItem.get("targets");
            for (Object item : jsonTargets) {
                JSONObject jsonT = (JSONObject) item;
                targets.add(jsonT);
            }
            return targets;

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }


        return null;
    }

    /**
     * Add a target to the API
     * @param target Target
     * @return boolean
     */
    public boolean addTarget(Target target) {
        try {
            JSONObject json = new JSONObject();
            json.put("code_name", target.getCodeName());
            json.put("name", target.getName());
            String jsonBody = json.toString();

            HttpResponse<String> response = HttpRequestBuilder.post(
                    cm.getProperty("api.url") + "/target/add",
                    jsonBody
            );

            if (response.statusCode() == 200) {
                JSONObject responseBody = (JSONObject) parser.parse(response.body());
                String generatedHash = (String) responseBody.get("hash");

                target.setHash(generatedHash);

                return true;
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }



        return false;
    }

    /**
     * Delete a target from the API
     * @param target Target
     * @return boolean
     */
    public boolean deleteTarget(Target target) {
        try {
            String apiUrl = cm.getProperty("api.url") + "/target/" + target.getHash();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .DELETE()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Target deleted successfully");
                return true;
            } else {
                System.out.println("Error deleting target: " + response.body());
                return false;
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error deleting target: " + e.getMessage());
            return false;
        }
    }

    /**
     * Build a JSON string from a Target object
     * @param t Target
     * @return String
     */
    private String buildJsonStringFromObject(Target t){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hash", t.getHash());
        jsonObject.put("codeName", t.getCodeName());
        jsonObject.put("name", t.getName());

        JSONArray positionsArray = new JSONArray();
        for (Position position : t.getPositions()) {
            JSONObject positionObject = new JSONObject();
            positionObject.put("id", position.getId());
            positionsArray.add(positionObject);
        }
        jsonObject.put("positions", positionsArray);

        return jsonObject.toString();
    }
}
