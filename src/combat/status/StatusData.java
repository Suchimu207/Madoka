package combat.status;

public class StatusData {
	public enum TipoStatus{
		POSITIVE("Positivo"),
		NEGATIVE("Negativo");
		
		private final String nomeTipo;
		
		TipoStatus(String nomeTipo){
			this.nomeTipo = nomeTipo;
		}
		
		public String getTipoNome(){
			return nomeTipo;
		}
	}
	public enum SubTipoStatus{
		CONTROL("Controle"),
		DOT("DoT"),
		HOT("HoT"),
		BUFF("Buff"),
		DEBUFF("Debuff");
		
		private final String nomeSubTipo;
		
		SubTipoStatus(String nomeSubTipo){
			this.nomeSubTipo = nomeSubTipo;
		}
		
		public String getSubTipoNome(){
			return nomeSubTipo;
		}
	}
	
	private final int id;
    private final String nome;
	private final TipoStatus tipo;
	private final SubTipoStatus subtipo;
	
    public StatusData(int id, String nome, String tipo, String subtipo){
        this.id = id;
        this.nome = nome;
        this.tipo = TipoStatus.valueOf(tipo);
        this.subtipo = SubTipoStatus.valueOf(subtipo);
    }
	
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTipo() { return tipo.getTipoNome(); }
    public String getSubtipo() { return subtipo.getSubTipoNome(); }
	
	//===
}