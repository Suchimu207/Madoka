package world;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import java.nio.file.Files;   
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;

public final class MapsManager {
	private static Map<String, String> mapasExistentes = new HashMap<>();
	private static String mapaAtual;
	
	private MapsManager(){
	}
	
	public static void carregarMapas(){
		Path diretorioMapas = Paths.get("data", "maps");
		
		if (Files.exists(diretorioMapas) && Files.isDirectory(diretorioMapas)){
			try (Stream<Path> stream = Files.list(diretorioMapas)){
				stream.filter(arquivo -> arquivo.toString().endsWith(".txt"))
				.forEach(arquivo -> {
					try{
					String conteudo = Files.readString(arquivo);
					mapasExistentes.put(arquivo.getFileName().toString(), conteudo);
					System.out.println("");
					System.out.println(">>Mapa carregado: " + arquivo.getFileName());
					}catch (IOException e){
						System.out.println("Erro ao ler o arquivo " + arquivo.getFileName() + ": " + e.getMessage());
					}
					});
					
			}catch(IOException e){
				System.out.println("Erro ao acessar o diretório de mapas: " + e.getMessage());
			}
		}else{
			System.out.println("Diretório de mapas não encontrado.");
		}
	}
	
	public static Map<String, String> getMapasExistentes(){
		return mapasExistentes;
	}
	
	//===
}