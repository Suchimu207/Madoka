package combat.status.strategies;

import bestiary.Monsters;

import combat.status.StatusBase;
import combat.status.StatusData;
import combat.status.StatusManager;

public class StatusImunAtord extends StatusBase {
	private int duraçãoBase, duraçãoAtual;
	private boolean isAtivo;

	private StatusBase statusImune;
	
    public StatusImunAtord(StatusData dados){
        super(dados);
		this.duraçãoBase = 0;
		this.duraçãoAtual = 0;
		this.isAtivo = false;
    }
	
    @Override
    public void aplicar(Monsters alvo, int duraçãoBase){
		if (duraçãoBase <= 0) return;
		
		this.duraçãoBase = duraçãoBase;
		this.duraçãoAtual = this.duraçãoBase;
		this.isAtivo = true;
		
		this.statusImune = StatusManager.getStatusPorId(4);
		
		alvo.receberImunidadeTemp(statusImune);
		alvo.receberStatus(this);
    }

    @Override
    public void checar(Monsters alvo){
		if (duraçãoAtual <= 0) return;
    }
	
	@Override
	public void reduzirDuração(Monsters alvo){
		duraçãoAtual -= 1;
		if (duraçãoAtual <= 0){
			alvo.removerImunidadeTemp(this.statusImune);
			isAtivo = false;
		}
	}
	
	@Override
	public void renovarDuração(){
		if (duraçãoBase <= 0) return;
	}
	
    @Override
    public boolean isAtivo(){
        return isAtivo;
    }

    @Override
    public boolean isPositivo(){
        return true; 
    }

    @Override
    public String getNome(){
        return "Imunidade a Atordoamento";
    }

    @Override
    public String getSubtipo(){
        return "..."; 
    }
	
	@Override
	public int getDuraçãoBase(){
		return this.duraçãoBase;
	}
	
	@Override
	public int getDuraçãoAtual(){
		return this.duraçãoAtual;
	}
	
	//===
}