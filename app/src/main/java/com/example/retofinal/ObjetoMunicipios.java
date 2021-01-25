package com.example.retofinal;

public class ObjetoMunicipios {

    private int CodMuni;
    private String Nombre;
    private String Descripcion;
    private int CodProv;
    private String latitud;
    private String longitud;
    public ObjetoMunicipios() {

    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public int getCodMuni() {
        return CodMuni;
    }

    public void setCodMuni(int codMuni) {
        CodMuni = codMuni;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getCodProv() {
        return CodProv;
    }

    public void setCodProv(int codProv) {
        CodProv = codProv;
    }
}
