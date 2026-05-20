# Desafio Itaú - Backend

API REST desenvolvida em Java com Spring Boot para receber transações financeiras e retornar estatísticas sobre elas.

## Baseado no desafio

[Desafio original - feltex/desafio-itau-backend](https://github.com/feltex/desafio-itau-backend)
## Tecnologias

- Java 21
- Spring Boot 4.0.6
- Lombok
- Spring Boot Actuator
- Springdoc OpenAPI (Swagger UI)
- Docker

## Estrutura do Projeto

```
src/main/java/space/gmarqueszx/desafio_itau/
├── controller/
│   ├── TransactionController.java
│   └── StatisticController.java
├── exception/
│   └── InvalidTransactionException.java
├── handler/
│   ├── ApiExceptionHandler.java
│   ├── Problem.java
│   └── ProblemType.java
├── model/
│   ├── TransactionRequest.java
│   └── StatisticResponse.java
├── service/
│   ├── TransactionService.java
│   └── StatisticService.java
└── DesafioItauApplication.java
```

## Endpoints

### POST /transaction
Recebe e valida uma transação.

**Request body:**
```json
{
  "value": 123.45,
  "timestamp": "2026-05-20T00:00:00.000-03:00"
}
```

**Respostas:**
- `201 Created` — transação válida e registrada
- `422 Unprocessable Entity` — transação inválida (valor negativo, data futura, campos nulos)
- `400 Bad Request` — JSON inválido ou malformado

---

### DELETE /transaction
Apaga todas as transações armazenadas em memória.

**Respostas:**
- `200 OK` — dados apagados com sucesso

---

### GET /statistic
Retorna estatísticas das transações dos últimos 60 segundos.

**Response body:**
```json
{
  "count": 3,
  "sum": 425.75,
  "avg": 141.92,
  "min": 75.25,
  "max": 250.50
}
```

**Respostas:**
- `200 OK` — estatísticas calculadas (todos os campos retornam `0` se não houver transações no período)

---

### GET /actuator/health
Verifica a saúde da aplicação.

```json
{
  "status": "UP"
}
```

## Regras de Negócio

- Todos os dados são armazenados **em memória** — sem banco de dados
- Transações com valor negativo são rejeitadas
- Transações com data futura são rejeitadas
- Campos `value` e `timestamp` são obrigatórios
- As estatísticas consideram apenas transações dos **últimos 60 segundos**

## Como executar

### Localmente

```bash
./mvnw clean install
./mvnw spring-boot:run
```

### Com Docker

```bash
docker build -t desafio-itau .
docker run -p 8080:8080 desafio-itau
```

## Documentação da API

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui.html
```