package manager;

public final class DataManager {

	private DataManager(){
	}
	
    public static void carregarDados(){
		MapsManager.carregarMapas();
		NPCManager.carregarNPCs();
		BattleManager.carregarDadosBatalha();
    }
	
	//===
}