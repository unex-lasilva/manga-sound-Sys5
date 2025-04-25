package br.com.mangarosa.games;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MangaSound {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String MUSIC_FOLDER = "src/br/com/mangarosa/sound";
    private static final Map<String, Playlist> playlists = new LinkedHashMap<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== MangaSound ===");
            System.out.println("1. Criar lista de reprodução");
            System.out.println("2. Editar lista de reprodução");
            System.out.println("3. Executar lista de reprodução");
            System.out.println("4. Mostrar todas as listas");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = readInt();

            switch (opcao) {
                case 1 -> createPlaylist();
                case 2 -> editPlaylist();
                case 3 -> playPlaylist();
                case 4 -> listPlaylists();
                case 5 -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static int readInt() {
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Digite um número válido: ");
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private static void playPlaylist() {
        Playlist playlist = selectPlaylist("executar");
        if (playlist == null) return;

        List<Music> songs = playlist.getSongs();
        if (songs.isEmpty()) {
            System.out.println("Playlist vazia.");
            return;
        }

        PlaybackManager manager = new PlaybackManager(songs);
        manager.start();

        while (true) {
            System.out.println("\nControles: 1.Pausar  2.Continuar  3.Próxima  4.Anterior  5.Parar");
            int opt = readInt();
            switch (opt) {
                case 1 -> manager.pause();
                case 2 -> manager.resume();
                case 3 -> manager.next();
                case 4 -> manager.previous();
                case 5 -> {
                    manager.stop();
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

}
