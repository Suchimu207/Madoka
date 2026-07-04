package combat.effects;

public class Effects {
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