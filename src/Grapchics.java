import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import java.awt.Color;

public class Grapchics{
	private AsciiPanel tela;
	private int tileSizeX, tileSizeY;
	
	public Grapchics(){
		tela = new AsciiPanel(40, 40, AsciiFont.CP437_16x16);
		tileSizeX = 40;
		tileSizeY = 40;
	}
	
	public void desenhaTela(char desenho, int posX, int posY){
		tela.write(desenho, posX, posY);
	}
	
	public void desenhaTela(char desenho, int posX, int posY, Color corAtual){
		tela.write(desenho, posX, posY, corAtual);
	}
	
	public void desenhaTela(char desenho, int posX, int posY, Color corFonte, Color corFundo){
		tela.write(desenho, posX, posY, corFonte, corFundo);
	}
	
	public void atualizarTela(){
		tela.repaint();
	}
	
	/*
	terminal.writeCenter(TITLE, 1, AsciiPanel.brightWhite);	
	*/
	
	public int getTileSizeX(){
		return tileSizeX;
	}
		
	public int getTileSizeY(){
		return tileSizeY;
	}
		
	public AsciiPanel getTela(){
		return tela;
    }
	
	//===
}