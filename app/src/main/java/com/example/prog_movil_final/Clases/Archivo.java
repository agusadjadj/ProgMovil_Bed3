package com.example.prog_movil_final.Clases;

public class Archivo {
    private String autor;
    private String comentario;
    private String link;

    public Archivo(String autor, String comentario, String link) {
        this.autor = autor;
        this.comentario = comentario;
        this.link = link;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}