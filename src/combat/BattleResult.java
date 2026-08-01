package combat;

import util.Grapchics;

public final class BattleResult {
    
    private BattleResult(){
    }
    
    public static void desenhaTelaVitoria(){
        Grapchics.limpaTela();
        Grapchics.desenhaCentroTTF("Vitoria", 10, Grapchics.BRANCO_CLARO);
        Grapchics.atualizarTela();
    }
    
    public static void desenhaTelaDerrota(){
        Grapchics.limpaTela();
        Grapchics.desenhaCentroTTF("Derrota", 10, Grapchics.BRANCO_CLARO);
        Grapchics.atualizarTela();
    }
	
	//===
}