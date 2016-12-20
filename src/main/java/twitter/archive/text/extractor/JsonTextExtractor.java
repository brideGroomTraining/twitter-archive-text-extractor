/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter.archive.text.extractor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author ryoji
 */
public class JsonTextExtractor {
    public static void main(String[] args) {
        JsonTextExtractor jsonTextExtractor = new JsonTextExtractor();
        jsonTextExtractor.extractText();
    }
    
    public void extractText() {
        for (File file : listArchiveFilesOrderByDate()) {
            try {
                final String psudoJsonStr = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                final String realJson     = org.apache.commons.lang.StringUtils.substringAfter(psudoJsonStr, "\n");
                final JSONArray twtArray  = new JSONArray(realJson);
                final Iterator iterator   = twtArray.iterator();
                while (iterator.hasNext()) {
                    final JSONObject tweet = (JSONObject)iterator.next();
                    System.out.println(extractTextFromTweet(tweet));
                }
            } catch (IOException ex) {
                Logger.getLogger(JsonTextExtractor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private List<File> listArchiveFilesOrderByDate() {
        List<File> files = (List<File>) FileUtils.listFiles(new File("data/js/tweets"), new String[]{"js"}, true);
        Collections.sort(files, new Comparator<File>(){
            @Override
            public int compare(File o1, File o2) {
                return o2.getName().compareTo(o1.getName());
            }
        });
        return files;
    }
    
    private String extractTextFromTweet(JSONObject tweet) {
        StringBuilder sb = new StringBuilder();
        final String text      = tweet.getString("text");
        final String timestamp = tweet.getString("created_at");
        return sb.append("\n").append(org.apache.commons.lang.StringUtils.substringBefore(timestamp, "+")).append("\n\n  ").append(text.replace("\n", "\n  ")).toString();
    }
}
