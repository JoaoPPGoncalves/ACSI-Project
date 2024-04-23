package com.tub.models;


import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "conta")
public class Conta {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cartao_id", referencedColumnName = "id")
    private Cartao cartao;

    private Integer pontos;

    @OneToMany(mappedBy="contaViajante")
    private List<Viagem> viagens;

    public Conta() {
    }

    public Conta(String name, Integer pontos) {
        this.name = name;
        this.pontos = pontos;
    }

    public Conta(String name, Cartao cartao, Integer pontos) {
        this.name = name;
        this.cartao = cartao;
        this.pontos = pontos;
    }

    public Integer getId() {
        return id;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    @Override
    public String toString() {
        return "Conta{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cartao=" + cartao +
                ", pontos=" + pontos +
                '}';
    }
}
