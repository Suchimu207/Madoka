package combat;

import bestiary.Monsters;
import bestiary.Skills;
import bestiary.Troop;

import combat.description.SkillDescription;

import combat.effects.Effects;

import combat.status.StatusBase;

import main.Player;

import util.Grapchics;
import util.Input;
import util.Utils;

import java.util.ArrayList;
import java.util.List;

import java.awt.Color;

public final class BattleField {
	public enum TipoDetalhe{
		ALIADO(0),
		INIMIGO(1);
		
		private final int valor;
		
		TipoDetalhe(int valor){
			this.valor = valor;
		}
		
		public int getValor(){
			return valor;
		}
	}
	
	// ==================== ATRIBUTOS ====================
	
	private Monsters[] aliados;
	private int maxAliados;
	private List<Integer> posiçõesAliadosX;
	private List<Integer> posiçõesAliadosY; 
	
	private Troop tropa;
	private Monsters monstroSelecionado, monstroVisualizado;
	private List<Monsters> monstrosAlvos;
	private Skills skillSelecionada, skillEspecial;
	private Skills skillUsada = null;
	private BattleActionResult resultadoAção = null;
	
	private List<Monsters> inimigos;
	private int maxInimigos;
	private List<Integer> posiçõesInimigosX;
	private List<Integer> posiçõesInimigosY;
	
	private boolean selecionarAlvo;
	private boolean aguardandoInimigo = false;
	private boolean aguardandoAliado = false; 
	private boolean especialAtivo = false;
	
	private String mensagemTurnoInimigo = null;
	private String danoTurnoInimigo = null;
	private String mensagemTurnoAliado = null;
	private String danoTurnoAliado = null;
	
	private int linhaInicial, linhaMáxima, linhaAtual = 0;
	
	private int detalheAtual;
	
	// ==================== CONSTRUTOR ====================
	
	protected BattleField(Monsters[] aliados, Troop tropa){
		this.aliados = aliados;
		this.maxAliados = aliados.length;
		this.posiçõesAliadosX = new ArrayList<Integer>();
		this.posiçõesAliadosY = new ArrayList<Integer>();
		
		this.tropa = tropa;
		this.monstrosAlvos = new ArrayList<Monsters>();
		
		this.inimigos = tropa.getMonstros();
		this.maxInimigos = tropa.getMonstros().size();
		this.posiçõesInimigosX = new ArrayList<Integer>();
		this.posiçõesInimigosY = new ArrayList<Integer>();
		
		prepararMonstros();
		inicializarActionValue();
	}
	
	// ==================== PREPARAÇÃO ====================
	
	private void prepararMonstros(){
		Utils.limpaPrompt();
		System.out.println("");
		
		for (int i = 0; i <= maxAliados-1; i++){
			Monsters monstro = aliados[i];
			if(monstro == null) continue;
			
			monstro.setForcaAtualCombate(monstro.getForcaAtual());
			monstro.setVidaAtualCombate(monstro.getVidaAtual());
			monstro.setVidaAtualCombateMaxima(monstro.getVidaAtual());
			monstro.setSpeedAtualCombate(monstro.getSpeedAtual());
			monstro.setEstaminaAtualCombate(monstro.getEstaminaAtual());
			monstro.desativarRecargas();
			monstro.limparStatus();
			monstro.zerarEscudoAtual();
		}
		System.out.println(">>Aliados inicializados.");
        System.out.println("");
		
		for (Monsters monstro : inimigos){
			if(monstro == null) continue;
			monstro.setForcaAtualCombate(monstro.getForcaAtual());
			monstro.setVidaAtualCombate(monstro.getVidaAtual());
			monstro.setVidaAtualCombateMaxima(monstro.getVidaAtual());
			monstro.setSpeedAtualCombate(monstro.getSpeedAtual());
			monstro.setEstaminaAtualCombate(monstro.getEstaminaAtual());
			monstro.desativarRecargas();
			monstro.limparStatus();
			monstro.zerarEscudoAtual();
		}
		System.out.println(">>Inimigos inicializados.");
        System.out.println("");
	}
	
	private void inicializarActionValue(){
		BattleTurn.getUnidades().clear();
		
        for (Monsters monstro : aliados){
            if (monstro != null){
                BattleTurn.getUnidades().add(new BattleUnit(monstro, true));
            }
        }
        
        for (Monsters monstro : inimigos){
            if (monstro != null){
                BattleTurn.getUnidades().add(new BattleUnit(monstro, false));
            }
        }
        
        BattleTurn.ordenarActionValue(BattleTurn.getUnidades());
        
        BattleTurn.avançarTurno();
    }
	
	// ==================== DESENHO ====================
	
	protected void desenhaBatalha(){
		Grapchics.limpaTela();
		
		linhaInicial = 0;
		linhaAtual = linhaInicial;
		
		desenhaBarraActionValue();
		if (!aguardandoAliado){
			if (selecionarAlvo){
				Grapchics.desenhaTTF("Q: Voltar", 0, linhaAtual++, Grapchics.PRETO_CLARO);
			}
			if (BattleTurn.isTurnoJogador()){
				Grapchics.desenhaTTF("E: Detalhes", 0, linhaAtual++, Grapchics.PRETO_CLARO);
			}
			if (especialAtivo && BattleTurn.isTurnoJogador() && !selecionarAlvo){
				Grapchics.desenhaTTF("E+Q: Ativar especial", 0, linhaAtual++, Grapchics.PRETO_CLARO);
			}
			if (BattleTurn.isTurnoJogador()){
				Grapchics.desenhaTTF("Shift: Recarregar estamina", 0, linhaAtual++, Grapchics.PRETO_CLARO);
			}
		}
		
		linhaAtual += 5;
		desenhaAliados();
		desenhaInimigos();
		
		if(selecionarAlvo && !posiçõesInimigosX.isEmpty()){
			desenhaSetaBatalha();
		}
		
		linhaAtual += 10;
		if (!aguardandoAliado){
			if (BattleTurn.isAguardandoTurno() && BattleTurn.isTurnoJogador()){
				if (!selecionarAlvo){
					desenhaBatalhaComandos();
				}else{
					desenhaComandoDetalhe();
				}
			}
		}
		desenhaLogBatalha();
		
		Grapchics.atualizarTela();
	}
	
	private void desenhaLogBatalha(){		
		if (aguardandoInimigo && mensagemTurnoInimigo != null){
			Grapchics.desenhaTela("____________________", 0,linhaAtual++, Grapchics.PRETO_CLARO);
			Grapchics.desenhaTela((char)6, 0, linhaAtual, Grapchics.VERMELHO_CLARO);
			desenhaHabilidadeUsadaInimigo();
			if (danoTurnoInimigo != "" && danoTurnoInimigo != null) Grapchics.desenhaTTF(danoTurnoInimigo, 0, linhaAtual++, Grapchics.BRANCO_CLARO);
			resultadoAção = BattleAI.getResultadoAção();
			
			if (resultadoAção != null && !resultadoAção.isAcerto()) Grapchics.desenhaTTF(">>Errou!", 0, linhaAtual++, Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTTF("[ENTER]  ", 0, linhaAtual++, Grapchics.AMARELO_CLARO);
			Grapchics.desenhaTela("____________________", 0, linhaAtual++, Grapchics.PRETO_CLARO);
		}
		
		if (aguardandoAliado && mensagemTurnoAliado != null){
			Grapchics.desenhaTela("____________________", 0, linhaAtual++, Grapchics.PRETO_CLARO);
			Grapchics.desenhaTela((char)4, 0, linhaAtual, Grapchics.AZUL_CLARO);
			desenhaHabilidadeUsadaAliado();
			if (danoTurnoAliado != "" && danoTurnoAliado != null) Grapchics.desenhaTTF(danoTurnoAliado, 0, linhaAtual++, Grapchics.BRANCO_CLARO);
			if (resultadoAção != null && !resultadoAção.isAcerto()) Grapchics.desenhaTTF(">>Errou!", 0, linhaAtual++, Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTTF("[ENTER]  ", 0, linhaAtual++,  Grapchics.AMARELO_CLARO);
			Grapchics.desenhaTela("____________________", 0, linhaAtual++, Grapchics.PRETO_CLARO);
		}
	}
	
	private void desenhaHabilidadeUsadaInimigo(){
		if (mensagemTurnoInimigo == null){
			return;
		}
		
		if (skillUsada == null){
			Grapchics.desenhaTTF(mensagemTurnoInimigo, 1, linhaAtual++, Grapchics.BRANCO_CLARO);
			return;
		}
	
		String mensagem = mensagemTurnoInimigo;
		int tamanhoMensagem = mensagem.length();
		String nomeHabilidade = skillUsada.getNomeHabilidade();
		int tamanhoNomeHabilidade = nomeHabilidade.length();
		Color corHabilidade = skillUsada.getCorHabilidade();
		
		Grapchics.desenhaTTF(mensagem, 1, linhaAtual++, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF(nomeHabilidade, 0, linhaAtual, corHabilidade);
		Grapchics.desenhaTela((char)19, tamanhoNomeHabilidade, linhaAtual++, Grapchics.BRANCO_CLARO);
	}
	
	private void desenhaHabilidadeUsadaAliado(){
		if (mensagemTurnoAliado == null){
			return;
		}
		
		if (skillUsada == null){
			Grapchics.desenhaTTF(mensagemTurnoAliado, 1, linhaAtual++, Grapchics.BRANCO_CLARO);
			return;
		}
	
		String mensagem = mensagemTurnoAliado;
		int tamanhoMensagem = mensagem.length();
		String nomeHabilidade = skillUsada.getNomeHabilidade();
		int tamanhoNomeHabilidade = nomeHabilidade.length();
		Color corHabilidade = skillUsada.getCorHabilidade();
		
		Grapchics.desenhaTTF(mensagem, 1, linhaAtual++, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF(nomeHabilidade, 0, linhaAtual, corHabilidade);
		Grapchics.desenhaTela((char)19, tamanhoNomeHabilidade, linhaAtual++, Grapchics.BRANCO_CLARO);
	}
	
	private void desenhaAliados(){
		posiçõesAliadosX.clear();
		posiçõesAliadosY.clear();
		
		int jogadorMonstrosX = 3;
		
		if (aliados[0] != null){
			desenhaMonstroBatalha(aliados[0], jogadorMonstrosX-2, linhaAtual-4);
			posiçõesAliadosX.add(jogadorMonstrosX-2);
			posiçõesAliadosY.add(linhaAtual-4);
		}
		if (maxAliados >= 2){
			if (aliados[1] != null){
			desenhaMonstroBatalha(aliados[1], jogadorMonstrosX, linhaAtual+1);
			posiçõesAliadosX.add(jogadorMonstrosX);
			posiçõesAliadosY.add(linhaAtual+1);
			}
		}
		if (maxAliados >= 3){
			if (aliados[2] != null){
			desenhaMonstroBatalha(aliados[2], jogadorMonstrosX-2, linhaAtual+6);
			posiçõesAliadosX.add(jogadorMonstrosX-2);
			posiçõesAliadosY.add(linhaAtual+6);
			}
		}
	}
	
	private void desenhaInimigos(){
		posiçõesInimigosX.clear();
		posiçõesInimigosY.clear();
		
		if(inimigos.get(0) != null){
			desenhaMonstroBatalha(inimigos.get(0), 24, linhaAtual-4);
			posiçõesInimigosX.add(24);
			posiçõesInimigosY.add(linhaAtual-4);
		}
		
		if (maxInimigos >= 2){
			if(inimigos.get(1) != null){
				desenhaMonstroBatalha(inimigos.get(1), 22, linhaAtual+1);
				posiçõesInimigosX.add(22);
				posiçõesInimigosY.add(linhaAtual+1);
			}
		}
		
		if (maxInimigos >= 3){
			if(inimigos.get(2) != null){
				desenhaMonstroBatalha(inimigos.get(2), 24, linhaAtual+6);
				posiçõesInimigosX.add(24);
				posiçõesInimigosY.add(linhaAtual+6);
			}
		}
	}
	
	private void desenhaBarraActionValue(){
		int count = 0;
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
        for (BattleUnit unidade : BattleTurn.getUnidades()){
            if (count >= 6) break;
            Monsters monstro = unidade.getMonstro();
            if (monstro == null) continue;
            
            String nome = monstro.getNomeMonstro();
            int av = unidade.getActionValue();
			boolean ehAliado = unidade.isAliado();
			Monsters monstroUnidade = BattleTurn.getUnidadeAtual().getMonstro();
			boolean unidadeAtual = (monstroUnidade == monstro);

            String texto = nome + " (AV: " + av + ")";
            if (unidade == BattleTurn.getUnidadeAtual()){
				texto += (char)17;
            }
            
			if (ehAliado && unidadeAtual){
				Grapchics.desenhaTela((char)4+texto, 0, linhaAtual, Grapchics.AZUL_CLARO);
				Grapchics.desenhaTTF(texto, 1, linhaAtual++, Grapchics.BRANCO_CLARO);
			}else if (ehAliado && !unidadeAtual){
				Grapchics.desenhaTela((char)4+texto, 0, linhaAtual, Grapchics.AZUL_CLARO);
				Grapchics.desenhaTTF(texto, 1, linhaAtual++, Grapchics.PRETO_CLARO);
			}else if (!ehAliado && !unidadeAtual){
				Grapchics.desenhaTela((char)6+texto, 0, linhaAtual, Grapchics.VERMELHO_CLARO);
				Grapchics.desenhaTTF(texto, 1, linhaAtual++, Grapchics.PRETO_CLARO);
			}else if (!ehAliado && unidadeAtual){
				Grapchics.desenhaTela((char)6, 0, linhaAtual, Grapchics.VERMELHO_CLARO);
				Grapchics.desenhaTTF(texto, 1, linhaAtual++, Grapchics.BRANCO_CLARO);
			}
			
			count++;
        }
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
    }
	
	private void desenhaMonstroBatalha(Monsters monstro, int x, int y){
		if (monstro == null) return;
		
		Monsters monstroUnidade = BattleTurn.getUnidadeAtual().getMonstro();
		boolean unidadeAtual = (monstroUnidade == monstro);
		
		BattleUnit unidadeAlvo = BattleTurn.getUnidadePorMonstro(monstro);
		
		if (unidadeAtual){
			Grapchics.desenhaTTF(monstro.getNomeMonstro(), x, y, Grapchics.AMARELO_CLARO);
			Grapchics.desenhaTela("PV: "+monstro.getVidaAtualCombate() + "/" + monstro.getVidaAtualCombateMaxima(), 
			x, y+1, Grapchics.BRANCO_CLARO);
			
			if (monstro.getEscudoAtual() > 0){
				Grapchics.desenhaTela("Escudo: "+monstro.getEscudoAtual(),x, y+2, Grapchics.BRANCO_CLARO);
				Grapchics.desenhaTela("STA: "+monstro.getEstaminaAtualCombate() + "/" + monstro.getEstaminaAtual(), 
				x, y+3, Grapchics.BRANCO_CLARO);
			}else{
				Grapchics.desenhaTela("STA: "+monstro.getEstaminaAtualCombate() + "/" + monstro.getEstaminaAtual(), 
				x, y+2, Grapchics.BRANCO_CLARO);
			}
		}else{
			if (unidadeAlvo != null && unidadeAlvo.isAlvo()){
				Grapchics.desenhaTela((char) 25, x+4, y - 1, Grapchics.AMARELO_CLARO);
				
				Grapchics.desenhaTela("PV: "+monstro.getVidaAtualCombate() + "/" + monstro.getVidaAtualCombateMaxima(), 
				x, y+1, Grapchics.BRANCO_CLARO);
				
				if (monstro.getEscudoAtual() > 0){
					Grapchics.desenhaTela("Escudo: "+monstro.getEscudoAtual(),x, y+2, Grapchics.BRANCO_CLARO);
					Grapchics.desenhaTela("STA: "+monstro.getEstaminaAtualCombate() + "/" + monstro.getEstaminaAtual(), 
					x, y+3, Grapchics.BRANCO_CLARO);
				}else{
					Grapchics.desenhaTela("STA: "+monstro.getEstaminaAtualCombate() + "/" + monstro.getEstaminaAtual(), 
					x, y+2, Grapchics.BRANCO_CLARO);
				}
			}else{
				Grapchics.desenhaTela("PV: "+monstro.getVidaAtualCombate() + "/" + monstro.getVidaAtualCombateMaxima(), 
				x, y+1, Grapchics.PRETO_CLARO);
				
				if (monstro.getEscudoAtual() > 0){
					Grapchics.desenhaTela("Escudo: "+monstro.getEscudoAtual(),x, y+2, Grapchics.PRETO_CLARO);
					Grapchics.desenhaTela("STA: "+monstro.getEstaminaAtualCombate() + "/" + monstro.getEstaminaAtual(), 
					x, y+3, Grapchics.PRETO_CLARO);
				}else{
					Grapchics.desenhaTela("STA: "+monstro.getEstaminaAtualCombate() + "/" + monstro.getEstaminaAtual(), 
					x, y+2, Grapchics.PRETO_CLARO);
				}
			}
			Grapchics.desenhaTTF(monstro.getNomeMonstro(), x, y, Grapchics.PRETO_CLARO);
		}
	}
	
	private void desenhaSetaBatalha(){    
		if (skillSelecionada == null) return;
		
		Skills.TipoAlvo tipoAlvo = skillSelecionada.getAlvoHabilidadeTipo();
		int x = 0;
		int y = 0;
		
		monstrosAlvos.clear();
		
		switch(tipoAlvo){
			case INIMIGO_UNICO:
			if (Input.getCursorX() < 0) Input.setCursorX(maxInimigos - 1);
			if (Input.getCursorX() >= maxInimigos) Input.setCursorX(0);
			
			x = posiçõesInimigosX.get(Input.getCursorX());
			y = posiçõesInimigosY.get(Input.getCursorX());
			
			monstroSelecionado = tropa.getMonstros().get(Input.getCursorX());
			if (monstroSelecionado == null) return;
			
			monstrosAlvos.add(monstroSelecionado);
			
			Grapchics.desenhaTela((char) 25, x+4, y - 1, Grapchics.AMARELO_CLARO);
			Grapchics.desenhaTTF(monstroSelecionado.getNomeMonstro(), x, y, 
			Grapchics.AMARELO_CLARO);
			break;
			
			case ALIADO_UNICO:
			if (Input.getCursorX() < 0) Input.setCursorX(posiçõesAliadosX.size() - 1);
            if (Input.getCursorX() >= posiçõesAliadosX.size()) Input.setCursorX(0);
			
			x = posiçõesAliadosX.get(Input.getCursorX());
            y = posiçõesAliadosY.get(Input.getCursorX());
			
			monstroSelecionado = obterAliadoPorIndiceValido(Input.getCursorX());
			if (monstroSelecionado == null) return;
			
			monstrosAlvos.add(monstroSelecionado);
			
			Grapchics.desenhaTela((char) 25, x+4, y - 1, Grapchics.AMARELO_CLARO);
			Grapchics.desenhaTTF(monstroSelecionado.getNomeMonstro(), x, y, 
			Grapchics.AMARELO_CLARO);
			break;
			
			case USUARIO:
            monstroSelecionado = BattleTurn.getUnidadeJogadorAtual().getMonstro();
			if (monstroSelecionado == null) return;
			
			monstrosAlvos.add(monstroSelecionado);
			
			int indiceAliado = -1;
			int contadorValidos = 0;
			for (int i = 0; i < maxAliados; i++){
				if (aliados[i] != null){
					if (aliados[i] == monstroSelecionado){
						indiceAliado = contadorValidos;
						break;
					}
					contadorValidos++;
				}
			}
			
			if (indiceAliado != -1 && indiceAliado < posiçõesAliadosX.size()){
				x = posiçõesAliadosX.get(indiceAliado);
				y = posiçõesAliadosY.get(indiceAliado);
				
				Grapchics.desenhaTela((char) 25, x + 4, y - 1, Grapchics.AMARELO_CLARO);
			}
            break;
			
			case INIMIGO_AREA:
            for (int i = 0; i < posiçõesInimigosX.size(); i++){
                Grapchics.desenhaTela((char) 25, posiçõesInimigosX.get(i)+4, 
				posiçõesInimigosY.get(i)-1, Grapchics.AMARELO_CLARO);
            }
			
			for (Monsters m : inimigos){
                if (m != null && m.getVidaAtualCombate() > 0) monstrosAlvos.add(m);
            }
            break;
			
			case ALIADO_AREA:
            for (int i = 0; i < posiçõesAliadosX.size(); i++){
                Grapchics.desenhaTela((char) 25, posiçõesAliadosX.get(i)+4, 
				posiçõesAliadosY.get(i)-1, Grapchics.AMARELO_CLARO);
            }
			
			for (Monsters m : aliados){
                if (m != null && m.getVidaAtualCombate() > 0) monstrosAlvos.add(m);
            }
            break;
			
			case CAMPO:
            for (int i = 0; i < posiçõesInimigosX.size(); i++){
				Grapchics.desenhaTela((char) 25, posiçõesInimigosX.get(i)+4, 
				posiçõesInimigosY.get(i)-1, Grapchics.AMARELO_CLARO);
			}				
            for (int i = 0; i < posiçõesAliadosX.size(); i++){
				Grapchics.desenhaTela((char) 25, posiçõesAliadosX.get(i)+4, 
				posiçõesAliadosY.get(i)-1, Grapchics.AMARELO_CLARO);
			}
			
			for (Monsters m : inimigos){
                if (m != null && m.getVidaAtualCombate() > 0) monstrosAlvos.add(m);
            }
            for (Monsters m : aliados){
                if (m != null && m.getVidaAtualCombate() > 0) monstrosAlvos.add(m);
			}
            break;
		}
	}
	
	private void desenhaBatalhaComandos(){
		Monsters monstroAtual = BattleTurn.getUnidadeJogadorAtual().getMonstro();
		if (monstroAtual == null) return;
		int tamanhoSkills = monstroAtual.getQuantidadeMaxSlotsHabilidade();
		
		if (maxAliados >= 3) linhaAtual++;
		skillEspecial = monstroAtual.getHabilidadeEspecial();
		desenhaMonstroEspecial(monstroAtual, skillEspecial);
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		for (int i = 0; i <= tamanhoSkills-1; i++){
			Skills skillCarregada = monstroAtual.getHabilidadeAtiva(i);
			
			if (skillCarregada != null){
				if (Input.getCursorY() == i){					
					if (skillCarregada.isRecarga() && skillCarregada.getRecargaAtual() > 0){
						Grapchics.desenhaTTF((i+1)+": ",0,linhaAtual, Grapchics.PRETO_CLARO);
						Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade()+" - Recarga:"+skillCarregada.getRecargaAtual(),4,linhaAtual++,
						Grapchics.AMARELO_CLARO);
						
						skillSelecionada = null;
					}else{
						Grapchics.desenhaTTF((i+1)+": ",0,linhaAtual, Grapchics.BRANCO_CLARO);
						Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade(),4,linhaAtual++,Grapchics.AMARELO_CLARO);
						
						skillSelecionada = skillCarregada;
					}						
				}else{
					if (skillCarregada.isRecarga() && skillCarregada.getRecargaAtual() > 0){
						Grapchics.desenhaTTF((i+1)+": ",0,linhaAtual, Grapchics.PRETO_CLARO);
						Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade()+" - Recarga:"+skillCarregada.getRecargaAtual(),3,linhaAtual++,
						Grapchics.PRETO_CLARO);
						
					}else{
						Grapchics.desenhaTTF((i+1)+": ",0,linhaAtual, Grapchics.BRANCO_CLARO);
						Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade(),3,linhaAtual++,skillCarregada.getCorHabilidade());
					}
				}
			}else{
				if (Input.getCursorY() == i){
					Grapchics.desenhaTTF("[VAZIO]",0,linhaAtual++,
					Grapchics.AMARELO_CLARO);
					skillSelecionada = null;
				}else{
					Grapchics.desenhaTTF("[VAZIO]",0,linhaAtual++,Grapchics.PRETO_CLARO);
				}
			}
		}
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		
		if (Input.getCursorY() > tamanhoSkills-1){
			Input.setCursorY(0);
		}else if (Input.getCursorY() < 0){
			Input.setCursorY(tamanhoSkills-1);
		}
	}
	
	private void desenhaMonstroEspecial(Monsters monstro, Skills habilidade){
		if (monstro == null || habilidade == null) return;
		
		int barraEspecialAtual = monstro.getBarraEspecialAtual();
		int barraEspecialMaxima = monstro.getBarraEspecialMaximo();
		boolean especialDisponivel = monstro.isEspecialCarregado();
		
		if (especialDisponivel){
			Grapchics.desenhaTTF(">>"+habilidade.getNomeHabilidade()+"<<",0,linhaAtual++, habilidade.getCorHabilidade());
			skillEspecial = habilidade;
			especialAtivo = true;
		}else{
			String nomeEspecial = habilidade.getNomeHabilidade()+" "+barraEspecialAtual;
			int tamanhoNomeEspecial = nomeEspecial.length();
			
			Grapchics.desenhaTTF(nomeEspecial,0,linhaAtual, Grapchics.PRETO_CLARO);
			Grapchics.desenhaTela((char)47, tamanhoNomeEspecial, linhaAtual, Grapchics.PRETO_CLARO);
			Grapchics.desenhaTTF(""+barraEspecialMaxima,tamanhoNomeEspecial+1,linhaAtual++, Grapchics.PRETO_CLARO);
			skillEspecial = null;
		}
	}
	
	private void desenhaComandoDetalhe(){
		if (skillSelecionada == null) return;
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF(">> ",0,linhaAtual, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF(skillSelecionada.getNomeHabilidade(),3,linhaAtual++, skillSelecionada.getCorHabilidade());
		
		linhaAtual = SkillDescription.infoHabilidade(skillSelecionada, linhaAtual, true);
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
	}
	
	protected void desenhaDetalhes(){
		Grapchics.limpaTela();
		
		linhaInicial = 0;
		linhaAtual = linhaInicial;
		
		if (detalheAtual == TipoDetalhe.ALIADO.getValor()){
			Grapchics.desenhaCentroTTF("Detalhes - Aliados",linhaAtual++, Grapchics.BRANCO_CLARO);
		}else if (detalheAtual == TipoDetalhe.INIMIGO.getValor()){
			Grapchics.desenhaCentroTTF("Detalhes - Inimigos",linhaAtual++, Grapchics.BRANCO_CLARO);
		}
		
		Grapchics.desenhaTTF("E: Voltar",0,linhaAtual++, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("Alternar página: ",0,linhaAtual, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTela((char)27+"/"+(char)26,17,linhaAtual++, Grapchics.PRETO_CLARO);
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		linhaInicial = linhaAtual;
		
		if (detalheAtual == TipoDetalhe.ALIADO.getValor()){
			desenhaDetalhesAliados();
		}else if (detalheAtual == TipoDetalhe.INIMIGO.getValor()){
			desenhaDetalhesInimigos();
		}
		
		linhaMáxima = linhaAtual-1;
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		linhaAtual++;
		
		desenhaMonstroDetalhes();
		
		if (Input.getCursorY() == 0){
			Input.setCursorY(linhaInicial);
		}else if (Input.getCursorY() > linhaMáxima){
			Input.setCursorY(linhaInicial);
		}else if (Input.getCursorY() < linhaInicial){
			Input.setCursorY(linhaMáxima);
		}
		
		Grapchics.atualizarTela();
	}
	
	private void desenhaDetalhesAliados(){
		if (aliados[0] != null){
			desenhaMonstroNome(aliados[0]);
		}
		if (aliados[1] != null){
			desenhaMonstroNome(aliados[1]);
		}
		if (aliados[2] != null){
			desenhaMonstroNome(aliados[2]);
		}
	}
	
	private void desenhaDetalhesInimigos(){
		for (Monsters monstro : inimigos){
			desenhaMonstroNome(monstro);
		}
	}
	
	private void desenhaMonstroNome(Monsters monstro){
		if (monstro == null) return;
		
		String texto = monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual()+" ";		
		int tamanhoTexto = texto.length();
		int tamanhoElemento = monstro.getElementosAtuais().length();
		
		if (Input.getCursorY() == linhaAtual){
			Grapchics.desenhaTTF(texto,0,linhaAtual, Grapchics.AMARELO_CLARO);
			monstroVisualizado = monstro;
		}else{
			Grapchics.desenhaTTF(texto,0,linhaAtual, Grapchics.BRANCO_CLARO);
		}
		
		Grapchics.desenhaTTF("(",tamanhoTexto,linhaAtual, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF(monstro.getElementosAtuais(),tamanhoTexto+1,linhaAtual, 
		monstro.getCorDoElemento(monstro.getElementosAtuais()));
		Grapchics.desenhaTTF(")",tamanhoTexto+tamanhoElemento+1,linhaAtual++, Grapchics.BRANCO_CLARO);
	}
	
	private void desenhaMonstroDetalhes(){
		if (monstroVisualizado == null) return;
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		String PV = "PV: "+monstroVisualizado.getVidaAtualCombate();
		int tamanhoPV = PV.length();
		
		Grapchics.desenhaTTF(PV,0,linhaAtual, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela((char)47,tamanhoPV,linhaAtual, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF(""+monstroVisualizado.getVidaAtualCombateMaxima(),tamanhoPV+1,linhaAtual++, Grapchics.BRANCO_CLARO);
		
		if (monstroVisualizado.getEscudoAtual() > 0){
			Grapchics.desenhaTTF("Escudo: "+monstroVisualizado.getEscudoAtual(),0,linhaAtual++, Grapchics.BRANCO_CLARO);
		}
		
		String STA = "Estamina: "+monstroVisualizado.getEstaminaAtualCombate();
		int tamanhoSTA = STA.length();
		
		Grapchics.desenhaTTF(STA,0,linhaAtual, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela((char)47,tamanhoSTA,linhaAtual, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF(""+monstroVisualizado.getEstaminaAtual(),tamanhoSTA+1,linhaAtual++, Grapchics.BRANCO_CLARO);
		
		if (monstroVisualizado.getForcaAtualCombate() == monstroVisualizado.getForcaAtual()){
			Grapchics.desenhaTTF("Força: "+monstroVisualizado.getForcaAtualCombate(),0,linhaAtual++, Grapchics.BRANCO_CLARO);
			
		}else if (monstroVisualizado.getForcaAtualCombate() > monstroVisualizado.getForcaAtual()){
			Grapchics.desenhaTTF("Força: ",0,linhaAtual, Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTTF(""+monstroVisualizado.getForcaAtualCombate(),7,linhaAtual++, Grapchics.VERDE_CLARO);
			
		}else if (monstroVisualizado.getForcaAtualCombate() < monstroVisualizado.getForcaAtual()){
			Grapchics.desenhaTTF("Força: ",0,linhaAtual, Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTTF(""+monstroVisualizado.getForcaAtualCombate(),7,linhaAtual++, Grapchics.VERMELHO_CLARO);
		}
		
		if (monstroVisualizado.getSpeedAtualCombate() == monstroVisualizado.getSpeedAtual()){
			Grapchics.desenhaTTF("Velocidade: "+monstroVisualizado.getSpeedAtualCombate(),0,linhaAtual++, Grapchics.BRANCO_CLARO);
			
		}else if (monstroVisualizado.getSpeedAtualCombate() > monstroVisualizado.getSpeedAtual()){
			Grapchics.desenhaTTF("Velocidade: ",0,linhaAtual, Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTTF(""+monstroVisualizado.getSpeedAtualCombate(),12,linhaAtual++, Grapchics.VERDE_CLARO);
			
		}else if (monstroVisualizado.getSpeedAtualCombate() < monstroVisualizado.getSpeedAtual()){
			Grapchics.desenhaTTF("Velocidade: ",0,linhaAtual, Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTTF(""+monstroVisualizado.getSpeedAtualCombate(),12,linhaAtual++, Grapchics.VERMELHO_CLARO);
			
		}
		
		Grapchics.desenhaTTF("Traço: "+monstroVisualizado.getNomesTraços(),0,linhaAtual++, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		
		for (int i = 0; i < monstroVisualizado.getQuantidadeMaxSlotsHabilidade(); i++){
			Skills skillCarregada = monstroVisualizado.getHabilidadeAtiva(i);
			if (skillCarregada != null){
				Grapchics.desenhaTTF((i+1)+": "+skillCarregada.getNomeHabilidade(),0,linhaAtual,Grapchics.BRANCO_CLARO);
				Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade(),3,linhaAtual++,skillCarregada.getCorHabilidade());
			}else{
				Grapchics.desenhaTTF("[VAZIO]",0,linhaAtual++,Grapchics.PRETO_CLARO);
			}
		}
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		
		List<StatusBase> listaStatus = monstroVisualizado.getStatusAtuais();
		if (listaStatus.isEmpty()){
			Grapchics.desenhaTTF("Status: Nenhum", 0, linhaAtual++, Grapchics.PRETO_CLARO);
		}else{
			String indicador = "";
			int tamanhoIndicador = 0;
			for (int i = 0; i < listaStatus.size(); i++){
				StringBuilder statusTexto = new StringBuilder("");
				StatusBase status = listaStatus.get(i);
				
				if (status != null && statusTexto != null && status.isAtivo()){
					statusTexto.append(status.getNome())
					.append(" (").append(status.getDuraçãoAtual()).append("t)");
				}
				
				if (status.isPositivo()){
					indicador = "[+] ";
					Grapchics.desenhaTela(indicador, 0, linhaAtual, Grapchics.VERDE_CLARO);
				}else if (!status.isPositivo()){
					indicador = "[-] ";
					Grapchics.desenhaTela(indicador, 0, linhaAtual, Grapchics.VERMELHO_CLARO);
				}
				tamanhoIndicador = indicador.length();
				
				Grapchics.desenhaTTF(statusTexto.toString(), tamanhoIndicador, linhaAtual++, Grapchics.BRANCO_CLARO);
			}
		}
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
	}
	
	// ==================== AÇÕES DO JOGADOR ====================
	
	protected void selecionarComandoBatalha(){
		if (aguardandoInimigo){
			confirmarMensagemInimigo();
			Input.resetarCursor();
			return;
		}
		
		if (aguardandoAliado){
			confirmarMensagemAliado();
			Input.resetarCursor();
			return;
		}
		
        if (!BattleTurn.isAguardandoTurno() || !BattleTurn.isTurnoJogador()) return;
        if (skillSelecionada == null) return;
        if (BattleTurn.getUnidadeJogadorAtual() == null) return;
        
        if (!selecionarAlvo){
            selecionarAlvo = true;
            return;
        }
        
        if (selecionarAlvo && !monstrosAlvos.isEmpty() && skillSelecionada != null){
            Monsters usuario = BattleTurn.getUnidadeJogadorAtual().getMonstro();
			
            if (BattleAction.verificarCustoHabilidade(usuario, skillSelecionada)){
				for (Monsters alvo : monstrosAlvos){
					BattleUnit unidadeAlvo = BattleTurn.getUnidadePorMonstro(alvo);
					if (unidadeAlvo != null) unidadeAlvo.setAlvo(true);
				}
				
				resultadoAção = BattleAction.executarHabilidade(usuario, monstrosAlvos, skillSelecionada);
				
                int danoRealizado = resultadoAção.getDanoRealizado();
				String nomeMonstro = usuario.getNomeMonstro() + " usou ";
				Skills ultimaSkill = skillSelecionada;
				
				String dano = null;
				if (danoRealizado > 0) dano = ">>Causou " + danoRealizado + " de dano.";
				
				selecionarAlvo = false;
				Battle.exibirMensagemAliado(nomeMonstro, ultimaSkill, dano);
            }
        }
    }
	
	protected void recarregarEnergiaUsuário(){		
        if (!BattleTurn.isAguardandoTurno() || !BattleTurn.isTurnoJogador()) return;
        if (BattleTurn.getUnidadeJogadorAtual() == null) return;
        Monsters usuario = BattleTurn.getUnidadeJogadorAtual().getMonstro();
        
        BattleAction.recarregarEnergia(usuario);
		if (selecionarAlvo) selecionarAlvo = false;
		skillUsada = null;
		
		String frase = usuario.getNomeMonstro()+" recarrega.";
		Battle.exibirMensagemAliado(frase, null, null);
    }
	
	protected void ativarEspecial(){
		if (aguardandoAliado){
			confirmarMensagemAliado();
			return;
		}
		
		if (!BattleTurn.isAguardandoTurno() || !BattleTurn.isTurnoJogador()) return;
		if (skillEspecial == null || selecionarAlvo) return;
		if (BattleTurn.getUnidadeJogadorAtual() == null) return;
		
		skillSelecionada = skillEspecial;
		
		if (!selecionarAlvo && especialAtivo){
            selecionarAlvo = true;
        }
	}
	
	// ==================== MÉTODOS AUXILIARES ====================
	
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
	
	private boolean verificarFimBatalha(){
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
			return true;
		}
		
		if (todosAliadosDerrotados()){
            Battle.setSubEstadoAtual(Battle.SubEstadosBatalha.DERROTA);
            return true;
        }
		
		return false;
	}
	
	private boolean todosInimigosDerrotados(){
		for (Monsters monstro : inimigos){
			if (monstro != null && monstro.getVidaAtualCombate() > 0){
				return false;
			}
		}
		return true;
	}
	
	private boolean todosAliadosDerrotados(){
        for (Monsters monstro : aliados){
            if (monstro != null && monstro.getVidaAtualCombate() > 0){
                return false;
            }
        }
        return true;
    }
	
	private Monsters obterAliadoPorIndiceValido(int indiceCursor){
		if (indiceCursor > maxAliados-1 || indiceCursor < 0) return null;
		
		int contadorValidos = 0;
		for (int i = 0; i <= maxAliados-1; i++){
			if (aliados[i] != null){
				if (contadorValidos == indiceCursor){
					return aliados[i];
				}
				contadorValidos++;
			}
		}
		return null;
	}
	
	protected void alternarDetalhe(boolean avancar){
		if (avancar){
			if (detalheAtual == TipoDetalhe.ALIADO.getValor()){
				detalheAtual = TipoDetalhe.INIMIGO.getValor();
			}else{
				detalheAtual = TipoDetalhe.ALIADO.getValor();
			}
		}else{
			if (detalheAtual == TipoDetalhe.ALIADO.getValor()){
				detalheAtual = TipoDetalhe.INIMIGO.getValor();
			}else{
				detalheAtual = TipoDetalhe.ALIADO.getValor();
			}
		}
	}
	
	protected void setMensagemInimigo(String mensagem, Skills habilidadeUsada, String dano){
		this.mensagemTurnoInimigo = mensagem;
		if (dano != null) this.danoTurnoInimigo = dano;
		if (habilidadeUsada != null) this.skillUsada = habilidadeUsada;
		this.aguardandoInimigo = true;
	}

	protected void setMensagemAliado(String mensagem, Skills habilidadeUsada, String dano){
		this.mensagemTurnoAliado = mensagem;
		if (dano != null) this.danoTurnoAliado = dano;
		if (habilidadeUsada != null) this.skillUsada = habilidadeUsada;
		this.aguardandoAliado = true;
	}
	
	protected boolean isAguardandoConfirmação(){
		return aguardandoInimigo;
	}
	
	protected void confirmarMensagemInimigo(){
		this.mensagemTurnoInimigo = null;
		this.danoTurnoInimigo = null;
		this.aguardandoInimigo = false;
		this.resultadoAção = null;
		
		BattleAI.setUltimaSkill(null);
		BattleAI.setResultadoAção(null);
		
		BattleTurn.finalizarTurno(); 
	}
	
	protected void confirmarMensagemAliado(){
		this.mensagemTurnoAliado = null;
		this.danoTurnoAliado = null;
		this.aguardandoAliado = false;
		this.resultadoAção = null;
		
        skillSelecionada = null;
		skillUsada = null;
		BattleTurn.setAguardandoTurno(false);
		monstrosAlvos.clear();
		
		BattleTurn.finalizarTurno();
	}
	
	protected void processarTurno(){
		verificarMonstros();
		
		if (!BattleTurn.isAguardandoTurno() && !BattleTurn.isTurnoJogador()){
			if (!isAguardandoConfirmação()){
				BattleAI.turnoInimigo();
			}
		}
		verificarFimBatalha();
	}
	
	// ==================== OUTROS ====================
	
	protected void setSelecionarAlvo(boolean selecionarAlvo){
		this.selecionarAlvo = selecionarAlvo;
	}
	
	protected void setDetalheAtual(int detalheAtual){
		if (detalheAtual < 0) detalheAtual = 0;
		this.detalheAtual = detalheAtual;
	}
	
	protected boolean isAguardandoInimigo(){
		return aguardandoInimigo;
	}

	protected boolean isAguardandoAliado(){
		return aguardandoAliado;
	}
		
	//===
}