package br.com.fiap.dao;

import br.com.fiap.to.ReceitaTO;
import java.sql.Connection; // Importa Connection
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReceitaDAO {

    private final String TABLE_NAME = "RECEITA";

    private ReceitaTO mapResultSetToReceitaTO(ResultSet rs) throws SQLException {
        ReceitaTO receita = new ReceitaTO();
        receita.setIdReceita(rs.getLong("id_receita"));
        receita.setIdConsulta(rs.getLong("id_consulta"));
        receita.setDescricaoReceita(rs.getString("descricao_receita"));
        return receita;
    }

    /**
     * Lista todas as receitas.
     */
    public ArrayList<ReceitaTO> findAll() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<ReceitaTO> receitas = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY id_receita";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                receitas.add(mapResultSetToReceitaTO(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro na consulta (findAll): " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
            ConnectionFactory.closeConnection();
        }
        return receitas;
    }

    /**
     * Busca uma receita pelo seu ID.
     */
    public ReceitaTO findById(Long idReceita) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReceitaTO receita = null;
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id_receita = ?";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, idReceita);
            rs = ps.executeQuery();

            if (rs.next()) {
                receita = mapResultSetToReceitaTO(rs);
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
        return receita;
    }

    /**
     * Salva uma nova receita e tenta retornar o ID gerado.
     */
    public ReceitaTO save(ReceitaTO receita) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rsGeneratedKeys = null;

        String sql_insert = "INSERT INTO " + TABLE_NAME + " (id_consulta, descricao_receita) VALUES (?, ?)";

        try {
            con = ConnectionFactory.getConnection();
            // Solicita retorno da chave
            ps = con.prepareStatement(sql_insert, new String[]{"id_receita"});

            ps.setLong(1, receita.getIdConsulta());
            ps.setString(2, receita.getDescricaoReceita());

            if (ps.executeUpdate() > 0) {
                // Tenta recuperar o ID gerado
                rsGeneratedKeys = ps.getGeneratedKeys();
                if (rsGeneratedKeys.next()) {
                    receita.setIdReceita(rsGeneratedKeys.getLong(1));
                    return receita;
                }
                // Se o ID não foi recuperado (mas a inserção foi OK), retorna o objeto
                return receita;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        } finally {
            try {
                if (rsGeneratedKeys != null) rsGeneratedKeys.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
            ConnectionFactory.closeConnection();
        }
        return null;
    }

    /**
     * Exclui uma receita pelo seu ID.
     */
    public boolean delete(Long idReceita) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean sucesso = false;
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id_receita = ?";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, idReceita);
            sucesso = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir: " + e.getMessage());
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
     * Atualiza uma receita existente.
     */
    public ReceitaTO update(ReceitaTO receita) {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "UPDATE " + TABLE_NAME + " SET id_consulta=?, descricao_receita=? WHERE id_receita=?";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, receita.getIdConsulta());
            ps.setString(2, receita.getDescricaoReceita());
            ps.setLong(3, receita.getIdReceita());

            if (ps.executeUpdate() > 0) {
                return receita;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar: " + e.getMessage());
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