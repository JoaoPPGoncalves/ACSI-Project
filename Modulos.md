# Validar QRCODE

QRCODE -> NIOP(descodificar qrcode, enviar por API) -> API regista num topico(topico="qrcode") -> Retirar da mensagem a conta de utilizador, cartão e tempo -> validar conta, cartao, tempo -> Se for valida  -> registar na BD, dar pontos -> meter no topico resposta
                                    -> se for invalida -> meter no topico resposta

# Gerar Qrcode

///

# Ver historico de viagens
Vem request -> API -> verificamos conta -> obtemos as viagens -> return viagens

# Verificar lugares diponiveis

Camara envia imagem -> openCV processa imagem e ve lugares disponiveis -> dos lugaras e com a mensagem da via verde verficiar se esta tudo certo -> mostrar para o utilizador

usar PIM e plataform specific(METEMOS A TECH).