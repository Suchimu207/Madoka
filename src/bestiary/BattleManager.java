package bestiary;

public final class BattleManager {
	private BattleManager(){
	}
	
	public static void carregarDadosBatalha(){
		TraitsManager.carregarTraços();
		SkillsManager.carregarHabilidades();
		MonstersManager.carregarMonstros();
		TroopManager.carregarTropas();
	}
	
	//===
}