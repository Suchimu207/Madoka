package util;

import combat.Battle;
import main.Player;

public final class Debug {
	private static boolean ativaDebug;
	
	private Debug(){
	}
	
	public static void mostrarDebug(int contadorFrames, String estadoAtual){
		if (Debug.ativaDebug){
			Utils.limpaPrompt();
			System.out.println("FPS Atual: " + contadorFrames);
			System.out.println("Jogador_X: "+Player.getJogadorX());
			System.out.println("Jogador_Y: "+Player.getJogadorY());
			System.out.println("Cursor_X: "+Input.getCursorX());
			System.out.println("Cursor_Y: "+Input.getCursorY());
			System.out.println("EstadoAtual: "+estadoAtual);
			if (Battle.getSubEstadoAtualTexto() != null) System.out.println("SubEstadoAtual: "+Battle.getSubEstadoAtualTexto());
		}
	}
	
	public static boolean isAtivaDebug(){
		return Debug.ativaDebug;
	}

	public static void setAtivaDebug(boolean ativaDebug){
		Debug.ativaDebug = ativaDebug;
	}
	
	//===
}