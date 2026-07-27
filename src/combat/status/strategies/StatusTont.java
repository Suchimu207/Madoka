package combat.status.strategies;

import bestiary.Monsters;
import bestiary.Skills;

import combat.status.StatusBase;
import combat.status.StatusData;

public class StatusTont extends StatusBase {
	private int duraçãoBase, duraçãoAtual;
	private boolean isAtivo;
	
    public StatusTont(StatusData dados){
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
		int forçaNerfada = (int) Math.ceil(forçaAtualCombate * 0.75);
		
		alvo.setForcaAtualCombate(forçaNerfada);
		
		for (int i = 0; i < alvo.getQuantidadeMaxSlotsHabilidade(); i++){
            Skills skill = alvo.getHabilidadeAtiva(i);
            if (skill != null) {
                int precisaoNerfada = (int) Math.ceil(skill.getPrecisaoAtual() * 0.75);
                skill.setPrecisaoAtual(precisaoNerfada);
            }
        }
		
		System.out.println("Debuff aplicado!");
		
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
			alvo.setForcaAtualCombate(alvo.getForcaAtual());
			
			for (int i = 0; i < alvo.getQuantidadeMaxSlotsHabilidade(); i++){
                Skills skill = alvo.getHabilidadeAtiva(i);
                if (skill != null){
                    skill.setPrecisaoAtual(skill.getPrecisaoBase());
                }
            }
			
			System.out.println("Debuf acabou!");
		}
	}
	
	@Override
	public void renovarDuração(){
		if (duraçãoBase <= 0) return;
		
		duraçãoAtual = duraçãoBase;
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
        return "Tontura";
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