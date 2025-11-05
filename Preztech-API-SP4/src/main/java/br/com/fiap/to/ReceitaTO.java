package br.com.fiap.to;

import jakarta.validation.constraints.*;

public class ReceitaTO {

    // O ID da Receita, geralmente Long para chaves primárias
    private Long idReceita;

    // ID da Consulta (Chave Estrangeira)
    // Não pode ser nulo ao criar ou atualizar uma receita válida
    @NotNull(message = "O ID da Consulta é obrigatório")
    @Positive(message = "O ID da Consulta deve ser um valor positivo")
    private Long idConsulta;

    // Descrição da Receita (Onde estava o 'remedioReceitado' + ajuste para a coluna 'descricao_receita')
    @NotBlank(message = "A descrição da receita não pode estar vazia")
    @Size(max = 400, message = "A descrição não pode exceder 400 caracteres") // Limite conforme o SQL
    private String descricaoReceita;

    // Construtores
    public ReceitaTO() {
    }

    public ReceitaTO(Long idReceita, Long idConsulta, String descricaoReceita) {
        this.idReceita = idReceita;
        this.idConsulta = idConsulta;
        this.descricaoReceita = descricaoReceita;
    }

    // Métodos Getters/Setters

    public Long getIdReceita() {
        return idReceita;
    }

    public void setIdReceita(Long idReceita) {
        this.idReceita = idReceita;
    }

    public Long getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Long idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getDescricaoReceita() {
        return descricaoReceita;
    }

    public void setDescricaoReceita(String descricaoReceita) {
        this.descricaoReceita = descricaoReceita;
    }
}