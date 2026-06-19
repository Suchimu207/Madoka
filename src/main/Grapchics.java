package main;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import java.awt.Color;

public final class Grapchics {
	private static int tileSizeX = 40;
	private static int tileSizeY = 40;
	private final static AsciiPanel tela = new AsciiPanel(tileSizeX, tileSizeY, AsciiFont.CP437_16x16);
	
	private Grapchics(){
	}
	
	public static void desenhaTela(char desenho, int posX, int posY){
		tela.write(desenho, posX, posY);
	}
	
	public static void desenhaTela(char desenho, int posX, int posY, Color corAtual){
		tela.write(desenho, posX, posY, corAtual);
	}
	
	public static void desenhaTela(char desenho, int posX, int posY, Color corFonte, Color corFundo){
		tela.write(desenho, posX, posY, corFonte, corFundo);
	}
	
	public static void desenhaTela(String desenho, int posX, int posY){
		tela.write(desenho, posX, posY);
	}

	public static void desenhaTela(String desenho, int posX, int posY, Color corAtual){
		tela.write(desenho, posX, posY, corAtual);
	}
	
	public static void desenhaTela(String desenho, int posX, int posY, Color corFonte, Color corFundo){
		tela.write(desenho, posX, posY, corFonte, corFundo);
	}

	public static void desenhaCentro(String desenho, int linha, Color corAtual){
		tela.writeCenter(desenho, linha, corAtual);
	}
	
	public static void desenhaCentro(String desenho, int linha, Color corFonte, Color corFundo){
		tela.writeCenter(desenho, linha, corFonte, corFundo);
	}
	
	public static void limpaTela(){
		tela.clear();
	}
	
	public static void atualizarTela(){
		tela.repaint();
	}
	
	public static int getTileSizeX(){
		return tileSizeX;
	}
		
	public static int getTileSizeY(){
		return tileSizeY;
	}

	public static AsciiPanel getTela(){
		return tela;
    }
	
	//===
}