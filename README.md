# Sistema de Irrigação

Projeto acadêmico desenvolvido em Java para cadastrar plantas, persistir os dados e simular uma recomendação de irrigação considerando clima, umidade do solo e necessidade hídrica da cultura.

## Funcionalidades

- Cadastro de plantas;
- Consulta e ordenação das plantas em tabela;
- Atualização de registros;
- Exclusão de registros;
- Persistência em `data/plants.csv`;
- Validação de campos e exceção personalizada de umidade;
- Simulação de evapotranspiração, déficit do solo e volume recomendado;
- Início e encerramento de uma irrigação simulada;
- Teste de integração sem bibliotecas externas.

## Tecnologias e conceitos

- Java 17;
- Java Swing;
- Programação orientada a objetos;
- Encapsulamento, interface e polimorfismo;
- Composição e coleções;
- Padrão DAO;
- Arquitetura em camadas;
- Manipulação de arquivos com NIO;
- Tratamento de exceções.

## Estrutura

```text
src/
├── Model/       # Entidades e objetos do domínio
├── dao/         # CRUD e persistência CSV
├── exception/   # Exceções personalizadas
├── main/        # Ponto de entrada
├── service/     # Regras da recomendação de irrigação
├── test/        # Teste de integração
└── view/        # Interface Swing
```

## Como executar no Eclipse

1. Instale o JDK 17 ou superior.
2. Abra o Eclipse.
3. Acesse **File > Import > Existing Projects into Workspace**.
4. Selecione a pasta deste projeto.
5. Execute `src/main/Main.java` como **Java Application**.

## Como executar pelo terminal

### Linux ou macOS

```bash
./run.sh
```

### Windows

```bat
run.bat
```

### Compilação manual

```bash
mkdir -p bin
javac --release 17 -encoding UTF-8 -d bin $(find src -name "*.java")
java -cp bin main.Main
```

## Testes

Linux ou macOS:

```bash
./test.sh
```

Windows:

```bat
test.bat
```

O resultado esperado é:

```text
TODOS OS TESTES PASSARAM
```

## Demonstração pelo console

A demonstração pelo console cria uma planta somente quando o arquivo está vazio, lê os dados persistidos e executa a simulação:

```bash
./demo-console.sh
```

Ou:

```bash
java -cp bin main.Main --demo
```

## Persistência

Os dados ficam no arquivo:

```text
data/plants.csv
```

O DAO grava primeiro em um arquivo temporário e depois substitui o arquivo original, reduzindo o risco de corromper os dados durante uma atualização.

## Observação sobre os cálculos

A fórmula usada é uma estimativa didática para demonstrar regras de negócio e orientação a objetos. Ela não substitui cálculos agronômicos, sensores calibrados ou recomendações de um profissional.
