package br.com.alura.literAlura.controller;

import br.com.alura.literAlura.model.Autor;
import br.com.alura.literAlura.model.DadosLivro;
import br.com.alura.literAlura.model.Livro;
import br.com.alura.literAlura.model.Resultado;
import br.com.alura.literAlura.repository.iAutorRepository;
import br.com.alura.literAlura.repository.iLivroRepository;
import br.com.alura.literAlura.service.ConsumoAPI;
import br.com.alura.literAlura.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {
    private final ConsumoAPI consumo = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();

    @Autowired
    private final iLivroRepository livrosRepositorio;

    private final iAutorRepository autorRepositorio;
    private static final String API_URL = "https://gutendex.com/books/?search=";

    public Principal(iLivroRepository livrosRepositorio, iAutorRepository autorRepositorio) {
        this.livrosRepositorio = livrosRepositorio;
        this.autorRepositorio = autorRepositorio;
    }

    public void exibeMenu() {
        try (Scanner scan = new Scanner(System.in)) {
            int opcao = -1;
            while (opcao != 0) {
                System.out.println("""
                        |***************************************************|
                        |*****                BEM-VINDO               ******|
                        |***************************************************|
                        
                        1 - Buscar livro por nome
                        2 - Listar livros salvos
                        3 - Listar autores salvos
                        4 - Listar autores vivos em um determinado ano
                        5 - Listar livros por idioma
                        
                        0 - Sair
                        
                        |***************************************************|
                        """);
                try {
                    System.out.print("Escolha uma opção: ");
                    opcao = scan.nextInt();
                    scan.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Por favor, insira um número válido.");
                    scan.nextLine();
                    continue;
                }

                switch (opcao) {
                case 1:
                    buscarLivro(scan);
                    break;
                case 2:
                    listarLivrosSalvos();
                    break;
                case 3:
                    listarAutoresSalvos();
                    break;
                case 4:
                    listarAutoresVivosEmUmAno(scan);
                    break;
                case 5:
                    listarLivrosPorIdioma(scan);
                    break;
                case 0:
                    System.out.println("|********************************|");
                    System.out.println("|     ENCERRANDO A APLICAÇÃO     |");
                    System.out.println("|********************************|\n");
                    break;
                default:
                    System.out.println("|********************************|");
                    System.out.println("|         OPCÃO INCORRETA        |");
                    System.out.println("|********************************|\n");
                    System.out.println("TENTE NOVAMENTE");
                    exibeMenu();
                    break;
            }



            }while (opcao != 0);
        }
    }

    private int obterOpcao(Scanner scanner) {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Por favor, insira um número válido.");
            scanner.nextLine();
            return -1;
        }
    }

    private void listarLivrosPorIdioma(Scanner scan) {

        System.out.println("""
        Lista de livros por idioma:
        ---- Escolha o idioma ----
        en - inglês
        es - espanhol
        fr - francês
        pt - português
        """);

        String idiomas = scan.nextLine();
        List<Livro> livros = livrosRepositorio.findByIdiomasContains(idiomas);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma escolhido.");
        } else {
            livros.stream()
                    .sorted(Comparator.comparing(Livro::getTitulo))
                    .forEach(System.out::println);
        }
    }

    private void listarAutoresVivosEmUmAno(Scanner scan) {
        System.out.print("Insira o ano para listar autores vivos: ");
        int ano = Integer.parseInt(scan.nextLine());
        List<Autor> autores = autorRepositorio
                .findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(ano, ano);
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor vivo encontrado.");
        } else {
            autores.stream()
                    .sorted(Comparator.comparing(Autor::getNome))
                    .forEach(System.out::println);
        }
    }

    private void listarAutoresSalvos() {
        System.out.println("Lista de autores no banco de dados:");
        List<Autor> autores = autorRepositorio.findAll();
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNome))
                .forEach(System.out::println);
    }

    private void listarLivrosSalvos() {
        System.out.println("Lista de livros no banco de dados:");
        List<Livro> livros = livrosRepositorio.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro salvo no banco de dados.");
        } else {
            livros.stream()
                    .sorted(Comparator.comparing(Livro::getTitulo))
                    .forEach(System.out::println);
        }
    }

    private void buscarLivro(Scanner scan) {
        System.out.print("Qual livro deseja buscar? ");
        String nomeLivro = scan.nextLine().toLowerCase();
        String json = consumo.obterDados(API_URL + nomeLivro.replace(" ", "%20").trim());
        Resultado dados = conversor.obterDados(json, Resultado.class);
        if (dados.resultado() == null ||dados.resultado().isEmpty()) {
            System.out.println("Livro não encontrado.");
        } else {
            DadosLivro dadosLivro = dados.resultado().get(0);
            Livro livro = new Livro(dadosLivro);
            Autor autor = new Autor(dadosLivro.autor().get(0));
            salvarDados(livro, autor);

        }

    }

    private void salvarDados(Livro livro, Autor autor) {
        if (livrosRepositorio.findByTituloContains(livro.getTitulo()).isPresent()) {
            System.out.println("Esse livro já existe no banco de dados." + livro);

        } else {
            livrosRepositorio.save(livro);
            System.out.println("Livro salvo com sucesso: \n" + livro);
        }

        if (autorRepositorio.findByNomeContains(autor.getNome()).isPresent()) {
            System.out.println("Esse autor já existe no banco de dados." + autor);

        } else {
            autorRepositorio.save(autor);
            System.out.println("Autor salvo com sucesso: \n" + autor);
        }
    }
}











