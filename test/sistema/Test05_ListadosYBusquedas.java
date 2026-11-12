package sistema;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sistema.Retorno.Resultado;

public class Test05_ListadosYBusquedas {

    private Sistema s;

    @Before
    public void setUp() {
        s = new ImplementacionSistema();
        s.inicializarSistema();
    }

    private void reg(String id, String codigo, Modalidad m) {
        Retorno r = s.registrarDespliegue(id, codigo, "desc " + id, false, m);
        assertEquals(Resultado.OK, r.getResultado());
    }

    private String datos(String id, String codigo, String modTexto) {
        return id + ";" + codigo + ";desc " + id + ";false;" + modTexto;
    }

    @Test
    public void listarPorIdDescendenteConIngresoDesordenado() {
        reg("DEP-0001", "LLM-070-A1B2", Modalidad.TEXTO);
        reg("DEP-0003", "AUD-002-C3D4", Modalidad.AUDIO);
        reg("DEP-0002", "VIS-004-C3D4", Modalidad.VISION);
        Retorno r = s.listarDesplieguesPorIdDescendente();
        assertEquals(Resultado.OK, r.getResultado());
        assertEquals(datos("DEP-0003", "AUD-002-C3D4", "Audio") + "|"
                + datos("DEP-0002", "VIS-004-C3D4", "Visión") + "|"
                + datos("DEP-0001", "LLM-070-A1B2", "Texto"), r.getValorString());
    }

    @Test
    public void buscarPorCodigoOk() {
        reg("DEP-0001", "LLM-070-A1B2", Modalidad.TEXTO);
        Retorno r = s.buscarDesplieguePorCodigo("LLM-070-A1B2");
        assertEquals(Resultado.OK, r.getResultado());
        assertEquals(datos("DEP-0001", "LLM-070-A1B2", "Texto"), r.getValorString());
    }

    @Test
    public void buscarPorCodigoError1() {
        assertEquals(Resultado.ERROR_1, s.buscarDesplieguePorCodigo(null).getResultado());
        assertEquals(Resultado.ERROR_1, s.buscarDesplieguePorCodigo("").getResultado());
        assertEquals(Resultado.ERROR_1, s.buscarDesplieguePorCodigo("   ").getResultado());
    }

    @Test
    public void buscarPorCodigoError2() {
        assertEquals(Resultado.ERROR_2, s.buscarDesplieguePorCodigo("llm-070-A1B2").getResultado());
        assertEquals(Resultado.ERROR_2, s.buscarDesplieguePorCodigo("LLM-070-A1_2").getResultado());
    }

    @Test
    public void buscarPorCodigoError3() {
        reg("DEP-0001", "LLM-070-A1B2", Modalidad.TEXTO);
        assertEquals(Resultado.ERROR_3, s.buscarDesplieguePorCodigo("AUD-002-C3D4").getResultado());
    }

    @Test
    public void listarPorCodigoAscendenteOrdenaPorCodigoNoPorId() {
        reg("DEP-0001", "LLM-070-A1B2", Modalidad.TEXTO);
        reg("DEP-0002", "AUD-002-C3D4", Modalidad.AUDIO);
        Retorno r = s.listarDesplieguesPorCodigoAscendente();
        assertEquals(Resultado.OK, r.getResultado());
        assertEquals(datos("DEP-0002", "AUD-002-C3D4", "Audio") + "|"
                + datos("DEP-0001", "LLM-070-A1B2", "Texto"), r.getValorString());
    }

    @Test
    public void listarPorCodigoVacioDevuelveOkYVacio() {
        Retorno r = s.listarDesplieguesPorCodigoAscendente();
        assertEquals(Resultado.OK, r.getResultado());
        assertEquals("", r.getValorString());
    }

    @Test
    public void listarPorModalidadNullError1() {
        assertEquals(Resultado.ERROR_1, s.listarDesplieguesPorModalidad(null).getResultado());
    }

    @Test
    public void listarPorModalidadSinElementosDevuelveOkYVacio() {
        reg("DEP-0001", "LLM-070-A1B2", Modalidad.TEXTO);
        Retorno r = s.listarDesplieguesPorModalidad(Modalidad.AUDIO);
        assertEquals(Resultado.OK, r.getResultado());
        assertEquals("", r.getValorString());
    }

    @Test
    public void listarPorModalidadFiltraYOrdenaPorCodigo() {
        reg("DEP-0001", "LLM-070-A1B2", Modalidad.TEXTO);
        reg("DEP-0002", "LLM-013-Z9Z9", Modalidad.TEXTO);
        reg("DEP-0003", "VIS-004-C3D4", Modalidad.VISION);
        Retorno r = s.listarDesplieguesPorModalidad(Modalidad.TEXTO);
        assertEquals(Resultado.OK, r.getResultado());
        assertEquals(datos("DEP-0002", "LLM-013-Z9Z9", "Texto") + "|"
                + datos("DEP-0001", "LLM-070-A1B2", "Texto"), r.getValorString());
    }
}