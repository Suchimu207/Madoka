package main;

public final class Player {
    private static int ouro;
	private static final int OURO_INICIAL = 3000;
	private static int jogadorX, jogadorY;
	
	private Player(){
	}
	
	public static void setarJogador(){
		ouro = OURO_INICIAL;
		jogadorX = 19;
		jogadorY = 9;
	}
	
	public static int getJogadorX(){
		return jogadorX;
	}

	public static int getJogadorY(){
		return jogadorY;
	}

	public static void setJogadorX(int jogadorX){
		if (jogadorX < 0) jogadorX = 0;
		Player.jogadorX = jogadorX;
	}

	public static void setJogadorY(int jogadorY){
		if (jogadorY < 0) jogadorY = 0;
		Player.jogadorY = jogadorY;
	}
	
	public static int getOuro(){
		return ouro;
	}
	
	public static void setOuro(int ouroDado){
		if (ouroDado < 0) ouroDado = 1;
		ouro = ouroDado;
	}
	
	public static void ganharOuro(int ouroDado){
		if (ouroDado <= 0) ouroDado = 1;
		ouro += ouroDado;
	}
	
	public static void perderOuro(int ouroPerdido){
		if (ouroPerdido <= 0) ouroPerdido = 0;
		ouro -= ouroPerdido;
	}
	
	//===
}