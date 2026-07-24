package bestiary.traits.strategies;

import bestiary.Monsters;

import bestiary.traits.TraitEffect;

public class TraitStatusArmor implements TraitEffect {
    private final int reducaoPrecisao;
    private boolean ativo = true;
    
    public TraitStatusArmor(int reducaoPrecisao) {
        this.reducaoPrecisao = Math.max(0, Math.min(100, reducaoPrecisao));
    }
    
    @Override
    public void aplicar(Monsters monstro){
        if (monstro != null){
            // monstro.setStatusArmor(reducaoPrecisao);
        }
    }
    
	//===
}