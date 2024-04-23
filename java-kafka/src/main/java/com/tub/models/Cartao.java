package com.tub.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cartao")
public class Cartao {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    private Boolean valido;

    @OneToOne(mappedBy = "cartao")
    private Conta contaAssociada;

    public Cartao() {
    }

    public Cartao(Boolean valido) {
        this.valido = valido;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }

    public Conta getContaAssociada() {
        return contaAssociada;
    }

    public void setContaAssociada(Conta contaAssociada) {
        this.contaAssociada = contaAssociada;
    }

    @Override
    public String toString() {
        return "Cartao{" +
                "id=" + id +
                ", valido=" + valido +
                //", contaAssociada=" + contaAssociada +
                '}';
    }
}
