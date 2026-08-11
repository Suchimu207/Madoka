package modes.arena;

import static main.Terminal.mudarEstado;

import bestiary.*;
import combat.*;

import main.Player;
import main.Inventory;

import util.GameState;
import util.Grapchics;
import util.Input;

import world.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.awt.event.KeyEvent;

public class ArenaMode implements GameState{
	private enum SubEstadosArena{
		TORNEIO("Torneio");
		
		private final String nome;
		
		SubEstadosArena(String nome){
			this.nome = nome;
		}
		
		public String getSubEstadoNome(){
			return nome;
		}
	}
	
	private static SubEstadosArena subEstadoAtual = null;
	
    public ArenaMode(){
		subEstadoAtual = null;
    }
	
	// ==================== ESTADO ====================
	
	@Override
	public void desenhaEstado(){
		Grapchics.limpaTela();
		
		if (subEstadoAtual == null){
			desenhaTorneios();
		}else if (subEstadoAtual == SubEstadosArena.TORNEIO){
			desenhaTorneioAtual();
		}
		
		Grapchics.atualizarTela();
	}
	
	@Override
    public void recebeComando(int tecla, Set<Integer> teclasPressionadas){
		switch (tecla){
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				teclaEsquerda();
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				teclaDireita();
				break;
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				teclaCima();
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				teclaBaixo();
				break;
			case KeyEvent.VK_ENTER:
				teclaEnter();
				break;
			case KeyEvent.VK_SHIFT:
				teclaShift();
				break;
			case KeyEvent.VK_E:
				teclaInventário();
				break;
			case KeyEvent.VK_ESCAPE:
				teclaEsc();
				break;
		}
	}
	
	// ==================== TECLAS ====================
	
	private void teclaEsquerda(){
	}
	
	private void teclaDireita(){
	}
	
	private void teclaCima(){
		if (subEstadoAtual == null){
			Input.decrementarCursorY();
		}
	}
	
	private void teclaBaixo(){
		if (subEstadoAtual == null){
			Input.incrementarCursorY();
		}
	}
	
	private void teclaEnter(){
		if (subEstadoAtual == null){
			subEstadoAtual = SubEstadosArena.TORNEIO;
		}
	}
	
	private void teclaShift(){
	}
	
	private void teclaInventário(){
	}
	
	private void teclaEsc(){
		mudarEstado(new Maps());
	}
	
	// ==================== DESENHO ====================
	
	private static void desenhaTorneios(){
		int linhaAtual = 0;
		
		if (Input.getCursorY() != 4){
			Input.setCursorY(4);
		}
		
		Grapchics.desenhaCentroTTF("Arena - Torneios", linhaAtual++, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF("ESC: Sair", 0, linhaAtual++, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("Enter: Selecionar torneio", 0, linhaAtual++, Grapchics.PRETO_CLARO);
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		
		if (Input.getCursorY() == linhaAtual){
			Grapchics.desenhaTTF("Newbies Cup", 1, linhaAtual++, Grapchics.AMARELO_CLARO);
		}else{
			Grapchics.desenhaTTF("Newbies Cup", 0, linhaAtual++, Grapchics.BRANCO_CLARO);
		}
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		linhaAtual+=25;
		
		Grapchics.desenhaCentroTTF("Equipe:",linhaAtual++, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		
		List<Monsters> equipe = Inventory.getEquipeLista();
		for (Monsters monstroEquipe : equipe){
			String nomeMonstroExibido = "";
			
			if (monstroEquipe != null){
				nomeMonstroExibido = monstroEquipe.getNomeMonstro()+" Nv"+monstroEquipe.getNivelAtual();
				Grapchics.desenhaTTF(nomeMonstroExibido, 0, linhaAtual++, Grapchics.BRANCO_CLARO);
			}else{
				Grapchics.desenhaTTF("[Vazio]", 0, linhaAtual++, Grapchics.PRETO_CLARO);
			}
		}
		
		Grapchics.desenhaTela("____________________",0,linhaAtual, Grapchics.PRETO_CLARO);
	}
	
	private static void desenhaTorneioAtual(){
		int linhaAtual = 0;
		
		Grapchics.desenhaCentroTTF("Arena - Newbies Cup", linhaAtual++, Grapchics.BRANCO_CLARO);
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
	}
	
	// ==================== MÉTODOS AUXILIARES ====================
	
	// ==================== OUTROS ====================
	
   //===
}