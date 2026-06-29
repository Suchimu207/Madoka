package bestiary;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Map;

public class Monsters {
	protected enum Classes{
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
		AGUA("Água"),
		FISICO("Físico");
		
		private String nomeElemento;
		
		Elementos(String nomeElemento){
			this.nomeElemento = nomeElemento;
		}
		
		public String getElementoNome(){
			return nomeElemento;
		}
		
		/*
		Fogo -> Natureza -> Magia -> Metal -> Vento -> 
		Luz -> Trevas -> Terra -> Trovão -> Água.
		public boolean temVantagemContra(Elementos outro){
			if (this == FOGO && outro == NATUREZA) return true;
			if (this == AGUA && outro == FOGO) return true;
			return false;
		}
		*/
	}
	protected enum Raridades{
		COMUM("Comum"),
		INCOMUM("Incomum"),
		RARO("Raro"),
		EPICO("Épico"),
		LENDARIO("Lendário");
		
		private String nomeRaridade;
		
		Raridades(String nomeRaridade){
			this.nomeRaridade = nomeRaridade;
		}
		
		public String getRaridadeNome(){
			return nomeRaridade;
		}
	}
	private enum SlotHabilidade{
		SLOT_1,
		SLOT_2,
		SLOT_3,
		SLOT_4,
	}
	
	private int idMonstro;
	private String nomeMonstro;
	private int nivelBase, nivelAtual;
	private int expAtual;
	private boolean monstroEquipado, monstroFavorito;
	
	private int forçaBase, forçaAtual, forçaAtualCombate;
	private int vidaBase, vidaAtual, vidaAtualCombate;
	private int speedBase, speedAtual, speedAtualCombate;
	private int estaminaBase, estaminaAtual, estaminaAtualCombate;
	private int[] traçosIds;
	
	private Classes classeAtual;
	private Elementos[] elementosAtuais;
	private Raridades raridadeMonstro;
	
	private final int NIVEL_MAXIMO = 40;
	
	
	private Map<Integer, List<Skills>> skillsTree = new HashMap<>();
	private List<Skills> skillsDesbloqueadas = new ArrayList<>();
	private EnumMap<SlotHabilidade, Skills> skillsAtivas = new EnumMap<>(SlotHabilidade.class);	
	
	public Monsters(int idMonstro, String nomeMonstro, Classes classeAtual, Elementos[] elementosAtuais,
	Raridades raridadeMonstro, int nivelBase, int forçaBase, int vidaBase, int speedBase, int estaminaBase, int[] traçosIds){
		try{
			if (idMonstro <= 0){
				throw new IllegalArgumentException("ID deve ser maior do que 0.");
			}
			this.idMonstro = idMonstro;
			
			this.nomeMonstro = nomeMonstro;
			this.classeAtual = classeAtual;
			
			if (elementosAtuais.length >= 3){
				throw new IllegalArgumentException("O monstro não pode ter mais de dois elementos.");
			}
			this.elementosAtuais = elementosAtuais;
			this.raridadeMonstro = raridadeMonstro;
			
			if (nivelBase <= 0){
				throw new IllegalArgumentException("Nivel base deve ser maior do que 0.");
			}
			this.nivelBase = nivelBase;
			this.nivelAtual = nivelBase;
			
			this.expAtual = 0;
			
			if (forçaBase <= 0 || vidaBase <= 0 || speedBase <= 0 || estaminaBase <= 0){
				throw new IllegalArgumentException("Atributos base devem ser maiores do que 0.");
			}
			this.forçaBase = forçaBase;
			this.forçaAtual = this.forçaBase;
			
			this.vidaBase = vidaBase;
			this.vidaAtual = this.vidaBase;
			
			this.speedBase = speedBase;
			this.speedAtual = this.speedBase;
			
			this.estaminaBase = estaminaBase;
			this.estaminaAtual = this.estaminaBase;
			
			this.traçosIds = traçosIds;
			this.monstroEquipado = false;
			this.monstroFavorito = false;
		}catch(IllegalArgumentException e){
			System.out.println("Erro ao criar monstro ID_"+idMonstro+": "+e.getMessage());
			System.exit(1);
		}
	}
	
   public Monsters(Monsters monstroRequerido){
	   this.idMonstro = monstroRequerido.getIdMonstro();
	   this.nomeMonstro = monstroRequerido.getNomeMonstro();
	   this.classeAtual = monstroRequerido.getClasseAtual();
	   this.elementosAtuais = monstroRequerido.getElementosAtuaisValores();
	   this.raridadeMonstro = monstroRequerido.getRaridadeMonstro();
	   this.nivelBase = monstroRequerido.getNivelBase();
	   this.nivelAtual = monstroRequerido.getNivelAtual();
	   this.expAtual = monstroRequerido.getExpAtual();
	   this.forçaBase = monstroRequerido.getForcaBase();
	   this.forçaAtual = monstroRequerido.getForcaAtual();
	   this.vidaBase = monstroRequerido.getVidaBase();
	   this.vidaAtual = monstroRequerido.getVidaAtual();
	   this.speedBase = monstroRequerido.getSpeedBase();
	   this.speedAtual = monstroRequerido.getSpeedAtual();
	   this.estaminaBase = monstroRequerido.getEstaminaBase();
	   this.estaminaAtual = monstroRequerido.getEstaminaAtual();
	   this.traçosIds = monstroRequerido.getTracosIds();
	   this.monstroEquipado = false;
	   this.monstroFavorito = false;
	   
	   this.skillsTree = new HashMap<>();
	   for (Map.Entry<Integer, List<Skills>> entry : monstroRequerido.skillsTree.entrySet()){
			List<Skills> listaClonada = new ArrayList<>();
			for (Skills skillAtual : entry.getValue()){
				listaClonada.add(new Skills(skillAtual));
			}
			this.skillsTree.put(entry.getKey(), listaClonada);
		}
		
	   this.desbloquearHabilidades();
	}
	
	public void adicionarHabilidadeArvore(int nivel, Skills skill){
		// A função só é executada se a chave "nivel" não existir.
		this.skillsTree.computeIfAbsent(nivel, k -> new ArrayList<>()).add(skill);
	}
	
	public void desbloquearHabilidades(){
		for (int nivel = 1; nivel <= this.nivelAtual; nivel++){
			if (this.skillsTree.containsKey(nivel)){
				List<Skills> habilidadesDoNivel = this.skillsTree.get(nivel);
            
				for (Skills skill : habilidadesDoNivel){
					if (!this.skillsDesbloqueadas.contains(skill)){
						this.skillsDesbloqueadas.add(skill);
						
						for (SlotHabilidade slot : SlotHabilidade.values()){
							if (!skillsAtivas.containsKey(slot) && !skill.isTipoEspecial(skill.getTipoHabilidade())){
								skillsAtivas.put(slot, skill);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	public Map<Integer, List<Skills>> getHabilidadesArvore(){
		return skillsTree;
	}
	
	public Skills getHabilidadeArvoreId(int idHabilidade){
		for (List<Skills> lista : skillsTree.values()){
			for (Skills skill : lista){
				if (skill.getIdHabilidade() == idHabilidade){
					return skill;
				}
			}
		}
		return null;
	}
	
	public int getTamanhoSkillsTree(){
		int total = 0;
		for (List<Skills> lista : skillsTree.values()){
			total += lista.size();
		}
		return total;
	}
	
	public List<Skills> getHabilidadesDesbloqueadas(){
        return skillsDesbloqueadas;
    }
	
	public boolean isHabilidadeDesbloqueada(Skills habilidade){
		if (habilidade == null) return false;
		return skillsDesbloqueadas.contains(habilidade);
	}
	
	protected EnumMap<SlotHabilidade, Skills> getSkillsAtivas(){
		return skillsAtivas;
	}
	
	public SlotHabilidade getSlotPorIndice(int indice){
		SlotHabilidade[] slots = SlotHabilidade.values();
			if (indice >= 0 && indice < slots.length){
				return slots[indice];
			}
			return null;
	}
	
	public int getQuantidadeMaxSlotsHabilidade(){
		return SlotHabilidade.values().length;
	}
	
	public Skills getHabilidadeAtiva(int indice){
		SlotHabilidade slot = getSlotPorIndice(indice);
		return slot != null ? skillsAtivas.get(slot) : null;
	}
	
	public boolean isHabilidadeAtiva(Skills habilidade){
		if (habilidade == null) return false;
		
		for (Map.Entry<SlotHabilidade, Skills> entry : skillsAtivas.entrySet()){
			if (entry.getValue() == habilidade){
				return true;
			}
		}
		return false;
	}
	
	public boolean removerHabilidadeAtiva(Skills skill){
		for (Map.Entry<SlotHabilidade, Skills> entry : skillsAtivas.entrySet()){
			if (entry.getValue() == skill){
				skillsAtivas.remove(entry.getKey());
				return true;
			}
		}
		return false;
	}
	
	public void adicionarHabilidadeAtiva(Skills skill){
		for (SlotHabilidade slot : SlotHabilidade.values()){
			if (!skillsAtivas.containsKey(slot)){
				skillsAtivas.put(slot, skill);
				break;
			}
		}
	}
	
	public void reordenarSkillsAtivas(){
		List<Skills> lista = new ArrayList<>(skillsAtivas.values());
		skillsAtivas.clear();
		for (int i = 0; i < lista.size() && i < SlotHabilidade.values().length; i++){
			skillsAtivas.put(SlotHabilidade.values()[i], lista.get(i));
		}
	}
	
	public void ganharExp(int expGanha){
		if (this.nivelAtual >= NIVEL_MAXIMO) return;
		
		this.expAtual += expGanha;
		int expNecessaria = getExpNecessaria();
		
		while (this.expAtual >= expNecessaria && this.nivelAtual < NIVEL_MAXIMO){
			expAtual -= expNecessaria;
			subirNivel(1);
			
			if (nivelAtual < NIVEL_MAXIMO){
				expNecessaria = getExpNecessaria();
			}
		}
	}
	
	public int getExpNecessaria(){
		if (this.nivelAtual >= NIVEL_MAXIMO) return -1;
		return 100 * this.nivelAtual;
	}
	
	public void subirNivel(int quantidade){
		if (quantidade <= 0) return;
    
		int niveisReais = Math.min(quantidade, NIVEL_MAXIMO-this.nivelAtual);
    
		for (int i = 0; i < niveisReais; i++){
			this.nivelAtual++;
			this.forçaAtual += this.forçaBase;
			this.vidaAtual += this.vidaBase;
			this.speedAtual += this.speedBase;
		}
		this.desbloquearHabilidades();
	}
	
	public int getQuantidadeSlotsOcupados(){
		return skillsAtivas.size();
	}
	
	public int getIdMonstro(){
		return idMonstro;
	}

	public String getNomeMonstro(){
		return nomeMonstro;
	}
		
	public Classes getClasseAtual(){
		return classeAtual;
	}
	
	public Elementos[] getElementosAtuaisValores(){
		return elementosAtuais;
	}
	
	public String getElementosAtuais(){
		return Arrays.toString(elementosAtuais).replaceAll("[\\[\\]]", "");
	}
	
	public Raridades getRaridadeMonstro(){
		return raridadeMonstro;
	}
	
	public int getNivelBase(){
		return nivelBase;
	}
	
	public int getNivelAtual(){
		return nivelAtual;
	}
	
	public int getExpAtual(){
		return expAtual;
	}
	
	public int getForcaBase(){
		return forçaBase;
	}
	
	public int getForcaAtual(){
		return forçaAtual;
	}
	
	public int getForcaAtualCombate(){
		return forçaAtualCombate;
	}

	public int getVidaBase(){
		return vidaBase;
	}

	public int getVidaAtual(){
		return vidaAtual;
	}

	public int getVidaAtualCombate(){
		return vidaAtualCombate;
	}

	public int getSpeedBase(){
		return speedBase;
	}
	
	public int getSpeedAtual(){
		return speedAtual;
	}
	
	public int getSpeedAtualCombate(){
		return speedAtualCombate;
	}

	public int getEstaminaBase(){
		return estaminaBase;
	}
	
	public int getEstaminaAtual(){
		return estaminaAtual;
	}

	public int getEstaminaAtualCombate(){
		return estaminaAtualCombate;
	}

	public int[] getTracosIds(){
		return traçosIds;
	}
	
	public boolean isMonstroEquipado(){ 
		return monstroEquipado;
	}
	
	public boolean isMonstroFavorito(){ 
		return monstroFavorito;
	}
	
	public void setNivelAtual(int nivelAtual){
		this.nivelAtual = nivelAtual;
	}
	
	public void setForcaAtual(int forcaAtual){
		this.forçaAtual = forcaAtual;
	}

	public void setForcaAtualCombate(int forcaAtualCombate){
		this.forçaAtualCombate = forcaAtualCombate;
	}

	public void setVidaAtual(int vidaAtual){
		this.vidaAtual = vidaAtual;
	}

	public void setVidaAtualCombate(int vidaAtualCombate){
		if(vidaAtualCombate < 0){
			vidaAtualCombate = 0;
		}
		
		this.vidaAtualCombate = vidaAtualCombate;
	}

	public void setSpeedAtual(int speedAtual){
		this.speedAtual = speedAtual;
	}

	public void setSpeedAtualCombate(int speedAtualCombate){
		this.speedAtualCombate = speedAtualCombate;
	}

	public void setEstaminaAtual(int estaminaAtual){
		this.estaminaAtual = estaminaAtual;
	}

	public void setEstaminaAtualCombate(int estaminaAtualCombate){
		this.estaminaAtualCombate = estaminaAtualCombate;
	}

	public void setMonstroEquipado(boolean monstroEquipado){
		this.monstroEquipado = monstroEquipado;
	}
	
	public void setMonstroFavorito(boolean monstroFavorito){
		this.monstroFavorito = monstroFavorito;
	}
	
	//===
}