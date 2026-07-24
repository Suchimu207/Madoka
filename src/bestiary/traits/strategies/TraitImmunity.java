package bestiary.traits.strategies;

import bestiary.Monsters;

import bestiary.traits.TraitEffect;

import combat.status.StatusManager;
import combat.status.StatusBase;

public class TraitImmunity implements TraitEffect {
    private final StatusBase statusImune;
    
    public TraitImmunity(int valor){
        this.statusImune = StatusManager.getStatusPorId(valor);
    }
    
    @Override
    public void aplicar(Monsters monstro){
        if (monstro != null && statusImune != null){
            monstro.adicionarImunidadeTraço(statusImune);
        }
    }
	
	//===
}