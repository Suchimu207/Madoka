package bestiary;

public class Skills {
	private enum TipoHabilidade{
		ESPECIAL("Especial"),
		OFENSIVA("Ofensiva"),
		DEFENSIVA("Defensiva"),
		NEUTRO("Neutra");
		
		private String nomeTipo;
		
		TipoHabilidade(String nomeTipo){
			this.nomeTipo = nomeTipo;
		}
		
		public String getTipoNome(){
			return nomeTipo;
		}
	}
	
	private enum TipoAlvo{
		USUARIO("Usuário"),
		ALIADO_UNICO("Um aliado"),
		ALIADO_AREA("Múltiplos aliados"),
		INIMIGO_UNICO("Um inimigo"),
		INIMIGO_AREA("Múltiplos inimigos"),
		CAMPO("Múltiplos alvos");
		
		private String nomeTipo;
		
		TipoAlvo(String nomeTipo){
			this.nomeTipo = nomeTipo;
		}
		
		public String getTipoNome(){
			return nomeTipo;
		}
	}
	
	private final String nomeHabilidade = "";
	private String descriçãoHabilidade;
	
	private final int precisãoBase = 0;
	private int precisãoAtual;
	
	private final int energiaHabilidade = 0;
	
	private final TipoHabilidade tipoHabilidade = TipoHabilidade.NEUTRO;
	private final TipoAlvo alvoHabilidade = TipoAlvo.ALIADO_UNICO;
	private final Monsters.Elementos elementoHabilidade = Monsters.Elementos.FOGO;
	
	private final int recargaHabilidade = 0;
	private int recargaAtual;
	
	public Skills(){
	}
	
	//===
}