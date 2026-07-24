package bestiary.traits.strategies;

import bestiary.Monsters;

import bestiary.traits.TraitEffect;

public class TraitAttuned implements TraitEffect {
    private final int REGEN_PERCENT;
    
    public TraitAttuned(int valor){
		if (valor > 0){
			REGEN_PERCENT = valor;
		}else REGEN_PERCENT = 0;
    }
    
    @Override
    public void aplicar(Monsters monstro){
        if (monstro != null){
            monstro.setHarmonizado(REGEN_PERCENT);
        }
    }
    
	/*
    public void regenerarEstamina(Monsters monstro){
        if (monstro == null || !ativo) return;
        int estaminaMaxima = monstro.getEstaminaAtual();
        int regeneracao = (int) Math.ceil(estaminaMaxima * REGEN_PERCENT);
        int novaEstamina = Math.min(estaminaMaxima, 
        monstro.getEstaminaAtualCombate() + regeneracao);
        monstro.setEstaminaAtualCombate(novaEstamina);
    }
	*/
    
	//===
}