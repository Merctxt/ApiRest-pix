# API REST PIX

API REST para geração de pagamentos PIX no padrão EMV (QR Code), desenvolvida com Java 17 e Spring Boot 3.

**Acesse a API em produção:**
 **[https://pix.giovannidev.com/swagger-ui/index.html](https://pix.giovannidev.com/swagger-ui/index.html)**


## Tecnologias

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Data JPA**
- **Hibernate Envers** - Auditoria de entidades
- **PostgreSQL** - Banco de dados
- **Lombok** - Redução de boilerplate
- **Springdoc OpenAPI** - Documentação Swagger
- **ZXing** - Geração de QR Codes
- **Docker** - Containerização

## Estrutura do Projeto

```
ApiRest-pix/
├── src/main/java/com/dio/pix/
│   ├── config/          # Configurações (CORS, OpenAPI, etc)
│   ├── controller/      # Controllers REST
│   ├── dto/             # Data Transfer Objects
│   ├── entity/          # Entidades JPA
│   ├── exception/       # Tratamento de exceções
│   ├── mapper/          # Mapeamento DTO <-> Entity
│   ├── repository/      # Repositórios JPA
│   └── service/         # Lógica de negócio
├── Dockerfile
├── pom.xml
└── README.md
```


## Executar com Docker

### Build
```bash
docker build -t api-pix .
```

### Run
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:port/db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=senha \
  api-pix
```

## Executar Localmente

### Pré-requisitos
- Java 17+
- PostgreSQL
- Maven 3.6+

### Configuração

Crie um arquivo `.env` na raiz:
```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/pix_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
```

### Executar
```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

## Padrão EMV PIX

A API gera payloads PIX seguindo o padrão EMV QR Code do Banco Central:

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

