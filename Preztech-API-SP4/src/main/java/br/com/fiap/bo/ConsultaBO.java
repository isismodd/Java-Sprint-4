package br.com.fiap.bo;

import br.com.fiap.dao.ConsultaDAO;
import br.com.fiap.to.ConsultaTO;
import java.util.ArrayList;

public class ConsultaBO {

    private ConsultaDAO consultaDAO;

    /**
     * Busca uma consulta pelo seu ID.
     */
    public ConsultaTO findById(Long idConsulta) {
        consultaDAO = new ConsultaDAO();
        // Implementar RN: Ex: Verificar se o ID é positivo
        if (idConsulta == null || idConsulta <= 0) {
            return null;
        }
        return consultaDAO.findById(idConsulta);
    }

    /**
     * Lista todas as consultas de um paciente pelo CPF.
     */
    public ArrayList<ConsultaTO> findByCpf(String cpfPaciente) {
        consultaDAO = new ConsultaDAO();
        // Implementar RN: Ex: Formatar/validar o CPF (embora a validação já esteja no TO)
        return consultaDAO.findByCpf(cpfPaciente);
    }

    /**
     * Agenda uma nova consulta.
     */
    public ConsultaTO save (ConsultaTO consulta) {
        consultaDAO = new ConsultaDAO();
        // Implementar RN: Ex: Verificar se o horário não está ocupado
        /* if (consultaDAO.isHorarioOcupado(consulta.getDataHora())) {
            return null;
        } */
        return consultaDAO.save(consulta);
    }

    /**
     * Exclui uma consulta.
     */
    public boolean delete(Long idConsulta) {
        consultaDAO = new ConsultaDAO();
        // Implementar RN: Ex: Não permitir exclusão de consultas realizadas no passado
        return consultaDAO.delete(idConsulta);
    }

    /**
     * Atualiza uma consulta.
     */
    public ConsultaTO update(ConsultaTO consulta) {
        consultaDAO = new ConsultaDAO();
        // Implementar RN: Ex: Garantir que o ID da consulta não pode ser alterado
        return consultaDAO.update(consulta);
    }
}