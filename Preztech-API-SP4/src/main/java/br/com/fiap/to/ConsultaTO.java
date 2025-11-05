package br.com.fiap.to;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class ConsultaTO {

    private Long idConsulta;

    @NotBlank(message = "O CPF do paciente é obrigatório.")
    @Pattern(regexp = "^[0-9]{11}$", message = "CPF deve conter exatamente 11 dígitos numéricos.")
    private String cpfPaciente;

    @NotNull(message = "A data e hora da consulta são obrigatórias.")
    @FutureOrPresent(message = "A consulta deve ser agendada para o presente ou futuro.")
    private LocalDateTime dataHora;

    // Construtores
    public ConsultaTO() {
    }

    public ConsultaTO(Long idConsulta, String cpfPaciente, LocalDateTime dataHora) {
        this.idConsulta = idConsulta;
        this.cpfPaciente = cpfPaciente;
        this.dataHora = dataHora;
    }

    // Getters e Setters
    public Long getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Long idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getCpfPaciente() {
        return cpfPaciente;
    }

    public void setCpfPaciente(String cpfPaciente) {
        this.cpfPaciente = cpfPaciente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}