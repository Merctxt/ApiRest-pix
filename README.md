# API REST PIX

API REST para geração de pagamentos via PIX no padrão EMV, desenvolvida com Java, Spring Boot, Hibernate Envers e Lombok.

## Tecnologias

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Data JPA**
- **Hibernate Envers** - Auditoria de entidades
- **Lombok** - Redução de boilerplate
- **Springdoc OpenAPI** - Documentação Swagger
- **ZXing** - Geração de QR Codes
- **PostgreSQL** - Banco de dados (Supabase)

## Pré-requisitos

- Java 17+
- Maven 3.6+

## Configuração

Crie um arquivo `.env` na raiz do projeto:

```env
# Environment (dev ou prod)
ENV=dev

# Server Configuration
SERVER_ADDRESS=0.0.0.0
SERVER_PORT=8080

# Database Configuration (PostgreSQL Supabase)
DB_HOST=seu-host.supabase.co
DB_PORT=5432
DB_NAME=postgres
DB_USER=postgres
DB_PASSWORD=sua_senha

# PIX Configuration
PIX_KEY=sua_chave_pix
PIX_RECEIVER_NAME=Nome do Recebedor
PIX_RECEIVER_CITY=CIDADE
```

### Variável ENV

- **`ENV=dev`**: Swagger aponta para `http://localhost:8080`
- **`ENV=prod`**: Swagger aponta para `https://pix.giovannidev.com`

Para produção, basta mudar:
```env
ENV=prod
SERVER_PORT=80
```

## Executar a Aplicação

```bash
# Compile e execute
./mvnw spring-boot:run
```

A aplicação vai usar as variáveis do `.env` automaticamente.

## Acessar a Aplicação

- **API**: http://localhost:8080 (ou porta configurada no .env)
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs


## Configuração

As configurações podem ser alteradas em `application.properties` ou via variáveis de ambiente:

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `PIX_KEY` | Chave PIX padrão | `sua_chave_pix_aqui` |
| `PIX_RECEIVER_NAME` | Nome do recebedor padrão | `Venus Store` |
| `PIX_RECEIVER_CITY` | Cidade do recebedor padrão | `SAO PAULO` |

## Padrão EMV PIX

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

## 🚀 Deploy em Produção

### Requisitos do Servidor

Para hospedar a aplicação, certifique-se de que:
- O servidor aceita tráfego na porta 80
- O servidor está configurado para escutar em 0.0.0.0
- O firewall permite conexões na porta 80
- O domínio `pix.giovannidev.com` está apontando para o IP do servidor

### Passos para Deploy

1. **Clone o repositório no servidor**
   ```bash
   git clone <seu-repositorio>
   cd api-rest-pix
   ```

2. **Configure as variáveis de ambiente**
   ```bash
   cp .env.example .env
   # Edite o .env com suas credenciais
   nano .env
   ```

3. **Execute o deploy**
   ```bash
   # Linux/Mac
   chmod +x deploy.sh
   ./deploy.sh
   
   # Windows PowerShell
   .\deploy.ps1
   ```

4. **Verifique se está rodando**
   ```bash
   docker ps
   docker logs api-rest-pix
   ```

### Configuração de Reverse Proxy (Nginx - Opcional)

Se você usar Nginx como reverse proxy:

```nginx
server {
    listen 80;
    server_name pix.giovannidev.com;

    location / {
        proxy_pass http://localhost:80;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### Configuração SSL com Let's Encrypt (Recomendado)

```bash
# Instalar certbot
sudo apt install certbot python3-certbot-nginx

# Obter certificado SSL
sudo certbot --nginx -d pix.giovannidev.com

# Auto-renovação
sudo certbot renew --dry-run
```

## 🔍 Troubleshooting

### Erro de conexão com banco de dados
- Verifique se as credenciais no `.env` estão corretas
- Confirme se o Supabase permite conexões do IP do servidor
- Teste a conexão: `psql -h DB_HOST -U DB_USER -d DB_NAME`

### Porta 80 já em uso
```bash
# Ver o que está usando a porta 80
sudo lsof -i :80
# Ou no Windows
netstat -ano | findstr :80

# Parar o serviço conflitante ou mudar a porta no .env
```

### Container não inicia
```bash
# Ver logs detalhados
docker logs api-rest-pix -f

# Reiniciar container
docker restart api-rest-pix
```

### Swagger não carrega
- Verifique se a variável `API_URL` no `.env` está correta
- Acesse diretamente: `https://pix.giovannidev.com/api-docs`
- Limpe o cache do navegador

### Erro de permissão na porta 80 (Linux)
```bash
# Permitir que aplicações non-root usem porta 80
sudo setcap 'cap_net_bind_service=+ep' /usr/bin/java

# Ou execute o container com privilégios
docker run -d -p 80:80 --user root ...
```

## 📝 Licença

Este projeto está sob a licença Apache 2.0.


