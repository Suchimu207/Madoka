package combat;

public class BattleActionResult {
    private int danoRealizado;
	private boolean acerto;
	
    protected BattleActionResult(int danoRealizado, boolean acerto){
		this.danoRealizado = danoRealizado;
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
	
	//===
}