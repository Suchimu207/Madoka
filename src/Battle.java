import bestiary.*;

public class Battle {
	public Battle(){
	}
	
	public static void carregarDadosBatalha(){
		TraitsManager.carregarTraços();
		MonstersManager.carregarMonstros();
		SkillsManager.carregarHabilidades();
	}
	
	//===
}