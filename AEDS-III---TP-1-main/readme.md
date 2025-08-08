Descrição Geral:
Esta aplicação tem como objetivo registrar dados em um arquivo externo, armazenando informações como nome, data de criação, data de conclusão, status e prioridade de cada registro. Os dados são persistidos em um formato estruturado para facilitar a leitura e análise posterior.

Funcionalidades:
Criação de registros: Permite adicionar novos registros ao sistema, definindo os atributos nome, data de criação, status e prioridade. A data de conclusão é inicializada como nula e pode ser atualizada posteriormente.
Leitura de registros: Carrega os registros existentes do arquivo para a memória, permitindo a visualização e manipulação dos dados.
Atualização de registros: Permite modificar os atributos de um registro existente, como status ou data de conclusão.
Remoção de registros: Permite excluir registros indesejados do sistema.
Persistência de dados: Salva as alterações realizadas nos registros no arquivo externo de forma persistente.
Tecnologias Utilizadas
Linguagem de programação: Java

Separador: O ponto e vírgula (;) é utilizado como separador entre os campos.
Formato de data: A data é armazenada no formato YYYY-MM-DD.

O trabalho possui um índice direto implementado com a tabela hash extensível?
Resposta: Não
A operação de inclusão insere um novo registro no fim do arquivo e no índice e retorna o ID desse registro?
Resposta: Sim
A operação de busca retorna os dados do registro, após localizá-lo por meio do índice direto?
Resposta: Sim
A operação de alteração altera os dados do registro e trata corretamente as reduções e aumentos no espaço do registro?
Resposta: Sim
A operação de exclusão marca o registro como excluído e o remove do índice direto?
Resposta: Sim
O trabalho está funcionando corretamente?
Resposta: Sim
O trabalho está completo?
Resposta: Sim
O trabalho é original e não a cópia de um trabalho de outro grupo?
Resposta: Sim, original

O esforço que tivemos foi principalmente na hora de seguir os métodos combinados pelo roteiro do TP1, e utilizar maneiras diferentes de construir um objeto, como o refactor.

Integrantes: Vinícius Figueiredo e Arthur Crossy.
