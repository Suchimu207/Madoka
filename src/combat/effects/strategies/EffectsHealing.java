package combat.effects.strategies;

import bestiary.Monsters;
import bestiary.Skills;

import combat.effects.Effects;
import combat.effects.EffectsStrategy;

public class EffectsHealing implements EffectsStrategy {
    
    @Override
    public void aplicar(Monsters usuario, Monsters alvo, Skills habilidade, Effects efeito){
        if (alvo == null || usuario == null) return;
        
        int curaPorcentagem = efeito.getValor();
		if (curaPorcentagem <= 0) return;
		
		int vidaAtual = alvo.getVidaAtual();
		int vidaAtualCombate = alvo.getVidaAtualCombate();
        if (vidaAtualCombate <= 0) return;
		
		int curaRealizada = (int) Math.ceil(vidaAtual * (curaPorcentagem / 100.0));
		
		alvo.setVidaAtualCombate(Math.min(vidaAtual, vidaAtualCombate+curaRealizada));
    }
    
    @Override
    public String getNome(){
        return "HEALING";
    }
    
	//===
}