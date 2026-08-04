package combat;

import bestiary.Monsters;

public final class BattleUnit {
	private static int contadorId = 0;
	
	private Monsters monstro;
	private boolean aliado;
	private boolean alvo;
	private int id;
	private int speed;
	private int actionValueAtual;
	
	private final int BASE_ACTION_VALUE_CONSTANT = 1000;
	
	protected BattleUnit(Monsters monstro, boolean aliado){
		this.id = ++contadorId;
		this.monstro = monstro;
		this.aliado = aliado;
		this.alvo = false;
		this.actionValueAtual = calcularActionValue();
	}
	
	protected int calcularActionValue(){
		if (this.monstro == null) return Integer.MAX_VALUE;
		this.speed = this.monstro.getSpeedAtualCombate();
		return BASE_ACTION_VALUE_CONSTANT / this.speed; 
	}
	
	public void avancarAção(double porcentagem){
		double reducao = this.actionValueAtual * (porcentagem / 100.0);
		this.actionValueAtual = (int) (Math.max(0, this.actionValueAtual - reducao));
	}
	
	protected int getId(){ 
		return id; 
	}
	
	protected Monsters getMonstro(){ 
		return monstro; 
	}
	
	protected int getActionValue(){ 
		return actionValueAtual; 
	}
	
	protected boolean isAliado(){ 
		return aliado;
	}
	
	protected boolean isAlvo(){ 
		return alvo;
	}
	
	protected void setAlvo(boolean alvo){
		this.alvo = alvo;
	}
	
	protected void setActionValueAtual(int actionValueAtual){
		this.actionValueAtual = actionValueAtual;
	}
	
	//===
}