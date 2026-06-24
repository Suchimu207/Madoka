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
	private static enum SlotEquipe{
		SLOT_1,
		SLOT_2,
		SLOT_3,
		SLOT_4,
		SLOT_5,
		SLOT_6;
	}
	private static Map<Integer, Monsters> monstrosInventário;
	private static ArrayList<Monsters> monstrosOrdenados;
	private static EnumMap<SlotEquipe, Monsters> equipeTabela;	
	private static List<Skills> skillsTree, skillsDesbloqueadas;
	private static SlotEquipe slotEncontrado;

	private static Monsters monstroCarregado, monstroMostrado;
	private static Skills skillCarregada, skillMostrada;
	private static Troop tropaCarregada;
	private static Monsters[] monstroSlotsAtivos;
	
	private static int idInventário, tamanhoInventário, totalPaginas, paginaAtual, 
	inicioLista, fimLista, ouro, linhaAtual, maxSlotsAtivos, linhaInicial, linhaMax;
	private static int posiçãoLinhaInventário, posiçãoLinhaEquipe, 
	posiçãoLinhaSkillsAtivas, posiçãoLinhaBatalha;
	
	private static String nomeMonstroExibido;
	
	private Battle(){
	}
	
	protected static void carregarDadosBatalha(){
		TraitsManager.carregarTraços();
		SkillsManager.carregarHabilidades();
		MonstersManager.carregarMonstros();
		TroopManager.carregarTropas();
		
		monstrosInventário = new HashMap<Integer, Monsters>();
		monstrosOrdenados = new ArrayList<Monsters>();
		equipeTabela =  new EnumMap<>(SlotEquipe.class);
		monstroSlotsAtivos = new Monsters[3];
		skillsTree = new ArrayList<>();
		skillsDesbloqueadas = new ArrayList<>();
		
		montarEquipeInicial();
	}
	
	private static void montarEquipeInicial(){
		idInventário = 1;
		paginaAtual = 1;
		inicioLista = 1;
		fimLista = 1;
		ouro = 3000;
		
		adicionarMonstroInventário(1);
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
		// As informações das tropas: quem é, nível e elementos. Exemplo: Firesaur Nv1 (Fogo).
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
		for (SlotEquipe slot : SlotEquipe.values()){
			Monsters monstroEquipe = equipeTabela.get(slot);
			
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
		
		for (Map.Entry<SlotEquipe, Monsters> entry : equipeTabela.entrySet()){
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
		
		if (Terminal.cursorX <= 0){
			Terminal.cursorX = 1;
		}else if (Terminal.cursorX >= monstrosInventário.size()) Terminal.cursorX = monstrosInventário.size();
		
		monstroCarregado = monstrosInventário.get(Terminal.cursorX);
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
		Grapchics.desenhaTela("Forca: "+monstroCarregado.getForcaBase(),0,10, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Vida: "+monstroCarregado.getVidaBase(),0,11, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Velocidade: "+monstroCarregado.getSpeedBase(),0,12, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Estamina: "+monstroCarregado.getEstaminaBase(),0,13, AsciiPanel.brightWhite);
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
		}else if (Terminal.cursorX >= monstrosInventário.size()) Terminal.cursorX = monstrosInventário.size();
		
		monstroCarregado = monstrosInventário.get(Terminal.cursorX);
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
	
	protected static void desenhaInventário(){
		Grapchics.limpaTela();
		
		reordenarListaInventário();
		if (monstrosInventário.isEmpty()){
			Grapchics.desenhaCentro("Inventario vazio.", 10, AsciiPanel.brightWhite);
			Grapchics.atualizarTela();
			return;
		}
		
		tamanhoInventário = monstrosInventário.size();
		totalPaginas = (int) Math.ceil(tamanhoInventário / 24.0);
		
		String indicadorPagina = "Pagina "+paginaAtual+"/"+totalPaginas;
		Grapchics.desenhaCentro("Inventario - "+indicadorPagina,0, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("E: Voltar", 0, 1, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Enter: Equipar/Desequipar", 0, 2, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Shift: Ver detalhes", 0, 3, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("____________________",0,4, AsciiPanel.brightWhite);
		desenhaListaInventário();
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaInventário, AsciiPanel.brightWhite);
		
		posiçãoLinhaEquipe = 33;
		Grapchics.desenhaCentro("Equipe:",31, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("____________________",0,32, AsciiPanel.brightWhite);
		for (SlotEquipe slot : SlotEquipe.values()){
			Monsters monstroEquipe = equipeTabela.get(slot);
			
			if (monstroEquipe != null){
				nomeMonstroExibido = monstroEquipe.getNomeMonstro()+" Nv"+monstroEquipe.getNivelAtual();
				Grapchics.desenhaTela(nomeMonstroExibido, 0, posiçãoLinhaEquipe++, AsciiPanel.brightWhite);
			}else{
				Grapchics.desenhaTela("[Vazio]", 0, posiçãoLinhaEquipe++, AsciiPanel.brightBlack);
			}
		}
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaEquipe, AsciiPanel.brightWhite);
		
		Grapchics.atualizarTela();
	}
	
	private static void desenhaListaInventário(){
		inicioLista = (paginaAtual - 1) * 24;
		fimLista = Math.min(inicioLista + 24, monstrosOrdenados.size());
		
		if (Terminal.cursorY < inicioLista + 1) Terminal.cursorY = fimLista;
		if (Terminal.cursorY > fimLista) Terminal.cursorY = inicioLista+1;
		
		posiçãoLinhaInventário = 5;
		for (int i = inicioLista+1; i <= fimLista; i++){
		Monsters monstro = monstrosInventário.get(i);
		if (monstro == null) continue;

		boolean selecionado = (i == Terminal.cursorY);
		String indicadorEquipado = monstro.isMonstroEquipado() ? " [E]" : "";
		String indicadorFavorito = monstro.isMonstroFavorito() ? " [F]" : "";
		
			if (selecionado){
				nomeMonstroExibido = monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual();
				Grapchics.desenhaTela(nomeMonstroExibido + indicadorEquipado + indicadorFavorito, 0, posiçãoLinhaInventário++, 
				AsciiPanel.brightYellow, AsciiPanel.brightBlack);
			}else{
				nomeMonstroExibido = monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual();
				Grapchics.desenhaTela(nomeMonstroExibido + indicadorEquipado + indicadorFavorito, 0, posiçãoLinhaInventário++, 
				AsciiPanel.brightWhite);
			}
		}
	}
	
	private static void reordenarListaInventário(){
		monstrosOrdenados.clear(); 
		
		for (int i = 1; i <= monstrosInventário.size(); i++){
			monstroCarregado = monstrosInventário.get(i);
			if (monstroCarregado != null && (monstroCarregado.isMonstroEquipado() || monstroCarregado.isMonstroFavorito())){
				monstrosOrdenados.add(monstroCarregado);
			}
		}
		
		for (int i = 1; i <= monstrosInventário.size(); i++){
			monstroCarregado = monstrosInventário.get(i);
			if (monstroCarregado != null && !monstroCarregado.isMonstroEquipado() && !monstroCarregado.isMonstroFavorito()){
				monstrosOrdenados.add(monstroCarregado);
			}
		}
		
		monstrosInventário.clear();
		idInventário = 1;
		for (Monsters m : monstrosOrdenados){
			monstrosInventário.put(idInventário++, m);
		}
	}
	
	protected static void alternarMonstroTabela(int id){
		Monsters monstro = monstrosInventário.get(id);
		if (monstro == null) return;

		if (monstro.isMonstroEquipado()){	
			slotEncontrado = null;
			for (Map.Entry<SlotEquipe, Monsters> entry : equipeTabela.entrySet()){
				if (entry.getValue() == monstro){
					slotEncontrado = entry.getKey();
					break;
				}
			}
			
			if (slotEncontrado != null && equipeTabela.size() >= 2){
				equipeTabela.remove(slotEncontrado);
				monstro.setMonstroEquipado(false);
				reordenarEquipe();
			}
		}else{
			// Procura um espaço vazio na tabela. 
			for (SlotEquipe slot : SlotEquipe.values()){
				if (!equipeTabela.containsKey(slot)){
					equipeTabela.put(slot, monstro);
					monstro.setMonstroEquipado(true);
					break;
				}
			}
		}
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
	
	protected static void alternarMonstroFavorito(int id){
		Monsters monstro = monstrosInventário.get(id);
		if (monstro == null) return;
		
		if (monstro.isMonstroFavorito()){
			monstro.setMonstroFavorito(false);
		}else monstro.setMonstroFavorito(true);
	}
	
	private static void reordenarEquipe(){
		Monsters[] monstrosAtuais = equipeTabela.values().toArray(new Monsters[0]);
		equipeTabela.clear();
		
		SlotEquipe[] slots = SlotEquipe.values();
		for (int i = 0; i < monstrosAtuais.length && i < slots.length; i++){
			equipeTabela.put(slots[i], monstrosAtuais[i]);
		}
	}
	
	protected static void adicionarMonstroInventário(int id){
		Monsters monstroRequerido = MonstersManager.getMonstro(id);
		
		monstroCarregado = new Monsters(monstroRequerido);
		
		monstrosInventário.put(idInventário++, monstroCarregado);
		
		for (SlotEquipe slot : SlotEquipe.values()){
			if (!equipeTabela.containsKey(slot)){
				equipeTabela.put(slot, monstroCarregado);
				monstroCarregado.setMonstroEquipado(true);
				break;
			}
		}
	}
	
	protected static void removerMonstroInventário(int id){
		monstroCarregado = monstrosInventário.get(id);
		if (monstroCarregado == null) return;
		
		monstrosInventário.remove(id);
		monstroCarregado.setMonstroEquipado(false);
		
		slotEncontrado = null;
		for (Map.Entry<SlotEquipe, Monsters> entry : equipeTabela.entrySet()){
			if (entry.getValue() == monstroCarregado){
				slotEncontrado = entry.getKey();
				break;
			}
		}
		if (slotEncontrado != null){
			equipeTabela.remove(slotEncontrado);
			reordenarEquipe();
		}
		
		// Reordena inventário.
		Monsters[] monstrosAtuais = monstrosInventário.values().toArray(new Monsters[0]);
		monstrosInventário.clear();
		
		idInventário = 1;
		for (int i = 0; i < monstrosAtuais.length; i++){
			monstrosInventário.put(idInventário++, monstrosAtuais[i]);
		}
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