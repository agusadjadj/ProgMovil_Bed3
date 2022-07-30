package com.example.prog_movil_final.Clases;

public class Clase {

    private String id;
    private String area;
    private String aula;
    private String evento;
    private String descripcion;
    private String fechaInicio;
    private String horaInicio;
    private String fechaFin;
    private String horaFin;
    private String estado;

    public Clase(){ }

    public Clase(String id, String area, String aula,
                 String evento, String descripcion, String fecha_inicio,
                 String hora_inicio, String fecha_fin, String hora_fin, String estado) {

        this.id = id;
        this.area = area;
        this.aula = aula;
        this.evento = evento;
        this.descripcion = descripcion;
        this.fechaInicio = fecha_inicio;
        this.horaInicio = hora_inicio;
        this.fechaFin = fecha_fin;
        this.horaFin = hora_fin;
        this.estado = estado;

    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getAula() { return aula; }
    public void setAula(String aula) { this.aula = aula; }

    public String getEvento() { return evento; }
    public void setEvento(String evento) { this.evento = evento; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fecha_inicio) { this.fechaInicio = fecha_inicio; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String hora_inicio) { this.horaInicio = hora_inicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fecha_fin) { this.fechaFin = fecha_fin; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String hora_fin) { this.horaFin = hora_fin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
