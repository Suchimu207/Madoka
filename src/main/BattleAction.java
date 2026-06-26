package main;

import bestiary.Monsters;
import bestiary.Skills;

public final class BattleAction {
	private static final int CONSTANTE = 10;
	
	private BattleAction(){
	}
	
	protected static boolean verificarCustoHabilidade(Monsters usuario, Monsters alvo, Skills habilidade){
		int estaminaAtualCombate = usuario.getEstaminaAtualCombate();
		int energiaHabilidade = habilidade.getEnergiaHabilidade();
		
		if (estaminaAtualCombate >= energiaHabilidade){
			return true;
		}
		
		return false;
	}
	
    protected static void executarHabilidade(Monsters usuario, Monsters alvo, Skills habilidade){
		int estaminaAtualCombate = usuario.getEstaminaAtualCombate();
		int energiaHabilidade = habilidade.getEnergiaHabilidade();
		
        usuario.setEstaminaAtualCombate(estaminaAtualCombate - energiaHabilidade);
        
        if (habilidade.getPoderHabilidade() > 0){
            calcularDano(usuario, alvo, habilidade);
        }
    }
	
	private static void calcularDano(Monsters usuario, Monsters alvo, Skills habilidade){
		int forçaMonstro = usuario.getForcaAtualCombate();
		int poderHabilidade = habilidade.getPoderHabilidade();
		
        double danoBase = Math.ceil((forçaMonstro / 1000.0) * (poderHabilidade) * CONSTANTE);
		
        int vidaRestante = (int) (alvo.getVidaAtualCombate() - danoBase);
        alvo.setVidaAtualCombate(vidaRestante);
    }
	
	//===
}