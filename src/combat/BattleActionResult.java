package combat;

public class BattleActionResult {
	public static final int NÃO_EFETIVO = 0;
	public static final int EFETIVO = 1;
	public static final int SUPER_EFETIVO = 2;
	
    private int danoRealizado;
	private int efetividade;
	private boolean acerto;
	
    protected BattleActionResult(int danoRealizado, int efetividade, boolean acerto){
		this.danoRealizado = danoRealizado;
		this.efetividade = efetividade;
		this.acerto = acerto;
    }
	
	public int getDanoRealizado(){
		return danoRealizado;
	}
	
	public void setDanoRealizado(int danoRealizado){
		if (danoRealizado < 0) danoRealizado = 0;
		this.danoRealizado = danoRealizado;
	}
	
	public boolean isAcerto(){
		return acerto;
	}

	public void setAcerto(boolean acerto){
		this.acerto = acerto;
	}
	
	public int getEfetividade(){
		return efetividade;
	}
	
	public void setEfetividade(int efetividade){
		this.efetividade = efetividade;
	}
	
	//===
}