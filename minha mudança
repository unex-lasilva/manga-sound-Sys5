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
