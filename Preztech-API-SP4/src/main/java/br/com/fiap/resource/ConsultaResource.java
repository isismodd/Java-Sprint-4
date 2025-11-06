package br.com.fiap.resource;

import br.com.fiap.bo.ConsultaBO;
import br.com.fiap.to.ConsultaTO;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

@Path("/consultas")
public class ConsultaResource {

    private ConsultaBO consultaBO = new ConsultaBO();

    /**
     * GET /consultas/{id}: Busca consulta pelo ID. (Seu GET(id(consulta)))
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        ConsultaTO resultado = consultaBO.findById(id);

        if (resultado != null) {
            return Response.ok(resultado).build();  // 200 OK
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();  // 404 NOT FOUND
        }
    }

    /**
     * GET /consultas/paciente/{cpf}: Lista consultas por CPF. (Seu GET(cpf_paciente))
     */
    @GET
    @Path("/paciente/{cpf}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByCpf(@PathParam("cpf") String cpf) {
        ArrayList<ConsultaTO> resultado = consultaBO.findByCpf(cpf);

        if (resultado != null && !resultado.isEmpty()) {
            return Response.ok(resultado).build(); // 200 OK
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(resultado).build(); // 404 NOT FOUND ou lista vazia
        }
    }

    /**
     * POST /consultas: Cria uma nova consulta. (Seu POST)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(@Valid ConsultaTO consulta) {
        ConsultaTO resultado = consultaBO.save(consulta);

        if (resultado != null && resultado.getIdConsulta() != null){
            return Response.created(null).entity(resultado).build();  // 201 CREATED
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();  // 400 BAD REQUEST
        }
    }

    /**
     * PUT /consultas/{id}: Atualiza uma consulta. (Seu SET)
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@Valid ConsultaTO consulta, @PathParam("id") Long id) {
        consulta.setIdConsulta(id);
        ConsultaTO resultado = consultaBO.update(consulta);

        if (resultado != null){
            return Response.ok(resultado).build(); // 200 OK
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();  // 400 BAD REQUEST
        }
    }

    /**
     * DELETE /consultas/{id}: Exclui uma consulta. (Seu DELETE)
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        if (consultaBO.delete(id)){
            return Response.status(Response.Status.NO_CONTENT).build();  // 204 NO CONTENT
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();  // 404 NOT FOUND
        }
    }
}