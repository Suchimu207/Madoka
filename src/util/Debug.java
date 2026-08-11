package util;

import combat.Battle;
import main.Player;

public final class Debug {
	private static boolean ativaDebug;
	private static String os;
	
	private Debug(){
	}
	
	public static void mostrarDebug(int contadorFrames){
		if (Debug.ativaDebug){
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
	
	public static boolean isAtivaDebug(){
		return Debug.ativaDebug;
	}

	public static void setAtivaDebug(boolean ativaDebug){
		if (ativaDebug == false) limpaPrompt();
		Debug.ativaDebug = ativaDebug;
	}
	
	//===
}