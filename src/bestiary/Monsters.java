package bestiary;

public class Monsters {
	private enum Classes{
		ATACANTE("Atacante"),
		SUPORTE("Suporte"),
		GENERALISTA("Generalista"),
		SABOTADOR("Sabotador"),
		TANQUE("Tanque");
		
		private String nomeClasse;
		
		Classes(String nomeClasse){
			this.nomeClasse = nomeClasse;
		}
		
		public String getClasseNome(){
			return nomeClasse;
		}
	}
	
	protected enum Elementos{
		FOGO("Fogo"),
		NATUREZA("Natureza"),
		MAGIA("Magia"),
		METAL("Metal"),
		VENTO("Vento"),
		LUZ("Luz"),
		TREVAS("Trevas"),
		TERRA("Terra"),
		TROVAO("Trovão"),
		AGUA("Água");
		
		private String nomeElemento;
		
		Elementos(String nomeElemento){
			this.nomeElemento = nomeElemento;
		}
		
		public String getElementoNome(){
			return nomeElemento;
		}
	}
	
	private int forçaBase, forçaAtual, forçaAtualCombate;
	private int vidaBase, vidaAtual, vidaAtualCombate;
	private int speedBase, speedAtual, speedAtualCombate;
	private int estaminaBase, estaminaAtual, estaminaAtualCombate;
	private int nivelBase, nivelAtual;
	private int expAtual;
	private Classes classeAtual;
	private Elementos elementosAtuais;
	// private traçosAtuais.
	
	public Monsters(){
	}
	
	//===
}