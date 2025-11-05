package br.com.fiap;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import br.com.fiap.dao.ConnectionFactory; // Importar se necessário para o setupDatabase

import java.sql.SQLException;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReceitaResourceTest {

    // Variáveis estáticas para compartilhar IDs entre os testes
    private static Long consultaId;
    private static Long receitaId;

    // Dados para a Consulta de teste (necessária como chave estrangeira)
    private static final String TEST_CPF = "12345678900";
    private static final String VALID_CONSULTA_JSON =
            "{\"cpfPaciente\": \"" + TEST_CPF + "\", \"dataHora\": \"2026-06-01T10:00:00\"}";

    private static final String RECEITA_DESCRICAO = "Analgésico 500mg, 1x ao dia.";

    // --- SETUP: Limpa e Prepara o ambiente ---

    @BeforeAll // Executa uma vez antes de todos os testes nesta classe
    static void setupDatabase() {
        // Limpa qualquer dado residual do CPF/Consulta/Receita de teste antes de começar.
        try (java.sql.Connection con = ConnectionFactory.getConnection();
             java.sql.PreparedStatement psReceita = con.prepareStatement("DELETE FROM RECEITA WHERE id_consulta IN (SELECT id_consulta FROM CONSULTA WHERE cpf_paciente = ?)");
             java.sql.PreparedStatement psConsulta = con.prepareStatement("DELETE FROM CONSULTA WHERE cpf_paciente = ?")) {

            // 1. Deleta Receitas que dependem da Consulta de teste
            psReceita.setString(1, TEST_CPF);
            psReceita.executeUpdate();

            // 2. Deleta a Consulta de teste
            psConsulta.setString(1, TEST_CPF);
            int rowsDeleted = psConsulta.executeUpdate();

            System.out.println("--- Limpeza de Dados ReceitaTest: " + rowsDeleted + " consultas deletadas. ---");

        } catch (SQLException e) {
            System.out.println("ERRO NA LIMPEZA DE BANCO: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    // --- TESTES DE PRÉ-REQUISITO (CRIAR CONSULTA) ---

    /**
     * PASSO 1: Cria uma consulta válida para ser usada como FK para a Receita.
     */
    @Test
    @Order(1)
    void test1_Prerequisito_PostNewConsulta() {
        System.out.println("Iniciando Teste 1: Criando Consulta Pré-requisito...");
        // Usa a correção para a ClassCastException (Integer -> Long)
        consultaId = ((Integer) given()
                .contentType("application/json")
                .body(VALID_CONSULTA_JSON)
                .when()
                .post("/consultas") // EndPoint da Consulta
                .then()
                .statusCode(201)
                .body("idConsulta", notNullValue())
                .extract().path("idConsulta")).longValue();

        System.out.println("✅ Consulta criada com ID: " + consultaId);
    }

    // --- TESTES DA RECEITA ---

    /**
     * PASSO 2: Tenta criar uma nova receita, vinculando-a à consulta criada.
     */
    @Test
    @Order(2)
    void test2_PostNewReceita() {
        if (consultaId == null) return; // Se o passo 1 falhou, pula

        String validReceitaJson = "{\"idConsulta\": " + consultaId + ", \"descricaoReceita\": \"" + RECEITA_DESCRICAO + "\"}";

        System.out.println("Iniciando Teste 2: Criando Receita...");

        // Usa a correção para a ClassCastException (Integer -> Long)
        receitaId = ((Integer) given()
                .contentType("application/json")
                .body(validReceitaJson)
                .when()
                .post("/receitas") // Seu @Path na classe ReceitaResource
                .then()
                .statusCode(201)
                .body("idReceita", notNullValue()) // Verifica se o ID foi gerado
                .body("idConsulta", is(consultaId.intValue())) // Verifica o FK
                .extract().path("idReceita")).longValue(); // Captura o ID da Receita

        System.out.println("✅ Receita criada com ID: " + receitaId);
    }

    /**
     * PASSO 3: Busca a receita recém-criada pelo ID.
     */
    @Test
    @Order(3)
    void test3_GetReceitaById() {
        if (receitaId == null) return;

        System.out.println("Iniciando Teste 3: Buscando Receita...");

        // Tenta buscar a receita usando o ID capturado
        given()
                .when()
                .get("/receitas/{id}", receitaId)
                .then()
                .statusCode(200)
                .body("idReceita", is(receitaId.intValue()))
                .body("descricaoReceita", is(RECEITA_DESCRICAO));

        System.out.println("✅ Receita ID: " + receitaId + " encontrada.");
    }

    /* * PASSO 4: Deleta a receita.
     * Este metodo foi COMENTADO para que os dados persistam no banco de dados
     * e possa verificar a população da tabela após a execução dos testes.
     */
    // @Test
    // @Order(4)
    // void test4_DeleteReceita() {
    //     if (receitaId == null) return;
    //
    //     System.out.println("Iniciando Teste 4: Deletando Receita...");
    //
    //     // Deleta a receita e espera 204 NO CONTENT
    //     given()
    //             .when()
    //             .delete("/receitas/{id}", receitaId)
    //             .then()
    //             .statusCode(204);
    //
    //     // Tenta buscar novamente para garantir que foi deletada (espera 404 NOT FOUND)
    //     given()
    //             .when()
    //             .get("/receitas/{id}", receitaId)
    //             .then()
    //             .statusCode(404);
    //
    //     System.out.println("✅ Receita ID: " + receitaId + " deletada com sucesso.");
    // }
}