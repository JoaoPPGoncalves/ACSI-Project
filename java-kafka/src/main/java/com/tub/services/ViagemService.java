package com.tub.services;

import com.tub.models.Conta;
import com.tub.models.Viagem;
import com.tub.repositories.ContaRepository;
import com.tub.repositories.ViagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViagemService {

    private ViagemRepository viagemRepository;
    private ContaRepository contaRepository;

    public ViagemService(ViagemRepository viagemRepository, ContaRepository contaRepository) {
        this.viagemRepository = viagemRepository;
        this.contaRepository = contaRepository;
    }

    public List<Viagem> getHistorico(int id) {
        Conta conta = contaRepository.findById(id).get();
        List<Viagem> a= viagemRepository.getAllByContaId(conta);
        //System.out.println(a);
        return a;
    }
}
