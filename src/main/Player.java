package main;

public final class Player {
    private static int ouro;
	
	private Player(){
	}
	
	public static int getOuro(){
		return ouro;
	}
	
	public static void setOuro(int ouroDado){
		if (ouroDado < 0) ouroDado = 1;
		ouro = ouroDado;
	}
	
	//===
}