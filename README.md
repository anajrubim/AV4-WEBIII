# AutoManager — AV4 · Guia de Utilização

Sistema de gerenciamento de oficina Toyota com autenticação e autorização via **JWT (JSON Web Token)**, desenvolvido em **Java 17 + Spring Boot 2.6.3**.

---

## Sumário

1. [Pré-requisitos](#1-pré-requisitos)
2. [Abrindo o projeto no VS Code](#2-abrindo-o-projeto-no-vs-code)
3. [Executando a aplicação](#3-executando-a-aplicação)
4. [Autenticação — obtendo o token JWT](#4-autenticação--obtendo-o-token-jwt)
5. [Usando o token nas requisições](#5-usando-o-token-nas-requisições)
6. [Perfis de usuário e permissões](#6-perfis-de-usuário-e-permissões)
7. [Usuários pré-cadastrados](#7-usuários-pré-cadastrados)
8. [Endpoints disponíveis](#8-endpoints-disponíveis)
9. [Exemplos de requisições](#9-exemplos-de-requisições)
10. [Banco de dados H2 (console)](#10-banco-de-dados-h2-console)

---

## 1. Pré-requisitos

Certifique-se de ter instalados:

| Ferramenta | Versão mínima | Download |
|---|---|---|
| Java JDK | 17 | https://adoptium.net |
| Maven | 3.6+ | https://maven.apache.org/download.cgi |
| VS Code | qualquer | https://code.visualstudio.com |
| Postman | qualquer | https://www.postman.com/downloads |

### Extensões obrigatórias do VS Code

Instale o pacote oficial de Java. Abra o VS Code, pressione `Ctrl+P` e execute:

```
ext install vscjava.vscode-java-pack
```

Esse pacote instala automaticamente:
- **Language Support for Java** (redhat)
- **Debugger for Java** (Microsoft)
- **Maven for Java** (Microsoft)
- **Project Manager for Java** (Microsoft)
- **Test Runner for Java** (Microsoft)

### Verificando as instalações

Abra um terminal e confirme que tudo está funcionando:

```bash
java -version
# Esperado: openjdk version "17.x.x" ...

javac -version
# Esperado: javac 17.x.x

mvn -version
# Esperado: Apache Maven 3.x.x ...
```

> **Windows:** certifique-se de que `JAVA_HOME` está configurado nas variáveis de ambiente apontando para a pasta do JDK 17.

---

## 2. Abrindo o projeto no VS Code

### Passo 1 — Extrair o zip

Extraia o arquivo `AV4-WEBIII-main.zip` em uma pasta de sua escolha. A estrutura ficará assim:

```
AV4-WEBIII-main/
└── automanager/
    ├── pom.xml
    ├── src/
    └── ...
```

### Passo 2 — Abrir no VS Code

**Opção A — pelo terminal:**

```bash
cd AV4-WEBIII-main/automanager
code .
```

**Opção B — pela interface:**

1. Abra o VS Code
2. Vá em **File → Open Folder**
3. Selecione a pasta `AV4-WEBIII-main/automanager`
4. Clique em **Open**

### Passo 3 — Aguardar o carregamento

Na primeira abertura, o VS Code vai detectar o `pom.xml` automaticamente e baixar todas as dependências Maven. Acompanhe o progresso na barra inferior. Quando aparecer **"Build Success"** na aba **Java Projects**, o projeto está pronto.

> Se aparecer um aviso pedindo para "import Java project", clique em **Yes**.

---

## 3. Executando a aplicação

### Via terminal (recomendado)

Abra o terminal integrado do VS Code com `` Ctrl+` `` ou vá em **Terminal → New Terminal**, então:

```bash
# Certifique-se de estar dentro da pasta automanager
cd AV4-WEBIII-main/automanager

# Compilar e executar
mvn spring-boot:run
```

Aguarde a mensagem de sucesso no console:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.6.3)

...
Started AutomanagerApplication in X.XXX seconds (JVM running for X.XXX)
```

A aplicação estará disponível em: **`http://localhost:8080`**

Ao subir, os dados de exemplo são carregados automaticamente: empresa, 4 usuários (admin, gerente, vendedor, cliente), mercadorias, serviços e uma venda.

### Outros comandos úteis no terminal

```bash
# Apenas compilar (sem executar)
mvn compile

# Compilar e gerar o .jar
mvn package

# Executar o .jar gerado
java -jar target/automanager-0.0.1-SNAPSHOT.jar

# Limpar arquivos de build anteriores
mvn clean

# Limpar e recompilar do zero
mvn clean install
```

### Via VS Code (interface gráfica)

1. Na barra lateral, clique no ícone de **Explorer** ou pressione `Ctrl+Shift+E`
2. Expanda **JAVA PROJECTS** no painel inferior
3. Localize `automanager` e clique no ícone ▶️ (Run)

Ou, alternativamente:

1. Abra o arquivo `AutomanagerApplication.java`
2. Clique em **Run** que aparece acima do método `main`

### Parando a aplicação

No terminal, pressione `Ctrl+C`.

---

## 4. Autenticação — obtendo o token JWT

Toda requisição (exceto o login) exige um token JWT válido. Para obtê-lo:

**Endpoint:** `POST http://localhost:8080/login`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "nomeUsuario": "admin",
  "senha": "admin123"
}
```

**Resposta de sucesso (HTTP 200):**

O token é retornado no **header** da resposta, no campo `Authorization`:

```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcxNjMyMjAwMH0.XXXXX
```

> Copie o valor completo — ele será necessário em todas as próximas requisições.

**Resposta de erro (HTTP 401):** credenciais inválidas ou usuário inexistente.

> **Atenção:** o token expira em **10 minutos** (`jwt.expiration=600000` ms). Após isso, faça login novamente para obter um novo token.

---

## 5. Usando o token nas requisições

Em **toda requisição protegida**, adicione o header:

```
Authorization: Bearer <seu_token_aqui>
```

### No Postman

1. Faça a requisição de login (`POST /login`) e copie o valor do header `Authorization` da resposta
2. Em qualquer nova requisição, vá na aba **Authorization**
3. Selecione o tipo **Bearer Token**
4. Cole o token no campo **Token**

### Via cURL (terminal)

```bash
# Primeiro, faça login e salve o token
TOKEN=$(curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"nomeUsuario":"admin","senha":"admin123"}' \
  -i | grep -i "Authorization:" | awk '{print $3}' | tr -d '\r')

# Use o token nas próximas requisições
curl -X GET http://localhost:8080/usuario \
  -H "Authorization: Bearer $TOKEN"
```

---

## 6. Perfis de usuário e permissões

O sistema possui quatro perfis com as seguintes autorizações:

| Perfil | CRUD Usuários | CRUD Mercadorias | CRUD Serviços | CRUD Vendas | Leitura Própria |
|---|:---:|:---:|:---:|:---:|:---:|
| **ROLE_ADMIN** | ✅ Todos | ✅ Todos | ✅ Todos | ✅ Todos | — |
| **ROLE_GERENTE** | ✅ Gerente/Vendedor/Cliente | ✅ Todos | ✅ Todos | ✅ Todos | — |
| **ROLE_VENDEDOR** | ✅ Apenas Cliente | 👁️ Somente leitura | 👁️ Somente leitura | ✅ Criar/Ler | — |
| **ROLE_CLIENTE** | — | — | — | 👁️ Suas vendas | 👁️ Seu cadastro |

**Regras detalhadas por recurso:**

```
/usuario    → GET, DELETE: ADMIN, GERENTE
              POST, PUT:   ADMIN, GERENTE, VENDEDOR

/cliente    → GET:                    ADMIN, GERENTE, VENDEDOR, CLIENTE
              POST, PUT, DELETE:      ADMIN, GERENTE, VENDEDOR

/mercadoria → GET:                    ADMIN, GERENTE, VENDEDOR
              POST, PUT, DELETE:      ADMIN, GERENTE

/servico    → GET:                    ADMIN, GERENTE, VENDEDOR
              POST, PUT, DELETE:      ADMIN, GERENTE

/venda      → GET:                    ADMIN, GERENTE, VENDEDOR, CLIENTE
              POST:                   ADMIN, GERENTE, VENDEDOR
              PUT, DELETE:            ADMIN, GERENTE

/empresa    → GET:                    ADMIN, GERENTE
              POST, PUT, DELETE:      ADMIN

/veiculo    → GET, POST, PUT:         ADMIN, GERENTE, VENDEDOR
              DELETE:                 ADMIN, GERENTE
```

---

## 7. Usuários pré-cadastrados

Ao iniciar a aplicação, os seguintes usuários são criados automaticamente:

| Nome de usuário | Senha | Perfil |
|---|---|---|
| `admin` | `admin123` | ROLE_ADMIN |
| `gerente` | `gerente123` | ROLE_GERENTE |
| `vendedor` | `vendedor123` | ROLE_VENDEDOR |
| `cliente` | `cliente123` | ROLE_CLIENTE |

---

## 8. Endpoints disponíveis

URL base: `http://localhost:8080`

### Autenticação
| Método | URL | Descrição | Auth |
|---|---|---|:---:|
| POST | `/login` | Autenticar e obter token JWT | ❌ Pública |

### Usuários
| Método | URL | Descrição |
|---|---|---|
| GET | `/usuario` | Listar todos os usuários |
| GET | `/usuario/{id}` | Buscar usuário por ID |
| POST | `/usuario` | Cadastrar novo usuário |
| PUT | `/usuario/{id}` | Atualizar usuário |
| DELETE | `/usuario/{id}` | Excluir usuário |

### Clientes
| Método | URL | Descrição |
|---|---|---|
| GET | `/cliente` | Listar todos os clientes |
| GET | `/cliente/{id}` | Buscar cliente por ID |
| POST | `/cliente` | Cadastrar novo cliente |
| PUT | `/cliente/{id}` | Atualizar cliente |
| DELETE | `/cliente/{id}` | Excluir cliente |

### Mercadorias
| Método | URL | Descrição |
|---|---|---|
| GET | `/mercadoria` | Listar todas as mercadorias |
| GET | `/mercadoria/{id}` | Buscar mercadoria por ID |
| POST | `/mercadoria` | Cadastrar nova mercadoria |
| PUT | `/mercadoria/{id}` | Atualizar mercadoria |
| DELETE | `/mercadoria/{id}` | Excluir mercadoria |

### Serviços
| Método | URL | Descrição |
|---|---|---|
| GET | `/servico` | Listar todos os serviços |
| GET | `/servico/{id}` | Buscar serviço por ID |
| POST | `/servico` | Cadastrar novo serviço |
| PUT | `/servico/{id}` | Atualizar serviço |
| DELETE | `/servico/{id}` | Excluir serviço |

### Vendas
| Método | URL | Descrição |
|---|---|---|
| GET | `/venda` | Listar todas as vendas |
| GET | `/venda/{id}` | Buscar venda por ID |
| POST | `/venda` | Cadastrar nova venda |
| PUT | `/venda/{id}` | Atualizar venda |
| DELETE | `/venda/{id}` | Excluir venda |

### Empresa
| Método | URL | Descrição |
|---|---|---|
| GET | `/empresa` | Listar empresas |
| GET | `/empresa/{id}` | Buscar empresa por ID |
| POST | `/empresa` | Cadastrar empresa |
| PUT | `/empresa/{id}` | Atualizar empresa |
| DELETE | `/empresa/{id}` | Excluir empresa |

### Veículos
| Método | URL | Descrição |
|---|---|---|
| GET | `/veiculo` | Listar todos os veículos |
| GET | `/veiculo/{id}` | Buscar veículo por ID |
| POST | `/veiculo` | Cadastrar veículo |
| PUT | `/veiculo/{id}` | Atualizar veículo |
| DELETE | `/veiculo/{id}` | Excluir veículo |

---

## 9. Exemplos de requisições

### 9.1 Login (obter token)

```bash
curl -i -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"nomeUsuario": "admin", "senha": "admin123"}'
```

O token estará no header `Authorization` da resposta.

---

### 9.2 Listar usuários (ADMIN ou GERENTE)

```bash
curl -X GET http://localhost:8080/usuario \
  -H "Authorization: Bearer <token>"
```

---

### 9.3 Cadastrar novo usuário

```bash
curl -X POST http://localhost:8080/usuario \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João da Silva",
    "nomeSocial": "João",
    "perfis": ["ROLE_VENDEDOR"],
    "credenciais": [
      {
        "tipo": "usuarioSenha",
        "nomeUsuario": "joaovendedor",
        "senha": "joao123",
        "inativo": false,
        "criacao": "2025-01-01",
        "ultimoAcesso": "2025-01-01"
      }
    ]
  }'
```

---

### 9.4 Cadastrar mercadoria (ADMIN ou GERENTE)

```bash
curl -X POST http://localhost:8080/mercadoria \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Filtro de óleo Toyota",
    "descricao": "Filtro de óleo original Toyota",
    "valor": 45.90,
    "quantidade": 50,
    "fabricao": "2024-01-01",
    "validade": "2026-01-01",
    "cadastro": "2025-01-01"
  }'
```

---

### 9.5 Cadastrar serviço (ADMIN ou GERENTE)

```bash
curl -X POST http://localhost:8080/servico \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Troca de óleo",
    "descricao": "Troca completa do óleo do motor",
    "valor": 80.00
  }'
```

---

### 9.6 Listar vendas

```bash
curl -X GET http://localhost:8080/venda \
  -H "Authorization: Bearer <token>"
```

---

### 9.7 Atualizar usuário

```bash
curl -X PUT http://localhost:8080/usuario/1 \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João da Silva Atualizado",
    "nomeSocial": "João Novo"
  }'
```

---

### 9.8 Excluir mercadoria (ADMIN ou GERENTE)

```bash
curl -X DELETE http://localhost:8080/mercadoria/1 \
  -H "Authorization: Bearer <token>"
```

---

## 10. Banco de dados H2 (console)

O H2 é um banco de dados em memória — os dados são **resetados a cada reinício** da aplicação.

Para acessar o console visual:

1. Com a aplicação rodando, abra no navegador: `http://localhost:8080/h2-console`
2. Preencha os campos:
   - **JDBC URL:** `jdbc:h2:mem:automanager`
   - **User Name:** `sa`
   - **Password:** *(deixe em branco)*
3. Clique em **Connect**

Exemplos de queries SQL para inspecionar os dados:

```sql
SELECT * FROM USUARIO;
SELECT * FROM CREDENCIAL_USUARIO_SENHA;
SELECT * FROM MERCADORIA;
SELECT * FROM VENDA;
SELECT * FROM PERFIL_USUARIO;
```
