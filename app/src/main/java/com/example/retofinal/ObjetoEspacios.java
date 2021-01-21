package com.example.retofinal;

public class ObjetoEspacios {

    private int CodEspacio;
    private String Nombre;
    private String Descripcion;
    private String Tipo;
    private int CodProv;

    public ObjetoEspacios() {
    }

    public int getCodEspacio() {
        return CodEspacio;
    }

    public void setCodEspacio(int codEspacio) {
        CodEspacio = codEspacio;
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

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public int getCodProv() {
        return CodProv;
    }

    public void setCodProv(int codProv) {
        CodProv = codProv;
    }
}
