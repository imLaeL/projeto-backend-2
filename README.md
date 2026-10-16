# projeto-backend-2 — API REST de Eventos

Exercício da disciplina **Programação Web Back-end II** (Pós-graduação Full Stack, 2026.2): API REST para cadastrar, consultar, atualizar e excluir eventos, construída sobre o projeto `appinscricao` desenvolvido em sala (módulo de pessoas).

**Participantes:** Antonio Dias e Isaque Lael

## Stack

- Java 25, Spring Boot 4.1 (Web MVC, Data JPA, Validation)
- MySQL 8.4 + Flyway (migrations em `appinscricao/src/main/resources/db/migration`)
- Lombok
- springdoc-openapi (Swagger UI)

## Arquitetura

```
Cliente HTTP → EventoController → EventoService (interface) → EventoServiceImpl → EventoRepository → MySQL
                                                                     ↘ EventoMapper (DTO ↔ entidade)
```

- A entidade `Evento` nunca é exposta: a API recebe `EventoRequest` e devolve `EventoResponse`.
- Erros de negócio são lançados como exceções que estendem `ApiException` e tratados com `try/catch` no próprio controller (sem `@RestControllerAdvice`), conforme o enunciado.

| Exceção | Status | Quando |
|---|---|---|
| `ResourceNotFoundException` | 404 | id inexistente |
| `ConflictException` | 409 | evento duplicado |
| `BusinessException` | 400 | exclusão de evento já realizado |

## Como executar

Pré-requisitos: Java 25 e Docker.

```bash
# 1. Subir o MySQL (porta 3306, banco inscricoes-db, usuário root/root)
docker compose up -d --wait

# 2. Subir a aplicação (Flyway cria as tabelas na primeira execução)
cd appinscricao
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **Roteiro de testes:** [`api.http`](api.http) — rode em ordem no HTTP Client do IntelliJ; cobre os critérios de aceitação CT01–CT12.

Para parar: `docker compose down` (ou `docker compose down -v` para apagar os dados).

## Entidade Evento (`tb_eventos`)

| Atributo | Tipo | Regras |
|---|---|---|
| `id` | UUID | gerado automaticamente |
| `nome` | String | obrigatório, até 150 caracteres |
| `dataHoraEvento` | LocalDateTime | obrigatório, no futuro |
| `local` | String | obrigatório, até 200 caracteres |
| `cidade` | String | obrigatório, até 100 caracteres |
| `estado` | String | obrigatório, sigla da UF com 2 caracteres, gravado em maiúsculas |
| `qtde` | Integer | obrigatório, no mínimo 1 |

`EventoResponse` inclui também o campo calculado `diasParaEvento`: dias entre hoje e a data do evento (0 no dia, negativo se já passou).

## Endpoints

Base: `/api/v1/eventos`

| Método | Caminho | Sucesso | Erros |
|---|---|---|---|
| POST | `/api/v1/eventos` | 201 Created | 400, 409 |
| GET | `/api/v1/eventos/{id}` | 200 OK | 404 |
| GET | `/api/v1/eventos?nome=&cidade=&estado=&page=&size=&sort=` | 200 OK | — |
| PUT | `/api/v1/eventos/{id}` | 200 OK | 400, 404, 409 |
| DELETE | `/api/v1/eventos/{id}` | 204 No Content | 400, 404 |

**Listagem:** 10 itens por página, ordenada por `dataHoraEvento` ascendente por padrão. Filtros sem diferenciar maiúsculas/minúsculas: `nome` (trecho), `cidade` (trecho) e `estado`. Se mais de um for enviado, vale a prioridade **nome → cidade → estado**.

### Exemplo

```http
POST /api/v1/eventos
Content-Type: application/json

{
  "nome": "Workshop Spring Boot",
  "dataHoraEvento": "2026-12-10T19:00:00",
  "local": "Auditório Central",
  "cidade": "João Pessoa",
  "estado": "pb",
  "qtde": 50
}
```

```json
{
  "id": "3f1c2a8e-...",
  "nome": "Workshop Spring Boot",
  "dataHoraEvento": "2026-12-10T19:00:00",
  "local": "Auditório Central",
  "cidade": "João Pessoa",
  "estado": "PB",
  "qtde": 50,
  "diasParaEvento": 55
}
```

## Regras de negócio

1. Todos os campos são obrigatórios na criação e na atualização.
2. A data/hora do evento deve estar no futuro.
3. O estado é gravado em maiúsculas.
4. Operações com id inexistente retornam 404.
5. **Evento duplicado:** não pode haver dois eventos com o mesmo nome, data/hora e cidade (sem diferenciar maiúsculas/minúsculas). Vale no cadastro e na atualização — na atualização o próprio evento é desconsiderado. Retorna 409.
6. **Evento já realizado:** não é possível excluir evento cuja data/hora já passou. Retorna 400 com a mensagem `"Não é possível excluir um evento já realizado"`.

## Formato dos erros

Todos os erros tratados no controller seguem o mesmo formato:

```json
{
  "timestamp": "2026-10-16T15:21:31.508481",
  "status": 404,
  "message": "Recurso não encontrado: 00000000-0000-0000-0000-000000000000"
}
```

Erros de validação do `@Valid` (campos obrigatórios, tamanhos, data no futuro) usam a resposta 400 padrão do Spring.

## Critérios de aceitação

Todos verificados pelo roteiro [`api.http`](api.http).

| # | Cenário | Resultado |
|---|---|---|
| CT01 | POST com todos os campos válidos | 201 e evento com id gerado |
| CT02 | POST com estado `"pb"` | gravado como `"PB"` |
| CT03 | POST sem nome ou com `qtde = 0` | 400 |
| CT04 | POST com data no passado | 400 |
| CT05 | GET com id inexistente | 404 |
| CT06 | GET listagem sem parâmetros | 200; 10 por página, ordem por data |
| CT07 | GET `?nome=spring` com evento "Workshop Spring Boot" | evento retornado |
| CT08 | DELETE de evento existente | 204; GET seguinte retorna 404 |
| CT09 | POST com mesmo nome, data/hora e cidade | 409 |
| CT10 | PUT sem alterar nome, data/hora e cidade | 200 |
| CT11 | DELETE de evento já passado (inserido direto no banco) | 400; evento não é excluído |
| CT12 | GET de evento daqui a 10 dias | 200; `diasParaEvento = 10` |

> O CT11 exige inserir o evento passado direto no banco, já que a API rejeita datas no passado. O comando está comentado no `api.http`.
