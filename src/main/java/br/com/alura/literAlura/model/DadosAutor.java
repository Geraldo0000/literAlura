package br.com.alura.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record DadosAutor(@JsonAlias("name") String nome,
    @JsonAlias("birth_year") Integer anoNascimento,
    @JsonAlias("death_year") Integer anoFalecimento,
    @JsonAlias("id") Long id,
    @JsonAlias("title") String titulo,
    @JsonAlias("authors")
    List<Autor> autores){

    }




