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

    private static void createPlaylist() {
        System.out.print("Nome da nova playlist: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Nome inválido.");
            return;
        }
        if (playlists.containsKey(name)) {
            System.out.println("Já existe playlist com esse nome.");
            return;
        }
        Playlist playlist = new Playlist(name);
        List<Music> songs = listAllSongs();
        if (songs.isEmpty()) return;
        System.out.println("\nSelecione músicas para adicionar (digite 0 para terminar):");
        while (true) {
            System.out.print("Número da música: ");
            int idx = readInt();
            if (idx == 0) break;
            if (idx < 1 || idx > songs.size()) {
                System.out.println("Número inválido.");
            } else {
                playlist.addSong(songs.get(idx - 1));
                System.out.println("Adicionada: " + songs.get(idx - 1).getName());
            }
        }
        playlists.put(name, playlist);
        System.out.println("Playlist '" + name + "' criada.");
    }

    private static List<Music> listAllSongs() {
        File folder = new File(MUSIC_FOLDER);
        File[] files = folder.listFiles((d, name) -> name.toLowerCase().endsWith(".wav"));
        List<Music> list = new ArrayList<>();
        if (files == null || files.length == 0) {
            System.out.println("Nenhuma música na pasta: " + MUSIC_FOLDER);
            return list;
        }
        System.out.println("Músicas disponíveis:");
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + ". " + files[i].getName());
            list.add(new Music(files[i].getName(), files[i].getPath()));
        }
        return list;
    }

    private static void listPlaylists() {
        if (playlists.isEmpty()) {
            System.out.println("Nenhuma playlist criada.");
            return;
        }
        System.out.println("Playlists disponíveis:");
        int i = 1;
        for (String name : playlists.keySet()) {
            System.out.println(i++ + ". " + name);
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
