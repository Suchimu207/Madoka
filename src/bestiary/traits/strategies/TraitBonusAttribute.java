package bestiary.traits.strategies;

import bestiary.Monsters;

import bestiary.traits.TraitEffect;

public class TraitBonusAttribute implements TraitEffect {
    private final double bonusPercentual;
    private final int atributoAlvo;
    
    public TraitBonusAttribute(int bonusPercentual, int atributoAlvo){
        this.bonusPercentual = bonusPercentual;
        this.atributoAlvo = atributoAlvo;
    }
    
    @Override
    public void aplicar(Monsters monstro){
        if (monstro == null) return;
        
        int bonus = 0;
        switch (atributoAlvo){
            case 0:
                bonus = (int) Math.ceil(monstro.getForcaBase() * (bonusPercentual / 100.0));
                monstro.setForcaAtual(monstro.getForcaAtual() + bonus);
                break;
            case 1:
                bonus = (int) Math.ceil(monstro.getVidaBase() * (bonusPercentual / 100.0));
                monstro.setVidaAtual(monstro.getVidaAtual() + bonus);
                break;
            case 2:
                bonus = (int) Math.ceil(monstro.getSpeedBase() * (bonusPercentual / 100.0));
                monstro.setSpeedAtual(monstro.getSpeedAtual() + bonus);
                break;
            case 3: 
				bonus = (int) Math.ceil(monstro.getEstaminaAtual() * (bonusPercentual / 100.0));
                monstro.setEstaminaAtual(monstro.getEstaminaAtual() + bonus);
				break;
        }
    }
    
	//===
}