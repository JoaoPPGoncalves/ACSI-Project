package com.tub.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "viagem")
public class Viagem {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID")
    private Integer id;
    private LocalDateTime data;
    private int pontos;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="conta_id", referencedColumnName = "id")
    private Conta contaViajante;

    public Viagem() {
    }

    public Viagem(LocalDateTime data, int pontos, Conta contaViajante) {
        this.data = data;
        this.pontos = pontos;
        this.contaViajante = contaViajante;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public Conta getConta() {
        return contaViajante;
    }

    public void setConta(Conta contaViajante) {
        this.contaViajante = contaViajante;
    }

    @Override
    public String toString() {
        return //"Viagem" +
                "{" +
                //"id=" + id +
                "data=" + data +
                ", pontos=" + pontos +
                '}';
    }
}
