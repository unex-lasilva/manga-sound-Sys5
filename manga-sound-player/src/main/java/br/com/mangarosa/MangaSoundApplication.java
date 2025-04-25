package br.com.mangarosa.games;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MangaSound {
    // Configurações do sistema
    private static final Scanner scanner = new Scanner(System.in);
    private static final String MUSIC_FOLDER = "src/br/com/mangarosa/sound";
    private static final Map<String, Playlist> playlists = new LinkedHashMap<>();
    private static PlaybackManager gerenciadorAtual;

    // Ponto de entrada principal
    public static void main(String[] args) {
        exibirMenuPrincipal();
    }

    // Navegação centralizada
    private static void exibirMenuPrincipal() {
        while (true) {
            System.out.println("\n=== MANGA SOUND ===");
            System.out.println("1. Gerenciar Playlists");
            System.out.println("2. Reprodutor Musical");
            System.out.println("3. Sair");
            System.out.print("Escolha: ");

            switch (lerInteiro()) {
                case 1 -> gerenciarPlaylists();
                case 2 -> controlarReprodutor();
                case 3 -> { encerrarSistema(); return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    // Gerenciamento completo de playlists
    private static void gerenciarPlaylists() {
        while (true) {
            System.out.println("\n=== GERENCIADOR ===");
            System.out.println("1. Nova Playlist");
            System.out.println("2. Editar Existente");
            System.out.println("3. Listar Todas");
            System.out.println("4. Voltar");
            System.out.print("Escolha: ");

            switch (lerInteiro()) {
                case 1 -> criarPlaylist();
                case 2 -> editarPlaylist();
                case 3 -> listarPlaylists();
                case 4 -> { return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    // Controle do reprodutor musical
    private static void controlarReprodutor() {
        Playlist selecionada = selecionarPlaylistParaTocar();
        if (selecionada == null) return;

        gerenciadorAtual = new PlaybackManager(selecionada.getMusicas());
        gerenciadorAtual.iniciar();

        while (true) {
            System.out.println("\n=== CONTROLES ===");
            System.out.println("1. ⏸ Pausar");
            System.out.println("2. ▶ Retomar");
            System.out.println("3. ⏭ Próxima");
            System.out.println("4. ⏮ Anterior");
            System.out.println("5. ⏹ Parar");
            System.out.print("Escolha: ");

            switch (lerInteiro()) {
                case 1 -> gerenciadorAtual.pausar();
                case 2 -> gerenciadorAtual.retomar();
                case 3 -> gerenciadorAtual.proxima();
                case 4 -> gerenciadorAtual.anterior();
                case 5 -> { gerenciadorAtual.parar(); return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    // Implementação dos métodos de playlist
    private static void criarPlaylist() {
        System.out.print("\nNome da playlist: ");
        String nome = scanner.nextLine().trim();

        if (nome.isEmpty() || playlists.containsKey(nome)) {
            System.out.println("Nome inválido ou já existente");
            return;
        }

        Playlist nova = new Playlist(nome);
        List<Music> musicas = carregarMusicas();

        if (!musicas.isEmpty()) {
            System.out.println("\nSelecione músicas (0 para finalizar):");
            while (true) {
                System.out.print("Número: ");
                int escolha = lerInteiro();
                if (escolha == 0) break;
                if (escolha >= 1 && escolha <= musicas.size()) {
                    nova.adicionarMusica(musicas.get(escolha - 1));
                    System.out.println("✔ " + musicas.get(escolha - 1).getNome());
                } else {
                    System.out.println("Número inválido");
                }
            }
        }

        playlists.put(nome, nova);
        System.out.println("\nPlaylist '" + nome + "' criada!");
    }

    private static void editarPlaylist() {
        Playlist playlist = selecionarPlaylist();
        if (playlist == null) return;

        while (true) {
            System.out.println("\nEDITANDO: " + playlist.getNome());
            playlist.listarMusicas();
            System.out.println("\n1. ➕ Adicionar");
            System.out.println("2. ➖ Remover");
            System.out.println("3. ✏ Renomear");
            System.out.println("4. ❌ Excluir");
            System.out.println("5. Voltar");
            System.out.print("Escolha: ");

            switch (lerInteiro()) {
                case 1 -> adicionarMusica(playlist);
                case 2 -> removerMusica(playlist);
                case 3 -> renomearPlaylist(playlist);
                case 4 -> { excluirPlaylist(playlist); return; }
                case 5 -> { return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    // Métodos utilitários
    private static List<Music> carregarMusicas() {
        File[] arquivos = new File(MUSIC_FOLDER).listFiles((dir, nome) -> 
            nome.toLowerCase().endsWith(".wav"));
        List<Music> lista = new ArrayList<>();

        if (arquivos == null || arquivos.length == 0) {
            System.out.println("\n⚠ Nenhuma música encontrada");
            return lista;
        }

        System.out.println("\nMÚSICAS DISPONÍVEIS:");
        for (int i = 0; i < arquivos.length; i++) {
            System.out.println((i + 1) + ". " + arquivos[i].getName());
            lista.add(new Music(arquivos[i].getName(), arquivos[i].getPath()));
        }
        return lista;
    }

    private static Playlist selecionarPlaylist() {
        listarPlaylists();
        System.out.print("\nNúmero da playlist: ");
        int escolha = lerInteiro();
        List<String> nomes = new ArrayList<>(playlists.keySet());
        
        if (escolha < 1 || escolha > nomes.size()) {
            System.out.println("Número inválido");
            return null;
        }
        return playlists.get(nomes.get(escolha - 1));
    }

    private static Playlist selecionarPlaylistParaTocar() {
        if (playlists.isEmpty()) {
            System.out.println("\nCrie uma playlist primeiro");
            return null;
        }
        return selecionarPlaylist();
    }

    private static void encerrarSistema() {
        if (gerenciadorAtual != null) {
            gerenciadorAtual.parar();
        }
        System.out.println("\nSistema encerrado");
    }

    private static int lerInteiro() {
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Digite um número válido: ");
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    // Classes internas (Parte 5) seriam implementadas aqui
    // [PlaybackManager, Music, Playlist]
}
