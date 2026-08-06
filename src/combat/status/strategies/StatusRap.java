package combat.status.strategies;

import bestiary.Monsters;
import bestiary.Skills;

import combat.status.StatusBase;
import combat.status.StatusData;

public class StatusRap extends StatusBase {
	private int duraçãoBase, duraçãoAtual;
	private boolean isAtivo;
	
    public StatusRap(StatusData dados){
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
		
		int speedAtualCombate = alvo.getSpeedAtualCombate();
		int speedBuffada = (int) Math.ceil(speedAtualCombate * 0.20);
		
		alvo.setSpeedAtualCombate(speedAtualCombate + speedBuffada);
		
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
			isAtivo = false;
			alvo.setSpeedAtualCombate(alvo.getSpeedAtual());
		}
	}
	
	@Override
	public void renovarDuração(){
		if (duraçãoBase <= 0) return;
		
		duraçãoAtual = duraçãoBase;
	}
	
	@Override
	public int getId(){
		return this.dados.getId();
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
        return this.dados.getNome();
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