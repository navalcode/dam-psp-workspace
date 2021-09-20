package com.eo1NavalManuel.EO1;

public class Saludo {

    private final long id;
    private final String content;

    public Saludo(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
