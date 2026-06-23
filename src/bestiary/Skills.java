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
	
	private int nivelNecessario;
	
	public Skills(int idHabilidade, String nomeHabilidade, Monsters.Elementos elementoHabilidade,
	TipoHabilidade tipoHabilidade, TipoAlvo alvoHabilidade, int poderHabilidade, int precisãoBase,
	int energiaHabilidade, int recargaHabilidade){
		try{
			if (idHabilidade <= 0){
				throw new IllegalArgumentException("ID deve ser maior do que 0.");
			}
		this.idHabilidade = idHabilidade;
		
		this.nomeHabilidade = nomeHabilidade;
		this.elementoHabilidade = elementoHabilidade;
		this.tipoHabilidade = tipoHabilidade;
		this.alvoHabilidade = alvoHabilidade;
		
			if (poderHabilidade < 0 || precisãoBase < 0 || energiaHabilidade < 0 || recargaHabilidade < 0){
				throw new IllegalArgumentException("Valores precisam ser iguais ou maiores do que zero.");
			}
		this.poderHabilidade = poderHabilidade;
		this.precisãoBase = precisãoBase;
		this.energiaHabilidade = energiaHabilidade;
		this.recargaHabilidade = recargaHabilidade;
		
		this.nivelNecessario = 1;
		}catch(IllegalArgumentException e){
			System.out.println("Erro ao criar habilidade ID_"+idHabilidade+": "+e.getMessage());
			System.exit(1);
		}
	}
	
	public Skills(Skills skillRequerida){
		this.idHabilidade = skillRequerida.getIdHabilidade();
		this.nomeHabilidade = skillRequerida.getNomeHabilidade();
		this.elementoHabilidade = skillRequerida.getElementoHabilidadeTipo();
		this.tipoHabilidade = skillRequerida.getTipoHabilidade();
		this.alvoHabilidade = skillRequerida.getAlvoHabilidadeTipo();
		this.poderHabilidade = skillRequerida.getPoderHabilidade();
		this.precisãoBase = skillRequerida.getPrecisaoBase();
		this.energiaHabilidade = skillRequerida.getEnergiaHabilidade();
		this.recargaHabilidade = skillRequerida.getRecargaHabilidade();
		this.nivelNecessario = skillRequerida.getNivelNecessario();
	}
	
	public Skills(){
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

	public Monsters.Elementos getElementoHabilidadeTipo(){
		return elementoHabilidade;
	}

	public TipoHabilidade getTipoHabilidade(){
		return tipoHabilidade;
	}
	
	public TipoAlvo getAlvoHabilidadeTipo(){
		return alvoHabilidade;
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
	
	public int getNivelNecessario(){
		return this.nivelNecessario;
	}
	
	public void setNivelNecessario(int nivelNecessario){
		this.nivelNecessario = nivelNecessario;
	}
	
	public boolean isTipoEspecial(TipoHabilidade tipoHabilidade){
		if (tipoHabilidade == TipoHabilidade.ESPECIAL){
			return true;
		}else return false;
	}
	
	//===
}