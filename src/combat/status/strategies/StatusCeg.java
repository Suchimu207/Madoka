package combat.status.strategies;

import bestiary.Monsters;
import bestiary.Skills;

import combat.status.StatusBase;
import combat.status.StatusData;

public class StatusCeg extends StatusBase {
	private int duraçãoBase, duraçãoAtual;
	private boolean isAtivo;
	
    public StatusCeg(StatusData dados){
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
		
		for (int i = 0; i < alvo.getQuantidadeMaxSlotsHabilidade(); i++){
            Skills skill = alvo.getHabilidadeAtiva(i);
            if (skill != null) {
                int precisaoNerfada = (int) Math.ceil(skill.getPrecisaoAtual() * 0.50);
                skill.setPrecisaoAtual(precisaoNerfada);
            }
        }
		
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
			
			for (int i = 0; i < alvo.getQuantidadeMaxSlotsHabilidade(); i++){
                Skills skill = alvo.getHabilidadeAtiva(i);
                if (skill != null){
                    skill.setPrecisaoAtual(skill.getPrecisaoBase());
                }
            }
			
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