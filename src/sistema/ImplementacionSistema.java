package sistema;

import dominio.Despliegue;
import dominio.Region;
import java.util.regex.Pattern;
import tads.Criterios;
import tads.ListaOrdenada;
import tads.ListaRegiones;

// Integrantes del equipo:
// Nombre Apellido - Nº de estudiante
// Nombre Apellido - Nº de estudiante
public class ImplementacionSistema implements Sistema {

    // matches() exige que coincida el String completo: AAA-BBB-CCCC
    private static final Pattern FORMATO_CODIGO =
            Pattern.compile("[A-Z]{3}-[0-9]{3}-[A-Za-z0-9]{4}");

    private ListaOrdenada desplieguesPorId;
    private ListaOrdenada desplieguesPorCodigo;
    private ListaOrdenada[] desplieguesPorModalidad;
    private ListaRegiones regiones;

    public ImplementacionSistema() {
        inicializarEstructuras();
    }

    private void inicializarEstructuras() {
        desplieguesPorId = new ListaOrdenada(Criterios.POR_ID);
        desplieguesPorCodigo = new ListaOrdenada(Criterios.POR_CODIGO);
        desplieguesPorModalidad = new ListaOrdenada[Modalidad.values().length];
        for (int i = 0; i < desplieguesPorModalidad.length; i++) {
            desplieguesPorModalidad[i] = new ListaOrdenada(Criterios.POR_CODIGO);
        }
        regiones = new ListaRegiones();
    }

    // Vacío = null, "" o solo espacios.
    private boolean esVacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    // ---------- 01 ----------
    @Override
    public Retorno inicializarSistema() {
        inicializarEstructuras();
        return Retorno.ok();
    }

    // ---------- 02 ----------
    @Override
    public Retorno registrarDespliegue(String id, String codigo, String descripcion,
                                      boolean cuantizado, Modalidad modalidad) {
        if (esVacio(id) || esVacio(codigo) || esVacio(descripcion) || modalidad == null) {
            return Retorno.error1();
        }
        if (!FORMATO_CODIGO.matcher(codigo).matches()) {
            return Retorno.error2();
        }
        if (desplieguesPorId.buscarPorId(id) != null) {
            return Retorno.error3();
        }
        if (desplieguesPorCodigo.buscarPorCodigo(codigo) != null) {
            return Retorno.error4();
        }
        Despliegue d = new Despliegue(id, codigo, descripcion, cuantizado, modalidad);
        desplieguesPorId.insertarOrdenado(d);
        desplieguesPorCodigo.insertarOrdenado(d);
        desplieguesPorModalidad[modalidad.getIndice()].insertarOrdenado(d);
        return Retorno.ok();
    }

    // ---------- 03 ----------
    @Override
    public Retorno buscarDesplieguePorId(String id) {
        if (esVacio(id)) {
            return Retorno.error1();
        }
        Despliegue d = desplieguesPorId.buscarPorId(id);
        if (d == null) {
            return Retorno.error2();
        }
        return Retorno.ok(d.datos());
    }

    // ---------- 04 ----------
    @Override
    public Retorno listarDesplieguesPorIdAscendente() {
        return Retorno.ok(desplieguesPorId.listarAscendente());
    }

    // ---------- 05 ----------
    @Override
    public Retorno listarDesplieguesPorIdDescendente() {
        return Retorno.ok(desplieguesPorId.listarDescendente());
    }

    // ---------- 06 ----------
    @Override
    public Retorno buscarDesplieguePorCodigo(String codigo) {
        if (esVacio(codigo)) {
            return Retorno.error1();
        }
        if (!FORMATO_CODIGO.matcher(codigo).matches()) {
            return Retorno.error2();
        }
        Despliegue d = desplieguesPorCodigo.buscarPorCodigo(codigo);
        if (d == null) {
            return Retorno.error3();
        }
        return Retorno.ok(d.datos());
    }

    // ---------- 07 ----------
    @Override
    public Retorno listarDesplieguesPorCodigoAscendente() {
        return Retorno.ok(desplieguesPorCodigo.listarAscendente());
    }

    // ---------- 08 ----------
    @Override
    public Retorno listarDesplieguesPorModalidad(Modalidad unaModalidad) {
        if (unaModalidad == null) {
            return Retorno.error1();
        }
        return Retorno.ok(desplieguesPorModalidad[unaModalidad.getIndice()].listarAscendente());
    }

    // ---------- 09 ----------
    @Override
    public Retorno registrarRegion(String codigo, String nombre, String pais, String zona) {
        if (esVacio(codigo) || esVacio(nombre) || esVacio(pais) || esVacio(zona)) {
            return Retorno.error1();
        }
        if (regiones.buscar(codigo) != null) {
            return Retorno.error2();
        }
        regiones.agregar(new Region(codigo, nombre, pais, zona));
        return Retorno.ok();
    }

    // ---------- 10 ----------
    @Override
    public Retorno obtenerRegion(String codigo) {
        if (esVacio(codigo)) {
            return Retorno.error1();
        }
        Region r = regiones.buscar(codigo);
        if (r == null) {
            return Retorno.error2();
        }
        return Retorno.ok(r.datos());
    }

    // ---------- 11 ----------
    @Override
    public Retorno solicitarDespliegueEnRegion(String idDespliegue, String codigoRegion) {
        if (esVacio(idDespliegue) || esVacio(codigoRegion)) {
            return Retorno.error1();
        }
        Despliegue d = desplieguesPorId.buscarPorId(idDespliegue);
        if (d == null) {
            return Retorno.error2();
        }
        Region r = regiones.buscar(codigoRegion);
        if (r == null) {
            return Retorno.error3();
        }
        if (r.getActivos().contiene(d) || r.getPendientes().contiene(d)) {
            return Retorno.error4();
        }
        r.getPendientes().encolar(d); // O(1)
        return Retorno.ok();
    }

    // ---------- 12 ----------
    @Override
    public Retorno procesarSiguienteDespliegue(String codigoRegion) {
        if (esVacio(codigoRegion)) {
            return Retorno.error1();
        }
        Region r = regiones.buscar(codigoRegion);
        if (r == null) {
            return Retorno.error2();
        }
        if (r.getPendientes().estaVacia()) {
            return Retorno.error3();
        }
        Despliegue d = r.getPendientes().desencolar(); // O(1)
        r.getActivos().insertarOrdenado(d);            // O(k)
        r.getHistorial().apilar(d);                    // O(1)
        return Retorno.ok(d.datos());
    }

    // ---------- 13 ----------
    @Override
    public Retorno listarDesplieguesActivosDeRegion(String codigoRegion) {
        if (esVacio(codigoRegion)) {
            return Retorno.error1();
        }
        Region r = regiones.buscar(codigoRegion);
        if (r == null) {
            return Retorno.error2();
        }
        return Retorno.ok(r.getActivos().listarAscendente());
    }

    // ---------- 14 ----------
    @Override
    public Retorno retirarUltimoDespliegueProcesado(String codigoRegion) {
        if (esVacio(codigoRegion)) {
            return Retorno.error1();
        }
        Region r = regiones.buscar(codigoRegion);
        if (r == null) {
            return Retorno.error2();
        }
        if (r.getHistorial().estaVacia()) {
            return Retorno.error3();
        }
        Despliegue d = r.getHistorial().desapilar(); // O(1)
        r.getActivos().eliminar(d);                  // O(k), la letra lo permite
        return Retorno.ok(d.datos());
    }
}