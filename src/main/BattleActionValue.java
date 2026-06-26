package main;

import bestiary.Monsters;

public final class BattleActionValue implements Comparable<BattleActionValue> {
	private Monsters monstro;
    private int tempoEspera;

    protected BattleActionValue(Monsters monstro, int tempoEspera){
        this.monstro = monstro;
        this.tempoEspera = tempoEspera;
    }
	
    @Override
    public int compareTo(BattleActionValue outro){
        return Integer.compare(this.tempoEspera, outro.tempoEspera);
    }
	
	//===
}