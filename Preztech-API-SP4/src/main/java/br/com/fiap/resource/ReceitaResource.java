package br.com.fiap.resource;

import br.com.fiap.bo.ReceitaBO;
import br.com.fiap.to.ReceitaTO;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

@Path("/receitas") // Mudamos o path base
public class ReceitaResource {

    private ReceitaBO receitaBO = new ReceitaBO(); // Usando o BO adaptado

    /**
     * GET /receitas: Lista todas as receitas.
     * @return 200 OK ou 404 NOT FOUND
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        ArrayList<ReceitaTO> resultado = receitaBO.findAll();
        Response.ResponseBuilder response = null;

        if (resultado != null && !resultado.isEmpty()) {
            response = Response.ok(); // 200 - OK
        }
        else {
            response = Response.status(404);  // 404 - NOT FOUND
        }
        response.entity(resultado);
        return response.build();
    }

    /**
     * GET /receitas/{id}: Busca receita pelo ID.
     * @param id Receita
     * @return 200 OK ou 404 NOT FOUND
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        ReceitaTO resultado = receitaBO.findById(id);
        Response.ResponseBuilder response = null;

        if (resultado != null) {
            response = Response.ok();  // 200 (OK)
        } else {
            response = Response.status(404);  // 404 (NOT FOUND)
        }
        response.entity(resultado);
        return response.build();
    }

    /**
     * POST /receitas: Cria uma nova receita.
     * @param receita Dados da Receita a ser criada
     * @return 201 CREATED ou 400 BAD REQUEST
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(@Valid ReceitaTO receita) {
        ReceitaTO resultado = receitaBO.save(receita);
        Response.ResponseBuilder response = null;

        if (resultado != null && resultado.getIdReceita() != null){
            // 201 CREATED - Retorna a nova entidade com o ID gerado
            response = Response.created(null);
        } else {
            response = Response.status(400);  // 400 - BAD REQUEST (erro de validação/persistência)
        }
        response.entity(resultado);
        return response.build();
    }

    /**
     * PUT /receitas/{id}: Atualiza uma receita existente.
     * @param receita Dados da Receita a ser atualizada
     * @param id ID da Receita na URI
     * @return 201 CREATED (ou 200 OK) ou 400 BAD REQUEST
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@Valid ReceitaTO receita, @PathParam("id") Long id) {
        // Garante que o ID da URI seja usado no objeto TO
        receita.setIdReceita(id);

        ReceitaTO resultado = receitaBO.update(receita);
        Response.ResponseBuilder response = null;

        if (resultado != null){
            // Retorna a entidade atualizada
            response = Response.status(Response.Status.OK); // 200 - OK para atualização
        } else {
            response = Response.status(400);  // 400 - BAD REQUEST
        }
        response.entity(resultado);
        return response.build();
    }

    /**
     * DELETE /receitas/{id}: Exclui uma receita.
     * @param id ID da Receita a ser excluída
     * @return 204 NO CONTENT ou 404 NOT FOUND
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Response.ResponseBuilder response = null;

        if (receitaBO.delete(id)){
            response = Response.status(204);  // 204 - NO CONTENT (sucesso sem corpo de retorno)
        } else {
            response = Response.status(404);  // 404 - NOT FOUND (ID não encontrado)
        }
        return response.build();
    }
}