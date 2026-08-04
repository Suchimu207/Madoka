package combat.status.strategies;

import bestiary.Monsters;

import combat.status.StatusBase;
import combat.status.StatusData;

public class StatusDoubleLife extends StatusBase {
	private int duraçãoBase, duraçãoAtual;
	private boolean isAtivo;
	
    public StatusDoubleLife(StatusData dados){
        super(dados);
		this.duraçãoBase = 0;
		this.duraçãoAtual = 0;
		this.isAtivo = false;
    }
	
    @Override
    public void aplicar(Monsters alvo, int duraçãoBase){
		if (duraçãoBase <= 0) return;
		
		
		int vidaAtualCombate = alvo.getVidaAtualCombate();
		int vidaAtualCombateMaxima = alvo.getVidaAtualCombateMaxima();
		
        if (vidaAtualCombate > 0){
			alvo.setVidaAtualCombate(vidaAtualCombate * 2);
			alvo.setVidaAtualCombateMaxima(vidaAtualCombateMaxima * 2);
		}
		
		this.duraçãoBase = duraçãoBase;
		this.duraçãoAtual = this.duraçãoBase;
		this.isAtivo = true;
		
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
			int vidaAtualCombateMaxima = alvo.getVidaAtualCombateMaxima();
			alvo.setVidaAtualCombateMaxima(vidaAtualCombateMaxima/2);
			isAtivo = false;
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