package combat.status.strategies;

import bestiary.Monsters;
import combat.status.StatusBase;
import combat.status.StatusData;

public class StatusDamageIncreased extends StatusBase {
    private final double porcentagemBônus;
    
    private int duraçãoBase, duraçãoAtual;
    private boolean isAtivo;
	
    public StatusDamageIncreased(StatusData dados, double porcentagemBônus){
        super(dados);
        this.porcentagemBônus = porcentagemBônus;
        this.duraçãoBase = 0;
        this.duraçãoAtual = 0;
        this.isAtivo = false;
    }
	
    @Override
    public void aplicar(Monsters alvo, int duraçãoBase){
        if (duraçãoBase <= 0) return;
		
		double bônusAtual = alvo.getDamageBonus();
		bônusAtual += porcentagemBônus;
		
		alvo.setDamageBonus(bônusAtual);
		
        this.duraçãoBase = duraçãoBase;
        this.duraçãoAtual = duraçãoBase;
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
			double bônusAtual = alvo.getDamageBonus();
			bônusAtual -= porcentagemBônus;
			alvo.setDamageBonus(bônusAtual);
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
        return this.dados.getSubtipo();
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