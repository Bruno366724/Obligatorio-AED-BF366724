package sistema;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sistema.Retorno.Resultado;

public class Test06_Regiones {

    private Sistema s;

    @Before
    public void setUp() {
        s = new ImplementacionSistema();
        s.inicializarSistema();
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "desc 1", false, Modalidad.TEXTO);
        s.registrarDespliegue("DEP-0002", "AUD-002-C3D4", "desc 2", true, Modalidad.AUDIO);
        s.registrarDespliegue("DEP-0003", "VIS-004-C3D4", "desc 3", false, Modalidad.VISION);
        s.registrarRegion("us-east-1", "Virginia", "EEUU", "a");
        s.registrarRegion("eu-west-1", "Irlanda", "Irlanda", "b");
    }

    private String datos(String id) {
        return s.buscarDesplieguePorId(id).getValorString();
    }

    // ---------- 09 y 10 ----------
    @Test
    public void registrarRegionError1() {
        assertEquals(Resultado.ERROR_1, s.registrarRegion(null, "n", "p", "z").getResultado());
        assertEquals(Resultado.ERROR_1, s.registrarRegion("c", " ", "p", "z").getResultado());
        assertEquals(Resultado.ERROR_1, s.registrarRegion("c", "n", "", "z").getResultado());
        assertEquals(Resultado.ERROR_1, s.registrarRegion("c", "n", "p", null).getResultado());
    }

    @Test
    public void registrarRegionError2() {
        assertEquals(Resultado.ERROR_2, s.registrarRegion("us-east-1", "x", "y", "z").getResultado());
    }

    @Test
    public void obtenerRegionOkYErrores() {
        Retorno r = s.obtenerRegion("us-east-1");
        assertEquals(Resultado.OK, r.getResultado());
        assertEquals("us-east-1;Virginia;EEUU;a", r.getValorString());
        assertEquals(Resultado.ERROR_1, s.obtenerRegion(" ").getResultado());
        assertEquals(Resultado.ERROR_2, s.obtenerRegion("xx").getResultado());
    }

    // ---------- 11 ----------
    @Test
    public void solicitarErroresEnOrden() {
        assertEquals(Resultado.ERROR_1, s.solicitarDespliegueEnRegion(null, "us-east-1").getResultado());
        assertEquals(Resultado.ERROR_1, s.solicitarDespliegueEnRegion("DEP-0001", " ").getResultado());
        assertEquals(Resultado.ERROR_2, s.solicitarDespliegueEnRegion("DEP-9999", "us-east-1").getResultado());
        assertEquals(Resultado.ERROR_3, s.solicitarDespliegueEnRegion("DEP-0001", "xx").getResultado());
        // si fallan 2 y 3 a la vez, gana el menor
        assertEquals(Resultado.ERROR_2, s.solicitarDespliegueEnRegion("DEP-9999", "xx").getResultado());
    }

    @Test
    public void solicitarRepetidoPendienteError4() {
        assertEquals(Resultado.OK, s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1").getResultado());
        assertEquals(Resultado.ERROR_4, s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1").getResultado());
    }

    @Test
    public void solicitarYaActivoError4() {
        s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1");
        s.procesarSiguienteDespliegue("us-east-1");
        assertEquals(Resultado.ERROR_4, s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1").getResultado());
    }

    @Test
    public void estadosSonIndependientesPorRegion() {
        s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1");
        assertEquals(Resultado.OK, s.solicitarDespliegueEnRegion("DEP-0001", "eu-west-1").getResultado());
    }

    // ---------- 12 ----------
    @Test
    public void procesarRespetaOrdenFifo() {
        s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1");
        s.solicitarDespliegueEnRegion("DEP-0002", "us-east-1");
        s.solicitarDespliegueEnRegion("DEP-0003", "us-east-1");
        assertEquals(datos("DEP-0001"), s.procesarSiguienteDespliegue("us-east-1").getValorString());
        assertEquals(datos("DEP-0002"), s.procesarSiguienteDespliegue("us-east-1").getValorString());
        assertEquals(datos("DEP-0003"), s.procesarSiguienteDespliegue("us-east-1").getValorString());
        assertEquals(Resultado.ERROR_3, s.procesarSiguienteDespliegue("us-east-1").getResultado());
    }

    @Test
    public void procesarErrores() {
        assertEquals(Resultado.ERROR_1, s.procesarSiguienteDespliegue(null).getResultado());
        assertEquals(Resultado.ERROR_2, s.procesarSiguienteDespliegue("xx").getResultado());
        assertEquals(Resultado.ERROR_3, s.procesarSiguienteDespliegue("us-east-1").getResultado());
    }

    // ---------- 13 ----------
    @Test
    public void activosOrdenadosPorCodigo() {
        s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1"); // LLM
        s.solicitarDespliegueEnRegion("DEP-0002", "us-east-1"); // AUD
        s.solicitarDespliegueEnRegion("DEP-0003", "us-east-1"); // VIS
        s.procesarSiguienteDespliegue("us-east-1");
        s.procesarSiguienteDespliegue("us-east-1");
        s.procesarSiguienteDespliegue("us-east-1");
        Retorno r = s.listarDesplieguesActivosDeRegion("us-east-1");
        assertEquals(Resultado.OK, r.getResultado());
        assertEquals(datos("DEP-0002") + "|" + datos("DEP-0001") + "|" + datos("DEP-0003"),
                r.getValorString());
    }

    @Test
    public void activosSinElementosYErrores() {
        Retorno r = s.listarDesplieguesActivosDeRegion("us-east-1");
        assertEquals(Resultado.OK, r.getResultado());
        assertEquals("", r.getValorString());
        assertEquals(Resultado.ERROR_1, s.listarDesplieguesActivosDeRegion("").getResultado());
        assertEquals(Resultado.ERROR_2, s.listarDesplieguesActivosDeRegion("xx").getResultado());
    }

    // ---------- 14 ----------
    @Test
    public void retirarRespetaOrdenLifo() {
        s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1");
        s.solicitarDespliegueEnRegion("DEP-0002", "us-east-1");
        s.procesarSiguienteDespliegue("us-east-1");
        s.procesarSiguienteDespliegue("us-east-1");
        assertEquals(datos("DEP-0002"), s.retirarUltimoDespliegueProcesado("us-east-1").getValorString());
        assertEquals(datos("DEP-0001"), s.listarDesplieguesActivosDeRegion("us-east-1").getValorString());
        assertEquals(datos("DEP-0001"), s.retirarUltimoDespliegueProcesado("us-east-1").getValorString());
        assertEquals(Resultado.ERROR_3, s.retirarUltimoDespliegueProcesado("us-east-1").getResultado());
        assertEquals("", s.listarDesplieguesActivosDeRegion("us-east-1").getValorString());
    }

    @Test
    public void retirarNoAfectaOtraRegionNiElCatalogo() {
        s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1");
        s.solicitarDespliegueEnRegion("DEP-0001", "eu-west-1");
        s.procesarSiguienteDespliegue("us-east-1");
        s.procesarSiguienteDespliegue("eu-west-1");
        s.retirarUltimoDespliegueProcesado("us-east-1");
        assertEquals(datos("DEP-0001"), s.listarDesplieguesActivosDeRegion("eu-west-1").getValorString());
        assertEquals(Resultado.OK, s.buscarDesplieguePorId("DEP-0001").getResultado());
    }

    @Test
    public void retiradoPuedeVolverASolicitarse() {
        s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1");
        s.procesarSiguienteDespliegue("us-east-1");
        s.retirarUltimoDespliegueProcesado("us-east-1");
        assertEquals(Resultado.OK, s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1").getResultado());
    }

    @Test
    public void retirarErrores() {
        assertEquals(Resultado.ERROR_1, s.retirarUltimoDespliegueProcesado(null).getResultado());
        assertEquals(Resultado.ERROR_2, s.retirarUltimoDespliegueProcesado("xx").getResultado());
        assertEquals(Resultado.ERROR_3, s.retirarUltimoDespliegueProcesado("us-east-1").getResultado());
    }
}