package manager;

import bestiary.traits.TraitsManager;
import combat.status.StatusManager;

public final class BattleManager {
	private BattleManager(){
	}
	
	protected final static void carregarDadosBatalha(){
		System.out.println("");
		SkillsManager.carregarHabilidades();
		System.out.println("");
		StatusManager.carregarStatus();
		System.out.println("");
		TraitsManager.carregarTraços();
		System.out.println("");
		MonstersManager.carregarMonstros();
		System.out.println("");
		TroopManager.carregarTropas();
		System.out.println("");
	}
	
	//===
}