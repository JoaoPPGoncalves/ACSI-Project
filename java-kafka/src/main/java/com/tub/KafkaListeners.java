package com.tub;

import com.tub.models.Cartao;
import com.tub.models.Conta;
import com.tub.models.Viagem;
import com.tub.repositories.CartaoRepository;
import com.tub.repositories.ContaRepository;
import com.tub.repositories.ViagemRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.lang.Integer.parseInt;

@Component
public class KafkaListeners {
    //private final ValidateQrCode validateQrCode = new ValidateQrCode();

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private ViagemRepository viagemRepository;

    private KafkaTemplate<String, String> kafkaTemplate;

    private final String stringViaVerde = "{lugaresOcupados:[1,1,1,0,0]}";
    private int a = 0;

    public KafkaListeners(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
            topics = "teste",
            groupId = "groupId"
    )
    void listener(String data){
        System.out.println("Listener Received <teste>: " + data + " !!");
    }

    @KafkaListener(
            topics = "qrcode",
            groupId = "groupId"
    )
    void validateQRCode(String data){//este listener ve quando existem novos qrcodes grcodes e vai verificar
        System.out.println("Listener Received <qrcode>: " + data + " !!");
        JSONObject jsonObject = null;//Para converter string em objeto json
        String objeto;//para ter apenas a parte que nos interessa da mensagem
        Conta conta = null;
        Cartao cartao = null;
        Boolean existeConta = true, existeCartao = true;

        try {
            jsonObject = new JSONObject(data);
        }catch (JSONException err){
            System.out.println("ERROR" + err);
        }

        //System.out.println("json:" + jsonObject.get("message").toString());
        objeto = jsonObject.get("message").toString();

        String[] qrCodeInfoSeparated = objeto.split("\\,");//Array com todos os parametros dividiso por ,

        //Pegar no json -> pegar na quantidade e preço e conseguir expor num endpoint GET.

        //System.out.println("id conta " + qrCodeInfoSeparated[0] + ",id cartao " + qrCodeInfoSeparated[1] + ", data: " + qrCodeInfoSeparated[2]);

        int contaId = parseInt(qrCodeInfoSeparated[0]);//conta id
        int cartaoId = parseInt(qrCodeInfoSeparated[1]);//cartao id
        String dataHoraQrCodeString = qrCodeInfoSeparated[2];//data

        JSONObject infoPassar = new JSONObject();

        infoPassar.put("contaId", contaId);
        infoPassar.put("cartaoId", cartaoId);
        infoPassar.put("dataHoraQrCodeString", dataHoraQrCodeString);

        kafkaTemplate.send("verificarInformacoesQrCode", infoPassar.toString());
    }

    @KafkaListener(
            topics = "verificarInformacoesQrCode",
            groupId = "groupId"
    )
    void listenerVerificarQrcode(String data){
        System.out.println("Listener Received <verificarInformacoesQrCode>: " + data + " !!");
        JSONObject jsonObject = null;//Para converter string em objeto json
        Conta conta = null;
        Cartao cartao = null;
        Boolean existeConta = true, existeCartao = true;

        try {//Passar da string para objeto json
            jsonObject = new JSONObject(data);
        }catch (JSONException err){
            System.out.println("ERROR: " + err);
        }

        //System.out.println("data no json: " + jsonObject);

        //pegar nas informações todas
        int contaId = parseInt(jsonObject.get("contaId").toString());
        int cartaoId = parseInt(jsonObject.get("cartaoId").toString());
        String dataHoraQrCodeString = jsonObject.get("dataHoraQrCodeString").toString();

        try{//tentar ir buscar a conta
            conta = contaRepository.findById(contaId).get();
        }catch(Exception err){
            System.out.println("ERROR: " + err);
            existeConta = false;
        }

        if (!existeConta) {//Se não existir, enviar para a repsosta com erro
            kafkaTemplate.send("respostaQrCode", "{'message': 'A conta não existe'}");//Enviar para o topico de resposta
            return;
        }

        try{//tentar ir buscar o cartao
            cartao = cartaoRepository.findById(cartaoId).get();
        }catch(Exception err){
            System.out.println("ERROR: " + err);
            existeCartao = false;
        }

        if (!existeCartao){//Se não existir, enviar para a repsosta com erro
            kafkaTemplate.send("respostaQrCode", "{'message': 'O cartao não existe'}");//Enviar para o topico de resposta
            return;
        }

        //System.out.println("oioi: " + (conta.getCartao().getId() == cartao.getId()) + " asdasdads: " + conta.getCartao().getId() + " tres: " + cartaoId);

        //verificar se o cartao dado pertence à conta
        if(!(conta.getCartao().getId() == cartao.getId() && conta.getCartao().getId() == cartaoId)){//Verificar se os ids são iguais
            kafkaTemplate.send("respostaQrCode", "{'message': 'O cartão dado, não pertence à conta'}");//Enviar para o topico de resposta
            return;
        }

        /*Validar Data e tempo*/
        LocalDateTime dateTime = LocalDateTime.parse(dataHoraQrCodeString);

        LocalDateTime dateTimeAgora= LocalDateTime.now();

        long timeInSecondsQr = dateTime.toEpochSecond(ZoneOffset.UTC);
        //System.out.println("tempo em segundos do qr " + timeInSecondsQr);

        long timeInSecondsAgora = dateTimeAgora.toEpochSecond(ZoneOffset.UTC);
        //System.out.println("tempo em segundos agora " + timeInSecondsAgora);

        long diferenca = timeInSecondsAgora - timeInSecondsQr;
        //System.out.println("diferença em segundos " + diferenca);

        if(diferenca < 60){//O maximo de diferenca é de 60 segundos/1 minuto

            JSONObject infoPassar = new JSONObject();//objeto que vai conter as informaç~eos a passar

            infoPassar.put("contaId", contaId);
            infoPassar.put("dataHoraQrCodeString", dataHoraQrCodeString);

            kafkaTemplate.send("registarViagem", infoPassar.toString());//Enviar para o tipo que registass

        }else{//Se for maior não é válido, não registar na bd
            kafkaTemplate.send("respostaQrCode", "{'message': 'QRcode não válido'}");//Enviar para o topico de resposta
        }
    }

    @KafkaListener(
            topics = "registarViagem",
            groupId = "groupId"
    )
    void listenerRegistarViagem(String data){//Listener para depois enviar mensagem
        System.out.println("Listener Received <registarTentativa>: " + data + " !!");
        JSONObject jsonObject = null;//Para converter string em objeto json
        Conta conta = null;

        System.out.println("data no json: " + jsonObject);

        try {//Passar da string para objeto json
            jsonObject = new JSONObject(data);
        }catch (JSONException err){
            System.out.println("ERROR: " + err);
        }

        //pegar nas informações todas
        int contaId = parseInt(jsonObject.get("contaId").toString());
        String dataHoraQrCodeString = jsonObject.get("dataHoraQrCodeString").toString();
        LocalDateTime dateTime = LocalDateTime.parse(dataHoraQrCodeString);


        try{//tentar ir buscar a conta
            conta = contaRepository.findById(contaId).get();
        }catch(Exception err){
            System.out.println("ERROR: " + err);
        }

        conta.setPontos(conta.getPontos() + 15);//atualizar a conta

        try{//par atualizar conta
            contaRepository.save(conta);
        }catch(Exception err){
            System.out.println("ERROR: " + err);
        }

        try{//para adicioanr viagem
            viagemRepository.save(new Viagem(dateTime, 15, conta));//adicionar a viagem
        }catch(Exception err){
            System.out.println("ERROR: " + err);
        }

        kafkaTemplate.send("respostaQrCode", "{'message': 'Qrcode válido, viagem registada'}");//Enviar para o topico de resposta
    }

    @KafkaListener(
            topics = "respostaQrCode",
            groupId = "groupId"
    )
    void listenerRespostaQrCode(String data){//Listener para depois enviar mensagem
        System.out.println("Listener Received <respostaQrCode>: " + data + " !!");

    }

    @KafkaListener(
            topics = "lugaresLivres",
            groupId = "groupId"
    )
    void listenerLugaresLivres(String data){//Listener para depois enviar mensagem
        System.out.println("Listener Received <lugaresLivres>: " + data + " !!");

        JSONObject viaVerde = new JSONObject(stringViaVerde);

        System.out.println(viaVerde.toString());
        int[] arrayViaVerde = {1,1,1,0,0};
        /*Object[] objectArray = (Object []) a;
        int[] arrayViaVerde = (int[])objectArray[0];*/
        int numeroLugaresOcupados = 0;

        for (int i = 0; i < arrayViaVerde.length ; i++) {
            if (arrayViaVerde[i] == 1) {
                numeroLugaresOcupados++;
            }
        }

        int lugaresDisponiveisViaVerde = arrayViaVerde.length - numeroLugaresOcupados;

        if(parseInt(data) < lugaresDisponiveisViaVerde){//Mandar para o tópico que de enviar email
            System.out.println("Enviar Email");
            kafkaTemplate.send("enviarEmail", "{'message': 'Lugares ocupados detetados nas camaras maior que os recebidos pela via verde'}");
        }else{//Proceder para a analise de informações
            kafkaTemplate.send("ocupacaoParque", "{'camara': {'lugaresLivres':'" + data + "'}, 'viaVerde'{'lugaresLivres':'" + lugaresDisponiveisViaVerde + "'}");
        }
    }

    @KafkaListener(
            topics = "enviarEmail",
            groupId = "groupId"
    )
    void listenerEnviarEmail(String data){//Listener para depois enviar mensagem
        System.out.println("Listener Received <enviarEmail>: " + data + " !!");
    }

    @KafkaListener(
            topics = "ocupacaoParque",
            groupId = "groupId"
    )
    void listenerOcupacaoParque(String data){//Listener para depois enviar mensagem
        System.out.println("Listener Received <ocupacaoParque>: " + data + " !!");



    }
}
