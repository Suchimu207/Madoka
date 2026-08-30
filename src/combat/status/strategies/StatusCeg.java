package combat.status.strategies;

import bestiary.Monsters;
import bestiary.Skills;

import combat.status.StatusBase;
import combat.status.StatusData;

public class StatusCeg extends StatusBase {
	private int duraçãoBase, duraçãoAtual;
	private boolean isAtivo;
	
	private int[] precisaoRemovida;
	
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
		
		int maxSlots = alvo.getQuantidadeMaxSlotsHabilidade();
        this.precisaoRemovida = new int[maxSlots];
        
        for (int i = 0; i < maxSlots; i++){
            Skills skill = alvo.getHabilidadeAtiva(i);
            if (skill != null){
                int precisaoAtual = skill.getPrecisaoAtual();
                
                int valorRemovido = (int) Math.ceil(precisaoAtual * 0.50);
                
                this.precisaoRemovida[i] = valorRemovido;
                
                skill.setPrecisaoAtual(precisaoAtual - valorRemovido);
            }else{
                this.precisaoRemovida[i] = 0;
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
                if (skill != null && this.precisaoRemovida != null){
                    int precisaoAtual = skill.getPrecisaoAtual();
                    skill.setPrecisaoAtual(precisaoAtual + this.precisaoRemovida[i]);
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