package combat.effects.strategies;

import bestiary.Monsters;
import bestiary.Skills;

import combat.effects.Effects;
import combat.effects.EffectsStrategy;

import combat.status.StatusManager;
import combat.status.StatusBase;

public class EffectsApplyStatus implements EffectsStrategy {
    
    @Override
    public void aplicar(Monsters usuario, Monsters alvo, Skills habilidade, Effects efeito){
        if (alvo == null || usuario == null || efeito == null) return;
		
		int efeitoValor = efeito.getValor();
		int efeitoTurnos = efeito.getTurnos();
		
		StatusBase status = StatusManager.getStatusPorId(efeitoValor);
		
		if (status == null || efeitoTurnos <= 0) return;
		
		if (!alvo.possuiStatus(status)){
			status.aplicar(alvo, efeitoTurnos);
		}else if(alvo.possuiStatus(status)){
			status.renovarDuração();
		}
    }
    
    @Override
    public String getNome(){
        return "APPLY_STATUS";
    }
    
	//===
}