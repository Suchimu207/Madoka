package combat.status.strategies;

import bestiary.Monsters;
import combat.status.StatusBase;
import combat.status.StatusData;

public class StatusFraquezaElemental extends StatusBase {
    private final Monsters.Elementos elementoFraqueza;
    
    private int duraçãoBase, duraçãoAtual;
    private boolean isAtivo;

    public StatusFraquezaElemental(StatusData dados, Monsters.Elementos elementoFraqueza){
        super(dados);
        this.elementoFraqueza = elementoFraqueza;
        this.duraçãoBase = 0;
        this.duraçãoAtual = 0;
        this.isAtivo = false;
    }

    public Monsters.Elementos getElementoFraqueza(){
        return elementoFraqueza;
    }

    @Override
    public void aplicar(Monsters alvo, int duraçãoBase){
        if (duraçãoBase <= 0) return;

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
        return false;
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