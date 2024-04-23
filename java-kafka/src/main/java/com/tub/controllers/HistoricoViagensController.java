package com.tub.controllers;

import com.tub.models.Viagem;
import com.tub.services.ViagemService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.Integer.parseInt;

@RestController
@RequestMapping("/api/v1/viagens")
public class HistoricoViagensController {

    private final ViagemService viagemService;

    @Autowired
    public HistoricoViagensController(ViagemService viagemService) {
        this.viagemService = viagemService;
    }

    @GetMapping("/{id}")
    public String historicoViagens(@PathVariable String id) {
        System.out.println("id: " + id);
        List<Viagem> b = viagemService.getHistorico(parseInt(id));
        System.out.println("VIAGENS: " + b);//JSONObject.quote("{\nViagens:\n" +b.toString() + "\n}")

        JSONArray jsonViagens = new JSONArray();
        b.forEach(jsonViagens::put);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Viagens", jsonViagens);
        System.out.println(jsonObject);

        return jsonObject.toString();
    }

}
