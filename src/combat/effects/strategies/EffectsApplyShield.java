package combat.effects.strategies;

import bestiary.Monsters;
import bestiary.Skills;

import combat.effects.Effects;
import combat.effects.EffectsStrategy;

import combat.status.StatusManager;
import combat.status.StatusBase;

public class EffectsApplyShield implements EffectsStrategy {
	private StatusBase status;
	
    @Override
    public void aplicar(Monsters usuario, Monsters alvo, Skills habilidade, Effects efeito){
        if (alvo == null || usuario == null || efeito == null) return;
        
        int efeitoValor = efeito.getValor();
		
		int vidaAtual = usuario.getVidaAtual();
		int limiteEscudo = vidaAtual * 2;
		
		if (alvo.getEscudoAtual() >= limiteEscudo) return;
		
		status = StatusManager.getStatusPorId(0);
		if (status == null) return;
		
		if (!alvo.possuiStatus(status)){
			status.aplicar(alvo, 3);
        }else{
            StatusBase statusExistente = alvo.getStatusPorId(0);
            if (statusExistente != null) statusExistente.renovarDuração();
        }
		
        int valorEscudo = (int) Math.ceil(vidaAtual * (efeitoValor / 100.0));
		
		int novoEscudo = Math.min(limiteEscudo, alvo.getEscudoAtual()+valorEscudo);
		
		alvo.setEscudoAtual(novoEscudo);
    }
    
    @Override
    public String getNome(){
        return "APLICAR_ESCUDO";
    }
    
	//===
}