package combat;

import asciiPanel.AsciiPanel;

import bestiary.Monsters;
import bestiary.Skills;
import bestiary.Troop;

import main.Player;

import util.Grapchics;
import util.Utils;

import java.util.ArrayList;
import java.util.List;

public final class BattleField {
	private Monsters[] aliados;
	private int maxAliados;
	
	private Troop tropa;
	private Monsters monstroSelecionado;
	private Skills skillSelecionada;
	
	private List<Monsters> inimigos;
	private int maxInimigos;
	private List<Integer> posiçõesInimigosX;
	private List<Integer> posiçõesInimigosY;
	
	private boolean selecionarAlvo;
	
	private int linhaInicial, linhaAtual;
	
	protected BattleField(Monsters[] aliados, Troop tropa){
		this.aliados = aliados;
		this.maxAliados = aliados.length;
		
		this.tropa = tropa;
		
		this.inimigos = tropa.getMonstros();
		this.maxInimigos = tropa.getMonstros().size();
		this.posiçõesInimigosX = new ArrayList<Integer>();
		this.posiçõesInimigosY = new ArrayList<Integer>();
		
		prepararMonstros();
	}
	
	private void prepararMonstros(){
		Utils.limpaPrompt();
		System.out.println("");
		
		for (Monsters monstro : inimigos){
			if(monstro == null) continue;
			monstro.setForcaAtualCombate(monstro.getForcaAtual());
			monstro.setVidaAtualCombate(monstro.getVidaAtual());
			monstro.setSpeedAtualCombate(monstro.getSpeedAtual());
			monstro.setEstaminaAtualCombate(monstro.getEstaminaAtual());
		}
		
		for (int i = 0; i <= maxAliados-1; i++){
			Monsters monstro = aliados[i];
			if(monstro == null) continue;
			
			monstro.setForcaAtualCombate(monstro.getForcaAtual());
			monstro.setVidaAtualCombate(monstro.getVidaAtual());
			monstro.setSpeedAtualCombate(monstro.getSpeedAtual());
			monstro.setEstaminaAtualCombate(monstro.getEstaminaAtual());
		}
	}
	
	protected void selecionarComandoBatalha(){
		if (skillSelecionada == null) return;
		
		if (!selecionarAlvo){
			selecionarAlvo = true;
		}else if (selecionarAlvo && monstroSelecionado != null && skillSelecionada != null){
			if (BattleAction.verificarCustoHabilidade(aliados[0], monstroSelecionado, skillSelecionada)){
				BattleAction.executarHabilidade(aliados[0], monstroSelecionado, skillSelecionada);
				selecionarAlvo = false;
			}
		}
	}
	
	protected void telaVitória(){
		Grapchics.limpaTela();
		
		Grapchics.desenhaTela("Vitoria",0,10, AsciiPanel.brightWhite);
		
		Grapchics.atualizarTela();
	}
	
	protected void desenhaBatalha(){
			Grapchics.limpaTela();
			
			if(selecionarAlvo){
			Grapchics.desenhaTela("Q: Voltar",0,0, AsciiPanel.brightBlack);
			}
			
			linhaInicial = 10;
			
			linhaAtual = linhaInicial;
			int jogadorMonstrosX = 3;
			
			if (aliados[0] != null){
				desenhaMonstroBatalha(aliados[0], jogadorMonstrosX-2, linhaAtual-4);
			}
			if (maxAliados >= 2){
				if (aliados[1] != null){
				desenhaMonstroBatalha(aliados[1], jogadorMonstrosX, linhaAtual);
				}
			}
			if (maxAliados >= 3){
				if (aliados[2] != null){
				desenhaMonstroBatalha(aliados[2], jogadorMonstrosX-2, linhaAtual+4);
				}
			}
			
			posiçõesInimigosX.clear();
			posiçõesInimigosY.clear();
			
			if(inimigos.get(0) != null){
				desenhaMonstroBatalha(inimigos.get(0), 24, linhaAtual-4);
				posiçõesInimigosX.add(24);
				posiçõesInimigosY.add(linhaAtual-4);
			}
			if (maxInimigos >= 2){
				if(inimigos.get(1) != null){
				desenhaMonstroBatalha(inimigos.get(1), 22, linhaAtual);
				posiçõesInimigosX.add(22);
				posiçõesInimigosY.add(linhaAtual);
				}
			}
			if (maxInimigos >= 3){
				if(inimigos.get(2) != null){
				desenhaMonstroBatalha(inimigos.get(2), 24, linhaAtual+4);
				posiçõesInimigosX.add(24);
				posiçõesInimigosY.add(linhaAtual+4);
				}
			}
			
			linhaAtual = 20;
			
			if(selecionarAlvo && !posiçõesInimigosX.isEmpty()){
				desenhaSetaBatalha();
			}
			
			if(!selecionarAlvo){
				desenhaBatalhaComandos();
			}else{
				desenhaComandoDetalhe();
			}
			
			verificarMonstros();
			verificarFimBatalha();
			
			Grapchics.atualizarTela();
	}
	
	private void verificarMonstros(){
		maxInimigos = tropa.getMonstros().size();
		maxAliados = aliados.length;
		
		for (int i = 0; i <= maxInimigos-1; i++){
			Monsters monstro = inimigos.get(i);
			if(monstro == null) continue;
			
			if (monstro.getVidaAtualCombate() <= 0){
				inimigos.set(i, null);
			}
		}
		
		for (int i = 0; i <= maxAliados-1; i++){
			if(aliados[i] == null) continue;
			
			if (aliados[i].getVidaAtualCombate() <= 0){
				aliados[i] = null;
			}
		}
	}
	
	private void verificarFimBatalha(){
		if (todosInimigosDerrotados()){
			Battle.setSubEstadoAtual(Battle.SubEstadosBatalha.VITORIA);
			
			int exp = tropa.getExp();
			int ouro = tropa.getOuro();
			
			maxAliados = aliados.length;
			for (int i = 0; i <= maxAliados-1; i++){
				if(aliados[i] == null) continue;
				aliados[i].ganharExp(exp);
			}
			Player.ganharOuro(ouro);
		}
	}
	
	private boolean todosInimigosDerrotados(){
		for (Monsters monstro : inimigos){
			if (monstro != null){
				return false;
			}
		}
		return true;
	}
	
	private void desenhaMonstroBatalha(Monsters monstro, int x, int y){
		if (monstro == null) return;
    
		Grapchics.desenhaTela(monstro.getNomeMonstro(), x, y, AsciiPanel.brightWhite);
		
		Grapchics.desenhaTela(monstro.getVidaAtualCombate() + "/" + monstro.getVidaAtual(), 
		x, y+1, AsciiPanel.brightWhite);
		
		Grapchics.desenhaTela(monstro.getEstaminaAtualCombate() + "/" + monstro.getEstaminaAtual(), 
		x, y+2, AsciiPanel.brightWhite);
	}
	
	private void desenhaSetaBatalha(){    
		if (Battle.getCursorX() < 0){
			Battle.setCursorX(maxInimigos-1);
		}else if (Battle.getCursorX() > maxInimigos-1){
			Battle.setCursorX(0);
		}
		
		int x = posiçõesInimigosX.get(Battle.getCursorX());
		int y = posiçõesInimigosY.get(Battle.getCursorX());
		
		Grapchics.desenhaTela((char) 25, x+4, y - 1, AsciiPanel.brightYellow);
		
		if (Battle.getCursorX() > maxInimigos-1) Battle.setCursorX(maxInimigos-1);
		if (Battle.getCursorX() < 0) Battle.setCursorX(0);
		
		monstroSelecionado = tropa.getMonstros().get(Battle.getCursorX());
		if (monstroSelecionado == null) return;
		
		Grapchics.desenhaTela(monstroSelecionado.getNomeMonstro(), x, y, 
		AsciiPanel.brightYellow, AsciiPanel.brightBlack);
	}
	
	public void desenhaBatalhaComandos(){
		if (aliados[0] == null) return;
		int tamanhoSkills = aliados[0].getQuantidadeMaxSlotsHabilidade();
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		for (int i = 0; i <= tamanhoSkills-1; i++){
			Skills skillCarregada = aliados[0].getHabilidadeAtiva(i);
			if (skillCarregada != null){
				if (Battle.getCursorY() == i){
					Grapchics.desenhaTela((i+1)+": "+skillCarregada.getNomeHabilidade(),0,linhaAtual++,
					AsciiPanel.brightYellow, AsciiPanel.brightBlack);
					skillSelecionada = skillCarregada;
				}else{
					Grapchics.desenhaTela((i+1)+": "+skillCarregada.getNomeHabilidade(),0,linhaAtual++,AsciiPanel.brightWhite);
				}
			}else{
				if (Battle.getCursorY() == i){
					Grapchics.desenhaTela("[VAZIO]",0,linhaAtual++,
					AsciiPanel.brightYellow, AsciiPanel.brightBlack);
					skillSelecionada = null;
				}else{
					Grapchics.desenhaTela("[VAZIO]",0,linhaAtual++,AsciiPanel.brightBlack);
				}
			}
		}
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		
		if (Battle.getCursorY() > tamanhoSkills-1){
			Battle.setCursorY(0);
		}else if (Battle.getCursorY() < 0){
			Battle.setCursorY(tamanhoSkills-1);
		}
	}
	
	private void desenhaComandoDetalhe(){
		if (skillSelecionada == null) return;
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		Grapchics.desenhaTela(">> "+skillSelecionada.getNomeHabilidade(),0,linhaAtual++, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Poder: "+skillSelecionada.getPoderHabilidade(),0,linhaAtual++, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Precisao: "+skillSelecionada.getPrecisaoBase(),0,linhaAtual++, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Energia: "+skillSelecionada.getEnergiaHabilidade(),0,linhaAtual++, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Recarga: "+skillSelecionada.getRecargaHabilidade(),0,linhaAtual++, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
	}
	
	protected void setSelecionarAlvo(boolean selecionarAlvo){
		this.selecionarAlvo = selecionarAlvo;
	}
	
	//===
}