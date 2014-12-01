package pl.touk.sputnik.processor.sonar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import pl.touk.sputnik.review.ReviewResult;
import pl.touk.sputnik.review.Severity;
import pl.touk.sputnik.review.Violation;

import com.google.common.collect.Maps;

@Slf4j
@AllArgsConstructor
class ResultParser {

    private final File file;


    public ReviewResult parseResults() {
        ReviewResult result = new ReviewResult();
        JSONObject report;
        try {
            report = (JSONObject) new JSONTokener(new FileReader(file)).nextValue();
        } catch (FileNotFoundException | JSONException e1) {
            throw new RuntimeException(e1);
        }
        try {
            JSONArray issues = report.getJSONArray("issues");
            HashMap<String, String> components = getComponents(report.getJSONArray("components"));
            for (int i = 0; i < issues.length(); i++) {
                JSONObject issue = (JSONObject) issues.get(i);
                if (!issue.has("line")) {
                    continue;
                }
                int line = issue.getInt("line");
                String message = issue.getString("message");
                File file = getFile(issue.getString("component"), components);
                String filename = getFilename(file);
                result.add(new Violation(filename, line, message, Severity.WARNING));
            }
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }


    private String getFilename(File file) throws IOException {
        String filename = file.getPath().replace('\\', '/');
        return filename.substring(filename.indexOf('/', filename.indexOf('/') + 1) + 1);
    }


    private File getFile(String input, HashMap<String, String> components) {
        String file = input.substring(input.lastIndexOf(':') + 1);
        String component = input.substring(0, input.lastIndexOf(':'));
        return new File(components.get(component),file);
    }


    private HashMap<String, String> getComponents(JSONArray components) throws JSONException {
        HashMap<String, String> paths = Maps.newHashMap();
        for (int i = 0; i < components.length(); i++) {
            JSONObject keyValue = components.getJSONObject(i);
            if (keyValue.has("path")) {
                paths.put(keyValue.getString("key"), keyValue.getString("path"));
            }
        }
        return paths;
    }
}

