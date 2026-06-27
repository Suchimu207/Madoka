package main;

import world.*;

import asciiPanel.AsciiPanel;

import util.Grapchics;

import java.util.HashMap;
import java.util.Map;

public final class Maps {
	private static Map<String, String> mapasExistentes; 
	private static String mapaAtual, mapaVerificado;
	private static int iLinha, jColuna = 0;
	private static boolean bloqueioJogador;
	
	private Maps(){
	}
	
	protected static void carregarMapas(){
		MapsManager.carregarMapas();
		mapasExistentes = MapsManager.getMapasExistentes();
	}
	
	protected static void desenhaMapa(String mapaNome, int jogadorX, int jogadorY){	
		mapaAtual = mapasExistentes.get(mapaNome+".txt");
		
		if (mapaAtual == null){
			System.out.println("Nenhum mapa para desenhar.");
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
					case '$':
					Grapchics.desenhaTela('$', jColuna, iLinha, AsciiPanel.brightYellow);
					break;
					case ']':
					Grapchics.desenhaTela(']', jColuna, iLinha, AsciiPanel.brightWhite);
					break;
					case '[':
					Grapchics.desenhaTela('[', jColuna, iLinha, AsciiPanel.brightWhite);
					break;
					case '-':
					Grapchics.desenhaTela('-', jColuna, iLinha, AsciiPanel.brightCyan);
					break;
					case 'A':
					Grapchics.desenhaTela('A', jColuna, iLinha, AsciiPanel.brightYellow);
					break;
					case '!':
					Grapchics.desenhaTela('!', jColuna, iLinha, AsciiPanel.brightYellow);
					break;
					case '?':
					Grapchics.desenhaTela('?', jColuna, iLinha, AsciiPanel.brightYellow);
					break;
					}
				}
			}
		}
		Grapchics.atualizarTela();
	}
	
	protected static boolean ehParede(String mapaNome, int jogadorX, int jogadorY){
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
		
		return linhaAlvo.charAt(jogadorX) == '#';
	}
	
	protected static char ehEvento(String mapaNome, int jogadorX, int jogadorY){
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
		
		if (linhaAlvo.charAt(jogadorX) == '$'){
			return '$';
		}else if (linhaAlvo.charAt(jogadorX) == '!'){
			return '!';
		}
		return '.';
	}
	
	//===
}