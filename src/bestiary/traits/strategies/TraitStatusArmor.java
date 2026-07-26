package bestiary.traits.strategies;

import bestiary.Monsters;

import bestiary.traits.TraitEffect;

public class TraitStatusArmor implements TraitEffect {
    private final int STATUS_ARMOR;
    
    public TraitStatusArmor(int STATUS_ARMOR){
		if (STATUS_ARMOR < 0) STATUS_ARMOR = 0;
        this.STATUS_ARMOR = STATUS_ARMOR;
    }
    
    @Override
    public void aplicar(Monsters monstro){
        if (monstro != null){
            monstro.setStatusArmor(STATUS_ARMOR);
        }
    }
    
	//===
}