package combat;

import bestiary.TraitsManager;
import bestiary.SkillsManager;
import bestiary.MonstersManager;
import bestiary.TroopManager;
import combat.status.StatusManager;

public final class BattleManager {
	private BattleManager(){
	}
	
	public final static void carregarDadosBatalha(){
		System.out.println("");
		TraitsManager.carregarTraços();
		System.out.println("");
		SkillsManager.carregarHabilidades();
		System.out.println("");
		StatusManager.carregarStatus();
		System.out.println("");
		MonstersManager.carregarMonstros();
		System.out.println("");
		TroopManager.carregarTropas();
		System.out.println("");
	}
	
	//===
}