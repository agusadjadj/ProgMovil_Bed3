package com.example.prog_movil_final.Clases;

public class Review {

    private String autor;
    private String comentario;

    public Review(String autor, String comentario) {
        this.autor = autor;
        this.comentario = comentario;
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
}
