package combat;

import bestiary.*;
import combat.*;
import main.Player;
import main.Inventory;

import asciiPanel.AsciiPanel;

import java.util.ArrayList;
import java.util.List;

import java.awt.event.KeyEvent;

public final class Battle {
	protected enum SubEstadosBatalha{
		PREPARO,
		CAMPO,
		VITORIA;
	}
	
	private static SubEstadosBatalha subEstadoAtual;
	private static Monsters[] monstroSlotsAtivos;
	private static Troop tropaCarregada;
	private static BattlePreparation menu;
	private static BattleField campoBatalha;

	private static Monsters monstroMostrado;
	private static Skills skillMostrada;
	
	private static int cursorX, cursorY; // Provisório.
	
	private Battle(){
	}
	
	public static void carregarDadosJogatina(){
		BattleManager.carregarDadosBatalha();
		Inventory.inicializarInventario();

		montarEquipeInicial();
	}
	
	private static void montarEquipeInicial(){
		Player.setOuro(150);
		
		Inventory.adicionarMonstroInventário(1);
		Monsters monstro = Inventory.getMonstroInventario(1);
		monstro.subirNivel(3);
	}
	
	public static void setarBatalha(){
		subEstadoAtual = SubEstadosBatalha.PREPARO;
		
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
					campoBatalha.telaVitória();
				}
				break;
		}
	}
	
	public static boolean recebeComandosBatalha(int tecla){
		switch (tecla){
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				cursorX--;
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				cursorX++;
				break;
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				cursorY--;
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				cursorY++;
				break;
			case KeyEvent.VK_ENTER:
				if (subEstadoAtual == SubEstadosBatalha.VITORIA){
					return true;
				}
				teclaEnter();
				break;
			case KeyEvent.VK_SHIFT:
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
			campoBatalha.selecionarComandoBatalha();
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
	
	protected static Monsters[] getMonstroSlotsAtivos(){
		return monstroSlotsAtivos;
	}
	
	protected static Troop getTropaCarregada(){
		return tropaCarregada;
	}
	
	protected static int getCursorX(){
		return cursorX;
	}

	protected static int getCursorY(){
		return cursorY;
	}
	
	protected SubEstadosBatalha getSubEstadoAtual(){
		return subEstadoAtual;
	}
	
	protected static void setCursorX(int cursorX){
		if (cursorX < 0) cursorX = 0;
		Battle.cursorX = cursorX;
	}

	protected static void setCursorY(int cursorY){
		if (cursorY < 0) cursorY = 0;
		Battle.cursorY = cursorY;
	}
	
	protected static void setMonstroMostrado(Monsters monstroMostrado){
		Battle.monstroMostrado = monstroMostrado;
	}
	
	public static void setSubEstadoAtual(SubEstadosBatalha subEstadoAtual){
		Battle.subEstadoAtual = subEstadoAtual;
	}
	
	//===
}