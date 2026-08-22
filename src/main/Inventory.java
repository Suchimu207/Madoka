package main;

import static main.Terminal.mudarEstado;

import bestiary.Monsters;
import manager.MonstersManager;
import bestiary.Skills;

import combat.description.SkillDescription;

import util.GameState;
import util.Grapchics;
import util.Input;

import world.Maps;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Comparator;

import java.util.Set;

import java.awt.event.KeyEvent;

import java.awt.Color;

public final class Inventory implements GameState{
	protected static enum SlotEquipe{
        SLOT_1,
        SLOT_2,
        SLOT_3,
        SLOT_4,
        SLOT_5,
        SLOT_6;
    }
	private enum SubEstadosInventário{
		DETALHES("Detalhes"),
		HABILIDADES("Habilidades");
		
		private final String nome;
		
		SubEstadosInventário(String nome){
			this.nome = nome;
		}
		
		public String getSubEstadoNome(){
			return nome;
		}
	}
	
	// ==================== ATRIBUTOS ====================
	
	private static Map<Integer, Monsters> monstrosInventario;
    private static List<Monsters> monstrosOrdenados;
    private static List<Skills> skillsTree, skillsDesbloqueadas;
	private static EnumMap<SlotEquipe, Monsters> equipeTabela;
    private static SubEstadosInventário subEstadoAtual = null;
	private static SlotEquipe slotEncontrado;

    private static Monsters monstroCarregado;
	private static Skills skillCarregada, skillMostrada;
	
	private static int idMonstroSelecionado = 1;
	
    private static int idInventario, tamanhoInventario, linhaAtual, paginaAtual, totalPaginas,
    inicioLista, fimLista, posiçãoLinhaInventário, posiçãoLinhaEquipe, posiçãoLinhaSkillsAtivas;
    private static String nomeMonstroExibido;
	
	public Inventory(){
		Inventory.subEstadoAtual = null;
	}
	
	// ==================== INICIALIZAÇÃO ====================
	
	public final static void inicializarInventario(){
        monstrosInventario = new HashMap<Integer, Monsters>();
        monstrosOrdenados = new ArrayList<Monsters>();
        equipeTabela = new EnumMap<>(SlotEquipe.class);
		skillsTree = new ArrayList<>();
		skillsDesbloqueadas = new ArrayList<>();
		
        idInventario = 1;
        paginaAtual = 1;
        inicioLista = 1;
        fimLista = 1;
		
		System.out.println(">>Inventário inicializado.");
		System.out.println("");
    }
	
	// ==================== ESTADO ====================
	
	@Override
	public void desenhaEstado(){
		Grapchics.limpaTela();
		
		if (subEstadoAtual == null){
			Inventory.desenhaInventário();
		}else if (subEstadoAtual == SubEstadosInventário.DETALHES){
			Inventory.desenhaMonstroDetalhes();
		}else if (subEstadoAtual == SubEstadosInventário.HABILIDADES){
			Inventory.desenhaHabilidadeDetalhes();
		}
		
		Grapchics.atualizarTela();
	}
	
	@Override
    public void recebeComando(int tecla, Set<Integer> teclasPressionadas){
		switch (tecla){
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				teclaEsquerda();
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				teclaDireita();
				break;
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				teclaCima();
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				teclaBaixo();
				break;
			case KeyEvent.VK_ENTER:
				teclaEnter();
				break;
			case KeyEvent.VK_SHIFT:
				teclaShift();
				break;
			case KeyEvent.VK_E:
				teclaInventário();
				break;
		}
	}
	
	// ==================== TECLAS ====================
	
	private void teclaEsquerda(){
		if (subEstadoAtual == null){
			Inventory.alternarPagina(false);
		}else if (subEstadoAtual == SubEstadosInventário.DETALHES){
			Input.decrementarCursorX();
			idMonstroSelecionado = Input.getCursorX();
		}else if (subEstadoAtual == SubEstadosInventário.HABILIDADES){
			Input.decrementarCursorX();
		}
	}
	
	private void teclaDireita(){
		if (subEstadoAtual == null){
			Inventory.alternarPagina(true);
		}else if (subEstadoAtual == SubEstadosInventário.DETALHES){
			Input.incrementarCursorX();
			idMonstroSelecionado = Input.getCursorX();
		}else if (subEstadoAtual == SubEstadosInventário.HABILIDADES){
			Input.incrementarCursorX();
		}
	}
	
	private void teclaCima(){
		Input.decrementarCursorY();
	}
	
	private void teclaBaixo(){
		Input.incrementarCursorY();
	}
	
	private void teclaEnter(){
		if (subEstadoAtual == null){
			Inventory.alternarMonstroTabela(idMonstroSelecionado);
		}else if (subEstadoAtual == SubEstadosInventário.DETALHES){
			Inventory.alternarMonstroFavorito(idMonstroSelecionado);
		}else if (subEstadoAtual == SubEstadosInventário.HABILIDADES){
			Inventory.alternarHabilidadeAtiva();
		}
	}
	
	private void teclaShift(){
		if (subEstadoAtual == null){
			Input.setCursorX(idMonstroSelecionado);
			subEstadoAtual = SubEstadosInventário.DETALHES;
		}else if (subEstadoAtual == SubEstadosInventário.DETALHES){
			Input.setCursorAnteriorY(Input.getCursorY());
			subEstadoAtual = SubEstadosInventário.HABILIDADES;
		}
	}
	
	private void teclaInventário(){
		if (subEstadoAtual == null){
			subEstadoAtual = null;
			mudarEstado(new Maps());
		}else if (subEstadoAtual == SubEstadosInventário.DETALHES){
			subEstadoAtual = null;
		}else if (subEstadoAtual == SubEstadosInventário.HABILIDADES){
			Input.setCursorY(Input.getCursorAnteriorY());
			subEstadoAtual = SubEstadosInventário.DETALHES;
		}
	}
	
	// ==================== DESENHO ====================
	
	private static void desenhaInventário(){
		reordenarListaInventario();
		if (monstrosInventario.isEmpty()){
			Grapchics.desenhaCentroTTF("Inventário vazio.", 10, Grapchics.BRANCO_CLARO);
			Grapchics.atualizarTela();
			return;
		}
		
		tamanhoInventario = monstrosInventario.size();
		totalPaginas = (int) Math.ceil(tamanhoInventario / 24.0);
		
		String pag = "Inventário - Página";
		String barra = (char)47+"";
		int tamanhoPag = pag.length();
		
		Grapchics.desenhaCentroTTF(pag,0, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela(" "+paginaAtual+barra+totalPaginas,tamanhoPag+10, 0, Grapchics.BRANCO_CLARO);
		
		Grapchics.desenhaTTF("E: Voltar", 0, 1, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("Enter: Equipar/Desequipar", 0, 2, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("Shift: Ver detalhes", 0, 3, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTela("____________________",0,4, Grapchics.PRETO_CLARO);
		desenhaListaInventário();
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaInventário, Grapchics.PRETO_CLARO);
		
		posiçãoLinhaEquipe = 33;
		Grapchics.desenhaCentroTTF("Equipe:",31, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("____________________",0,32, Grapchics.PRETO_CLARO);
		for (SlotEquipe slot : SlotEquipe.values()){
			Monsters monstroEquipe = equipeTabela.get(slot);
			
			if (monstroEquipe != null){
				nomeMonstroExibido = monstroEquipe.getNomeMonstro()+" Nv"+monstroEquipe.getNivelAtual();
				Grapchics.desenhaTTF(nomeMonstroExibido, 0, posiçãoLinhaEquipe++, Grapchics.BRANCO_CLARO);
			}else{
				Grapchics.desenhaTTF("[Vazio]", 0, posiçãoLinhaEquipe++, Grapchics.PRETO_CLARO);
			}
		}
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaEquipe, Grapchics.PRETO_CLARO);
	}
	
	private static void desenhaListaInventário(){
		inicioLista = (paginaAtual - 1) * 24;
		fimLista = Math.min(inicioLista + 24, monstrosOrdenados.size());
		
		if (Input.getCursorY() < inicioLista + 1) Input.setCursorY(fimLista);
		if (Input.getCursorY() > fimLista) Input.setCursorY(inicioLista+1);
		
		posiçãoLinhaInventário = 5;
		for (int i = inicioLista+1; i <= fimLista; i++){
			Monsters monstro = monstrosInventario.get(i);
			if (monstro == null) continue;

			boolean selecionado = (i == Input.getCursorY());
			int indicadorEquipado = monstro.isMonstroEquipado() ? 69 : 0; 
			int indicadorFavorito = monstro.isMonstroFavorito() ? 3 : 0;
		
			if (selecionado){
				idMonstroSelecionado = i;
				
				nomeMonstroExibido = monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual();
			
				Grapchics.desenhaHibrido(nomeMonstroExibido, indicadorEquipado, indicadorFavorito, 1, posiçãoLinhaInventário++,
				Grapchics.AMARELO_CLARO);
				
			}else{
				if (monstro.isMonstroEquipado() || monstro.isMonstroFavorito()){
					nomeMonstroExibido = monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual();
					
					Grapchics.desenhaHibrido(nomeMonstroExibido, indicadorEquipado, indicadorFavorito, 0, posiçãoLinhaInventário++, 
					Grapchics.BRANCO_CLARO);
					
				}else{
					nomeMonstroExibido = monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual();
					
					Grapchics.desenhaHibrido(nomeMonstroExibido, indicadorEquipado, indicadorFavorito, 0, posiçãoLinhaInventário++, 
					Grapchics.PRETO_CLARO);
				}
			}
		}
	}
	
	private static void desenhaMonstroDetalhes(){	
		if (Input.getCursorX() <= 0) Input.setCursorX(1);
		
		int tamanho = getTamanhoInventario();
		if (Input.getCursorX() > tamanho) Input.setCursorX(tamanho);
		
		monstroCarregado = getMonstroInventario(Input.getCursorX());
		if (monstroCarregado == null) return;
		
		int indicadorFavorito = monstroCarregado.isMonstroFavorito() ? 3 : 0;
		
		Grapchics.desenhaCentroTTF("Detalhes - Inventário",0, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF("E: Voltar", 0, 1, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("Enter: Marcar/Desmarcar favorito",0,2, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("Shift: Ver habilidades",0,3, Grapchics.PRETO_CLARO);
		
		Grapchics.desenhaTela("____________________",0,4, Grapchics.PRETO_CLARO);
		
		Grapchics.desenhaHibrido("Nome: "+monstroCarregado.getNomeMonstro(),indicadorFavorito,0,5, Grapchics.BRANCO_CLARO);
		
		Grapchics.desenhaTTF("Nível: "+monstroCarregado.getNivelAtual(),0,6, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF("Classe: "+monstroCarregado.getClasseAtualTexto(),0,7, Grapchics.BRANCO_CLARO);
		desenhaElementoMonstro(8);
		Grapchics.desenhaTTF("Raridade: "+monstroCarregado.getRaridadeMonstroTexto(),0,9, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF("Força: "+monstroCarregado.getForcaAtual(),0,10, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF("Vida: "+monstroCarregado.getVidaAtual(),0,11, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF("Velocidade: "+monstroCarregado.getSpeedAtual(),0,12, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF("Estamina: "+monstroCarregado.getEstaminaAtual(),0,13, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF("Energia: "+monstroCarregado.getBarraEspecialMaximo(),0,14, Grapchics.BRANCO_CLARO);
		
		Grapchics.desenhaTTF("Traços: "+monstroCarregado.getNomesTraços(),0,15, Grapchics.BRANCO_CLARO);
		
		Grapchics.desenhaTela("____________________",0,16,Grapchics.PRETO_CLARO);
		
		Grapchics.desenhaTela("____________________",0,18,Grapchics.PRETO_CLARO);
		desenhaExp(19);
		Grapchics.desenhaTela("____________________",0,20,Grapchics.PRETO_CLARO);
		
		Grapchics.desenhaTTF("Habilidades:",0,22,Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("____________________",0,23,Grapchics.PRETO_CLARO);
		posiçãoLinhaSkillsAtivas = 24;
		desenhaListaHabilidade();
	}
	
	private static void desenhaExp(int linha){
		if (monstroCarregado.isNivelMaximo()){
			Grapchics.desenhaTTF("NÍVEL MÁXIMO",0,linha, Grapchics.BRANCO_CLARO);
		}else{
			String nextLevel = "Próximo nível: "+monstroCarregado.getExpAtual();
			int tamanhoTexto = nextLevel.length();
			
			Grapchics.desenhaTTF(nextLevel,0,linha, Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTela(""+(char)47+monstroCarregado.getExpNecessaria(),tamanhoTexto,linha, Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTTF(monstroCarregado.getExpNecessaria()+" EXP",tamanhoTexto+1,linha, Grapchics.BRANCO_CLARO);
		}
	}
	
	private static void desenhaElementoMonstro(int linha){
		Grapchics.desenhaTTF("Elementos: ", 0, linha, Grapchics.BRANCO_CLARO);

		Monsters.Elementos[] elementos = monstroCarregado.getElementosAtuaisValores();
		if (elementos == null || elementos.length == 0) return;

		int colunaX = 11;

		for (int i = 0; i < elementos.length; i++){
			Monsters.Elementos elemento = elementos[i];
			if (elemento == null) continue;

			String nomeElemento = elemento.getElementoNome();
			Color corElemento = monstroCarregado.getCorDoElemento(elemento.name());
			
			Grapchics.desenhaTTF(nomeElemento, colunaX, linha, corElemento);
			colunaX += nomeElemento.length();
			
			if (i < elementos.length - 1){
				Grapchics.desenhaTela((char)47, colunaX, linha, Grapchics.BRANCO_CLARO);
				colunaX += 1;
			}
		}
	}
	
	private static void desenhaListaHabilidade(){
		for (int i = 0; i < monstroCarregado.getQuantidadeMaxSlotsHabilidade(); i++){
			skillCarregada = monstroCarregado.getHabilidadeAtiva(i);
			if (skillCarregada != null){
				Grapchics.desenhaTTF((i+1)+": "+skillCarregada.getNomeHabilidade(),0,posiçãoLinhaSkillsAtivas,Grapchics.BRANCO_CLARO);
				Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade(),3,posiçãoLinhaSkillsAtivas++,skillCarregada.getCorHabilidade());
			}else{
				Grapchics.desenhaTTF("[VAZIO]",0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
			}
		}
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas,Grapchics.PRETO_CLARO);
		
		skillCarregada = monstroCarregado.getHabilidadeEspecial();
		if (skillCarregada != null && skillCarregada.isTipoEspecial(skillCarregada.getTipoHabilidade())){
			Grapchics.desenhaTTF("Especial:",0,posiçãoLinhaSkillsAtivas+2,Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas+3,Grapchics.PRETO_CLARO);
			Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade(),0,posiçãoLinhaSkillsAtivas+4,skillCarregada.getCorHabilidade());
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas+5,Grapchics.PRETO_CLARO);
		}
	}
	
	private static void desenhaHabilidadeDetalhes(){		
		if (Input.getCursorX() <= 0){
			Input.setCursorX(1);
		}else if (Input.getCursorX() >= getTamanhoInventario()) Input.setCursorX(getTamanhoInventario());
		
		monstroCarregado = getMonstroInventario(Input.getCursorX());
		if (monstroCarregado == null) return;
		
		if (Input.getCursorY() < 5){
			Input.setCursorY(posiçãoLinhaSkillsAtivas-1);
		}else if (Input.getCursorY() > posiçãoLinhaSkillsAtivas-1){
			Input.setCursorY(5);
		}
		
		Grapchics.desenhaCentroTTF("Habilidades",0, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF("E: Voltar", 0, 1, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("Enter: Ativar/Desativar habilidade",0,2, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("Monstro: "+monstroCarregado.getNomeMonstro()+" Nv"+monstroCarregado.getNivelAtual(),0,3, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("____________________",0,4,Grapchics.PRETO_CLARO);
		
		posiçãoLinhaSkillsAtivas = 5;
		linhaAtual = posiçãoLinhaSkillsAtivas;
		for (int i = 0; i < monstroCarregado.getQuantidadeMaxSlotsHabilidade(); i++){
			skillCarregada = monstroCarregado.getHabilidadeAtiva(i);
			
			if (skillCarregada != null){
				if (Input.getCursorY() == linhaAtual){
					Grapchics.desenhaTTF((i+1)+": ",0,posiçãoLinhaSkillsAtivas,Grapchics.BRANCO_CLARO);
					Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade(),4,posiçãoLinhaSkillsAtivas++,Grapchics.AMARELO_CLARO);
					skillMostrada = skillCarregada;
				}else{
					Grapchics.desenhaTTF((i+1)+": ",0,posiçãoLinhaSkillsAtivas,Grapchics.BRANCO_CLARO);
					Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade(),3,posiçãoLinhaSkillsAtivas++,skillCarregada.getCorHabilidade());
				}
			}else{
				if (Input.getCursorY() == linhaAtual){
					Grapchics.desenhaTTF("[VAZIO]",0,posiçãoLinhaSkillsAtivas++, Grapchics.AMARELO_CLARO);
					skillMostrada = null;
				}else{
					Grapchics.desenhaTTF("[VAZIO]",0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
				}
			}
			linhaAtual++;
		}
		posiçãoLinhaSkillsAtivas = linhaAtual;
		
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
		listaArvoreHabilidades();
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
		posiçãoLinhaSkillsAtivas += 1;
		
		skillCarregada = monstroCarregado.getHabilidadeEspecial();
		
		if (skillCarregada != null){
			Grapchics.desenhaTTF("Especial:",0,posiçãoLinhaSkillsAtivas++,Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
			if (Input.getCursorY() == linhaAtual){
				Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade(),1,posiçãoLinhaSkillsAtivas++,Grapchics.AMARELO_CLARO);
				skillMostrada = skillCarregada;
			}else{
				Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade(),0,posiçãoLinhaSkillsAtivas++,skillCarregada.getCorHabilidade());
			}
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
		}
		
		posiçãoLinhaSkillsAtivas += 1;
		infoHabilidade();
		
		linhaAtual++;
		posiçãoLinhaSkillsAtivas = linhaAtual;
	}
	
	private static void listaArvoreHabilidades(){
		linhaAtual = posiçãoLinhaSkillsAtivas;
		
		Map<Integer, List<Skills>>  arvoreHabilidades = monstroCarregado.getHabilidadesArvore();
		if (arvoreHabilidades != null){
        List<Skills> todasSkills = new ArrayList<>();
        for (List<Skills> listaSkills : arvoreHabilidades.values()){
            if (listaSkills != null) {
                todasSkills.addAll(listaSkills);
            }
        }
		
        todasSkills.sort(Comparator.comparingInt(Skills::getNivelNecessario));

        for (Skills skill : todasSkills){
				skillCarregada = skill;
				if (skillCarregada == null) continue;
				
				if (!skillCarregada.isTipoEspecial(skillCarregada.getTipoHabilidade()) 
					&& !monstroCarregado.isHabilidadeAtiva(skillCarregada)){
					
					if (monstroCarregado.getNivelAtual() >= skillCarregada.getNivelNecessario()){
						if (Input.getCursorY() == linhaAtual){
							Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade() + " (Nv" + skillCarregada.getNivelNecessario() + ")",
							1, posiçãoLinhaSkillsAtivas++, Grapchics.AMARELO_CLARO);
							skillMostrada = skillCarregada;
						}else{
							int tamanhoString = skillCarregada.getNomeHabilidade().length();
							
							Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade(),
							0, posiçãoLinhaSkillsAtivas, skillCarregada.getCorHabilidade());
							Grapchics.desenhaTTF("(Nv" + skillCarregada.getNivelNecessario() + ")",
							tamanhoString + 1, posiçãoLinhaSkillsAtivas++, Grapchics.BRANCO_CLARO);
						}
					}else{
						if (Input.getCursorY() == linhaAtual){
							Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade() + " (Nv" + skillCarregada.getNivelNecessario() + ")",
							1, posiçãoLinhaSkillsAtivas++, Grapchics.AMARELO_CLARO);
							skillMostrada = skillCarregada;
						}else{
							Grapchics.desenhaTTF(skillCarregada.getNomeHabilidade() + " (Nv" + skillCarregada.getNivelNecessario() + ")",
							0, posiçãoLinhaSkillsAtivas++, Grapchics.PRETO_CLARO);
						}
					}
					linhaAtual++;
				}
			}
		}
		
		posiçãoLinhaSkillsAtivas = linhaAtual;
	}
	
	private static void infoHabilidade(){
		if (skillMostrada != null){
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
			posiçãoLinhaSkillsAtivas = SkillDescription.infoHabilidade(skillMostrada, posiçãoLinhaSkillsAtivas, false);
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
		}
	}
	
	// ==================== AÇÕES DO JOGADOR ====================
	
	private static void alternarHabilidadeAtiva(){
		if (monstroCarregado == null || skillMostrada == null) return;
		
		int maxSlots = monstroCarregado.getQuantidadeMaxSlotsHabilidade();
		int slotsOcupados = monstroCarregado.getQuantidadeSlotsOcupados();
		boolean isEspecial = skillMostrada.isTipoEspecial(skillMostrada.getTipoHabilidade());
		boolean isAtiva = monstroCarregado.isHabilidadeAtiva(skillMostrada);
		boolean isDesbloqueada = monstroCarregado.isHabilidadeDesbloqueada(skillMostrada);
		
		if (isEspecial){
			return; 
		}
		
		if (isAtiva && slotsOcupados >= 2){
			if (monstroCarregado.removerHabilidadeAtiva(skillMostrada)){
				monstroCarregado.reordenarSkillsAtivas(); 
			}
		}else if (!isAtiva && isDesbloqueada){
			monstroCarregado.adicionarHabilidadeAtiva(skillMostrada);
		}
	}
	
    private static void alternarMonstroTabela(int id){
        Monsters monstro = monstrosInventario.get(id);
        if (monstro == null)
            return;

        if (monstro.isMonstroEquipado()){
            slotEncontrado = null;
            for (Map.Entry<SlotEquipe, Monsters> entry : equipeTabela.entrySet()){
                if (entry.getValue() == monstro){
                    slotEncontrado = entry.getKey();
                    break;
                }
            }

            if (slotEncontrado != null && equipeTabela.size() >= 2){
                equipeTabela.remove(slotEncontrado);
                monstro.setMonstroEquipado(false);
                reordenarEquipe();
            }
        }else{
            for (SlotEquipe slot : SlotEquipe.values()){
                if (!equipeTabela.containsKey(slot)){
                    equipeTabela.put(slot, monstro);
                    monstro.setMonstroEquipado(true);
                    break;
                }
            }
        }
    }

    private static void alternarMonstroFavorito(int id){
        Monsters monstro = monstrosInventario.get(id);
        if (monstro == null) return;

        monstro.setMonstroFavorito(!monstro.isMonstroFavorito());
    }
	
    private static void alternarPagina(boolean avancar){
        if (avancar){
            paginaAtual++;
            if (paginaAtual > totalPaginas) paginaAtual = 1;
        }else{
            paginaAtual--;
            if (paginaAtual < 1) paginaAtual = totalPaginas;
        }
    }
	
	// ==================== MÉTODOS AUXILIARES ====================
	
	public static void adicionarMonstroInventário(int id){
		if (monstrosInventario == null) return;
		
        Monsters monstroRequerido = MonstersManager.getMonstro(id);
        monstroCarregado = new Monsters(monstroRequerido);
        monstrosInventario.put(idInventario++, monstroCarregado);
		
        for (SlotEquipe slot : SlotEquipe.values()){
            if (!equipeTabela.containsKey(slot)){
                equipeTabela.put(slot, monstroCarregado);
                monstroCarregado.setMonstroEquipado(true);
                break;
            }
        }
    }
	
    public static void removerMonstroInventário(int id){
        monstroCarregado = monstrosInventario.get(id);
        if (monstroCarregado == null) return;
		
        monstrosInventario.remove(id);
        monstroCarregado.setMonstroEquipado(false);

        slotEncontrado = null;
        for (Map.Entry<SlotEquipe, Monsters> entry : equipeTabela.entrySet()){
            if (entry.getValue() == monstroCarregado){
                slotEncontrado = entry.getKey();
                break;
            }
        }
        if (slotEncontrado != null){
            equipeTabela.remove(slotEncontrado);
            reordenarEquipe();
        }

        // Reordena inventário.
        Monsters[] monstrosAtuais = monstrosInventario.values().toArray(new Monsters[0]);
        monstrosInventario.clear();
        idInventario = 1;
        for (int i = 0; i < monstrosAtuais.length; i++){
            monstrosInventario.put(idInventario++, monstrosAtuais[i]);
        }
    }
	
	private static void removerTodosMonstrosInventário(int id){
        monstroCarregado = monstrosInventario.get(id);
        if (monstroCarregado == null) return;
		
        monstrosInventario.remove(id);
        monstroCarregado.setMonstroEquipado(false);

        slotEncontrado = null;
        for (Map.Entry<SlotEquipe, Monsters> entry : equipeTabela.entrySet()){
            if (entry.getValue() == monstroCarregado){
                slotEncontrado = entry.getKey();
                break;
            }
        }
        if (slotEncontrado != null){
            equipeTabela.remove(slotEncontrado);
        }
    }
	
	private static void reordenarEquipe(){
		Monsters[] monstrosAtuais = equipeTabela.values().toArray(new Monsters[0]);
		equipeTabela.clear();
		
		SlotEquipe[] slots = SlotEquipe.values();
		for (int i = 0; i < monstrosAtuais.length && i < slots.length; i++){
			equipeTabela.put(slots[i], monstrosAtuais[i]);
		}
	}
	
	private static void reordenarListaInventario(){
        monstrosOrdenados.clear();

        for (int i = 1; i <= monstrosInventario.size(); i++){
            monstroCarregado = monstrosInventario.get(i);
            if (monstroCarregado != null && (monstroCarregado.isMonstroEquipado() || monstroCarregado.isMonstroFavorito())){
                monstrosOrdenados.add(monstroCarregado);
            }
        }

        for (int i = 1; i <= monstrosInventario.size(); i++){
            monstroCarregado = monstrosInventario.get(i);
            if (monstroCarregado != null && !monstroCarregado.isMonstroEquipado() && !monstroCarregado.isMonstroFavorito()){
                monstrosOrdenados.add(monstroCarregado);
            }
        }

        monstrosInventario.clear();
        idInventario = 1;
        for (Monsters m : monstrosOrdenados){
            monstrosInventario.put(idInventario++, m);
        }
    }
	
	// ==================== OUTROS ====================
	
	protected static Map<Integer, Monsters> getMonstrosInventario(){
        return monstrosInventario;
    }
	
    protected static EnumMap<SlotEquipe, Monsters> getEquipeTabela(){
        return equipeTabela;
    }

    public static int getTamanhoInventario(){
        return monstrosInventario != null ? monstrosInventario.size() : 0;
    }

    public static Monsters getMonstroInventario(int id){
        return monstrosInventario != null ? monstrosInventario.get(id) : null;
    }
	
	public static void preencherInventario(){
        if (monstrosInventario == null) return;
		
		int monstrosExistentes = MonstersManager.getMonstrosExistentesTamanho();
		for (int i = 1; i <= monstrosExistentes; i++){
			adicionarMonstroInventário(i);
		}
    }
	
	public static void limparInventario(){
        if (monstrosInventario == null) return;
		
		int tamanhoInventárioAtual = getTamanhoInventario();
		for (int i = 1; i <= tamanhoInventárioAtual; i++){
			removerTodosMonstrosInventário(i);
		}
    }
	
	public static List<Monsters> getEquipeLista(){
		List<Monsters> lista = new ArrayList<>();
		for (SlotEquipe slot : SlotEquipe.values()){
			lista.add(equipeTabela.get(slot));
		}
		return lista;
	}
	
	public static int getTamanhoEquipe(){
		return equipeTabela.size();
	}

	public static boolean temSlotVazio(){
		return equipeTabela.size() < SlotEquipe.values().length;
	}
	
	//===
}