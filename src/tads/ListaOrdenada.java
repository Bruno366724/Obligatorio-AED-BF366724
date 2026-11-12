package tads;

import dominio.Despliegue;

public class ListaOrdenada {

    private NodoLista primero;
    private NodoLista ultimo;
    private int cantidad;
    private final Criterio criterio;

    public ListaOrdenada(Criterio criterio) {
        this.criterio = criterio;
    }

    public boolean esVacia() { return primero == null; }
    public int getCantidad() { return cantidad; }

    // O(n): busca el primer nodo estrictamente mayor e inserta antes.
    public void insertarOrdenado(Despliegue d) {
        NodoLista nuevo = new NodoLista(d);
        NodoLista actual = primero;
        while (actual != null && criterio.comparar(actual.dato, d) <= 0) {
            actual = actual.siguiente;
        }
        if (actual == null) { // al final (incluye lista vacía)
            nuevo.anterior = ultimo;
            if (ultimo == null) primero = nuevo; else ultimo.siguiente = nuevo;
            ultimo = nuevo;
        } else { // antes de actual
            nuevo.siguiente = actual;
            nuevo.anterior = actual.anterior;
            if (actual.anterior == null) primero = nuevo;
            else actual.anterior.siguiente = nuevo;
            actual.anterior = nuevo;
        }
        cantidad++;
    }

    // O(n): elimina por referencia (misma instancia).
    public boolean eliminar(Despliegue d) {
        NodoLista actual = primero;
        while (actual != null && actual.dato != d) {
            actual = actual.siguiente;
        }
        if (actual == null) return false;
        if (actual.anterior == null) primero = actual.siguiente;
        else actual.anterior.siguiente = actual.siguiente;
        if (actual.siguiente == null) ultimo = actual.anterior;
        else actual.siguiente.anterior = actual.anterior;
        cantidad--;
        return true;
    }

    // O(n)
    public boolean contiene(Despliegue d) {
        for (NodoLista n = primero; n != null; n = n.siguiente) {
            if (n.dato == d) return true;
        }
        return false;
    }

    // O(n)
    public Despliegue buscarPorId(String id) {
        for (NodoLista n = primero; n != null; n = n.siguiente) {
            if (n.dato.getId().equals(id)) return n.dato;
        }
        return null;
    }

    // O(n)
    public Despliegue buscarPorCodigo(String codigo) {
        for (NodoLista n = primero; n != null; n = n.siguiente) {
            if (n.dato.getCodigo().equals(codigo)) return n.dato;
        }
        return null;
    }

    // O(n): sin "|" al final.
    public String listarAscendente() {
        StringBuilder sb = new StringBuilder();
        for (NodoLista n = primero; n != null; n = n.siguiente) {
            if (n != primero) sb.append('|');
            sb.append(n.dato.datos());
        }
        return sb.toString();
    }

    // O(n): recorre hacia atrás con 'anterior'.
    public String listarDescendente() {
        StringBuilder sb = new StringBuilder();
        for (NodoLista n = ultimo; n != null; n = n.anterior) {
            if (n != ultimo) sb.append('|');
            sb.append(n.dato.datos());
        }
        return sb.toString();
    }
}