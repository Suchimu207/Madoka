package combat.status;

import bestiary.Monsters;

import java.util.Map;

public abstract class StatusBase {
	public StatusData dados;
	
    public StatusBase(StatusData dados){
		this.dados = dados;
    }
	
	public abstract void aplicar(Monsters alvo, int duraçãoBase);
    public abstract void checar(Monsters alvo); // Começo do turno.
	public abstract void reduzirDuração(Monsters alvo); // Final do turno.
	public abstract void renovarDuração();
	public boolean impedeAção(){
		return false;
	}
	public int getId(){
		return dados.getId();
	}
	public abstract boolean isAtivo();
	public abstract boolean isPositivo();
	public abstract String getNome();
	public abstract String getSubtipo();
	public abstract int getDuraçãoBase();
	public abstract int getDuraçãoAtual();
	
	//===
}