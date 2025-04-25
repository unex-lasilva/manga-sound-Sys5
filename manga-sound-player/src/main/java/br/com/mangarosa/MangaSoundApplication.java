package br.com.mangarosa.games;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MangaSound {
    // Constantes, scanner e estrutura de dados
    private static final Scanner scanner = new Scanner(System.in);
    private static final String MUSIC_FOLDER = "src/br/com/mangarosa/sound";
    private static final Map<String, Playlist> playlists = new LinkedHashMap<>();

    // Menu principal
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== MangaSound ===");
            System.out.println("1. Criar lista de reprodução");
            // ... opções do menu ...
            switch (opcao) {
                case 1 -> createPlaylist();
                // ... outros casos ...
            }
        }
    }

    // Criação de playlists + listagem de músicas
    private static void createPlaylist() {
        // ... lógica de criação ...
    }

    private static List<Music> listAllSongs() {
        // ... lista arquivos .wav da pasta ...
    }

    private static void listPlaylists() {
        // ... exibe playlists existentes ...
    }

    // Utilitário para leitura de inteiros
    private static int readInt() {
        // ... valida entrada ...
    }
}