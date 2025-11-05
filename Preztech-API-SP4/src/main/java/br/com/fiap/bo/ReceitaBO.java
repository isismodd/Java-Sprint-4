package br.com.fiap.bo;

// Assegure-se de que ReceitaDAO e ReceitaTO existam em seus respectivos pacotes
import br.com.fiap.dao.ReceitaDAO;
import br.com.fiap.to.ReceitaTO;

import java.util.ArrayList;

public class ReceitaBO {

    private ReceitaDAO receitaDAO;

    /**
     * Lista todas as receitas cadastradas.
     * @return ArrayList de ReceitaTO.
     */
    public ArrayList<ReceitaTO> findAll() {
        receitaDAO = new ReceitaDAO();
        // ** Implementação da regra de negócios para listar todas as receitas aqui, se houver **
        return receitaDAO.findAll();
    }

    /**
     * Busca uma receita pelo seu ID.
     * @param idReceita O ID da receita a ser buscada.
     * @return ReceitaTO correspondente ou null se não for encontrada.
     */
    public ReceitaTO findById(Long idReceita) {
        receitaDAO = new ReceitaDAO();
        // ** Implementação da regra de negócios para buscar por ID aqui, se houver **
        // Por exemplo: verificar permissão de acesso à receita
        return receitaDAO.findById(idReceita);
    }

    /**
     * Salva uma nova receita.
     * @param receita O objeto ReceitaTO a ser salvo.
     * @return ReceitaTO com o ID gerado ou null em caso de erro.
     */
    public ReceitaTO save (ReceitaTO receita) {
        receitaDAO = new ReceitaDAO();

        // ** Implementação da regra de negócios para salvar a receita aqui **
        // Por exemplo:
        // 1. Verificar se o id_consulta associado existe.
        // 2. Aplicar alguma formatação na descricao_receita.

        /* // Exemplo de RN: Verifica se a descrição da receita não está vazia.
        if (receita.getDescricaoReceita() == null || receita.getDescricaoReceita().trim().isEmpty()) {
            return null; // Retorna null se a regra de negócio não for atendida
        }
        */

        return receitaDAO.save(receita);
    }

    /**
     * Exclui uma receita pelo seu ID.
     * @param idReceita O ID da receita a ser excluída.
     * @return true se a exclusão foi bem-sucedida, false caso contrário.
     */
    public boolean delete(Long idReceita) {
        receitaDAO = new ReceitaDAO();
        // ** Implementação da regra de negócios para exclusão aqui, se houver **
        return receitaDAO.delete(idReceita);
    }

    /**
     * Atualiza uma receita existente.
     * @param receita O objeto ReceitaTO com os dados a serem atualizados.
     * @return ReceitaTO atualizada ou null em caso de erro.
     */
    public ReceitaTO update(ReceitaTO receita) {
        receitaDAO = new ReceitaDAO();
        // ** Implementação da regra de negócios para atualização aqui **
        return receitaDAO.update(receita);
    }
}