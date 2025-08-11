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

6. **Criptografia:**
   - Cifra e decifra os dados usando Vigenére.


---

## **Tecnologias Utilizadas**
- **Linguagem de Programação:** Java

---

## **Classes Criadas no TP4**

### **1. Classe `Criptografia`**
- Cifra dados usando Vigenére.
- Deifra dados usando Vigenére.


### **2. Classe `ArquivoLZW`**
- Compacta (codifica e descodifica) dados usando o algoritmo LZW.



---

## **Principais Desafios**
- **Tempo para fazer:** Por ser final de semestre, sofri um pouco com o tempo para fazer o trabalho, mas ainda bem que o professor é muito legal e extendeu a data para entregar :).

---

## **Checklist**
Há uma rotina de compactação usando o algoritmo LZW para fazer backup dos arquivos? Sim
Há uma rotina de descompactação usando o algoritmo LZW para recuperação dos arquivos? Sim
O usuário pode escolher a versão a recuperar? Sim
Qual foi a taxa de compressão alcançada por esse backup? 160%
O trabalho está funcionando corretamente? Tem um problema na hora da escolha dos arquivos dos dados do backup.
O trabalho está completo? Visto que tem um problema não resolvido, ele não está completo.
O trabalho é original e não a cópia de um trabalho de um colega? Sim

---
