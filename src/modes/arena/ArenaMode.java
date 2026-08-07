package modes.arena;

import bestiary.*;
import combat.*;
import main.Player;
import main.Inventory;
import util.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.awt.event.KeyEvent;

public class ArenaMode {
	protected enum SubEstadosArena{
		SALAO("Salão");
		
		private final String nome;
		
		SubEstadosArena(String nome){
			this.nome = nome;
		}
		
		public String getSubEstadoNome(){
			return nome;
		}
	}
	
	private static SubEstadosArena subEstadoAtual;
	
    private ArenaMode(){
    }
	
	public static void desenhaEstadoArena(){
		/*
		switch (subEstadoAtual){
		}
		*/
	}
	
	public static boolean recebeComandosArena(int tecla, Set<Integer> teclasPressionadas){
		/*
		if (teclasPressionadas != null && 
			teclasPressionadas.contains(KeyEvent.VK_E) &&
			teclasPressionadas.contains(KeyEvent.VK_Q)){
			
			if (subEstadoAtual == SubEstadosBatalha.CAMPO && campoBatalha != null){
				campoBatalha.ativarEspecial();
				return false;
			}
		}
		
		switch (tecla){
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				Input.decrementarCursorX();
				teclaEsquerda();
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				Input.incrementarCursorX();
				teclaDireita();
				break;
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				Input.decrementarCursorY();
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				Input.incrementarCursorY();
				break;
			case KeyEvent.VK_ENTER:
				if (subEstadoAtual == SubEstadosBatalha.VITORIA){
					subEstadoAtual = null;
					return true;
				}
				if (subEstadoAtual == SubEstadosBatalha.DERROTA){
					subEstadoAtual = null;
					return true;
				}
				teclaEnter();
				break;
			case KeyEvent.VK_SHIFT:
				teclaShift();
				break;
			case KeyEvent.VK_E:
				teclaE();
				break;
			case KeyEvent.VK_Q:
				teclaQ();
				break;
		}
		if (subEstadoAtual == null) return true;
		*/
		return false;
	}
	
	
	
	
	
	
	
   //===
}