package bestiary;

public class Status {
	private enum TipoStatus{
		POSITIVO("Positivo"),
		NEGATIVO("Negativo");
		
		private String nomeTipo;
		
		TipoStatus(String nomeTipo){
			this.nomeTipo = nomeTipo;
		}
		
		public String getTipoNome(){
			return nomeTipo;
		}
	}
	
	private final TipoStatus tipoStatus = TipoStatus.POSITIVO;
	
	public Status(){
	}
	
	//===
}