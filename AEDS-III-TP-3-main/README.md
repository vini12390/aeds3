# **Projeto de Registro de Dados e Manipulação com Estruturas Avançadas**

## **Integrantes**
- **Vinícius Figueiredo**
- **Arthur Crossy**

---

## **Descrição Geral**
Esta aplicação tem como objetivo registrar dados em um arquivo externo, armazenando informações como:
- **Nome**
- **Data de criação**
- **Data de conclusão**
- **Status**
- **Prioridade**

Os dados são persistidos em um formato estruturado para facilitar a leitura e análise posterior.

Além disso, o projeto incorpora:
- **Criação e manipulação de entidades de tarefas e categorias.**
- **Utilização de técnicas avançadas de estrutura de dados**, como a **Árvore B+** para implementar índices e relacionamentos.

---

## **Funcionalidades**
1. **Criação de registros:**
   - Permite adicionar novos registros ao sistema, definindo os atributos: **nome**, **data de criação**, **status** e **prioridade**.
   - A **data de conclusão** é inicializada como nula e pode ser atualizada posteriormente.

2. **Leitura de registros:**
   - Carrega os registros existentes do arquivo para a memória, permitindo a visualização e manipulação.

3. **Atualização de registros:**
   - Permite modificar os atributos de um registro existente, como **status** ou **data de conclusão**.

4. **Remoção de registros:**
   - Exclui registros indesejados do sistema.

5. **Persistência de dados:**
   - Salva as alterações realizadas nos registros em um arquivo externo de forma persistente.

---

## **Tecnologias Utilizadas**
- **Linguagem de Programação:** Java

---

## **Classes Criadas no TP3**

### **1. Classe `ArquivoRotulo`**
- Constrói o arquivo de rótulos.
- Implementa o CRUD de rótulos.
- Métodos para manipulação de rótulos.

### **2. Classe `ElementoLista`**
- Estrutura da **lista invertida** implementada.

### **3. Classe `Rotulo`**
- Entidade criada com seus atributos.
- Métodos de serialização e deserialização.
- Implementação de `toString` e `print`.

### **4. Classe `TarefaRotulo`**
- Monta o par `TarefaRotulo`, permitindo a busca de tarefas por rótulo.

### **5. Classe `RotuloTarefa`**
- Monta o par `RotuloTarefa`, permitindo a busca de tarefas por rótulo.

### **6. Classe `MenuRotulo`**
- Interface de rótulos acessada no menu do programa.

### **7. Classe `ParNomeIDRotulo`**
- Relaciona **nome**, **ID** e **rótulos**.

### **8. Classe `ParNomeIDTarefa`**
- Relaciona **nome**, **ID** e **tarefas**.

---

## **Principais Desafios**
- **Lista invertida:** A implementação dessa estrutura foi uma novidade neste trabalho e exigiu maior atenção.
- **Árvore B+:** As operações relacionadas ao relacionamento utilizando a Árvore B+ foram menos desafiadoras devido à introdução dessa estrutura no TP2.

---

## **Checklist**
- O índice invertido com os termos das tarefas foi criado usando a classe `ListaInvertida`? **Sim**
- O CRUD de rótulos foi implementado? **Sim**
- No arquivo de tarefas, os rótulos são incluídos, alterados e excluídos em uma Árvore B+? **Sim**
- É possível buscar tarefas por palavras usando o índice invertido? **Sim**
- É possível buscar tarefas por rótulos usando uma Árvore B+? **Sim**
- O trabalho está completo? **Sim**
- O trabalho é original e não é uma cópia de um trabalho de um colega? **Sim**

---
