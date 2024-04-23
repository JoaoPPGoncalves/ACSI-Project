# Iniciar
Abrir uma janeça com, iniciar zookeeper:
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

Abrir outra janela com, iniciar o kafka:
.\bin\windows\kafka-server-start.bat .\config\server.properties

# Criar um topico:
.\bin\windows\kafka-topics.bat --create --topic fisrtTopic --bootstrap-server localhost:9092

# Producer: escrever no tópico
.\bin\windows\kafka-console-producer.bat --topic fisrtTopic --bootstrap-server localhost:9092

# Consumer: iniciar consola do consumer
.\bin\windows\kafka-console-consumer.bat --topic fisrtTopic --from-beginning --bootstrap-server localhost:9092

##### Video que usei para instalar:
https://www.youtube.com/watch?v=heXd6JA2TQc