package dominio;

import tads.Cola;
import tads.Criterios;
import tads.ListaOrdenada;
import tads.Pila;

public class Region {

    private final String codigo;
    private final String nombre;
    private final String pais;
    private final String zona;

    private final Cola pendientes = new Cola();
    private final ListaOrdenada activos = new ListaOrdenada(Criterios.POR_CODIGO);
    private final Pila historial = new Pila();

    public Region(String codigo, String nombre, String pais, String zona) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.pais = pais;
        this.zona = zona;
    }

    public String getCodigo() { return codigo; }
    public Cola getPendientes() { return pendientes; }
    public ListaOrdenada getActivos() { return activos; }
    public Pila getHistorial() { return historial; }

    // Formato de salida: codigo;nombre;pais;zona
    public String datos() {
        StringBuilder sb = new StringBuilder();
        sb.append(codigo).append(';').append(nombre).append(';')
          .append(pais).append(';').append(zona);
        return sb.toString();
    }
}