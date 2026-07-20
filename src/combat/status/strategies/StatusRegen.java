package combat.status.strategies;

import bestiary.Monsters;

import combat.status.StatusBase;
import combat.status.StatusData;

public class StatusRegen extends StatusBase {
	private int duraçãoBase, duraçãoAtual;
	private boolean isAtivo;
	
    public StatusRegen(StatusData dados){
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
		
		alvo.receberStatus(this);
		
		System.out.println(dados.getNome()+" aplicada. Turnos: "+duraçãoBase);
    }

    @Override
    public void checar(Monsters alvo){
		if (duraçãoAtual <= 0) return;
		
		int cura = (int) Math.ceil(alvo.getVidaAtual() * (20 / 100.0));
		alvo.ganharVida(cura);
    }

	@Override
	public void reduzirDuração(Monsters alvo){
		duraçãoAtual -= 1;
		
		if (duraçãoAtual <= 0) isAtivo = false;
		
		System.out.println("Turno: "+duraçãoAtual);
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
        return "...";
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