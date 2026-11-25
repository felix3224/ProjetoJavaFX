package org.example.projetojavafx.model.entities;

public class Turma {
    private int cod_turma;
    private String nome;

    public Turma() {
    }

    public Turma(int cod_turma, String nome) {
        this.cod_turma = cod_turma;
        this.nome = nome;
    }

    public int getCod_turma() {
        return cod_turma;
    }

    public void setCod_turma(int cod_turma) {
        this.cod_turma = cod_turma;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
