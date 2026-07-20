package util;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import java.awt.Color;

public final class Grapchics {
	private final static int TILE_SIZE_X = 40;
	private final static int TILE_SIZE_Y = 40;
	
	private final static AsciiPanel tela = new AsciiPanel(TILE_SIZE_X, TILE_SIZE_Y, AsciiFont.CP437_16x16);
	
	// ==================== CORES ====================
	
	public final static Color PRETO = new Color(10, 10, 10);
	public final static Color VERMELHO = new Color(69, 5, 5);
	public final static Color VERDE = new Color(6, 76, 6);
	public final static Color AMARELO = new Color(173, 173, 11);
	public final static Color AZUL = new Color(5, 5, 76);
	public final static Color MAGENTA = new Color(76, 5, 76);
	public final static Color CIANO = new Color(5, 76, 76);
	public final static Color BRANCO = new Color(235, 235, 235);
	
	public final static Color PRETO_CLARO = new Color(153, 153, 153);
	public final static Color VERMELHO_CLARO = new Color(236, 9, 9);
	public final static Color VERDE_CLARO = new Color(138, 249, 138);
	public final static Color AMARELO_CLARO = new Color(248, 248, 98);
	public final static Color AZUL_CLARO = new Color(73, 73, 253);
	public final static Color MAGENTA_CLARO = new Color(250, 53, 253);
	public final static Color CIANO_CLARO = new Color(154, 254, 254);
	public final static Color BRANCO_CLARO = new Color(247, 250, 250);
	
	public final static Color ELEMENTO_NATUREZA = new Color(100, 247, 100);
	public final static Color ELEMENTO_FOGO = new Color(235, 71, 71);
	public final static Color ELEMENTO_TERRA = new Color(197, 170, 150);
	public final static Color ELEMENTO_TROVAO = new Color(250, 250, 137);
	public final static Color ELEMENTO_AGUA = new Color(58, 64, 248);
	public final static Color ELEMENTO_LUZ = new Color(243, 247, 247);
	public final static Color ELEMENTO_MAGIA = new Color(247, 136, 251);
	public final static Color ELEMENTO_TREVAS = new Color(92, 92, 92);
	public final static Color ELEMENTO_METAL = new Color(173, 173, 173);
	public final static Color ELEMENTO_VENTO = new Color(177, 251, 177);
	public final static Color ELEMENTO_FISICO = new Color(249, 192, 78);
	
	// ==================== INICIALIZAÇÃO ====================
	
	static{
		tela.setDefaultBackgroundColor(PRETO);
	}

	private Grapchics(){
	}
	
	// ==================== DESENHO ====================
	
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
	
	// ==================== MÉTODOS AUXILIARES ====================
	
	public static void limpaTela(){
		tela.clear();
	}
	
	public static void atualizarTela(){
		tela.repaint();
	}
	
	public static int getTileSizeX(){
		return TILE_SIZE_X;
	}
		
	public static int getTileSizeY(){
		return TILE_SIZE_Y;
	}

	public static AsciiPanel getTela(){
		return tela;
    }
	
	//===
}