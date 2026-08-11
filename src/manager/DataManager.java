package manager;

public final class DataManager {

	private DataManager(){
	}
	
    public static void carregarDados(){
		MapsManager.carregarMapas();
		BattleManager.carregarDadosBatalha();
    }
	
	//===
}