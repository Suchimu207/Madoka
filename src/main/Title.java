package main;

import util.Grapchics;
import util.Input;

public final class Title {
    private static String TITLE_NAME;
	
	private Title(){
	}
	
	protected static void desenhaTítulo(){
		if (TITLE_NAME == null) return;
		
		Grapchics.limpaTela();
		
		Grapchics.desenhaCentro(TITLE_NAME, 14, Grapchics.BRANCO_CLARO);
		
		if (Input.getCursorY() >= 4){
			Input.setCursorY(1);
		}else if (Input.getCursorY() <= 0) Input.setCursorY(3);
		
		if (Input.getCursorY() == 1){
			Grapchics.desenhaCentro("Novo jogo", 18, Grapchics.AMARELO_CLARO);
		}else{
			Grapchics.desenhaCentro("Novo jogo", 18, Grapchics.BRANCO_CLARO);
		}
		
		if (Input.getCursorY() == 2){
			Grapchics.desenhaCentro("Continuar", 20, Grapchics.AMARELO_CLARO);
		}else{
			Grapchics.desenhaCentro("Continuar", 20, Grapchics.BRANCO_CLARO);
		}
		
		if (Input.getCursorY() == 3){
			Grapchics.desenhaCentro("Sair     ", 22, Grapchics.AMARELO_CLARO);
		}else{
			Grapchics.desenhaCentro("Sair     ", 22, Grapchics.BRANCO_CLARO);
		}
		
		Grapchics.desenhaTela("Desenvolvido por Carlos S. Rehem.",0,39, Grapchics.BRANCO_CLARO);
		Grapchics.atualizarTela();
	}
	
	public static void setTITLE_NAME(String TITLE_NAME){
		Title.TITLE_NAME = TITLE_NAME;
	}
	
	//===
}