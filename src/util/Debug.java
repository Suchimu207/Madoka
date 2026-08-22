package util;

import combat.Battle;
import main.Player;

public final class Debug {
	private static boolean ativaTelaDebug;
	private static String os;
	
	private Debug(){
	}
	
	public static void mostrarTelaDebug(int contadorFrames){
		if (Debug.ativaTelaDebug){
			limpaPrompt();
			System.out.println("FPS Atual: " + contadorFrames);
			System.out.println("Jogador_X: "+Player.getJogadorX());
			System.out.println("Jogador_Y: "+Player.getJogadorY());
			System.out.println("Cursor_X: "+Input.getCursorX());
			System.out.println("Cursor_Y: "+Input.getCursorY());
		}
	}
	
	public static void limpaPrompt(){
		os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")){
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }else if (os.contains("linux") || os.contains("unix")){
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        }catch (Exception e){
			System.out.println("Erro ao limpar prompt: "+e.getMessage());
			System.exit(1);
        }
      //===
    }
	
	public static boolean isAtivaTelaDebug(){
		return Debug.ativaTelaDebug;
	}

	public static void setAtivaTelaDebug(boolean ativaTelaDebug){
		if (ativaTelaDebug == false) limpaPrompt();
		Debug.ativaTelaDebug = ativaTelaDebug;
	}
	
	//===
}