# Relatório técnico resumido

## 1. Visão geral

O Sistema de Irrigação é uma aplicação desktop desenvolvida em Java para representar um cenário simplificado de gerenciamento de irrigação. O sistema permite cadastrar plantas, manter os dados salvos em arquivo e calcular uma recomendação de irrigação a partir da necessidade hídrica da cultura, das condições climáticas e da umidade do solo.

## 2. Requisitos atendidos

| Requisito | Implementação |
|---|---|
| Compilar sem erros | Projeto validado com Java 17 e script `test.sh` |
| Executar corretamente | Interface Swing e modo de demonstração no console |
| Persistência funcional | Arquivo CSV em `data/plants.csv` |
| Operações básicas | Cadastro, consulta, atualização e exclusão |
| Conteúdos da disciplina | POO, interface, polimorfismo, composição, coleção, DAO, exceções, arquivos e interface gráfica |

## 3. Arquitetura

O projeto está organizado em camadas:

- **Model:** entidades e comportamentos do domínio;
- **DAO:** operações de persistência das plantas;
- **Service:** regras de cálculo da recomendação;
- **View:** interface gráfica em Java Swing;
- **Main:** inicialização da aplicação;
- **Exception:** exceções específicas do domínio;
- **Test:** teste de integração.

Essa separação reduz o acoplamento e facilita a manutenção. A interface não manipula diretamente o arquivo CSV; ela chama o DAO. Da mesma forma, a regra de recomendação fica concentrada no serviço, em vez de permanecer dentro da tela.

## 4. Persistência

A classe `PlantDAO` implementa as seguintes operações:

- `salvar`;
- `listar`;
- `buscarPorId`;
- `atualizar`;
- `excluir`.

Os registros são gravados em CSV com identificador único. Para reduzir o risco de corrupção, a atualização é escrita primeiro em um arquivo temporário e depois substitui o arquivo principal.

## 5. Regras e validações

O sistema valida:

- campos obrigatórios;
- necessidade hídrica maior que zero;
- temperatura dentro de uma faixa aceitável;
- umidade entre 0% e 100%;
- umidade do solo menor ou igual à capacidade de retenção;
- precipitação não negativa;
- profundidade maior que zero;
- horário no formato `HH:mm`.

A classe `UmidadeInvalidaException` demonstra a criação e utilização de uma exceção personalizada.

## 6. Simulação

A simulação produz:

- evapotranspiração estimada;
- umidade média do solo;
- déficit médio do solo;
- volume de água recomendado;
- justificativa da recomendação.

A fórmula é didática e foi criada para demonstrar regras de negócio. Ela não substitui métodos agronômicos ou dados de sensores calibrados.

## 7. Testes

O teste de integração cobre:

- criação do arquivo de persistência;
- cadastro de duas plantas;
- recuperação por uma nova instância do DAO;
- atualização;
- busca por ID;
- exclusão;
- cálculo de uma recomendação;
- rejeição de umidade inválida pela exceção personalizada.

## 8. Limitações e melhorias futuras

- substituir o CSV por banco de dados;
- adicionar autenticação;
- manter histórico das irrigações;
- incluir mais camadas de solo na interface;
- integrar sensores e API meteorológica;
- adotar fórmulas agronômicas validadas;
- gerar relatórios e gráficos.
