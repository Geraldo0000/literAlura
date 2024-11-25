package br.com.alura.literAlura.service;

import org.springframework.stereotype.Repository;

@Repository
public interface iConverteDados {
    <E> E obterDados(String json, Class<E> classe);
}
