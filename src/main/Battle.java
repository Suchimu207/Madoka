package main;

import bestiary.*;

import asciiPanel.AsciiPanel;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Map;

public final class Battle {
	private static List<Monsters> monstrosTropa;
	private static List<Skills> skillsTree, skillsDesbloqueadas;

	private static Monsters monstroCarregado, monstroMostrado;
	private static Skills skillCarregada, skillMostrada;
	private static Troop tropaCarregada;
	private static Monsters[] monstroSlotsAtivos;
	
	private static int paginaAtual, totalPaginas,  
	inicioLista, fimLista, ouro, maxSlotsAtivos, maxInimigos;
	
	private static List<Integer> posiçõesInimigosX;
	private static List<Integer> posiçõesInimigosY;
	
	private static int linhaAtual, linhaInicial, linhaMax,
	posiçãoLinhaX, posiçãoLinhaY, posiçãoLinhaSkillsAtivas, 
	posiçãoLinhaBatalha;
	
	private static boolean equipeSetada, selecionarAlvo;
	
	private Battle(){
	}
	
	protected static void carregarDadosBatalha(){
		TraitsManager.carregarTraços();
		SkillsManager.carregarHabilidades();
		MonstersManager.carregarMonstros();
		TroopManager.carregarTropas();
		
		Inventory.inicializarInventario();

		monstrosTropa = new ArrayList<Monsters>();
		monstroSlotsAtivos = new Monsters[3];
		
		skillsTree = new ArrayList<>();
		skillsDesbloqueadas = new ArrayList<>();
		posiçõesInimigosX = new ArrayList<>();
		posiçõesInimigosY = new ArrayList<>();
		
		montarEquipeInicial();
	}
	
	private static void montarEquipeInicial(){
		paginaAtual = 1;
		inicioLista = 1;
		fimLista = 1;
		ouro = 3000;
		
		Inventory.adicionarMonstroInventário(1);
	}
	
	protected static boolean setarBatalha(){
		if (tropaCarregada == null || monstroSlotsAtivos == null) return false;
		
		equipeSetada = false;
		for (Monsters monstro : monstroSlotsAtivos){
			if (monstro != null){
				equipeSetada = true;
				break;
			}
		}
		if (!equipeSetada) return false;
		
		for (Monsters monstro : tropaCarregada.getMonstros()){
			monstro.setForcaAtualCombate(monstro.getForcaAtual());
			monstro.setVidaAtualCombate(monstro.getVidaAtual());
			monstro.setSpeedAtualCombate(monstro.getSpeedAtual());
			monstro.setEstaminaAtualCombate(monstro.getEstaminaAtual());
			monstrosTropa.add(monstro);
		}
		
		maxSlotsAtivos = monstroSlotsAtivos.length;
		for (int i = 0; i <= maxSlotsAtivos-1; i++){
			Monsters monstro = monstroSlotsAtivos[i];
			if(monstro == null) continue;
			
			monstro.setForcaAtualCombate(monstro.getForcaAtual());
			monstro.setVidaAtualCombate(monstro.getVidaAtual());
			monstro.setSpeedAtualCombate(monstro.getSpeedAtual());
			monstro.setEstaminaAtualCombate(monstro.getEstaminaAtual());
		}
		
		System.out.println(">>Batalha setada com sucesso: Tropa_"+tropaCarregada.getId());
		System.out.println("");
		return true;
	}
	
	protected static void desenhaBatalha(){
		Grapchics.limpaTela();
		
		maxSlotsAtivos = monstroSlotsAtivos.length;
		maxInimigos = tropaCarregada.getMonstros().size();
		List<Monsters> inimigos = tropaCarregada.getMonstros();
		
		linhaInicial = 10;
		
		linhaAtual = linhaInicial;
		int jogadorMonstrosX = 3;
		
		if (monstroSlotsAtivos[0] != null){
			desenhaMonstroBatalha(monstroSlotsAtivos[0], jogadorMonstrosX-2, linhaAtual-4);
		}
		if (maxSlotsAtivos >= 2){
			if (monstroSlotsAtivos[1] != null){
			desenhaMonstroBatalha(monstroSlotsAtivos[1], jogadorMonstrosX, linhaAtual);
			}
		}
		if (maxSlotsAtivos >= 3){
			if (monstroSlotsAtivos[2] != null){
			desenhaMonstroBatalha(monstroSlotsAtivos[2], jogadorMonstrosX-2, linhaAtual+4);
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
		
		Grapchics.atualizarTela();
	}
	
	private static void desenhaMonstroBatalha(Monsters monstro, int x, int y){
		if (monstro == null) return;
    
		Grapchics.desenhaTela(monstro.getNomeMonstro(), x, y, AsciiPanel.brightWhite);
		
		Grapchics.desenhaTela(monstro.getVidaAtualCombate() + "/" + monstro.getVidaAtual(), 
		x, y+1, AsciiPanel.brightWhite);
		
		Grapchics.desenhaTela(monstro.getEstaminaAtualCombate() + "/" + monstro.getEstaminaAtual(), 
		x, y+2, AsciiPanel.brightWhite);
	}

	private static void desenhaBatalhaComandos(){
		if (monstroSlotsAtivos[0] == null) return;
		int tamanhoSkills = monstroSlotsAtivos[0].getQuantidadeMaxSlotsHabilidade();
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		for (int i = 0; i <= tamanhoSkills-1; i++){
			skillCarregada = monstroSlotsAtivos[0].getHabilidadeAtiva(i);
			if (skillCarregada != null){
				if (Terminal.cursorY == i){
					Grapchics.desenhaTela((i+1)+": "+skillCarregada.getNomeHabilidade(),0,linhaAtual++,
					AsciiPanel.brightYellow, AsciiPanel.brightBlack);
					skillMostrada = skillCarregada;
				}else{
					Grapchics.desenhaTela((i+1)+": "+skillCarregada.getNomeHabilidade(),0,linhaAtual++,AsciiPanel.brightWhite);
				}
			}else{
				if (Terminal.cursorY == i){
					Grapchics.desenhaTela("[VAZIO]",0,linhaAtual++,
					AsciiPanel.brightYellow, AsciiPanel.brightBlack);
					skillMostrada = null;
				}else{
					Grapchics.desenhaTela("[VAZIO]",0,linhaAtual++,AsciiPanel.brightBlack);
				}
			}
		}
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		
		if (Terminal.cursorY > tamanhoSkills-1){
			Terminal.cursorY = 0;
		}else if (Terminal.cursorY < 0){
			Terminal.cursorY = tamanhoSkills-1;
		}
	}
	
	private static void desenhaComandoDetalhe(){
		if (skillMostrada == null) return;
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		Grapchics.desenhaTela(">>"+skillMostrada.getNomeHabilidade(),0,linhaAtual++, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Poder: "+skillMostrada.getPoderHabilidade(),0,linhaAtual++, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Precisao: "+skillMostrada.getPrecisaoBase(),0,linhaAtual++, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Energia: "+skillMostrada.getEnergiaHabilidade(),0,linhaAtual++, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Recarga: "+skillMostrada.getRecargaHabilidade(),0,linhaAtual++, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
	}

	private static void desenhaSetaBatalha(){    
		if (Terminal.cursorX < 0){
			Terminal.cursorX = maxInimigos-1;
		}else if (Terminal.cursorX > maxInimigos-1){
			Terminal.cursorX = 0;
		}
		
		int x = posiçõesInimigosX.get(Terminal.cursorX);
		int y = posiçõesInimigosY.get(Terminal.cursorX);
		
		Grapchics.desenhaTela((char) 25, x+4, y - 1, AsciiPanel.brightYellow);
		
		if (Terminal.cursorX > maxInimigos-1) Terminal.cursorX = maxInimigos-1;
		if (Terminal.cursorX < 0) Terminal.cursorX = 0;
		
		monstroMostrado = tropaCarregada.getMonstros().get(Terminal.cursorX);
		if (monstroMostrado == null) return;
		
		Grapchics.desenhaTela(monstroMostrado.getNomeMonstro(), x, y, 
		AsciiPanel.brightYellow, AsciiPanel.brightBlack);
	}
	
	protected static void selecionarComandoBatalha(){
		if (skillMostrada == null) return;
		
		// Usuario, alvo, habilidade.
		
		
		
		if (!selecionarAlvo) selecionarAlvo = true;		
	}
	
	protected static void desenhaTelaPreparo(){
		Grapchics.limpaTela();
		
		maxSlotsAtivos = monstroSlotsAtivos.length;
		linhaAtual = 1;
		
		Grapchics.desenhaCentro("Batalha",0, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("E: Voltar", 0,linhaAtual++, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Q: Iniciar batalha",0,linhaAtual++, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Enter: Escolher integrante",0,linhaAtual++, AsciiPanel.brightBlack);	
		Grapchics.desenhaTela("Oponente: ",0,linhaAtual++, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		tropaCarregada = TroopManager.getTroop(1);
		if (tropaCarregada == null) return;
		
		for (Monsters monstro : tropaCarregada.getMonstros()){
			Grapchics.desenhaTela(monstro.getNomeMonstro()+" Nv"+
			monstro.getNivelAtual()+" ("+monstro.getElementosAtuais()+")"
			,0,linhaAtual++, AsciiPanel.brightWhite);
		}
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		linhaAtual += 1;
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		linhaInicial = linhaAtual;
		for (int i = 0; i <= maxSlotsAtivos-1; i++){
			Monsters monstro = monstroSlotsAtivos[i];
			if(monstro == null){
				if (Terminal.cursorY == linhaAtual){
					Grapchics.desenhaTela("["+(i+1)+"]", 0, linhaAtual++, AsciiPanel.brightYellow, 
					AsciiPanel.brightBlack);
					monstroMostrado = null;
				}else{
					Grapchics.desenhaTela("["+(i+1)+"]", 0, linhaAtual++, AsciiPanel.brightBlack);
				}
			}else{
				if (Terminal.cursorY == linhaAtual){
					Grapchics.desenhaTela(monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual(), 0, linhaAtual++, 
					AsciiPanel.brightYellow, AsciiPanel.brightBlack);
					monstroMostrado = monstro;
				}else{
					Grapchics.desenhaTela(monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual(), 0, linhaAtual++, 
					AsciiPanel.brightWhite);
				}
			}
		}
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		linhaAtual += 1;
		
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		for (Inventory.SlotEquipe slot : Inventory.SlotEquipe.values()){
			Monsters monstroEquipe = Inventory.getEquipeTabela().get(slot);
			String nomeMonstroExibido = "";
			
			if (monstroEquipe != null){
				if (Terminal.cursorY == linhaAtual){
					monstroMostrado = null;
					nomeMonstroExibido = monstroEquipe.getNomeMonstro()+" Nv"+monstroEquipe.getNivelAtual();
					Grapchics.desenhaTela(nomeMonstroExibido, 0, linhaAtual++, AsciiPanel.brightYellow, AsciiPanel.brightBlack);
					monstroMostrado = monstroEquipe;
				}else{
					nomeMonstroExibido = monstroEquipe.getNomeMonstro()+" Nv"+monstroEquipe.getNivelAtual();
					Grapchics.desenhaTela(nomeMonstroExibido, 0, linhaAtual++, AsciiPanel.brightWhite);
				}
			}else{
				if (Terminal.cursorY == linhaAtual){
					Grapchics.desenhaTela("[Vazio]", 0, linhaAtual++, AsciiPanel.brightYellow, AsciiPanel.brightBlack);
					monstroMostrado = null;
				}else{
					Grapchics.desenhaTela("[Vazio]", 0, linhaAtual++, AsciiPanel.brightBlack);
				}
			}
		}
		linhaMax = linhaAtual;
		Grapchics.desenhaTela("____________________",0,linhaAtual, AsciiPanel.brightWhite);
		
		if (Terminal.cursorY > linhaMax-1){
			Terminal.cursorY = linhaInicial;
		}else if (Terminal.cursorY < linhaInicial){
			Terminal.cursorY = linhaMax-1;
		}
		
		Grapchics.atualizarTela();
	}
	
	protected static void desenhaInfoEquipe(){
		int coordenadaY = 21;
		
		for (Map.Entry<Inventory.SlotEquipe, Monsters> entry : Inventory.getEquipeTabela().entrySet()){
			Monsters monstro = entry.getValue();
			Grapchics.desenhaTela("||||||||||",0,coordenadaY, AsciiPanel.brightWhite, AsciiPanel.brightWhite);
			Grapchics.desenhaTela(monstro.getNomeMonstro(), 10, coordenadaY, AsciiPanel.brightWhite);
			Grapchics.desenhaTela("||||||||||", 0, coordenadaY + 1, AsciiPanel.brightRed, AsciiPanel.brightRed);
			coordenadaY += 3;
		}
		Grapchics.desenhaTela("Shift: Esconder equipe",0,39, AsciiPanel.brightBlack);
		
		Grapchics.atualizarTela();
	}
	
	protected static void desenhaMonstroDetalhes(){
		Grapchics.limpaTela();
		
		if (Terminal.cursorX <= 0) Terminal.cursorX = 1;
		
		int tamanho = Inventory.getTamanhoInventario();
		if (Terminal.cursorX > tamanho) Terminal.cursorX = tamanho;
		
		monstroCarregado = Inventory.getMonstroInventario(Terminal.cursorX);
		if (monstroCarregado == null) return;
		
		String indicadorFavorito = monstroCarregado.isMonstroFavorito() ? " [F]" : "";
		
		Grapchics.desenhaCentro("Detalhes",0, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("E: Voltar", 0, 1, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Enter: Marcar/Desmarcar favorito",0,2, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Shift: Ver habilidades",0,3, AsciiPanel.brightBlack);
		
		Grapchics.desenhaTela("____________________",0,4, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Nome: "+monstroCarregado.getNomeMonstro() + indicadorFavorito,0,5, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Nivel: "+monstroCarregado.getNivelAtual(),0,6, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Classe: "+monstroCarregado.getClasseAtual(),0,7, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Elementos: "+monstroCarregado.getElementosAtuais(),0,8, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Raridade: "+monstroCarregado.getRaridadeMonstro(),0,9, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Forca: "+monstroCarregado.getForcaAtual(),0,10, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Vida: "+monstroCarregado.getVidaAtual(),0,11, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Velocidade: "+monstroCarregado.getSpeedAtual(),0,12, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Estamina: "+monstroCarregado.getEstaminaAtual(),0,13, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Tracos: "+Arrays.toString(monstroCarregado.getTracosIds()),0,14, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("____________________",0,15,AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Habilidades:",0,17,AsciiPanel.brightWhite);
		Grapchics.desenhaTela("____________________",0,18,AsciiPanel.brightWhite);
		posiçãoLinhaSkillsAtivas = 19;
		desenhaListaHabilidade();
		
		Grapchics.atualizarTela();
	}
	
	private static void desenhaListaHabilidade(){
		for (int i = 0; i < monstroCarregado.getQuantidadeMaxSlotsHabilidade(); i++){
			skillCarregada = monstroCarregado.getHabilidadeAtiva(i);
			if (skillCarregada != null){
				Grapchics.desenhaTela((i+1)+": "+skillCarregada.getNomeHabilidade(),0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
			}else{
				Grapchics.desenhaTela("[VAZIO]",0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightBlack);
			}
		}
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas,AsciiPanel.brightWhite);
		
		skillsDesbloqueadas = monstroCarregado.getHabilidadesDesbloqueadas();
		skillCarregada = skillsDesbloqueadas.get(0);
		
		if (skillCarregada != null && skillCarregada.isTipoEspecial(skillCarregada.getTipoHabilidade())){
			Grapchics.desenhaTela("Especial:",0,posiçãoLinhaSkillsAtivas+2,AsciiPanel.brightWhite);
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas+3,AsciiPanel.brightWhite);
			Grapchics.desenhaTela(skillCarregada.getNomeHabilidade(),0,posiçãoLinhaSkillsAtivas+4,AsciiPanel.brightWhite);
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas+5,AsciiPanel.brightWhite);
		}
	}
	
	protected static void desenhaHabilidadeDetalhes(){
		Grapchics.limpaTela();
		
		if (Terminal.cursorX <= 0){
			Terminal.cursorX = 1;
		}else if (Terminal.cursorX >= Inventory.getTamanhoInventario()) Terminal.cursorX = Inventory.getTamanhoInventario();
		
		monstroCarregado = Inventory.getMonstroInventario(Terminal.cursorX);
		if (monstroCarregado == null) return;
		
		if (Terminal.cursorY < 5){
			Terminal.cursorY = posiçãoLinhaSkillsAtivas-1;
		}else if (Terminal.cursorY > posiçãoLinhaSkillsAtivas-1){
			Terminal.cursorY = 5;
		}
		
		Grapchics.desenhaCentro("Habilidades",0, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("E: Voltar", 0, 1, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Enter: Ativar/Desativar habilidade",0,2, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Monstro: "+monstroCarregado.getNomeMonstro(),0,3, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("____________________",0,4,AsciiPanel.brightWhite);
		
		posiçãoLinhaSkillsAtivas = 5;
		linhaAtual = posiçãoLinhaSkillsAtivas;
		for (int i = 0; i < monstroCarregado.getQuantidadeMaxSlotsHabilidade(); i++){
			skillCarregada = monstroCarregado.getHabilidadeAtiva(i);
			
			if (skillCarregada != null){
				if (Terminal.cursorY == linhaAtual){
					Grapchics.desenhaTela((i+1)+": "+skillCarregada.getNomeHabilidade(),0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightYellow,
					AsciiPanel.brightBlack);
					skillMostrada = skillCarregada;
				}else{
					Grapchics.desenhaTela((i+1)+": "+skillCarregada.getNomeHabilidade(),0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
				}
			}else{
				if (Terminal.cursorY == linhaAtual){
					Grapchics.desenhaTela("[VAZIO]",0,posiçãoLinhaSkillsAtivas++,
					AsciiPanel.brightYellow, AsciiPanel.brightBlack);
					skillMostrada = null;
				}else{
					Grapchics.desenhaTela("[VAZIO]",0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightBlack);
				}
			}
			linhaAtual++;
		}
		posiçãoLinhaSkillsAtivas = linhaAtual;
		
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
		listaArvoreHabilidades();
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
		posiçãoLinhaSkillsAtivas += 1;
		
		skillsDesbloqueadas = monstroCarregado.getHabilidadesDesbloqueadas();
		skillCarregada = skillsDesbloqueadas.get(0);
		
		if (skillCarregada != null && skillCarregada.isTipoEspecial(skillCarregada.getTipoHabilidade())){
			Grapchics.desenhaTela("Especial:",0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
			if (Terminal.cursorY == linhaAtual){
				Grapchics.desenhaTela(skillCarregada.getNomeHabilidade(),0,posiçãoLinhaSkillsAtivas++,
				AsciiPanel.brightYellow, AsciiPanel.brightBlack);
				skillMostrada = skillCarregada;
			}else{
				Grapchics.desenhaTela(skillCarregada.getNomeHabilidade(),0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
			}
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
		}
		
		posiçãoLinhaSkillsAtivas += 1;
		infoHabilidade();
		
		linhaAtual++;
		posiçãoLinhaSkillsAtivas = linhaAtual;
		
		Grapchics.atualizarTela();
	}
	
	private static void infoHabilidade(){
		if (skillMostrada != null){
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
			
			if (skillMostrada.getPoderHabilidade() > 0){
				Grapchics.desenhaTela("Poder: "+skillMostrada.getPoderHabilidade(),0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
			}
			if (skillMostrada.getPrecisaoBase() > 0){
				Grapchics.desenhaTela("Precisao: "+skillMostrada.getPrecisaoBase(),0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
			}
			if (skillMostrada.getEnergiaHabilidade() > 0){
				Grapchics.desenhaTela("Energia: "+skillMostrada.getEnergiaHabilidade(),0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
			}
			if (skillMostrada.getRecargaHabilidade() > 0){
				Grapchics.desenhaTela("Recarga: "+skillMostrada.getRecargaHabilidade(),0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
			}
			
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
		}
	}
	
	private static void listaArvoreHabilidades(){
		linhaAtual = posiçãoLinhaSkillsAtivas;
		for (int i = 0; i <= monstroCarregado.getTamanhoSkillsTree(); i++){
			skillCarregada = monstroCarregado.getHabilidadeArvoreId(i);
			if (skillCarregada == null) continue;
			
			if (!skillCarregada.isTipoEspecial(skillCarregada.getTipoHabilidade()) 
				&& !monstroCarregado.isHabilidadeAtiva(skillCarregada)){
				if (monstroCarregado.getNivelAtual() >= skillCarregada.getNivelNecessario()){
					
					if (Terminal.cursorY == linhaAtual){
						Grapchics.desenhaTela(skillCarregada.getNomeHabilidade()+" (Nv"+skillCarregada.getNivelNecessario()+")",
						0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightYellow, AsciiPanel.brightBlack);
						skillMostrada = skillCarregada;
					}else{
						Grapchics.desenhaTela(skillCarregada.getNomeHabilidade()+" (Nv"+skillCarregada.getNivelNecessario()+")",
						0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightWhite);
					}
					
				}else{
					if (Terminal.cursorY == linhaAtual){
						Grapchics.desenhaTela(skillCarregada.getNomeHabilidade()+" (Nv"+skillCarregada.getNivelNecessario()+")",
						0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightYellow, AsciiPanel.brightBlack);
						skillMostrada = skillCarregada;
					}else{
						Grapchics.desenhaTela(skillCarregada.getNomeHabilidade()+" (Nv"+skillCarregada.getNivelNecessario()+")",
						0,posiçãoLinhaSkillsAtivas++,AsciiPanel.brightBlack);
					}
				}
				linhaAtual++;
			}
		}
		posiçãoLinhaSkillsAtivas = linhaAtual;
	}
	
	protected static void alternarMonstroSlotsAtivos(){
		if (monstroMostrado == null) return;
		
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
	
	protected static void alternarHabilidadeAtiva(){
		if (monstroCarregado == null || skillMostrada == null) return;
		
		int maxSlots = monstroCarregado.getQuantidadeMaxSlotsHabilidade();
		int slotsOcupados = monstroCarregado.getQuantidadeSlotsOcupados();
		boolean isEspecial = skillMostrada.isTipoEspecial(skillMostrada.getTipoHabilidade());
		boolean isAtiva = monstroCarregado.isHabilidadeAtiva(skillMostrada);
		boolean isDesbloqueada = monstroCarregado.isHabilidadeDesbloqueada(skillMostrada);
		
		if (isEspecial){
			return; 
		}
		
		if (isAtiva && slotsOcupados >= 2){
			if (monstroCarregado.removerHabilidadeAtiva(skillMostrada)){
				monstroCarregado.reordenarSkillsAtivas(); 
			}
		}else if (!isAtiva && isDesbloqueada){
			monstroCarregado.adicionarHabilidadeAtiva(skillMostrada);
		}
	}
	
	protected static void alternarPagina(boolean avançar){
		if (avançar){
			paginaAtual++;
			if (paginaAtual > totalPaginas) paginaAtual = 1;
		}else paginaAtual--; if (paginaAtual < 1) paginaAtual = totalPaginas;
	}
	
	protected static void resetarSlotsAtivos(){
		monstroSlotsAtivos = new Monsters[3];
	}
	
	public static int getOuro(){
		return ouro;
	}
	
	public static void setOuro(int ouro){
		Battle.ouro = ouro;
	}

	//===
}