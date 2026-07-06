package main;

public final class Player {
    private static int ouro;
	private static final int OURO_INICIAL = 3000;
	
	private Player(){
	}
	
	public static void setarJogador(){
		ouro = OURO_INICIAL;
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