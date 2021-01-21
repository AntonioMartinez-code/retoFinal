package com.example.retofinal;

public class ObjetoMunicipios {

    private int CodMuni;
    private String Nombre;
    private String Descripcion;
    private int CodProv;

    public ObjetoMunicipios() {

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
