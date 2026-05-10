# CP2 – SOA | Sistema de Pedidos

## Integrantes (ESPY)
- Guilherme Santos Nunes – RM558989
- Kaique Rodrigues Zaffarani – RM556677
- Kairo da Silva Silvestre de Carvalho – RM558288
- Pedro Josué Pereira Almeida – RM554913

---

## Descrição do sistema

API REST de gerenciamento de pedidos com fluxo completo: criação do pedido, validação de estoque, simulação de pagamento e registro de notificações. O pagamento é aprovado automaticamente para pedidos até R$ 150,00; acima disso o pedido é cancelado. O banco de dados é iniciado com 3 clientes e 4 produtos pré-cadastrados via Flyway.

---

## Tecnologias utilizadas

- Java 21
- Spring Boot 4.0.6 (Web MVC, Data JPA, Validation)
- H2 (banco em memória)
- Flyway (migrations)
- Lombok
- SpringDoc OpenAPI 3.0 (Swagger UI)
- Maven

Demais dependências: `pom.xml`.

---

## Como executar o projeto

```bash
./mvnw spring-boot:run
```

- API: `http://localhost:8081`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- H2 Console: `http://localhost:8081/h2-console`
  - JDBC URL: `jdbc:h2:mem:pedidosdb`
  - Usuário: `root` | Senha: `1234`

---

## Lista de endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/pedidos` | Cria um novo pedido e executa o fluxo completo |
| `GET` | `/pedidos/{id}` | Busca pedido por ID |
| `PUT` | `/pedidos/{id}/status` | Atualiza o status de um pedido manualmente |

**Exemplo de criação de pedido:**
```json
POST /pedidos
{
  "clienteId": 1,
  "itens": [
    { "produtoId": 1, 
      "quantidade": 1 
    }
  ]
}
```

**Exemplo de resposta:**
```json
{
  "id": 1,
  "clienteId": 1,
  "clienteNome": "Ana Souza",
  "status": "FINALIZADO",
  "valorTotal": 32.9,
  "dataCriacao": "2026-05-10T01:16:58.677891",
  "itens": [
    {
      "produtoId": 1,
      "nomeProduto": "Hambúrguer Artesanal",
      "quantidade": 1,
      "precoUnitario": 32.9,
      "subtotal": 32.9
    }
  ]
}
```

**Regras de negócio:**
- Pedido com valor > R$ 150,00 → status `CANCELADO` + erro 400
- Cliente ou produto inexistente → erro 404
- Quantidade de itens = 0 → erro 400
- `PUT /status` permite atualizar pedidos cancelados para finalizados; o inverso não é possível

---

## Fluxo do sistema

Ao chamar `POST /pedidos`, o `PedidoService` executa sequencialmente:

1. Valida se há itens e se as quantidades são > 0
2. Verifica se o cliente existe
3. Verifica disponibilidade em estoque para todos os itens
4. Cria o pedido com status `AGUARDANDO_PAGAMENTO` e calcula o valor total
5. Processa o pagamento (simulado): aprovado se ≤ R$ 150,00
6. Se recusado: cancela o pedido e registra notificação `PEDIDO_CANCELADO`
7. Se aprovado: baixa estoque, muda status para `FINALIZADO`, registra notificações `PAGAMENTO_APROVADO` e `PEDIDO_FINALIZADO`

**Ciclo de status:** `CRIADO` → `AGUARDANDO_PAGAMENTO` → `PAGO` → `FINALIZADO` | `CANCELADO`

---

## Explicação da arquitetura

O projeto segue SOA (Service-Oriented Architecture): cada domínio tem responsabilidade isolada e expõe seus comportamentos via serviço. Os domínios são:

| Domínio | Responsabilidade |
|---------|-----------------|
| `Pedido` | Orquestra o fluxo; único ponto de entrada REST |
| `Estoque` | Valida e baixa quantidade disponível nos produtos |
| `Pagamento` | Processa e persiste o resultado do pagamento |
| `Notificacao` | Registra eventos do ciclo de vida do pedido |
| `Cliente` | Fornece dados do cliente vinculado ao pedido |
| `Produto` | Fornece dados e preço dos itens pedidos |

---

## Como as responsabilidades foram separadas

Dentro de cada domínio, o código segue a divisão:

```
<Domínio>/
├── Controller   → recebe e responde requisições HTTP
├── Service      → contém a lógica de negócio
├── Repository   → acesso ao banco via JPA
├── Model        → entidade JPA mapeada
├── DTO/         → contratos de entrada (Request) e saída (Response)
└── Enum/        → estados possíveis
```

Erros são centralizados em `Shared/Exception/GlobalExceptionHandler` (`@RestControllerAdvice`), que converte todas as exceções de domínio em respostas HTTP padronizadas via `ErrorResponseDTO`.

---

## Diagrama arquitetural

```
Cliente HTTP
     │
     ▼
PedidoController
     │
     ▼
PedidoService ──────────────────────────────────────┐
     │                                              │
     ├──► ClienteRepository (valida cliente)        │
     ├──► ProdutoRepository (busca produtos)        │
     ├──► EstoqueService (valida/baixa estoque)     │
     ├──► PagamentoService (processa pagamento)     │
     └──► NotificacaoService (registra eventos)     │
                                                    │
                              H2 Database ◄─────────┘
```

---

## Perguntas Discursivas

### 1. Como a comunicação entre os componentes foi organizada?

O `PedidoService` é o único orquestrador: ele chama `EstoqueService`, `PagamentoService` e `NotificacaoService` via injeção de dependência, em sequência definida pela regra de negócio. Nenhum serviço chama outro de volta (sem dependência circular). Cada serviço conhece apenas seu domínio — `PagamentoService`, por exemplo, só sabe processar um pagamento e persistir o resultado; não conhece nada sobre estoque ou notificação. Isso mantém cada componente testável e substituível de forma independente.

### 2. Se o componente de pagamento ficasse indisponível, qual seria o impacto? Como evoluir?

Na implementação atual, a falha do pagamento lança exceção e cancela o pedido imediatamente — sem retry nem fila. Em um cenário real, isso significaria perda de venda sempre que o serviço externo estivesse instável.

Para reduzir esse impacto, a evolução natural seria:
- Usar **mensageria assíncrona** (ex.: Kafka ou RabbitMQ): o pedido é criado com status `AGUARDANDO_PAGAMENTO` e uma mensagem é publicada; o serviço de pagamento consome quando disponível.
- Implementar **circuit breaker** (ex.: Resilience4j) para evitar que falhas em cascata derrubem toda a aplicação.
- Adicionar **retry com backoff** para tentativas automáticas em caso de falha transitória.
