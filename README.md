# API REST PIX

API REST para geração de pagamentos via PIX no padrão EMV, desenvolvida com Java, Spring Boot, Hibernate Envers e Lombok.

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Data JPA**
- **Hibernate Envers** - Auditoria de entidades
- **Lombok** - Redução de boilerplate
- **Springdoc OpenAPI** - Documentação Swagger
- **ZXing** - Geração de QR Codes
- **H2 Database** - Banco de dados em memória (desenvolvimento)

## 📋 Funcionalidades

### Pagamentos PIX
- ✅ Criar pagamento PIX com geração de payload EMV
- ✅ Gerar QR Code a partir do payload
- ✅ Listar pagamentos (todos ou por status)
- ✅ Buscar pagamento por ID ou TXID
- ✅ Atualizar pagamento
- ✅ Aprovar pagamento (simular confirmação)
- ✅ Cancelar pagamento
- ✅ Excluir pagamento

### Configuração de Recebedores
- ✅ Cadastrar recebedores PIX
- ✅ Definir recebedor padrão
- ✅ Listar recebedores
- ✅ Atualizar recebedor
- ✅ Excluir recebedor

## 🏃 Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.6+

### Executar a aplicação

```bash
# Clone o repositório
cd api_rest_pix

# Compile e execute
./mvnw spring-boot:run
```

### Acessar a aplicação

- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:pixdb`)

## 📖 Endpoints

### Pagamentos (`/api/payments`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/payments` | Criar pagamento PIX |
| GET | `/api/payments` | Listar todos os pagamentos |
| GET | `/api/payments/{id}` | Buscar pagamento por ID |
| GET | `/api/payments/txid/{txid}` | Buscar pagamento por TXID |
| GET | `/api/payments/status/{status}` | Listar por status |
| PUT | `/api/payments/{id}` | Atualizar pagamento |
| PATCH | `/api/payments/{id}/approve` | Aprovar pagamento |
| PATCH | `/api/payments/{id}/cancel` | Cancelar pagamento |
| DELETE | `/api/payments/{id}` | Excluir pagamento |
| GET | `/api/payments/{id}/payload` | Obter payload PIX |
| GET | `/api/payments/{id}/qrcode` | Gerar QR Code PNG |

### Recebedores (`/api/receivers`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/receivers` | Criar recebedor |
| GET | `/api/receivers` | Listar recebedores |
| GET | `/api/receivers/{id}` | Buscar recebedor por ID |
| GET | `/api/receivers/default` | Buscar recebedor padrão |
| PUT | `/api/receivers/{id}` | Atualizar recebedor |
| PATCH | `/api/receivers/{id}/set-default` | Definir como padrão |
| DELETE | `/api/receivers/{id}` | Excluir recebedor |


## 🔧 Configuração

As configurações podem ser alteradas em `application.properties` ou via variáveis de ambiente:

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `PIX_KEY` | Chave PIX padrão | `sua_chave_pix_aqui` |
| `PIX_RECEIVER_NAME` | Nome do recebedor padrão | `Venus Store` |
| `PIX_RECEIVER_CITY` | Cidade do recebedor padrão | `SAO PAULO` |

## 📊 Padrão EMV PIX

O payload PIX segue o padrão EMV QR Code conforme especificação do Banco Central do Brasil:

| ID | Campo | Descrição |
|----|-------|-----------|
| 00 | Payload Format Indicator | Sempre "01" |
| 26 | Merchant Account Information | Contém a chave PIX |
| 52 | Merchant Category Code | Código MCC |
| 53 | Transaction Currency | "986" (BRL) |
| 54 | Transaction Amount | Valor do pagamento |
| 58 | Country Code | "BR" |
| 59 | Merchant Name | Nome do recebedor (máx. 25 chars) |
| 60 | Merchant City | Cidade do recebedor (máx. 15 chars) |
| 62 | Additional Data Field | Contém o TXID |
| 63 | CRC16 | Checksum CRC16-CCITT-FALSE |


