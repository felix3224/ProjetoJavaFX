# ProjetoJavaFX - Gerenciamento de Alunos e Turmas

Sistema desktop desenvolvido como projeto acadêmico para gerenciamento de alunos e turmas escolares, com interface gráfica em JavaFX e persistência em banco de dados relacional MySQL.

---

## Sumário

- [Visão Geral](#visão-geral)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura e Padrões de Projeto](#arquitetura-e-padrões-de-projeto)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Modelagem do Banco de Dados](#modelagem-do-banco-de-dados)
- [Como Configurar e Executar](#como-configurar-e-executar)
- [Objetivo Acadêmico](#objetivo-acadêmico)

---

## Visão Geral

O projeto tem como finalidade praticar conceitos fundamentais do desenvolvimento de software em Java, aplicando separação de responsabilidades (MVC), isolamento do acesso a dados (DAO) e integração com banco de dados MySQL via JDBC. O sistema organiza dados escolares em um relacionamento de 1 para N (uma turma pode ter vários alunos vinculados).

---

## Funcionalidades

### Gestão de Turmas
- **Cadastro de turma:** Inclusão de novas turmas com geração automática do identificador (`codigo`).
- **Consulta de turma:** Busca por identificador e listagem de todas as turmas cadastradas para seleção na interface.
- **Atualização de turma:** Alteração do nome de turmas existentes.
- **Exclusão de turma:** Método de remoção por ID implementado na camada de dados (`TurmaDAO`).

### Gestão de Alunos (Camada de Dados)
- **Cadastro de aluno:** Inserção com nome, data de nascimento (`LocalDate`), foto em formato binário (`byte[]`/`BLOB`) e vínculo com a turma correspondente.
- **Consulta de aluno:** Busca por matrícula e listagem geral com junção (`JOIN`) dos dados da turma associada.
- **Filtro por turma:** Consulta de alunos matriculados em uma turma específica ordenados por nome.
- **Atualização de aluno:** Edição dos dados cadastrais por matrícula.
- **Exclusão de aluno:** Remoção por matrícula.

> **Nota sobre a interface gráfica:** As operações de inclusão, consulta e atualização de turmas estão integradas à tela FXML (`turma-view.fxml`). As operações da entidade Aluno estão implementadas na camada DAO/JDBC (`AlunoDAOJDBC`), prontas para integração com futuras telas da interface.

---

## Tecnologias Utilizadas

- **Linguagem:** Java 21
- **Interface Gráfica:** JavaFX 21 (com FXML e Scene Builder)
- **Persistência / Conectividade:** JDBC (Java Database Connectivity)
- **Driver de Banco:** MySQL Connector/J 9.3.0
- **Banco de Dados:** MySQL Server
- **Gerenciador de Dependências e Build:** Apache Maven
- **Testes (configurados no POM):** JUnit 5.10.2

---

## Arquitetura e Padrões de Projeto

O código foi estruturado seguindo boas práticas de projeto orientado a objetos:

### 1. MVC (Model-View-Controller)
- **Model (`com.aula.projetojavafx.model`):** Representa as entidades de domínio (`Aluno` e `Turma`) e as interfaces e implementações de acesso a dados.
- **View (`src/main/resources/...`):** Telas declarativas escritas em FXML (`application-view.fxml` e `turma-view.fxml`), isolando layout e componentes visuais do código Java.
- **Controller (`com.aula.projetojavafx.controller`):** Classes controladoras (`ApplicationController` e `TurmaController`) que escutam eventos da interface, interagem com a camada de modelo e atualizam os componentes em tela.

### 2. DAO (Data Access Object)
- As regras de persistência SQL ficam isoladas em interfaces (`AlunoDAO`, `TurmaDAO`) e implementações JDBC (`AlunoDAOJDBC`, `TurmaDAOJDBC`).
- Garante que mudanças no banco de dados ou nas consultas SQL não afetem a lógica dos controladores nem a interface do usuário.

### 3. Factory Method (`DAOFactory`)
- A interface `DAOFactory` centraliza a instanciação dos DAOs (`createTurmaDAO()` e `createAlunoDAO()`), injetando a conexão gerenciada pela classe utilitária `DataBase`.

### 4. Gerenciamento de Conexão (`DataBase`)
- Classe utilitária com métodos estáticos para obtenção de conexão única (`getConnection`) e fechamento seguro de recursos do JDBC (`Connection`, `PreparedStatement` e `ResultSet`), prevenindo vazamento de conexões.

---

## Estrutura do Projeto

```text
ProjetoJavaFX/
├── src/
│   └── main/
│       ├── java/
│       │   ├── module-info.java
│       │   └── org/example/projetojavafx/
│       │       ├── Application.java
│       │       ├── controller/
│       │       │   ├── ApplicationController.java
│       │       │   └── TurmaController.java
│       │       ├── db/
│       │       │   └── DB.java
│       │       ├── model/
│       │       │   ├── dao/
│       │       │   │   ├── AlunoDAO.java
│       │       │   │   ├── DAOFactory.java
│       │       │   │   ├── TurmaDAO.java
│       │       │   │   └── impl/
│       │       │   │       ├── AlunoDAOJDBC.java
│       │       │   │       └── TurmaDAOJDBC.java
│       │       │   └── entities/
│       │       │       ├── Aluno.java
│       │       │       └── Turma.java
│       │       └── utils/
│       │           └── Alertas.java
│       └── resources/
│           └── com/aula/projetojavafx/
│               └── controller/
│                   ├── application-view.fxml
│                   └── turma-view.fxml
├── pom.xml
└── README.md
```

---

## Modelagem do Banco de Dados

O sistema utiliza duas tabelas com relacionamento de chave estrangeira:

```
+---------------+              +--------------------+
|     turma     | 1          N |       aluno        |
+---------------+--------------+--------------------+
| codigo (PK)   |<-------------| codigo (FK)        |
| nome          |              | matricula (PK)     |
+---------------+              | nome               |
                               | dataNascimento     |
                               | foto (BLOB)        |
                               +--------------------+
```

### Script de Criação (DDL)

Caso queira configurar a base de dados no MySQL para executar a aplicação, utilize o script abaixo:

```sql
CREATE DATABASE IF NOT EXISTS projetojavafx;
USE projetojavafx;

CREATE TABLE IF NOT EXISTS turma (
    codigo INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS aluno (
    matricula INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    dataNascimento DATE NOT NULL,
    foto LONGBLOB,
    codigo INT NOT NULL,
    CONSTRAINT fk_aluno_turma FOREIGN KEY (codigo) REFERENCES turma(codigo)
);
```

---

## Como Configurar e Executar

### Pré-requisitos
- **Java Development Kit (JDK) 21** instalado.
- **MySQL Server** ativo e em execução localmente (porta 3306).
- **Apache Maven** (opcional, caso não use o Maven Wrapper incluso `mvnw`).

### Passo a Passo

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/ProjetoJavaFX.git
   cd ProjetoJavaFX
   ```

2. **Configurar o banco de dados:**
   - Crie a base de dados executando o script SQL apresentado na seção [Modelagem do Banco de Dados](#modelagem-do-banco-de-dados).
   - Se necessário, ajuste as credenciais de acesso no arquivo [DB.java](src/main/java/com/aula/projetojavafx/db/DB.java):
     ```java
     connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetojavafx", "seu_usuario", "sua_senha");
     ```

3. **Executar a aplicação:**
   - **Pelo terminal com o Maven Wrapper:**
     - No Linux/macOS:
       ```bash
       ./mvnw clean javafx:run
       ```
     - No Windows (PowerShell/CMD):
       ```cmd
       .\mvnw clean javafx:run
       ```
   - **Pela IDE (IntelliJ IDEA, Eclipse ou VS Code):**
     - Execute o método `main` da classe [Application.java](src/main/java/com/aula/projetojavafx/Application.java).

---

## Objetivo Acadêmico

Este projeto foi construído com fins didáticos, consolidando o aprendizado prático em:

- **Programação Orientada a Objetos (POO):** Encapsulamento, abstração e desacoplamento via interfaces.
- **Padrões Arquiteturais e de Projeto:** Aplicação prática de MVC, DAO e Factory Method.
- **Acesso a Banco Relacional:** Uso de JDBC puro (`PreparedStatement`, tratamento de SQL Injection, mapeamento relacional/objeto e controle de transações e fechamento de conexões).
- **Tratamento de Dados Especiais no BD:** Manipulação de tipos de data com `java.time.LocalDate` e armazenamento binário (`byte[]`) para fotos via `BLOB`.
- **Interfaces Ricas (Desktop GUI):** Ciclo de vida de aplicações JavaFX, manipulação de eventos e uso do padrão declarativo FXML.
