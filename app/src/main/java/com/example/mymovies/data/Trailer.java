package com.example.mymovies.data;

public class Trailer {
/*{
    "id": 537061,
    "results": [
        {
            "id": "5d079138c3a3680a5723d02e",
            "iso_639_1": "en",
            "iso_3166_1": "US",
            "key": "TlmfPB20Ln4",
            "name": "Steven Universe | Steven Universe The Movie Official Teaser | Cartoon Network",
            "site": "YouTube",
            "size": 1080,
            "type": "Teaser"
        },*/

    private String key;
    private String name;

    public Trailer(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
