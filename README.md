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
O dev roda o H2, banco de dados em memória e já popula o DB com alguns dados (Mais abaixo explico melhor). <br />
Já o test acessa o MySql que está sendo executado pelo Docker e é necessário inserir a massa de testes.
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

Além disso, o projeto conta com um tratamento de exceções global [GlobalExceptionHandler](https://github.com/pedroviniciusz/teste-grupo-primo/blob/master/src/main/java/com/example/teste/grupo/primo/web/advice/GlobalExceptionHandler.java) <br />

Bem como, o resilience4j para definirmos métricas de timeout e indisponibilidade no [application.yml](https://github.com/pedroviniciusz/teste-grupo-primo/blob/master/src/main/resources/application.yml), as quais são capturadas pelo GlobalExceptionHandler. <br />

Em todo Endpoint, é feito um log de informação dizendo que aquele endpoint foi acessado, e quais parâmetros chegaram também. <br />

Para casos mais extremos, foi criado um arquivo para armazenar os logs num geral, definido também  no [application.yml](https://github.com/pedroviniciusz/teste-grupo-primo/blob/master/src/main/resources/application.yml)


## Swagger
Você pode checar a documentação como: controllers, entidades e etc. <br />
```shell
http://localhost:8080/swagger-ui/index.html
```

![Print do swagger](https://github.com/user-attachments/assets/8ae87c7c-1c0b-471d-803c-80fae62f0ac0)

## Casos de Teste
### Concorrência 1

Após subir a aplicação com profile dev, o [data.sql](https://github.com/pedroviniciusz/teste-grupo-primo/blob/master/src/main/resources/data.sql) vai automaticamente popular o banco com 2 contas <br />
A de id 1, terá um saldo inicial de 100, enquanto a segunda e a terceira terão um saldo zerado. <br />
Se chamarmos a URL abaixo para testarmos a concorrência 1 do teste
```shell
http://localhost:8080/api/conta-concorrencia/deposito-saque
```
e passarmos este JSON
```json
{
  "depositoDto": {
    "idConta": 1,
    "valor": 50
  },
  "saqueDto": {
    "idConta": 1,
    "valor": 30
  }
}
```
Teremos o resultado de 2 transações, resultando em 120 no saldo da conta <br />
100 + 50 - 30 = 120 <br />
Este resultado só é possível graças a lógica de Lock no DB para evitarmos dados inconsistentes

### Concorrência 2
Se chamarmos a URL abaixo para testarmos a concorrência 2 do teste
```shell
http://localhost:8080/api/conta-concorrencia/deposito-transferencia
```
e passarmos este JSON
```json
{
  "depositoDto": {
    "idConta": 1,
    "valor": 100
  },
  "transferenciaDto": {
    "idContaOrigem": 1,
    "idContaDestino": 2,
    "valor": 50
  }
}
```
Teremos o resultado de 2 transações, resultando em 120 no saldo da conta de ID 1 e 50 na conta de ID 2 <br />
Vale ressaltar que o valor de 120 inicial é resultado do primeiro teste de concorrência <br />
Conta ID 1 -> 120 + 100 - 50 = 170 <br />
Conta ID 2 -> 0 + 50 = 50 <br />

### Concorrência 3
Se chamarmos a URL abaixo para testarmos a concorrência 2 do teste
```shell
http://localhost:8080/api/conta-concorrencia/transferencia-transferencia
```
e passarmos este JSON
```json
{
  "transferenciaDto1": {
    "idContaOrigem": 1,
    "idContaDestino": 2,
    "valor": 20
  },
  "transferenciaDto2": {
    "idContaOrigem": 2,
    "idContaDestino": 3,
    "valor": 10
  }
}
```
Teremos o resultado de 3 transações, resultando em 50 no saldo da conta de ID 1 e 10 na conta de ID 2 e 10 na conta de ID 3 <br />
Vale ressaltar que o valor de 170 inicial para conta de ID 1 e 50 incial para conta de ID 2 é resultado do primeiro e do segundo teste de concorrência <br />
Conta ID 1 -> 170 - 20 = 150
Conta ID 2 -> 50 + 20 = 70 <br />
Conta ID 2 -> 70 + - 10 = 60 <br />
Conta ID 3 -> 0 + 10 = 10 <br />