package main;

import asciiPanel.AsciiPanel;

import util.Grapchics;

public final class Title {
    private static String TITLE_NAME;
	
	private Title(){
	}
	
	protected static void desenhaTítulo(){
		if (TITLE_NAME == null) return;
		
		Grapchics.limpaTela();
		
		Grapchics.desenhaCentro(TITLE_NAME, 14, AsciiPanel.brightWhite);
		
		if (Terminal.cursorY >= 4){
			Terminal.cursorY = 1;
		}else if (Terminal.cursorY <= 0) Terminal.cursorY = 3;
		
		if (Terminal.cursorY == 1){
			Grapchics.desenhaCentro("Novo jogo", 18, AsciiPanel.brightYellow, 
			AsciiPanel.brightBlack);
		}else{
			Grapchics.desenhaCentro("Novo jogo", 18, AsciiPanel.brightWhite);
		}
		
		if (Terminal.cursorY == 2){
			Grapchics.desenhaCentro("Continuar", 20, AsciiPanel.brightYellow, 
			AsciiPanel.brightBlack);
		}else{
			Grapchics.desenhaCentro("Continuar", 20, AsciiPanel.brightWhite);
		}
		
		if (Terminal.cursorY == 3){
			Grapchics.desenhaCentro("Sair     ", 22, AsciiPanel.brightYellow,
			AsciiPanel.brightBlack);
		}else{
			Grapchics.desenhaCentro("Sair     ", 22, AsciiPanel.brightWhite);
		}
		
		Grapchics.desenhaTela("Desenvolvido por Carlos S. Rehem.",0,39, AsciiPanel.brightWhite);
		Grapchics.atualizarTela();
	}
	
	public static void setTITLE_NAME(String TITLE_NAME){
		Title.TITLE_NAME = TITLE_NAME;
	}
	
	//===
}