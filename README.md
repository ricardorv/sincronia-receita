## Desafio Sincronização Receita

#### Execução

Para compilar o projeto:

    ./mvnw package

Para executar:

    java -jar target/sincronia-receita-0.0.1-SNAPSHOT.jar /caminho/arquivo/entrada.csv /caminho/arquivo/saida.csv


#### Desenvolvimento

 - Criação da classe `Conta`
    - Representa através de uma classe, os valores de cada linha do arquivo .csv;
    - Possuí um campo adicional que será utilizado para armazenar o resultado do processamento;

 - Criação da classe `ContaProcessor`
    - Implementa a interface `ItemProcessor`;
    - Realiza o processamento de um objeto da classe `Conta` e retorna um objeto da classe `Conta`;
    - Neste projeto, a classe realiza uma chamada para o `ReceitaService`, simulando uma chamada externa;
    - O valor retornado pelo `ReceitaService` é adicionado ao objeto e retornado.
 
 - Criação da classe `BatchConfig`
    - Na primeira parte do código, é definido a leitura, processamento e escrita dos dados:
        - Para a leitura dos dados, foi utilizado a implementação `FlatFileItemReader` de `ItemReader`;
        - Para a escrita dos dados, foi utilizado a implementação `FlatFileItemWriter` de `ItemWriter`;
        - Para o processamento dos dados, foi criado uma instância da classe `ContaProcessor` criada anteriormente;
    - Na segunda parte do código, é realizado a configuração do Job:
        - O primeiro método define o Job;
        - O segundo método define o Step (um Job pode ter múltiplos "Steps")
        - Cada step pode envolver leitura, processamento e escrita;
        - Também é definido se o Step é tolerante a falhas e o que será feito nessa situação;
            - Neste projeto, caso ocorra uma falha, ele ira executar o Step novamente (máximo de 3 tentativas);