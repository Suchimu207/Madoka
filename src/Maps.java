import asciiPanel.AsciiPanel;

import java.util.ArrayList;
import java.util.stream.Stream;

import java.nio.file.Files;   
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;

public final class Maps {
	private static ArrayList<String> mapasExistentes = new ArrayList<String>();
	private static String mapaAtual;
	private static int iLinha, jColuna = 0;
	
	private Maps(){
	}
	
	protected static void carregarMapas(){
	
		Path diretorioMapas = Paths.get("data", "maps");
			
		if (Files.exists(diretorioMapas) && Files.isDirectory(diretorioMapas)){
			try (Stream<Path> stream = Files.list(diretorioMapas)){
				stream.filter(arquivo -> arquivo.toString().endsWith(".txt"))
				.forEach(arquivo -> {
					try{
					String conteudo = Files.readString(arquivo);
					mapasExistentes.add(conteudo);
					System.out.println("Mapa carregado: " + arquivo.getFileName());
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
		
		if (!mapasExistentes.isEmpty()) mapaAtual = mapasExistentes.get(0);
	}
	
	protected static void desenhaMapa(int jogadorX, int jogadorY){
		if (mapaAtual == null) {
			System.out.println("Nenhum mapa para desenhar.");
			return;
		}
		
		String[] linhas = mapaAtual.split("\\R");
		
		for (iLinha = 0; iLinha < linhas.length; iLinha++){
			char[] caracteres = linhas[iLinha].toCharArray();
			for (jColuna = 0; jColuna < caracteres.length; jColuna++){
				char tile = linhas[iLinha].charAt(jColuna);
				if (jColuna == jogadorX && iLinha == jogadorY){
					Grapchics.desenhaTela('@', jogadorX, jogadorY, AsciiPanel.brightWhite);
				}else{
					switch(tile){
					case '#':
					Grapchics.desenhaTela('#', jColuna, iLinha, AsciiPanel.brightBlack);
					break;
					case '.':
					Grapchics.desenhaTela('.', jColuna, iLinha, AsciiPanel.brightWhite);
					break;
					case '_':
					Grapchics.desenhaTela('_', jColuna, iLinha, AsciiPanel.brightWhite);
					break;
					}
				}
			}
		}
		Grapchics.atualizarTela();
    }
	
	//===
}