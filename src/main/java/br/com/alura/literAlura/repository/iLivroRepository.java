package br.com.alura.literAlura.repository;

import br.com.alura.literAlura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface iLivroRepository extends JpaRepository<Livro, Long> {
    Optional<Livro> findByTituloContains(String  titulo);
    List<Livro> findByIdiomasContains(String idiomas);




}
