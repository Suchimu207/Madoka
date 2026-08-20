package modes.arena;

import main.Terminal;

import bestiary.*;
import combat.Battle;

import main.Inventory;
import main.Player;
import main.Shop;

import manager.MonstersManager;

import util.Audio;
import util.GameState;
import util.Grapchics;
import util.Input;

import world.Maps;

import java.util.List;
import java.util.Set;

import java.util.LinkedHashMap;
import java.util.Map;

import java.awt.event.KeyEvent;

public class ArenaMode implements GameState{
	private enum SubEstadosArena{
		TORNEIO("Torneio"),
		RECOMPENSA("Recompensa");
		
		private final String nome;
		
		SubEstadosArena(String nome){
			this.nome = nome;
		}
		
		public String getSubEstadoNome(){
			return nome;
		}
	}
	
	private static SubEstadosArena subEstadoAtual = null;
	private static Map<Integer, Tournament> torneios;
	private static Tournament torneioAtual;
	
	private static int torneioSelecionado = 0;
	private static int rodadaAtual = -1;
	
	private static Monsters monstroDesbloqueado = null;
	
	private static boolean batalha = false;
	
    public ArenaMode(){
		monstroDesbloqueado = null;
		setarTorneios();
    }
	
	// ==================== INICIALIZAÇÃO ====================
	
	private void setarTorneios(){
		if (torneios == null){
			torneios = new LinkedHashMap<>();
			
			torneios.put(1, 
			new Tournament("Newbies´s Tournament", new int[]{1, 2, 3, 4, 5, 6, 7}, 
			14)
			);
			
			System.out.println(">>Torneios setados: "+torneios.size());
		}
	}
	
	// ==================== ESTADO ====================
	
	@Override
	public void desenhaEstado(){
		Grapchics.limpaTela();
		
		verificarResultadoBatalha();
		
		if (subEstadoAtual == null){
			desenhaTorneios();
		}else if (subEstadoAtual == SubEstadosArena.TORNEIO){
			desenhaTorneioAtual();
		}else if (subEstadoAtual == SubEstadosArena.RECOMPENSA){
			desenhaRecompensa();
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
            if (torneioSelecionado > 0 && torneios.containsKey(torneioSelecionado)){
                this.torneioAtual = torneios.get(torneioSelecionado);
                subEstadoAtual = SubEstadosArena.TORNEIO;
				Input.resetarCursor();
				ArenaMode.rodadaAtual = 1;
            }
        }else if (subEstadoAtual == SubEstadosArena.TORNEIO){
			ArenaMode.batalha = true;
			Terminal.setEstadoAnterior(this);
			Terminal.mudarEstado(new Battle(torneioAtual.getBatalha(rodadaAtual)));
        }else if (subEstadoAtual == SubEstadosArena.RECOMPENSA){
			subEstadoAtual = null;
			torneioAtual = null;
			Input.resetarCursor();
		}
	}
	
	private void teclaShift(){
	}
	
	private void teclaInventário(){
		if (subEstadoAtual == SubEstadosArena.TORNEIO){
            subEstadoAtual = null;
            Input.resetarCursor();
            Input.setCursorY(4);
        }else{
			subEstadoAtual = null;
			torneioSelecionado = -1;
            Terminal.mudarEstado(new Maps());
        }
	}
	
	private void teclaEsc(){
	}
	
	// ==================== DESENHO ====================
	
	private static void desenhaTorneios(){
		if (torneios == null || torneios.size() <= 0) return;
		
		int linhaAtual = 0;
		int linhaInicio = 0;
		int linhaFim = 0;
		
		Grapchics.desenhaCentroTTF("Arena - Torneios", linhaAtual++, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF("E: Sair", 0, linhaAtual++, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("Enter: Selecionar torneio", 0, linhaAtual++, Grapchics.PRETO_CLARO);
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		
		linhaInicio = linhaAtual;
		
		for (Map.Entry<Integer, Tournament> entry : torneios.entrySet()){
            int indice = entry.getKey();
            Tournament torneio = entry.getValue();
			
			int torneioFinalizado = torneio.isConcluido() ? 15 : 0;
			
            if (Input.getCursorY() == linhaAtual){
                Grapchics.desenhaHibrido(torneio.getNomeTorneio(), torneioFinalizado, 1, linhaAtual++, Grapchics.AMARELO_CLARO);
				torneioSelecionado = indice;
            }else{
				Grapchics.desenhaHibrido(torneio.getNomeTorneio(), torneioFinalizado, 0, linhaAtual++, Grapchics.BRANCO_CLARO);
            }
        }
		
		linhaFim = linhaAtual;
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		linhaAtual += 25;
		
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
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		
		if (Input.getCursorY() < linhaInicio){
			Input.setCursorY(linhaFim);
		}else if (Input.getCursorY() > linhaFim){
			Input.setCursorY(linhaInicio);
		}else if (Input.getCursorY() == 0) Input.setCursorY(linhaInicio);
	}
	
	private static void desenhaTorneioAtual(){
		if (torneioAtual == null) return;
		
        int linhaAtual = 0;
		
        Grapchics.desenhaCentroTTF(torneioAtual.getNomeTorneio(), linhaAtual++, Grapchics.BRANCO_CLARO);
		
        Grapchics.desenhaTTF("E: Voltar", 0, linhaAtual++, Grapchics.PRETO_CLARO);
        Grapchics.desenhaTTF("Enter: Iniciar Rodada", 0, linhaAtual++, Grapchics.PRETO_CLARO);
		
        Grapchics.desenhaTela("____________________", 0, linhaAtual++, Grapchics.PRETO_CLARO);
		
		int totalBatalhas = torneioAtual.getTotalBatalhas();
		
        for (int i = totalBatalhas; i >= 1; i--){
            Troop tropaInimiga = torneioAtual.getBatalha(i);
            String infoTropa = "Rodada "+i+":"+tropaInimiga.getNomeTropa();
			int tamanhoTexto = infoTropa.length();
			
			if (rodadaAtual == i){
				Grapchics.desenhaTTF(infoTropa, 0, linhaAtual, Grapchics.AMARELO_CLARO);
				Grapchics.desenhaTela((char)17, tamanhoTexto+1, linhaAtual++, Grapchics.AMARELO_CLARO);
			}else{
				Grapchics.desenhaTTF(infoTropa, 0, linhaAtual++, Grapchics.BRANCO_CLARO);
			}
        }
		
        Grapchics.desenhaTela("____________________", 0, linhaAtual++, Grapchics.PRETO_CLARO);
	}
	
	private static void desenhaRecompensa(){
		if (monstroDesbloqueado == null) return;
		Grapchics.desenhaCentroTTF("Monstro desbloqueado: "+monstroDesbloqueado.getNomeMonstro(), 10, Grapchics.BRANCO_CLARO);
	}
	
	// ==================== MÉTODOS AUXILIARES ====================
	
	private static void torneioVencido(){
		if (torneioAtual != null && rodadaAtual > torneioAtual.getTotalBatalhas()){
			if (!torneioAtual.isConcluido()){
				subEstadoAtual = SubEstadosArena.RECOMPENSA;
				
				int idRecompensa = torneioAtual.getRecompensaMonstro();
				
				Inventory.adicionarMonstroInventário(idRecompensa);
				Shop.adicionarItemEstoque(idRecompensa, 500);
				
				Audio.tocarSom("Item", 0.2f);
				
				monstroDesbloqueado = MonstersManager.getMonstro(idRecompensa);
				
				torneioAtual.setConcluido(true);
			}
		}
	}
	
	private static void verificarResultadoBatalha(){
		if (ArenaMode.batalha){
			if (Battle.verificarVitória()){
				ArenaMode.rodadaAtual++;
            
				if (subEstadoAtual == SubEstadosArena.TORNEIO){
					torneioVencido();
				}
            
				Battle.resetarVitória();
			}else{
				ArenaMode.rodadaAtual = 1;
				subEstadoAtual = null;
				Input.resetarCursor();
			}
		}
		ArenaMode.batalha = false;
	}
	// ==================== OUTROS ====================
	
   //===
}