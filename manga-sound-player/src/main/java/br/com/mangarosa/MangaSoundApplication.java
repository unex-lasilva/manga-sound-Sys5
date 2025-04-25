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

    private static void editPlaylist() {
        Playlist playlist = selectPlaylist("editar");
        if (playlist == null) return;
        while (true) {
            System.out.println("\nEditando '" + playlist.getName() + "':");
            System.out.println("1. Adicionar música");
            System.out.println("2. Remover música");
            System.out.println("3. Renomear playlist");
            System.out.println("4. Deletar playlist");
            System.out.println("5. Ver músicas");
            System.out.println("6. Voltar");
            System.out.print("Escolha uma opção: ");
            int opt = readInt();
            switch (opt) {
                case 1 -> {
                    List<Music> songs = listAllSongs();
                    if (songs.isEmpty()) break;
                    System.out.print("Número da música para adicionar: ");
                    int idx = readInt();
                    if (idx >= 1 && idx <= songs.size()) {
                        playlist.addSong(songs.get(idx - 1));
                        System.out.println("Adicionada: " + songs.get(idx - 1).getName());
                    } else {
                        System.out.println("Número inválido.");
                    }
                }
                case 2 -> {
                    playlist.listSongs();
                    System.out.print("Número da música para remover: ");
                    int idx = readInt();
                    if (playlist.removeSong(idx - 1)) {
                        System.out.println("Removida.");
                    } else {
                        System.out.println("Número inválido.");
                    }
                }
                case 3 -> {
                    System.out.print("Novo nome: ");
                    String newName = scanner.nextLine().trim();
                    if (newName.isEmpty() || playlists.containsKey(newName)) {
                        System.out.println("Nome inválido ou já existe.");
                    } else {
                        playlists.remove(playlist.getName());
                        playlist.setName(newName);
                        playlists.put(newName, playlist);
                        System.out.println("Renomeado para '" + newName + "'.");
                    }
                }
                case 4 -> {
                    playlists.remove(playlist.getName());
                    System.out.println("Playlist deletada.");
                    return;
                }
                case 5 -> playlist.listSongs();
                case 6 -> { return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void playPlaylist() {
        Playlist playlist = selectPlaylist("executar");
        if (playlist == null) return;
        List<Music> songs = playlist.getSongs();
        if (songs.isEmpty()) {
            System.out.println("Playlist vazia.");
            return;
        }

        // Gerenciador de reprodução com auto-avançar
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
                case 5 -> { manager.stop(); return; }
                default -> System.out.println("Opção inválida.");
            }
        }
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

    private static Playlist selectPlaylist(String action) {
        if (playlists.isEmpty()) {
            System.out.println("Nenhuma playlist para " + action + ".");
            return null;
        }
        List<String> keys = new ArrayList<>(playlists.keySet());
        System.out.println("Playlists disponíveis:");
        for (int i = 0; i < keys.size(); i++) {
            System.out.println((i + 1) + ". " + keys.get(i));
        }
        System.out.print("Número da playlist para " + action + ": ");
        int idx = readInt();
        if (idx >= 1 && idx <= keys.size()) {
            return playlists.get(keys.get(idx - 1));
        }
        System.out.println("Número inválido.");
        return null;
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

    private static int readInt() {
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Digite um número válido: ");
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    // Gerencia reprodução com auto-avançar
    static class PlaybackManager {
        private final List<Music> songs;
        private Clip clip;
        private int index = 0;
        private boolean manualStop = false;

        public PlaybackManager(List<Music> songs) {
            this.songs = songs;
        }

        public void start() {
            playCurrent();
        }

        private void playCurrent() {
            Music music = songs.get(index);
            try {
                if (clip != null) {
                    clip.stop();
                    clip.close();
                }
                AudioInputStream stream = AudioSystem.getAudioInputStream(new File(music.getPath()));
                clip = AudioSystem.getClip();
                clip.open(stream);
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        if (!manualStop && clip.getMicrosecondPosition() >= clip.getMicrosecondLength()) {
                            index = (index + 1) % songs.size();
                            playCurrent();
                        }
                    }
                });
                manualStop = false;
                clip.start();
                System.out.println("Tocando: " + music.getName());
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println("Erro ao tocar '" + music.getName() + "': " + e.getMessage());
            }
        }

        public void pause() {
            if (clip != null && clip.isRunning()) clip.stop();
        }

        public void resume() {
            if (clip != null && !clip.isRunning()) clip.start();
        }

        public void next() {
            manualStop = true;
            index = (index + 1) % songs.size();
            playCurrent();
        }

        public void previous() {
            manualStop = true;
            index = (index - 1 + songs.size()) % songs.size();
            playCurrent();
        }

        public void stop() {
            manualStop = true;
            if (clip != null) {
                clip.stop();
                clip.close();
            }
        }
    }

    static class Music {
        private final String name;
        private final String path;
        public Music(String name, String path) { this.name = name; this.path = path; }
        public String getName() { return name; }
        public String getPath() { return path; }
    }

    static class Playlist {
        private String name;
        private final List<Music> songs = new ArrayList<>();
        public Playlist(String name) { this.name = name; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public void addSong(Music m) { songs.add(m); }
        public boolean removeSong(int index) { if (index >= 0 && index < songs.size()) { songs.remove(index); return true; } return false; }
        public List<Music> getSongs() { return songs; }
        public void listSongs() {
            if (songs.isEmpty()) {
                System.out.println("Nenhuma música nesta playlist.");
                return;
            }
            for (int i = 0; i < songs.size(); i++) {
                System.out.println((i + 1) + ". " + songs.get(i).getName());
            }
        }
    }
}
