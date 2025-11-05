package br.com.fiap.dao;

import br.com.fiap.to.ConsultaTO;
import java.sql.*;
import java.util.ArrayList;

public class ConsultaDAO {

    private final String TABLE_NAME = "CONSULTA";

    private ConsultaTO mapResultSetToConsultaTO(ResultSet rs) throws SQLException {
        ConsultaTO consulta = new ConsultaTO();
        consulta.setIdConsulta(rs.getLong("id_consulta"));
        consulta.setCpfPaciente(rs.getString("cpf_paciente"));
        consulta.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        return consulta;
    }

    /**
     * Busca uma consulta pelo seu ID.
     */
    public ConsultaTO findById(Long idConsulta) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ConsultaTO consulta = null;
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id_consulta = ?";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, idConsulta);
            rs = ps.executeQuery();

            if (rs.next()) {
                consulta = mapResultSetToConsultaTO(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro na consulta (findById): " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
            ConnectionFactory.closeConnection();
        }
        return consulta;
    }

    /**
     * Lista consultas por CPF do paciente.
     */
    public ArrayList<ConsultaTO> findByCpf(String cpfPaciente) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<ConsultaTO> consultas = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE cpf_paciente = ? ORDER BY data_hora DESC";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, cpfPaciente);
            rs = ps.executeQuery();

            while (rs.next()) {
                consultas.add(mapResultSetToConsultaTO(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro na consulta (findByCpf): " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
            ConnectionFactory.closeConnection();
        }
        return consultas;
    }

    /**
     * Salva uma nova consulta.
     */
    public ConsultaTO save(ConsultaTO consulta) {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "INSERT INTO " + TABLE_NAME + " (cpf_paciente, data_hora) VALUES (?, ?)";

        try {
            con = ConnectionFactory.getConnection();
            // Necessário para obter o ID gerado
            ps = con.prepareStatement(sql, new String[]{"id_consulta"});

            ps.setString(1, consulta.getCpfPaciente());
            ps.setTimestamp(2, Timestamp.valueOf(consulta.getDataHora()));

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        consulta.setIdConsulta(rs.getLong(1));
                        return consulta;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao salvar (save): " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
            ConnectionFactory.closeConnection();
        }
        return null;
    }

    /**
     * Exclui uma consulta.
     */
    public boolean delete(Long idConsulta) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean sucesso = false;
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id_consulta = ?";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, idConsulta);
            sucesso = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir (delete): " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
            ConnectionFactory.closeConnection();
        }
        return sucesso;
    }

    /**
     * Atualiza uma consulta.
     */
    public ConsultaTO update(ConsultaTO consulta) {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "UPDATE " + TABLE_NAME + " SET cpf_paciente=?, data_hora=? WHERE id_consulta=?";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, consulta.getCpfPaciente());
            ps.setTimestamp(2, Timestamp.valueOf(consulta.getDataHora()));
            ps.setLong(3, consulta.getIdConsulta());

            if (ps.executeUpdate() > 0) {
                return consulta;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar (update): " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
            ConnectionFactory.closeConnection();
        }
        return null;
    }
}