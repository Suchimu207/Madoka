import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import java.awt.Color;

public final class Grapchics {
	private static int tileSizeX = 40;
	private static int tileSizeY = 40;
	private static AsciiPanel tela = new AsciiPanel(tileSizeX, tileSizeY, AsciiFont.CP437_16x16);
	
	private Grapchics(){
	}
	
	protected static void desenhaTela(char desenho, int posX, int posY){
		tela.write(desenho, posX, posY);
	}
	
	protected static void desenhaTela(char desenho, int posX, int posY, Color corAtual){
		tela.write(desenho, posX, posY, corAtual);
	}
	
	protected static void desenhaTela(char desenho, int posX, int posY, Color corFonte, Color corFundo){
		tela.write(desenho, posX, posY, corFonte, corFundo);
	}
		
	protected static void desenhaCentro(String desenho, int linha, Color corAtual){
		tela.writeCenter(desenho, linha, corAtual);
	}
	
	protected static void desenhaCentro(String desenho, int linha, Color corFonte, Color corFundo){
		tela.writeCenter(desenho, linha, corFonte, corFundo);
	}
	
	protected static void limpaTela(){
		tela.clear();
	}
	
	protected static void atualizarTela(){
		tela.repaint();
	}
	
	protected static int getTileSizeX(){
		return tileSizeX;
	}
		
	protected static int getTileSizeY(){
		return tileSizeY;
	}

	protected static AsciiPanel getTela(){
		return tela;
    }
	
	//===
}