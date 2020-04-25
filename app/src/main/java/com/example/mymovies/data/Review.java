package com.example.mymovies.data;

public class Review {

    /* "id": 335983,
    "page": 1,
    "results": [
        {
            "author": "Gimly",
            "content": "I honestly don't know what everyone's  if Tom Hardy wasn't
            ve this one.\r\n\r\n_Final rating:★★★ - I liked it. Would personally
            recommend you give it a go._",
            "id": "5bd28c050e0a2616cf00459a",
            "url": "https://www.themoviedb.org/review/5bd28c050e0a2616cf00459a"
        },*/

    private String author;
    private String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
