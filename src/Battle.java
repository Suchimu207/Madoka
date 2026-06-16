import bestiary.*;

import asciiPanel.AsciiPanel;

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
	private static EnumMap<SlotEquipe, Monsters> equipeTabela;

	private static Monsters monstroCarregado;
	private static Monsters[] monstroSlotsAtivos;
	private static int idInventário, posiçãoLinhaInventário, posiçãoLinhaEquipe;
	
	private Battle(){
	}
	
	protected static void carregarDadosBatalha(){
		TraitsManager.carregarTraços();
		MonstersManager.carregarMonstros();
		SkillsManager.carregarHabilidades();
		
		monstrosInventário = new HashMap<Integer, Monsters>();
		equipeTabela =  new EnumMap<>(SlotEquipe.class);
		monstroSlotsAtivos = new Monsters[3];
		
		montarEquipeInicial();
	}
	
	private static void montarEquipeInicial(){
		idInventário = 1;
		
		monstroCarregado = MonstersManager.getMonstro(1);
		monstroCarregado.setMonstroEquipado(true);
		monstrosInventário.put(idInventário++, monstroCarregado);
		equipeTabela.put(SlotEquipe.SLOT_1, monstroCarregado);
		monstroSlotsAtivos[0] = monstroCarregado;
		
		monstroCarregado = MonstersManager.getMonstro(2);
		monstroCarregado.setMonstroEquipado(true);
		monstrosInventário.put(idInventário++, monstroCarregado);
		equipeTabela.put(SlotEquipe.SLOT_2, monstroCarregado);
		monstroSlotsAtivos[1] = monstroCarregado;
		
		monstroCarregado = MonstersManager.getMonstro(3);
		monstroCarregado.setMonstroEquipado(true);
		monstrosInventário.put(idInventário++, monstroCarregado);
		equipeTabela.put(SlotEquipe.SLOT_3, monstroCarregado);
		monstroSlotsAtivos[2] = monstroCarregado;
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
		
		Grapchics.desenhaTela("E: Inventario",0,38, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Ouro: 0",0,39, AsciiPanel.brightWhite);
		Grapchics.atualizarTela();
	}
	
	protected static void desenhaInventário(){
		Grapchics.limpaTela();
		
		if (monstrosInventário.isEmpty()){
			Grapchics.desenhaCentro("Inventario vazio.", 10, AsciiPanel.brightWhite);
			Grapchics.atualizarTela();
			return;
		}
		
		if (Terminal.cursorY > monstrosInventário.size()) {
			Terminal.cursorY = 1;
		} else if (Terminal.cursorY < 1) {
			Terminal.cursorY = monstrosInventário.size();
		}
		
		Grapchics.desenhaCentro("Inventario",1, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("E: Voltar", 0, 2, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Enter: Equipar/Desequipar", 0, 3, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("____________________",0,4, AsciiPanel.brightWhite);
		
		posiçãoLinhaInventário = 5;
		for (int i = 1; i <= monstrosInventário.size(); i++){
			Monsters monstro = monstrosInventário.get(i);
			if (monstro == null) continue;

			boolean selecionado = (i == Terminal.cursorY);
			String indicadorEquipado = monstro.isMonstroEquipado() ? " (E)" : "";
			
			if (selecionado){
				Grapchics.desenhaTela(monstro.getNomeMonstro() + indicadorEquipado, 0, posiçãoLinhaInventário++, 
				AsciiPanel.brightYellow, AsciiPanel.brightBlack);
			}else{
				Grapchics.desenhaTela(monstro.getNomeMonstro() + indicadorEquipado, 0, posiçãoLinhaInventário++, 
				AsciiPanel.brightWhite);
			}
		}
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaInventário, AsciiPanel.brightWhite);
		
		posiçãoLinhaEquipe = 33;
		Grapchics.desenhaCentro("Equipe:",31, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("____________________",0,32, AsciiPanel.brightWhite);
		for (SlotEquipe slot : SlotEquipe.values()){
			Monsters monstroEquipe = equipeTabela.get(slot);
			
			if (monstroEquipe != null){
				Grapchics.desenhaTela(monstroEquipe.getNomeMonstro(), 0, posiçãoLinhaEquipe++, AsciiPanel.brightWhite);
			}else{
				Grapchics.desenhaTela("[Vazio]", 0, posiçãoLinhaEquipe++, AsciiPanel.brightBlack);
			}
		}
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaEquipe, AsciiPanel.brightWhite);
		
		Grapchics.atualizarTela();
	}
	
	protected static void alternarMonstroTabela(int id){
		Monsters monstro = monstrosInventário.get(id);
		if (monstro == null) return;

		if (monstro.isMonstroEquipado()){	
			SlotEquipe slotEncontrado = null;
			for (Map.Entry<SlotEquipe, Monsters> entry : equipeTabela.entrySet()){
				if (entry.getValue() == monstro){
					slotEncontrado = entry.getKey();
					break;
				}
			}
			
			if (slotEncontrado != null && equipeTabela.size() >= 2){
				equipeTabela.remove(slotEncontrado);
				monstro.setMonstroEquipado(false);
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
	
	//===
}