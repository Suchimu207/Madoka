package combat;

import bestiary.Monsters;
import bestiary.Skills;

import java.util.List;

public final class BattleAction {
	private static final int CONSTANTE = 10;
	
	private BattleAction(){
	}
	
	protected static void recarregarEnergia(Monsters usuario){
		int maxEstamina = usuario.getEstaminaAtual();
		int regneração = (int) Math.ceil(maxEstamina * 0.5); 
		int novaEstamina = regneração + usuario.getEstaminaAtualCombate();
		
		if (novaEstamina >= maxEstamina){
			novaEstamina = maxEstamina;
		}
		
		usuario.setEstaminaAtualCombate(novaEstamina);
	}
	
	protected static boolean verificarCustoHabilidade(Monsters usuario, Skills habilidade){
		int estaminaAtualCombate = usuario.getEstaminaAtualCombate();
		int energiaHabilidade = habilidade.getEnergiaHabilidade();
		
		if (estaminaAtualCombate >= energiaHabilidade){
			return true;
		}
		
		return false;
	}
	
    protected static void executarHabilidade(Monsters usuario, List<Monsters> alvos, Skills habilidade){
		int estaminaAtualCombate = usuario.getEstaminaAtualCombate();
		int energiaHabilidade = habilidade.getEnergiaHabilidade();
		
        usuario.setEstaminaAtualCombate(estaminaAtualCombate - energiaHabilidade);
        
        if (habilidade.getPoderHabilidade() > 0){
            calcularDano(usuario, alvos, habilidade);
        }
    }
	
	private static void calcularDano(Monsters usuario, List<Monsters> alvos, Skills habilidade){
		int forçaMonstro = usuario.getForcaAtualCombate();
		int poderHabilidade = habilidade.getPoderHabilidade();
		
        double danoBase = Math.ceil((forçaMonstro / 1000.0) * (poderHabilidade) * CONSTANTE);
		
		for (Monsters monstro : alvos){
			int vidaRestante = (int) (monstro.getVidaAtualCombate() - danoBase);
			monstro.setVidaAtualCombate(vidaRestante);
		}
    }
	
	//===
}