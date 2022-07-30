package com.example.prog_movil_final.Clases;

public class EventoAgenda {

    private String id;
    private String title;       //Titulo para la noticia
    private String copete;      //Copete para la noticia. Es una "preview" de la misma.
    private String body;        //Cuerpo completo de la noticia
    private String time;        //Fecha y hora que fue utilizada
    private String pathImage;   //Path de la imagen si es que contiene.
    private String volanta;

    public EventoAgenda() {
        this.id = "";
        this.title = "";
        this.copete = "";
        this.body = "";
        this.time = "";
        this.pathImage = "";
        this.volanta = "";
    }

    public int compareTo(EventoAgenda o) {
        return time.compareTo(o.getTime());
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCopete() { return copete; }
    public void setCopete(String copete) { this.copete = copete; }

    public String getBody() { return body; }
    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getPathImage() {
        return pathImage;
    }
    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getVolanta(){ return volanta; }
    public void setVolanta(String volanta){ this.volanta = volanta; }

}
