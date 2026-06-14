package bestiary;

import java.util.Arrays;

public class Skills {
	protected enum TipoHabilidade{
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
	
	protected enum TipoAlvo{
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
	
	private int idHabilidade;
	private String nomeHabilidade;
	private String descriçãoHabilidade;
	
	private int poderHabilidade;
	private int precisãoBase;
	private int precisãoAtual;
	private int energiaHabilidade;
	
	private TipoHabilidade tipoHabilidade;
	private TipoAlvo alvoHabilidade;
	private Monsters.Elementos elementoHabilidade;
	
	private int recargaHabilidade;
	private int recargaAtual;
	
	public Skills(int idHabilidade, String nomeHabilidade, Monsters.Elementos elementoHabilidade,
	TipoHabilidade tipoHabilidade, TipoAlvo alvoHabilidade, int poderHabilidade, int precisãoBase,
	int energiaHabilidade, int recargaHabilidade){
		this.idHabilidade = idHabilidade;
		this.nomeHabilidade = nomeHabilidade;
		this.elementoHabilidade = elementoHabilidade;
		this.tipoHabilidade = tipoHabilidade;
		this.alvoHabilidade = alvoHabilidade;
		this.poderHabilidade = poderHabilidade;
		this.precisãoBase = precisãoBase;
		this.energiaHabilidade = energiaHabilidade;
		this.recargaHabilidade = recargaHabilidade;
	}
	
	public int getIdHabilidade(){
		return idHabilidade;
	}

	public String getNomeHabilidade(){
		return nomeHabilidade;
	}

	public String getElementoHabilidade(){
		return elementoHabilidade.toString();
	}

	public String getTipoHabilidade(){
		return tipoHabilidade.toString();
	}

	public String getAlvoHabilidade(){
		return alvoHabilidade.toString();
	}
	
	public int getPoderHabilidade(){
		return poderHabilidade;
	}

	public int getPrecisaoBase(){
		return precisãoBase;
	}

	public int getEnergiaHabilidade(){
		return energiaHabilidade;
	}

	public int getRecargaHabilidade(){
		return recargaHabilidade;
	}
	
	//===
}