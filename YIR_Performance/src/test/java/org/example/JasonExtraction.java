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

    static  {
        // Initializing the HashMap with some sample data
        artistMap.put("B00157GJ20", "Swift Taylor");
        artistMap.put("B00GB0QTOY", "The Beatles");
        artistMap.put("B002F0BWIM", "Justin Bieber");
        artistMap.put("B000QJKJYM", "Tracy Chapman");
        artistMap.put("B0015H24HO", "Ed Sheeran");
        artistMap.put("B000QJJOTI", "Neil Young");
        artistMap.put("B000QJPXG6", "Beastie Boys");
        artistMap.put("B000QJNL2Y", "Pitbull");
        artistMap.put("B000QJO20O", "Nas");
        artistMap.put("B00GGOQNTG", "The Cool Kids");


        // Initializing the HashMap with some sample data
        topGenreMap.put("dpRWm1xb", "Dance & Electronic");
        topGenreMap.put("za-1TQ7u", "Rap & Hip Hop");
        topGenreMap.put("aFyug8oD", "Pop");
        topGenreMap.put("cwCUrJOR", "Dance Pop");
        topGenreMap.put("_YBW-yD-", "Alternative");

        // Initializing the HashMap with some sample data
        songsMap.put("B004LQW0NO", "Somebody To Love Remix [feat. USHER]");
        songsMap.put("B01LXAA72R", "Die For You");
        songsMap.put("B077TD3696", "Perfect Duet (with Beyoncé)");
        songsMap.put("B08W8ZFD22", "Love Story (Taylor’s Version)");
        songsMap.put("B09DK3PYJD", "Until I Found You");
        songsMap.put("B0C6WQRDGQ", "Enchanted (Taylor's Version)");
        songsMap.put("B0011Z4Z3G", "Fast Car");
        songsMap.put("B07SYZKYSW", "Lover");
        songsMap.put("B09WTMMYTS", "Love Of My Life");
        songsMap.put("B0CJ3S5PDW", "Snooze (Acoustic) [Explicit]");
        songsMap.put("B0BRYG3YQJ", "REACT");
        songsMap.put("B00DFQDOW2", "N.Y. State of Mind [Explicit]");
        songsMap.put("B07HKT8DL5", "Wu Tang Forever [feat. Ghostface Killah & Raekwon & RZA & Method Man & Inspectah Deck & Cappadonna & Jackpot Scotty Wotty & U-God & Masta Killa & GZA] [Explicit]");
        songsMap.put("B0CPJCPDH4", "Fire!");
        songsMap.put("B0D3WLP4XN", "OK [Explicit]");
        songsMap.put("B001NB4W4A", "(Coffee's For Closers) (Album Version) ");
        songsMap.put("B00C7D3VT0", "The Mighty Fall [feat. Big Sean] [Explicit]");
        songsMap.put("B07WDNL1DV", "IHOP");
        songsMap.put("B07WDNL8BK", "Jose & Mark");
        songsMap.put("B097Q8DRW8", "Affirmative Action [Explicit] ");
    }

    static String filePath = "cnf_prime.json";
    static String jsonString;

    // Initialize JSON arrays
    static JSONArray topDecadesArray;
    static JSONArray topGenresArray;
    static JSONArray topArtistsArray;
    static JSONArray topTracksArray;

    static JSONObject firstHalfObject;
    static JSONObject secondHalfObject;
    static {
        try {
            jsonString = Files.readString(Paths.get(filePath));
            JSONObject jsonObject = new JSONObject(jsonString);
            topDecadesArray = jsonObject.getJSONArray("topDecades");
            topGenresArray = jsonObject.getJSONArray("topGenres");
            topTracksArray = jsonObject.getJSONArray("topTracks");
            firstHalfObject = jsonObject.getJSONObject("firstHalf");
            secondHalfObject = jsonObject.getJSONObject("secondHalf");
            topArtistsArray =  jsonObject.getJSONArray("topArtists");


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
        //HashMap<String, String> map = new HashMap<>();
        return jsonToMap(firstHalfObject);
    }

    public static HashMap<String, Object> getSecondHalf(){
        //HashMap<String, String> map = new HashMap<>();
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
            FileReader reader = new FileReader("cnf_prime.json");
            Type dataType = new TypeToken<Data>() {}.getType();
            Data data = gson.fromJson(reader, dataType);

            // Print out IDs of top artists
            System.out.println("Top Artists:");
            for (TopArtist artist : data.getTopArtists()) {
                System.out.println("Artist ID: " + artist.getId() + ", Name: " + getArtistName(artist.getId()));
            }

            // Print out total minutes from totalStats/tracks
            System.out.println("\nTotal Minutes this year:");
            System.out.println("Total Minutes: " + data.getTotalStats().getTracks().getTotalMinutes());

            //Print top Decade
            String topDecadesId = topDecadesArray.getJSONObject(0).getString("id");
            System.out.println("\nTop Decades : " + topDecadesId);
            JSONArray topDecadesTracks = topDecadesArray.getJSONObject(0).getJSONArray("tracks");
            System.out.println("There are " + topDecadesTracks.length() + " tracks in Decades");
            for(Object obj : topDecadesTracks){
                JSONObject track = (JSONObject) obj;
                // Access individual fields within each track object
                String id = track.getString("id");
                int count = track.getInt("count");
                System.out.println("Track ID: " + id);//
            }

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





        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
