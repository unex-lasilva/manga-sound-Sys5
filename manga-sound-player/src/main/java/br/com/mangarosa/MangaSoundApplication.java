package br.com.mangarosa.games;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MangaSound {
    // Configurações iniciais do sistema
    private static final Scanner scanner = new Scanner(System.in);
    private static final String MUSIC_FOLDER = "src/br/com/mangarosa/sound";
    private static final Map<String, Playlist> playlists = new LinkedHashMap<>();

    // Ponto de entrada do programa
    public static void main(String[] args) {
        exibirMenuPrincipal();
    }

    // Navegação principal
    private static void exibirMenuPrincipal() {
        while (true) {
            System.out.println("\n=== MangaSound Player ===");
            System.out.println("1. Gerenciar Playlists");
            System.out.println("2. Reprodutor Musical");
            System.out.println("3. Sair");
            System.out.print("Escolha: ");

            switch (readInt()) {
                case 1 -> gerenciarPlaylists();
                case 2 -> iniciarReprodutor();
                case 3 -> { System.out.println("Encerrando..."); return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    // Gerenciamento completo de playlists
    private static void gerenciarPlaylists() {
        while (true) {
            System.out.println("\n=== GERENCIADOR DE PLAYLISTS ===");
            System.out.println("1. Criar nova playlist");
            System.out.println("2. Editar playlist existente");
            System.out.println("3. Listar todas as playlists");
            System.out.println("4. Voltar ao menu principal");
            System.out.print("Escolha: ");

            switch (readInt()) {
                case 1 -> criarNovaPlaylist();
                case 2 -> editarPlaylist();
                case 3 -> listarPlaylists();
                case 4 -> { return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    // Criação de nova playlist
    private static void criarNovaPlaylist() {
        System.out.print("\nNome da nova playlist: ");
        String nome = scanner.nextLine().trim();

        if (nome.isEmpty() || playlists.containsKey(nome)) {
            System.out.println("Nome inválido ou já existente");
            return;
        }

        Playlist novaPlaylist = new Playlist(nome);
        List<Music> musicasDisponiveis = carregarMusicas();

        if (!musicasDisponiveis.isEmpty()) {
            System.out.println("\nAdicionar músicas (digite 0 para finalizar):");
            while (true) {
                System.out.print("Número da música: ");
                int escolha = readInt();
                
                if (escolha == 0) break;
                if (escolha < 1 || escolha > musicasDisponiveis.size()) {
                    System.out.println("Número inválido");
                } else {
                    novaPlaylist.adicionarMusica(musicasDisponiveis.get(escolha - 1));
                    System.out.println("Adicionada: " + musicasDisponiveis.get(escolha - 1).getNome());
                }
            }
        }

        playlists.put(nome, novaPlaylist);
        System.out.println("\nPlaylist '" + nome + "' criada com sucesso!");
    }

    // Edição flexível de playlists
    private static void editarPlaylist() {
        if (playlists.isEmpty()) {
            System.out.println("\nNenhuma playlist disponível para edição");
            return;
        }

        Playlist playlist = selecionarPlaylist();
        if (playlist == null) return;

        while (true) {
            System.out.println("\nEDITANDO: " + playlist.getNome());
            playlist.listarMusicas();
            System.out.println("\n1. Adicionar música");
            System.out.println("2. Remover música");
            System.out.println("3. Renomear playlist");
            System.out.println("4. Excluir playlist");
            System.out.println("5. Voltar");
            System.out.print("Escolha: ");

            switch (readInt()) {
                case 1 -> adicionarMusicaPlaylist(playlist);
                case 2 -> removerMusicaPlaylist(playlist);
                case 3 -> renomearPlaylist(playlist);
                case 4 -> { excluirPlaylist(playlist); return; }
                case 5 -> { return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    // Métodos auxiliares de gerenciamento
    private static List<Music> carregarMusicas() {
        File diretorio = new File(MUSIC_FOLDER);
        File[] arquivos = diretorio.listFiles((dir, nome) -> nome.toLowerCase().endsWith(".wav"));
        List<Music> lista = new ArrayList<>();

        if (arquivos == null || arquivos.length == 0) {
            System.out.println("\nNenhum arquivo de música encontrado em: " + MUSIC_FOLDER);
            return lista;
        }

        System.out.println("\nMúsicas disponíveis:");
        for (int i = 0; i < arquivos.length; i++) {
            System.out.println((i + 1) + ". " + arquivos[i].getName());
            lista.add(new Music(arquivos[i].getName(), arquivos[i].getPath()));
        }
        return lista;
    }

    private static Playlist selecionarPlaylist() {
        listarPlaylists();
        System.out.print("\nNúmero da playlist: ");
        int escolha = readInt();
        
        if (escolha < 1 || escolha > playlists.size()) {
            System.out.println("Número inválido");
            return null;
        }
        
        return new ArrayList<>(playlists.values()).get(escolha - 1);
    }

    // Continua com os métodos restantes...
    // [As partes 4 e 5 seriam incluídas aqui]
}