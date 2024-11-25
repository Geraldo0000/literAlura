package br.com.alura.literAlura.repository;

import br.com.alura.literAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface iAutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNomeContains(String nome);
    List<Autor> findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(Integer anoNascimento, Integer anoFalecimento);
}
