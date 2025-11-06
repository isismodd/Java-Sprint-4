package br.com.fiap;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Garante a ordem de execução
class ConsultaResourceTest {

    // Variáveis estáticas para compartilhar dados entre os testes
    // O ID real será capturado após a inserção
    private static Long consultaId;
    private static final String TEST_CPF = "99988877766";

    // JSON para criação (a data/hora será no futuro)
    private static final String VALID_CONSULTA_JSON =
            "{\"cpfPaciente\": \"" + TEST_CPF + "\", \"dataHora\": \"2026-06-01T10:00:00\"}";

    /**
     * TESTE 1: Tenta criar uma nova consulta via POST.
     */
    @Test
    @Order(1)
    void test1_PostNewConsulta() {
        // Correção: Faz o cast para Integer e depois usa .longValue() para obter um Long
        consultaId = ((Integer) given()
                .contentType("application/json")
                .body(VALID_CONSULTA_JSON)
                .when()
                .post("/consultas")
                .then()
                .statusCode(201)
                .body("idConsulta", notNullValue())
                .body("cpfPaciente", is(TEST_CPF))
                .extract().path("idConsulta")).longValue();

        System.out.println("✅ Consulta criada com ID: " + consultaId);
    }

    /**
     * TESTE 2: Busca a consulta recém-criada pelo ID.
     */
    @Test
    @Order(2)
    void test2_GetConsultaById() {
        if (consultaId == null) return; // Caso o teste 1 falhe

        // Tenta buscar a consulta usando o ID capturado
        given()
                .when()
                .get("/consultas/{id}", consultaId)
                .then()
                .statusCode(200)
                .body("idConsulta", is(consultaId.intValue())) // Verifica se o ID corresponde
                .body("cpfPaciente", is(TEST_CPF));

        System.out.println("✅ Consulta ID: " + consultaId + " encontrada.");
    }

    /**
     * TESTE 3: Busca a consulta recém-criada pelo CPF (deve retornar um array).
     */
    @Test
    @Order(3)
    void test3_GetConsultasByCpf() {
        // Tenta buscar as consultas do paciente usando o CPF
        given()
                .when()
                .get("/consultas/paciente/{cpf}", TEST_CPF)
                .then()
                .statusCode(200)
                .body("size()", is(1)); // Verifica se pelo menos 1 consulta foi retornada

        System.out.println("✅ Consulta encontrada pelo CPF: " + TEST_CPF + ".");
    }

    /**
     * TESTE 4: Deleta a consulta.
     */
    @Test
    @Order(4)
    void test4_DeleteConsulta() {
        if (consultaId == null) return; // Caso o teste 1 falhe

        // Deleta a consulta e espera 204 NO CONTENT
        given()
                .when()
                .delete("/consultas/{id}", consultaId)
                .then()
                .statusCode(204);

        // Tenta buscar novamente para garantir que foi deletada (espera 404 NOT FOUND)
        given()
                .when()
                .get("/consultas/{id}", consultaId)
                .then()
                .statusCode(404);

        System.out.println("✅ Consulta ID: " + consultaId + " deletada com sucesso.");
    }
}