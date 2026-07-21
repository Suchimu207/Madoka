package combat;

import bestiary.*;
import combat.*;
import main.Player;
import main.Inventory;
import util.Input;

import asciiPanel.AsciiPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.awt.event.KeyEvent;

public final class Battle {
	protected enum SubEstadosBatalha{
		PREPARO,
		CAMPO,
		VITORIA,
		DERROTA;
	}
	
	private static SubEstadosBatalha subEstadoAtual;
	private static Monsters[] monstroSlotsAtivos;
	private static Troop tropaCarregada;
	private static BattlePreparation menu;
	private static BattleField campoBatalha;

	private static Monsters monstroMostrado;
	private static Skills skillMostrada;
	
	private Battle(){
	}
	
	public static void carregarDadosJogatina(){
		BattleManager.carregarDadosBatalha();
		Inventory.inicializarInventario();
		Player.setarJogador();
		
		montarEquipeInicial();
	}
	
	private static void montarEquipeInicial(){
		Inventory.adicionarMonstroInventário(1);
		//Monsters monstro = Inventory.getMonstroInventario(1);
		//monstro.subirNivel(39);
	}
	
	public static void setarBatalha(){
		subEstadoAtual = SubEstadosBatalha.PREPARO;
		
		Input.resetarCursor();
		menu = new BattlePreparation();
		
		monstroSlotsAtivos = new Monsters[3];
		tropaCarregada = TroopManager.getTroop(1);
	}
	
	public static void desenhaEstadoBatalha(){	
		switch (subEstadoAtual){
			case PREPARO:
				menu.desenhaTelaPreparo();
				break;
			case CAMPO:
				if (campoBatalha != null){
					campoBatalha.desenhaBatalha();
				}
				break;
			case VITORIA:
				if (campoBatalha != null){
					campoBatalha.desenhaTelaVitória();
				}
				break;
			case DERROTA:
				if (campoBatalha != null){
					campoBatalha.desenhaTelaDerrota();
				}
				break;
		}
	}
	
	public static boolean recebeComandosBatalha(int tecla, Set<Integer> teclasPressionadas){
		if (teclasPressionadas != null && 
			teclasPressionadas.contains(KeyEvent.VK_Q) &&
			teclasPressionadas.contains(KeyEvent.VK_E)){
			
			if (subEstadoAtual == SubEstadosBatalha.CAMPO && campoBatalha != null){
				campoBatalha.ativarEspecial();
				return false;
			}
		}
		
		switch (tecla){
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				Input.decrementarCursorX();
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				Input.incrementarCursorX();
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
					return true;
				}
				if (subEstadoAtual == SubEstadosBatalha.DERROTA){
					return true;
				}
				teclaEnter();
				break;
			case KeyEvent.VK_SHIFT:
				teclaShift();
				break;
			case KeyEvent.VK_E:
				if (subEstadoAtual == SubEstadosBatalha.PREPARO){
					return true;
				}
				break;
			case KeyEvent.VK_Q:
				teclaQ();
				break;
		}
		return false;
	}
		
	private static void teclaEnter(){
		if (subEstadoAtual == SubEstadosBatalha.PREPARO){
			alternarMonstroSlotsAtivos();
		}
		if (subEstadoAtual == SubEstadosBatalha.CAMPO){
			if (campoBatalha != null){
				campoBatalha.selecionarComandoBatalha();
			}
		}
	}
	
	private static void teclaShift(){
		if (subEstadoAtual == SubEstadosBatalha.CAMPO){
			if (campoBatalha != null){
				campoBatalha.recarregarEnergiaUsuário();
			}
		}
	}
	
	private static void teclaQ(){
		if (subEstadoAtual == SubEstadosBatalha.PREPARO){
			if (isEquipeSetada() && tropaCarregada != null){				
				campoBatalha = new BattleField(monstroSlotsAtivos, tropaCarregada);
				subEstadoAtual = SubEstadosBatalha.CAMPO;
			}
		}
		if (subEstadoAtual == SubEstadosBatalha.CAMPO){
			if (campoBatalha != null){
				campoBatalha.setSelecionarAlvo(false);
			}
		}
	}
	
	private static void alternarMonstroSlotsAtivos(){
		if (monstroMostrado == null) return;
		
		int maxSlotsAtivos = monstroSlotsAtivos.length;
		int slotExistente = -1;
		int primeiroVazio = -1;

		for (int i = 0; i <= maxSlotsAtivos-1; i++){
			if (monstroSlotsAtivos[i] == monstroMostrado){
				slotExistente = i;
			}
			if (monstroSlotsAtivos[i] == null && primeiroVazio == -1){
				primeiroVazio = i;
			}
		}
		
		if (slotExistente != -1){
			monstroSlotsAtivos[slotExistente] = null;
			reordenarMonstroSlotsAtivos();
		}else{
			if (primeiroVazio != -1){
				monstroSlotsAtivos[primeiroVazio] = monstroMostrado;
			}
		}
		
	}
	
	private static void reordenarMonstroSlotsAtivos(){
		int writeIndex = 0;
		for (int i = 0; i <= monstroSlotsAtivos.length-1; i++){
			if (monstroSlotsAtivos[i] != null){
				monstroSlotsAtivos[writeIndex++] = monstroSlotsAtivos[i];
			}
		}
		while (writeIndex < monstroSlotsAtivos.length){
			monstroSlotsAtivos[writeIndex++] = null;
		}
	}
	
	private static boolean isEquipeSetada(){
		if (monstroSlotsAtivos == null) return false;
		
		for (Monsters monstro : monstroSlotsAtivos){
			if (monstro != null){
				return true;
			}
		}
		return false;
	}
	
	protected static void exibirMensagemInimigo(String mensagem){
		if (campoBatalha != null){
			campoBatalha.setMensagemInimigo(mensagem);
		}
	}
	
	protected static void exibirMensagemAliado(String mensagem){
		if (campoBatalha != null){
			campoBatalha.setMensagemAliado(mensagem);
		}
	}
	
	protected static Monsters[] getMonstroSlotsAtivos(){
		return monstroSlotsAtivos;
	}
	
	protected static Troop getTropaCarregada(){
		return tropaCarregada;
	}
	
	protected SubEstadosBatalha getSubEstadoAtual(){
		return subEstadoAtual;
	}
	
	protected static void setMonstroMostrado(Monsters monstroMostrado){
		Battle.monstroMostrado = monstroMostrado;
	}
	
	protected static void setSubEstadoAtual(SubEstadosBatalha subEstadoAtual){
		Battle.subEstadoAtual = subEstadoAtual;
	}
	
	//===
}