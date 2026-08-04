package combat.effects.strategies;

import bestiary.Monsters;
import bestiary.Skills;
import combat.BattleTurn;
import combat.BattleUnit;
import combat.effects.Effects;
import combat.effects.EffectsStrategy;

public class EffectsActionAdvance implements EffectsStrategy {

    @Override
    public void aplicar(Monsters usuario, Monsters alvo, Skills habilidade, Effects efeito){
		if (usuario == null || alvo == null || efeito == null) return;
		
        BattleUnit unidade = BattleTurn.getUnidadePorMonstro(alvo);
        
		int efeitoValor = efeito.getValor();
		
		if (unidade != null && efeitoValor > 0){
            if (efeitoValor >= 100){
                BattleTurn.forcarProximoTurno(unidade);
            }else{
                unidade.avancarAção(efeitoValor);
            }
        }
    }
	
    @Override
    public String getNome() {
        return "ACTION_ADVANCE";
    }
	
	//===
}