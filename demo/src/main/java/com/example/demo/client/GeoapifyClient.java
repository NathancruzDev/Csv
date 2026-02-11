package com.example.demo.client;


import org.springframework.beans.factory.annotation.Value;

public class GeoapifyClient extends  GeoapifyClientHttp {
    @Value("${geoapify.api.key}")
    private final String API_KEY= "83987d49b8d9472a96d632314ee496cc";
    private final String BASE_URL="https://api.geoapify.com/v1/routing";


    public String distance(Double longitude1, Double latitude1, Double longitude2, Double latitude2) throws Exception {
        String url = String.format(java.util.Locale.US,
                "%s?waypoints=%f,%f%%7C%f,%f&mode=drive&apiKey=%s",
                BASE_URL, longitude1,latitude1,longitude2,latitude2, API_KEY);

        return get(url);
    }


}
