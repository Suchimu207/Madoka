package combat.effects;

public class Effects {
	public static final int MESMO_ALVO = -1;
    public static final int ALIADO_UNICO = 0;
    public static final int ALIADO_AREA = 1;
    public static final int INIMIGO_UNICO = 2;
    public static final int INIMIGO_AREA = 3;
    public static final int USUARIO = 4;
	
    private final String tipo;
    private final int alvo;
    private final int valor;
    private final int chance;

    public Effects(String tipo, int alvo, int valor, int chance){
        this.tipo = tipo;
        this.alvo = alvo;
        this.valor = valor;
        this.chance = chance;
    }

    public String getTipo() { return tipo; }
    public int getAlvo() { return alvo; }
    public int getValor() { return valor; }
    public int getChance() { return chance; }
	
	//===
}