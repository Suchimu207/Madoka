import bestiary.*;

import asciiPanel.AsciiPanel;

import java.util.Arrays;
import java.util.ArrayList;
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
	private static SlotEquipe slotEncontrado;

	private static Monsters monstroCarregado;
	private static Monsters[] monstroSlotsAtivos;
	private static int idInventário, posiçãoLinhaInventário, posiçãoLinhaEquipe, 
	tamanhoInventário, totalPaginas, paginaAtual, inicioLista, fimLista;
	private static String nomeMonstroExibido;
	
	private Battle(){
	}
	
	protected static void carregarDadosBatalha(){
		TraitsManager.carregarTraços();
		MonstersManager.carregarMonstros();
		SkillsManager.carregarHabilidades();
		
		monstrosInventário = new HashMap<Integer, Monsters>();
		monstrosOrdenados = new ArrayList<Monsters>();
		equipeTabela =  new EnumMap<>(SlotEquipe.class);
		monstroSlotsAtivos = new Monsters[3];
		
		montarEquipeInicial();
	}
	
	private static void montarEquipeInicial(){
		idInventário = 1;
		paginaAtual = 1;
		inicioLista = 1;
		fimLista = 1;
		
		adicionarMonstroInventário(1);
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
		
		if (Terminal.cursorY <= 0){
			Terminal.cursorY = 1;
		}else if (Terminal.cursorY >= monstrosInventário.size()) Terminal.cursorY = monstrosInventário.size();
		
		monstroCarregado = monstrosInventário.get(Terminal.cursorY);
		if (monstroCarregado == null) return;
		
		String indicadorFavorito = monstroCarregado.isMonstroFavorito() ? " [F]" : "";
		
		Grapchics.desenhaCentro("Detalhes",0, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("E: Voltar", 0, 1, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Enter: Marcar/Desmarcar favorito",0,2, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Shift: Ver habilidades",0,3, AsciiPanel.brightBlack);
		
		Grapchics.desenhaTela("____________________",0,4, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Nome: "+monstroCarregado.getNomeMonstro() + indicadorFavorito,0,5, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Nivel: "+monstroCarregado.getNivelAtual(),0,6, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Exp: "+monstroCarregado.getExpAtual(),0,7, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Forca: "+monstroCarregado.getForcaBase(),0,8, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Vida: "+monstroCarregado.getVidaBase(),0,9, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Velocidade: "+monstroCarregado.getSpeedBase(),0,10, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Estamina: "+monstroCarregado.getEstaminaBase(),0,11, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Classe: "+monstroCarregado.getClasseAtual(),0,12, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Elementos: "+monstroCarregado.getElementosAtuais(),0,13, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("Tracos: "+Arrays.toString(monstroCarregado.getTracosIds()),0,14, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("____________________",0,15,AsciiPanel.brightWhite);
		
		Grapchics.desenhaTela("Habilidades:",0,17,AsciiPanel.brightWhite);
		
		Grapchics.desenhaTela("____________________",0,18,AsciiPanel.brightWhite);
		Grapchics.desenhaTela("1: NomeHabilidade",0,19,AsciiPanel.brightWhite);
		Grapchics.desenhaTela("2: NomeHabilidade",0,20,AsciiPanel.brightWhite);
		Grapchics.desenhaTela("3: NomeHabilidade",0,21,AsciiPanel.brightWhite);
		Grapchics.desenhaTela("4: NomeHabilidade",0,22,AsciiPanel.brightWhite);
		Grapchics.desenhaTela("5: HabilidadeEspecial",0,23,AsciiPanel.brightWhite);
		Grapchics.desenhaTela("____________________",0,24,AsciiPanel.brightWhite);
		
		Grapchics.atualizarTela();
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
	
	private static void adicionarMonstroInventário(int id){
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
	
	private static void removerMonstroInventário(int id){
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

	//===
}