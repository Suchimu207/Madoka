package world;

public final class NPC {
    private String nome;
    private int npcX, npcY;
    private String[] dialogo;
    private boolean isBatalha;
    private int idBatalha;
    
    public NPC(String nome, int npcX, int npcY, boolean isBatalha, int idBatalha){
        this.nome = nome;
        this.npcX = npcX;
        this.npcY = npcY;
        this.isBatalha = isBatalha;
        this.idBatalha = idBatalha;
        this.dialogo = new String[0];
    }
     
    public int getNpcX() { return npcX; }
    public int getNpcY() { return npcY; }
    public String getNome() { return nome; }
    public String[] getDialogo() { return dialogo; }
    public boolean isBatalha() { return isBatalha; }
    public int getIdBatalha() { return idBatalha; }
    
	public void setDialogo(String[] dialogo){ this.dialogo = dialogo; }
	
    //===
}