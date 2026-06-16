import bestiary.*;

import asciiPanel.AsciiPanel;

import java.util.HashMap;
import java.util.Map;

public final class Battle {
	private static Map<Integer, Monsters> monstrosInventário; 
	private static Map<Integer, Monsters> equipeTabela;
	private static Monsters monstroCarregado, monstroSlot_1, monstroSlot_2, monstroSlot_3;
	private static int idInventário;
	
	protected Battle(){
	}
	
	protected static void carregarDadosBatalha(){
		TraitsManager.carregarTraços();
		MonstersManager.carregarMonstros();
		SkillsManager.carregarHabilidades();
		
		monstrosInventário = new HashMap<Integer, Monsters>();
		equipeTabela = new HashMap<Integer, Monsters>();
		idInventário = 1;
		
		// Monta a equipe inicial.
		monstroCarregado = MonstersManager.getMonstro(1);
		monstrosInventário.put(idInventário++, monstroCarregado);
		equipeTabela.put(1, monstroCarregado);
		monstroSlot_1 = monstroCarregado;
		
		monstroCarregado = MonstersManager.getMonstro(2);
		monstrosInventário.put(idInventário++, monstroCarregado);
		equipeTabela.put(2, monstroCarregado);
		monstroSlot_2 = monstroCarregado;
		
		monstroCarregado = MonstersManager.getMonstro(3);
		monstrosInventário.put(idInventário++, monstroCarregado);
		equipeTabela.put(3, monstroCarregado);
		monstroSlot_3 = monstroCarregado;
	}
	
	protected static void desenhaInfoEquipe(){
		Grapchics.desenhaTela("||||||||||",0,21, AsciiPanel.brightWhite, AsciiPanel.brightWhite);
		Grapchics.desenhaTela(monstroSlot_1.getNomeMonstro(),10,21, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("||||||||||",0,22, AsciiPanel.brightRed, AsciiPanel.brightRed);
		
		Grapchics.desenhaTela("||||||||||",0,24, AsciiPanel.brightWhite, AsciiPanel.brightWhite);
		Grapchics.desenhaTela(monstroSlot_2.getNomeMonstro(),10,24, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("||||||||||",0,25, AsciiPanel.brightRed, AsciiPanel.brightRed);
		
		Grapchics.desenhaTela("||||||||||",0,27, AsciiPanel.brightWhite, AsciiPanel.brightWhite);
		Grapchics.desenhaTela(monstroSlot_3.getNomeMonstro(),10,27, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("||||||||||",0,28, AsciiPanel.brightRed, AsciiPanel.brightRed);
		
		Grapchics.desenhaTela("Ouro: 0",0,39, AsciiPanel.brightWhite);
		Grapchics.atualizarTela();
	}
	
	//===
}