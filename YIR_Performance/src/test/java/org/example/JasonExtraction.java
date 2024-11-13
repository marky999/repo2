package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JasonExtraction {
    public static final Map<String, String> artistMap = new HashMap<>();
    public static final Map<String, String> topGenreMap = new HashMap<>();
    public static final Map<String, String> songsMap = new HashMap<>();
    public static final Map<String, String> topPodcastsMap = new HashMap<>();
    public static final Map<String, String> topEarlyAlbumDiscoveryMap = new HashMap<>();


    static {
    try {
        String content = new String(Files.readAllBytes(Paths.get("data.json")));
        JSONObject jsonObject = new JSONObject(content);

        // Populate artistMap
        JSONObject artistJson = jsonObject.getJSONObject("artistMap");
        for (String key : artistJson.keySet()) {
            artistMap.put(key, artistJson.getString(key));
        }

        // Populate topGenreMap
        JSONObject genreJson = jsonObject.getJSONObject("topGenreMap");
        for (String key : genreJson.keySet()) {
            topGenreMap.put(key, genreJson.getString(key));
        }

        // Populate songsMap
        JSONObject songsJson = jsonObject.getJSONObject("songsMap");
        for (String key : songsJson.keySet()) {
            songsMap.put(key, songsJson.getString(key));
        }

        //populate topEarlyAlbumDiscoveryMap
        JSONObject topEarlyAlbumJson = jsonObject.getJSONObject("topEarlyAlbumDiscoveryMap");
        for (String key : topEarlyAlbumJson.keySet()) {
            topEarlyAlbumDiscoveryMap.put(key, topEarlyAlbumJson.getString(key));
        }

        JSONObject topPodcastsJson = jsonObject.getJSONObject("topPodcastsMap");
        for (String key : topPodcastsJson.keySet()) {
            topPodcastsMap.put(key, topPodcastsJson.getString(key));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    static String filePath = Config.JSON_FILE_PATH;
    static String jsonString;

    // Initialize JSON arrays
    static JSONArray topDecadesArray;
    static JSONArray topGenresArray;
    static JSONArray topArtistsArray;
    static JSONArray topTracksArray;
    static JSONArray topNewArtistDiscoveryArray;
    static JSONArray topAlexaRequestsArray;
    static JSONArray topPodcastsArray;
    static JSONArray topEarlyAlbumDiscoveryArray;

    static JSONObject totalStatsObject;
    static JSONObject firstHalfObject;
    static JSONObject secondHalfObject;
    static {
        try {
            jsonString = Files.readString(Paths.get(filePath));
            JSONObject jsonObject = new JSONObject(jsonString);
            topDecadesArray = jsonObject.getJSONArray("topDecades");
            topGenresArray = jsonObject.getJSONArray("topGenres");
            topTracksArray = jsonObject.getJSONArray("topTracks");
            topArtistsArray =  jsonObject.getJSONArray("topArtists");
            topNewArtistDiscoveryArray = jsonObject.getJSONArray("topNewArtistDiscovery");
            topAlexaRequestsArray = jsonObject.getJSONArray("topAlexaRequests");
            topPodcastsArray = jsonObject.getJSONArray("topPodcasts");
            topEarlyAlbumDiscoveryArray = jsonObject.getJSONArray("topEarlyAlbumDiscovery");

            firstHalfObject = jsonObject.getJSONObject("firstHalf");
            secondHalfObject = jsonObject.getJSONObject("secondHalf");
            totalStatsObject = jsonObject.getJSONObject("totalStats");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    static class TopArtist {
        private String id;
        public String getId() {
            return id;
        }
    }

    static class TopTrack {
        private String id;
        public String getId() {
            return id;
        }
    }

    public static class TracksStats {
        private  int totalMinutes;
        public  int getTotalMinutes() {
            return totalMinutes;
        }
    }

    public static class ArtistsStats {
        private final int distinctCount;
        public ArtistsStats(int distinctCount) {
            this.distinctCount = distinctCount;
        }
        public  int getDistinctCount() {
            return distinctCount;
        }
    }
    static class TotalStats {
        private TracksStats tracks;
        private ArtistsStats artists;
        public TracksStats getTracks() {
            return tracks;
        }

        public ArtistsStats getArtists() {
            return artists;
        }
    }


    static class Data {
        private List<TopArtist> topArtists;
        private List<TopTrack> topTracks;  //  field for top tracks
        private TotalStats totalStats;  //  field for total stats

        public List<TopArtist> getTopArtists() {
            return topArtists;
        }

        public List<TopTrack> getTopTracks() {
            return topTracks;  // New getter for top tracks
        }

        public TotalStats getTotalStats() {
            return totalStats;
        }
    }

    public static HashMap<String, Object> getFirstHalf(){
        return jsonToMap(firstHalfObject);
    }

    public static HashMap<String, Object> getSecondHalf(){
        return jsonToMap(secondHalfObject);
    }

    public static HashMap<String, Object> getTopTracks(){
        return(jsonArrayToMap(topTracksArray));
    }

    public static HashMap<String, Object> getTopArtists(){
        return(jsonArrayToMap(topArtistsArray));
    }

    public static void dumpHashMap(HashMap<String, Object> map) {
        for (String key : map.keySet()) {
            Object value = map.get(key);
            // Check if the value is another HashMap, List, or other structure
            if (value instanceof HashMap) {
                System.out.println(key + " : ");
                dumpHashMap((HashMap<String, Object>) value); // Recursive call for nested HashMaps
            } else if (value instanceof Iterable) {
                System.out.println(key + " : ");
                for (Object item : (Iterable<?>) value) {
                    System.out.println("  - " + item);
                }
            } else {
                System.out.println(key + " : " + value);
            }
        }
    }

    // Convert a JSONObject to a HashMap
    public static HashMap<String, Object> jsonToMap(JSONObject jsonObject) {
        HashMap<String, Object> map = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                value = jsonToMap((JSONObject) value); // Recursive call for nested objects
            } else if (value instanceof JSONArray) {
                value = jsonArrayToMap((JSONArray) value); // Convert JSONArray to HashMap
            }
            map.put(key, value);
        }
        return map;
    }

    // Convert a JSONArray to a HashMap
    public static HashMap<String, Object> jsonArrayToMap(JSONArray jsonArray) {
        HashMap<String, Object> map = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            map.put(String.valueOf(i), jsonToMap(item));
        }
        return map;
    }


    public static List<String> getTopGenres(){
        List<String> genres = new ArrayList<>();
        for(Object obj :topGenresArray){
            JSONObject genre = (JSONObject) obj;
            String id = genre.getString("id");
            //System.out.println("Genre ID: " + id + ", " + topGenreMap.get(id));
            genres.add(topGenreMap.get(id));
        }

        return genres;
    }

    public static int getNumOfTracksInDecadeFromJson(){
        JSONArray topDecadesTracks = topDecadesArray.getJSONObject(0).getJSONArray("tracks");
        return topDecadesTracks.length();
    }
    private static String getArtistName(String id) {
        String name = artistMap.get(id);
        return name == null ? "Unknown" : name;
    }

    private static String getTrackName(String id) {
        String name = songsMap.get(id);
        return name == null ? "Unknown" : name;
    }

    public static String getTopDecadeFromJson(){
        System.out.println(topDecadesArray.getJSONObject(0).getString("id"));
        return topDecadesArray.getJSONObject(0).getString("id");
    }



    public static void extractJson() {
        Gson gson = new GsonBuilder().create();
        try {
            // Change this path to your actual JSON file path
            FileReader reader = new FileReader(Config.JSON_FILE_PATH);
            Type dataType = new TypeToken<Data>() {}.getType();
            Data data = gson.fromJson(reader, dataType);

            // Print out IDs of top artists
            System.out.println("Top Artists:");
            for (TopArtist artist : data.getTopArtists()) {
                System.out.println("Artist ID: " + artist.getId() + ", Name: " + getArtistName(artist.getId()));
            }

            // Print out total minutes from totalStats/tracks
//            System.out.println("\nTotal Minutes this year:");
//            System.out.println("Total Minutes: " + data.getTotalStats().getTracks().getTotalMinutes());

            //Print top Decade
//            String topDecadesId = topDecadesArray.getJSONObject(0).getString("id");
//            System.out.println("\nTop Decades : " + topDecadesId);
//            JSONArray topDecadesTracks = topDecadesArray.getJSONObject(0).getJSONArray("tracks");
//            System.out.println("There are " + topDecadesTracks.length() + " tracks in Decades");
//            for(Object obj : topDecadesTracks){
//                JSONObject track = (JSONObject) obj;
//                // Access individual fields within each track object
//                String id = track.getString("id");
//                int count = track.getInt("count");
//                System.out.println("Track ID: " + id);//
//            }

            //-----------Print out top Genres-------------------------------------------//
            System.out.println("\nTop Genres:");
            //String id;// = topGenresArray.getJSONObject(0).getString("id");
            for(Object obj :topGenresArray){
                JSONObject genre = (JSONObject) obj;
                String topGenreId = genre.getString("id");
                System.out.println("topGenre: " + topGenreMap.get(topGenreId));
            }

            //-----------Dump FirstHalf--------------------------//-
            System.out.println("\nFirstHalf");
            HashMap<String, Object> firstHalfFromJSon = JasonExtraction.getFirstHalf();
            dumpHashMap(firstHalfFromJSon);

            //-----------Dump SecondtHalf--------------------------//
            System.out.println("\nSecondHalf");
            HashMap<String, Object> secondHalfFromJSon = jsonToMap(secondHalfObject);
            dumpHashMap(secondHalfFromJSon);

            //----------- Dump topTacks -------------//
            System.out.println("\ntopTracks");
            HashMap<String, Object> topTracksFromJSon = jsonArrayToMap(topTracksArray);
            dumpHashMap(topTracksFromJSon);

            System.out.println("\nTracks with title:");
            for (TopTrack track : data.getTopTracks()) {
                System.out.println("Track ID: " + track.getId() + ", " + getTrackName(track.getId()));
            }

            //------------- Dump topArtists ------------//
            System.out.println("\ntopArtists");
            HashMap<String, Object> topArtistFromJSon = jsonArrayToMap(topArtistsArray);
            dumpHashMap(topArtistFromJSon);

            //------------- Dump topAPodcasts ------------//
            System.out.println("\ntopPodcasts");
            HashMap<String, Object> topPodcastsFromJSon = jsonArrayToMap(topPodcastsArray);
            dumpHashMap(topPodcastsFromJSon);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
