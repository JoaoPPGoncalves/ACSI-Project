package com.tub.repositories;

import com.tub.models.Viagem;
import com.tub.models.Conta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface ViagemRepository extends CrudRepository<Viagem, Integer> {

    @Query("SELECT v FROM Viagem v WHERE v.contaViajante = ?1")
    List<Viagem> getAllByContaId(Conta conta);

}
