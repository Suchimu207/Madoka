package world;

import util.Grapchics;

import java.util.HashMap;
import java.util.Map;

public final class Maps {
	public static final char PAREDE = '#';
	public static final char BATALHA = '!';
	public static final char LOJA = '$';
	public static final char ARENA = 'A';
	public static final char PORTAL = '-';
	
	private static Map<String, String> mapasExistentes; 
	private static String mapaAtual, mapaVerificado;
	private static int iLinha, jColuna = 0;
	private static boolean bloqueioJogador;
	
	private Maps(){
	}
	
	public static void carregarMapas(){
		MapsManager.carregarMapas();
		mapasExistentes = MapsManager.getMapasExistentes();
	}
	
	public static void desenhaMapa(String mapaNome, int jogadorX, int jogadorY){	
		mapaAtual = mapasExistentes.get(mapaNome+".txt");
		
		if (mapaAtual == null){
			System.out.println("Nenhum mapa para desenhar: "+mapaAtual);
		}
		
		String[] linhas = mapaAtual.split("\\R");
		
		for (iLinha = 0; iLinha < linhas.length; iLinha++){
			char[] caracteres = linhas[iLinha].toCharArray();
			for (jColuna = 0; jColuna < caracteres.length; jColuna++){
				char tile = linhas[iLinha].charAt(jColuna);
				if (jColuna == jogadorX && iLinha == jogadorY){
					Grapchics.desenhaTela('@', jogadorX, jogadorY, Grapchics.BRANCO_CLARO);
				}else{
					switch(tile){
					case Maps.PAREDE:
					Grapchics.desenhaTela('#', jColuna, iLinha, Grapchics.PRETO_CLARO);
					break;
					case '.':
					Grapchics.desenhaTela('.', jColuna, iLinha, Grapchics.PRETO_CLARO);
					break;
					case Maps.LOJA:
					Grapchics.desenhaTela('$', jColuna, iLinha, Grapchics.AMARELO_CLARO);
					break;
					case ']':
					Grapchics.desenhaTela(']', jColuna, iLinha, Grapchics.BRANCO_CLARO);
					break;
					case '[':
					Grapchics.desenhaTela('[', jColuna, iLinha, Grapchics.BRANCO_CLARO);
					break;
					case Maps.PORTAL:
					Grapchics.desenhaTela('-', jColuna, iLinha, Grapchics.CIANO_CLARO);
					break;
					case Maps.ARENA:
					Grapchics.desenhaTela('A', jColuna, iLinha, Grapchics.AMARELO_CLARO);
					break;
					case Maps.BATALHA:
					Grapchics.desenhaTela('!', jColuna, iLinha, Grapchics.AMARELO_CLARO);
					break;
					case '?':
					Grapchics.desenhaTela('?', jColuna, iLinha, Grapchics.AMARELO_CLARO);
					break;
					}
				}
			}
		}
		Grapchics.atualizarTela();
	}
	
	public static boolean ehParede(String mapaNome, int jogadorX, int jogadorY){
		mapaVerificado = mapasExistentes.get(mapaNome + ".txt");
		if (mapaVerificado == null){
			return true;
		}
		
		String[] linhas = mapaVerificado.split("\\R");
		
		if (jogadorY < 0 || jogadorY >= linhas.length){
			return true;
		}
		String linhaAlvo = linhas[jogadorY];
		if (jogadorX < 0 || jogadorX >= linhaAlvo.length()){
			return true;
		}
		
		return linhaAlvo.charAt(jogadorX) == Maps.PAREDE;
	}
	
	public static char ehEvento(String mapaNome, int jogadorX, int jogadorY){
		mapaVerificado = mapasExistentes.get(mapaNome + ".txt");
		if (mapaVerificado == null){
			return '.';
		}
		
		String[] linhas = mapaVerificado.split("\\R");
		
		if (jogadorY < 0 || jogadorY >= linhas.length){
			return '.';
		}
		String linhaAlvo = linhas[jogadorY];
		if (jogadorX < 0 || jogadorX >= linhaAlvo.length()){
			return '.';
		}
		
		if (linhaAlvo.charAt(jogadorX) == Maps.LOJA){
			return Maps.LOJA;
		}else if (linhaAlvo.charAt(jogadorX) == Maps.BATALHA){
			return Maps.BATALHA;
		}else if (linhaAlvo.charAt(jogadorX) == Maps.ARENA){
			return Maps.ARENA;
		}
		return '.';
	}
	
	//===
}