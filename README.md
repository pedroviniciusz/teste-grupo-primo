# Teste Grupo Primo

## Visão Geral
Este é um projeto que lida com concorrência em sistemas bancários

## Pré-requisito
- Java JDK 21 ou posterior
- Maven
- Docker

## Clonando o projeto
```shell
git clone https://github.com/pedroviniciusz/teste-grupo-primo.git
```

## Rodando o projeto

Este comando starta o MySql. Você pode conferir em [docker-compose.yml](https://github.com/pedroviniciusz/teste-grupo-primo/blob/master/docker-compose.yml)

**Inicie o container com o comando**:
   ```shell
   docker-compose up
   ```

**Compile e baixe as dependências do projeto**:
```shell
mvn clean install
```

**Execute o projeto**: <br />
Exitstem 2 profiles, dev e test. <br />
O dev roda o H2, banco de dados em memória. <br />
Já o test acessa o MySql que está sendo executado pelo Docker.
```shell
mvn spring-boot:run -Dspring-boot.run.profiles=dev ou test 
```

Após estes passos, o projeto responderá na url abaixo:
```shell
http://localhost:8080
```

## Divisão
O projeto foi divido em 2 controllers principais, o ContaController lida com operações normais sem concorrência. <br />
Já o ContaConcorrenciaController lida com as 3 situações descritas na tabela verdade no Readme do desafio. <br />
Além disso, o projeto conta com um tratamento de exceções global [GlobalExceptionHandler](https://https://github.com/pedroviniciusz/teste-grupo-primo/blob/master/src/main/java/com/example/teste/grupo/primo/web/advice/GlobalExceptionHandler.java) <br />
Bem como, o resilience4j para definirmos métricas de timeout e indisponibilidade no [application.yml](https://github.com/pedroviniciusz/teste-grupo-primo/blob/master/src/main/resources/application.yml), as quais são capturadas pelo GlobalExceptionHandler. <br />
Em todo Endpoint, é feito um log de informação dizendo que aquele endpoint foi acessado, e quais parâmetros chegaram também. <br />
Para casos mais extremos, foi criado um arquivo para armazenar os logs num geral, definido também  no [application.yml](https://github.com/pedroviniciusz/teste-grupo-primo/blob/master/src/main/resources/application.yml)


## Swagger
Você pode checar a documentação como: controllers, entidades e etc. <br />
```shell
http://localhost:8080/swagger-ui/index.html
```

![Print do swagger](https://github.com/user-attachments/assets/8ae87c7c-1c0b-471d-803c-80fae62f0ac0)