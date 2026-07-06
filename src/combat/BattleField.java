package combat;

import bestiary.Monsters;
import bestiary.Skills;
import bestiary.Troop;

import main.Player;

import util.Grapchics;
import util.Utils;

import java.util.ArrayList;
import java.util.List;

public final class BattleField {
	// ==================== ATRIBUTOS ====================
	
	private Monsters[] aliados;
	private int maxAliados;
	private List<Integer> posiçõesAliadosX;
	private List<Integer> posiçõesAliadosY; 
	
	private Troop tropa;
	private Monsters monstroSelecionado;
	private List<Monsters> monstrosAlvos;
	private Skills skillSelecionada;
	
	private List<Monsters> inimigos;
	private int maxInimigos;
	private List<Integer> posiçõesInimigosX;
	private List<Integer> posiçõesInimigosY;
	
	private boolean selecionarAlvo;
	private boolean aguardandoInimigo = false;
	private boolean aguardandoAliado = false; 
	
	private String mensagemTurnoInimigo = null;
	private String mensagemTurnoAliado = null;
	
	private int linhaInicial, linhaAtual;
	
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
			monstro.setSpeedAtualCombate(monstro.getSpeedAtual());
			monstro.setEstaminaAtualCombate(monstro.getEstaminaAtual());
			monstro.desativarRecargas();
		}
		System.out.println(">>Aliados inicializados.");
        System.out.println("");
		
		for (Monsters monstro : inimigos){
			if(monstro == null) continue;
			monstro.setForcaAtualCombate(monstro.getForcaAtual());
			monstro.setVidaAtualCombate(monstro.getVidaAtual());
			monstro.setSpeedAtualCombate(monstro.getSpeedAtual());
			monstro.setEstaminaAtualCombate(monstro.getEstaminaAtual());
			monstro.desativarRecargas();
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
				Grapchics.desenhaTela("Q: Voltar", 0, linhaAtual++, Grapchics.PRETO_CLARO);
			}
			if (BattleTurn.isTurnoJogador()){
				Grapchics.desenhaTela("Shift: Recarregar estamina", 0, linhaAtual++, Grapchics.PRETO_CLARO);
			}
		}
		
		linhaAtual += 10;
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
		processarTurno();
		
		Grapchics.atualizarTela();
	}
	
	private void desenhaLogBatalha(){
		if (aguardandoInimigo && mensagemTurnoInimigo != null){
			Grapchics.desenhaTela("____________________", 0,linhaAtual++, Grapchics.PRETO_CLARO);
			Grapchics.desenhaTela(mensagemTurnoInimigo, 0, linhaAtual++, Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTela("[ENTER]  ", 0, linhaAtual++, Grapchics.AMARELO_CLARO);
			Grapchics.desenhaTela("____________________", 0, linhaAtual++, Grapchics.PRETO_CLARO);
		}
		
		if (aguardandoAliado && mensagemTurnoAliado != null) {
			Grapchics.desenhaTela("____________________", 0, linhaAtual++, Grapchics.PRETO_CLARO);
			Grapchics.desenhaTela(mensagemTurnoAliado, 0, linhaAtual++, Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTela("[ENTER]  ", 0, linhaAtual++,  Grapchics.AMARELO_CLARO);
			Grapchics.desenhaTela("____________________", 0, linhaAtual++, Grapchics.PRETO_CLARO);
		}
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
			desenhaMonstroBatalha(aliados[1], jogadorMonstrosX, linhaAtual);
			posiçõesAliadosX.add(jogadorMonstrosX);
			posiçõesAliadosY.add(linhaAtual);
			}
		}
		if (maxAliados >= 3){
			if (aliados[2] != null){
			desenhaMonstroBatalha(aliados[2], jogadorMonstrosX-2, linhaAtual+4);
			posiçõesAliadosX.add(jogadorMonstrosX-2);
			posiçõesAliadosY.add(linhaAtual+4);
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
				Grapchics.desenhaTela((char)4+texto, 0, linhaAtual++, Grapchics.BRANCO_CLARO);
			}else if (ehAliado && !unidadeAtual){
				Grapchics.desenhaTela((char)4+texto, 0, linhaAtual++, Grapchics.PRETO_CLARO);
			}else if (!ehAliado && !unidadeAtual){
				Grapchics.desenhaTela((char)6+texto, 0, linhaAtual++, Grapchics.PRETO_CLARO);
			}else if (!ehAliado && unidadeAtual){
				Grapchics.desenhaTela((char)6+texto, 0, linhaAtual++, Grapchics.BRANCO_CLARO);
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
			Grapchics.desenhaTela(monstro.getNomeMonstro(), x, y, Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTela(monstro.getVidaAtualCombate() + "/" + monstro.getVidaAtual(), 
			x, y+1, Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTela(monstro.getEstaminaAtualCombate() + "/" + monstro.getEstaminaAtual(), 
			x, y+2, Grapchics.BRANCO_CLARO);
		}else{
			if (unidadeAlvo != null && unidadeAlvo.isAlvo()){
				Grapchics.desenhaTela(monstro.getNomeMonstro(), x, y, Grapchics.AMARELO_CLARO);
			}else Grapchics.desenhaTela(monstro.getNomeMonstro(), x, y, Grapchics.PRETO_CLARO);
			
			Grapchics.desenhaTela(monstro.getVidaAtualCombate() + "/" + monstro.getVidaAtual(), 
			x, y+1, Grapchics.PRETO_CLARO);
			Grapchics.desenhaTela(monstro.getEstaminaAtualCombate() + "/" + monstro.getEstaminaAtual(), 
			x, y+2, Grapchics.PRETO_CLARO);
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
			if (Battle.getCursorX() < 0) Battle.setCursorX(maxInimigos - 1);
			if (Battle.getCursorX() >= maxInimigos) Battle.setCursorX(0);
			
			x = posiçõesInimigosX.get(Battle.getCursorX());
			y = posiçõesInimigosY.get(Battle.getCursorX());
			
			monstroSelecionado = tropa.getMonstros().get(Battle.getCursorX());
			if (monstroSelecionado == null) return;
			
			monstrosAlvos.add(monstroSelecionado);
			
			Grapchics.desenhaTela((char) 25, x+4, y - 1, Grapchics.AMARELO_CLARO);
			Grapchics.desenhaTela(monstroSelecionado.getNomeMonstro(), x, y, 
			Grapchics.AMARELO_CLARO);
			break;
			
			case ALIADO_UNICO:
			if (Battle.getCursorX() < 0) Battle.setCursorX(posiçõesAliadosX.size() - 1);
            if (Battle.getCursorX() >= posiçõesAliadosX.size()) Battle.setCursorX(0);
			
			x = posiçõesAliadosX.get(Battle.getCursorX());
            y = posiçõesAliadosY.get(Battle.getCursorX());
			
			monstroSelecionado = obterAliadoPorIndiceValido(Battle.getCursorX());
			if (monstroSelecionado == null) return;
			
			monstrosAlvos.add(monstroSelecionado);
			
			Grapchics.desenhaTela((char) 25, x+4, y - 1, Grapchics.AMARELO_CLARO);
			Grapchics.desenhaTela(monstroSelecionado.getNomeMonstro(), x, y, 
			Grapchics.AMARELO_CLARO);
			break;
			
			case USUARIO:
            monstroSelecionado = BattleTurn.getUnidadeJogadorAtual().getMonstro();
			if (monstroSelecionado == null) return;
			
			monstrosAlvos.add(monstroSelecionado);
            
            Grapchics.desenhaTela((char) 25, x+4, y - 1, Grapchics.AMARELO_CLARO);
			Grapchics.desenhaTela(monstroSelecionado.getNomeMonstro(), x, y, 
			Grapchics.AMARELO_CLARO);
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
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		for (int i = 0; i <= tamanhoSkills-1; i++){
			Skills skillCarregada = monstroAtual.getHabilidadeAtiva(i);
			
			if (skillCarregada != null){
				if (Battle.getCursorY() == i){					
					if (skillCarregada.isRecarga()){
						Grapchics.desenhaTela((i+1)+": "+skillCarregada.getNomeHabilidade()+" - Recarga:"+skillCarregada.getRecargaAtual(),0,linhaAtual++,
						Grapchics.AMARELO_CLARO);
						
						skillSelecionada = null;
					}else{
						Grapchics.desenhaTela((i+1)+": "+skillCarregada.getNomeHabilidade(),0,linhaAtual++,
						Grapchics.AMARELO_CLARO);
						
						skillSelecionada = skillCarregada;
					}						
				}else{
					if (skillCarregada.isRecarga()){
						Grapchics.desenhaTela((i+1)+": "+skillCarregada.getNomeHabilidade()+" - Recarga:"+skillCarregada.getRecargaAtual(),0,linhaAtual++,
						Grapchics.PRETO_CLARO);
					}else{
						Grapchics.desenhaTela((i+1)+": "+skillCarregada.getNomeHabilidade(),0,linhaAtual++,Grapchics.BRANCO_CLARO);
					}
				}
			}else{
				if (Battle.getCursorY() == i){
					Grapchics.desenhaTela("[VAZIO]",0,linhaAtual++,
					Grapchics.AMARELO_CLARO);
					skillSelecionada = null;
				}else{
					Grapchics.desenhaTela("[VAZIO]",0,linhaAtual++,Grapchics.PRETO_CLARO);
				}
			}
		}
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		
		if (Battle.getCursorY() > tamanhoSkills-1){
			Battle.setCursorY(0);
		}else if (Battle.getCursorY() < 0){
			Battle.setCursorY(tamanhoSkills-1);
		}
	}
	
	private void desenhaComandoDetalhe(){
		if (skillSelecionada == null) return;
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTela(">> "+skillSelecionada.getNomeHabilidade(),0,linhaAtual++, Grapchics.BRANCO_CLARO);
		
		if (skillSelecionada.getPoderHabilidade() > 0){
			Grapchics.desenhaTela("Poder: "+skillSelecionada.getPoderHabilidade(),0,linhaAtual++, Grapchics.BRANCO_CLARO);
		}
		
		if (skillSelecionada.getPrecisaoBase() > 0){
			Grapchics.desenhaTela("Precisao: "+skillSelecionada.getPrecisaoBase(),0,linhaAtual++, Grapchics.BRANCO_CLARO);
		}
		
		if (skillSelecionada.getEnergiaHabilidade() > 0){
			Grapchics.desenhaTela("Energia: "+skillSelecionada.getEnergiaHabilidade(),0,linhaAtual++, Grapchics.BRANCO_CLARO);
		}
		
		if (skillSelecionada.getRecargaHabilidade() > 0){
			Grapchics.desenhaTela("Recarga: "+skillSelecionada.getRecargaHabilidade(),0,linhaAtual++, Grapchics.BRANCO_CLARO);
		}
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
	}
	
	protected void desenhaTelaVitória(){
		Grapchics.limpaTela();
		
		Grapchics.desenhaCentro("Vitoria",10, Grapchics.BRANCO_CLARO);
		
		Grapchics.atualizarTela();
	}
	
	protected void desenhaTelaDerrota(){
		Grapchics.limpaTela();
		
		Grapchics.desenhaCentro("Derrota",10, Grapchics.BRANCO_CLARO);
		
		Grapchics.atualizarTela();
	}
	
	// ==================== AÇÕES DO JOGADOR ====================
	
	protected void selecionarComandoBatalha(){
		if (aguardandoInimigo){
			confirmarMensagemInimigo();
			return;
		}
		
		if (aguardandoAliado){
			confirmarMensagemAliado();
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
				
                BattleAction.executarHabilidade(usuario, monstrosAlvos, skillSelecionada);
				String frase = usuario.getNomeMonstro()+" usou "+skillSelecionada.getNomeHabilidade() + (char)19;
				
				selecionarAlvo = false;
				Battle.exibirMensagemAliado(frase);
            }
        }
    }
	
	protected void recarregarEnergiaUsuário(){		
        if (!BattleTurn.isAguardandoTurno() || !BattleTurn.isTurnoJogador()) return;
        if (BattleTurn.getUnidadeJogadorAtual() == null) return;
        Monsters usuario = BattleTurn.getUnidadeJogadorAtual().getMonstro();
        
        BattleAction.recarregarEnergia(usuario);
		if (selecionarAlvo) selecionarAlvo = false;
		
		String frase = usuario.getNomeMonstro()+" recarrega.";
		Battle.exibirMensagemAliado(frase);
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
	
	protected void setMensagemInimigo(String mensagem){
		this.mensagemTurnoInimigo = (char)6+mensagem;
		this.aguardandoInimigo = true;
	}

	protected void setMensagemAliado(String mensagem){
		this.mensagemTurnoAliado = (char)4+mensagem; 
		this.aguardandoAliado = true;
	}
	
	protected boolean isAguardandoConfirmação(){
		return aguardandoInimigo;
	}
	
	protected void confirmarMensagemInimigo(){
		this.mensagemTurnoInimigo = null;
		this.aguardandoInimigo = false;
		
		BattleTurn.finalizarTurno(); 
	}
	
	protected void confirmarMensagemAliado(){
		this.mensagemTurnoAliado = null;
		this.aguardandoAliado = false;
		
        skillSelecionada = null;
		BattleTurn.setAguardandoTurno(false);
		monstrosAlvos.clear();
		
		BattleTurn.finalizarTurno();
	}
	
	private void processarTurno(){
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
	
	protected boolean isAguardandoInimigo(){
		return aguardandoInimigo;
	}

	protected boolean isAguardandoAliado(){
		return aguardandoAliado;
	}
	
	//===
}