package combat.status.strategies;

import bestiary.Monsters;

import combat.status.StatusBase;
import combat.status.StatusData;

public class StatusSang extends StatusBase {
	private int duraçãoBase, duraçãoAtual;
	private boolean isAtivo;
	
	private int forcaRemovida;
	
    public StatusSang(StatusData dados){
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
		
		int forçaAtualCombate = alvo.getForcaAtualCombate();
        int valorRemovido = (int) Math.ceil(forçaAtualCombate * 0.20);
        this.forcaRemovida = valorRemovido;
        
        alvo.setForcaAtualCombate(forçaAtualCombate - valorRemovido);
		
		alvo.receberStatus(this);
    }

    @Override
    public void checar(Monsters alvo){
		if (duraçãoAtual <= 0) return;
		
		int dano = (int) Math.ceil(alvo.getVidaAtualCombateMaxima() * (10 / 100.0));
		alvo.perderVidaSemEscudo(dano);
    }
	
	@Override
	public void reduzirDuração(Monsters alvo){
		duraçãoAtual -= 1;
		
		if (duraçãoAtual <= 0){
            isAtivo = false;
            int forcaAtual = alvo.getForcaAtualCombate();
            alvo.setForcaAtualCombate(forcaAtual + this.forcaRemovida);
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
        return false; 
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