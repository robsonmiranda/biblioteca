package br.com.alura.biblioteca.repository;

import br.com.alura.biblioteca.model.Livro;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    @Lock(LockModeType.READ)
    Optional<Livro> findById(Long id);

    /*
    1 - Vimos, em vídeo, como funciona a atualização com o Lock otimista, usando o @Version. Para confirmar esse fato, tente atualizar o título
        de um livro, utilizando o método PUT do controller. Veja se a versão foi atualizada, fazendo um select no banco.
        - Ao executarmos a atualização com o LockType.OPTIMISTIC, obtemos um incremento na versão, assim como ao apenas utilizar o @Version.
    2 - Teste o GET do controller e confirme: o lock optimistic altera a versão somente em casos de escrita no banco.
        - Ao executarmos a busca com o lock OPTIMISTIC, a versão não é incrementada.
    3 - Agora, na classe LivroRepository, troque o tipo de Lock de OPTIMISTIC para OPTMISTIC_FORCE_INCREMENT e repita o GET
        - Com o OPTIMISTIC_FORCE_INCREMENT, tanto na atualização quanto na busca do livro, a versão é incrementada.
    4 - Tente apagar o @Transactional do método de obter livro por id. O que acontece ao tentar obter o livro? Tente pensar no motivo pelo qual
        isso ocorre.
        - Estamos forçando um incremento. então a leitura também escreve no banco de dados, pois ela precisa incrementar a versão.
          Dessa forma, se o método não é anotado com Transactional, recebemos uma exceção.
    5 - Repita os testes para os locks READ e WRITE
        - O Lock READ realmente funciona como o OPTIMISTIC e o WRITE funciona como o OPTIMISTIC_FORCE_INCREMENT.
    6 - Iremos testar os locks pessimistas. Para isso, você pode comentar o atributo versao. Inicie com o PESSIMISTIC_READ e tente executar
        requisições paralelas.
        - O PESSIMISTIC_READ permite que leituras (buscas no banco de dados) sejam executadas paralelamente, mas escritas (atualização do título)
          não podem ser feitas simultaneamente. Ao tentar atualizar o registro ao mesmo tempo, obtemos a exceção: MySQLTransactionRollbackException.
    7 - Repita o processo para PESSIMISTIC_WRITE
        - No PESSIMISTIC_WRITE nenhuma exceção é lançada. Isso porque, como usamos o @Transactional, ele começa a transação e bloqueia o banco de
          dados, esperando que ela finalize. Só depois que a operação termina, que outra requisição pode acessar o banco.
    8 - Por fim, teste o PESSIMISTIC_FORCE_INCREMENT. Para isso, você precisará voltar com o atributo de versão. Veja como a versão é incrementada
        a cada atualização/leitura e depois tente fazer requisições paralelas. Como o OPTIMISTIC_FORCE_INCREMENT e o PESSIMISTIC_FORCE_INCREMENT se
        diferenciam?
        - No incremento da versão, eles são muito parecidos. O que muda é a forma de bloqueio: um é otimista e usa a parte da versão, e outro é
          pessimista e bloqueia recursos.
    */
}

