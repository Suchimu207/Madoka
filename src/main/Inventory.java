package main;

import bestiary.Monsters;
import bestiary.MonstersManager;
import bestiary.Skills;

import util.Grapchics;
import util.Input;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Inventory  {
	protected static enum SlotEquipe {
        SLOT_1,
        SLOT_2,
        SLOT_3,
        SLOT_4,
        SLOT_5,
        SLOT_6;
    }
	// ==================== ATRIBUTOS ====================
	
	private static Map<Integer, Monsters> monstrosInventario;
    private static List<Monsters> monstrosOrdenados;
    private static List<Skills> skillsTree, skillsDesbloqueadas;
	private static EnumMap<SlotEquipe, Monsters> equipeTabela;
    private static SlotEquipe slotEncontrado;

    private static Monsters monstroCarregado;
	private static Skills skillCarregada, skillMostrada;
	
    private static int idInventario, tamanhoInventario, linhaAtual, paginaAtual, totalPaginas,
    inicioLista, fimLista, posiçãoLinhaInventário, posiçãoLinhaEquipe, posiçãoLinhaSkillsAtivas;
    private static String nomeMonstroExibido;
	
	private Inventory(){
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
	
	// ==================== DESENHO ====================
	
	protected static void desenhaInfoEquipe(){
		int coordenadaY = 21;
		
		for (Map.Entry<SlotEquipe, Monsters> entry : equipeTabela.entrySet()){
			Monsters monstro = entry.getValue();
			Grapchics.desenhaTela("||||||||||",0,coordenadaY, Grapchics.BRANCO_CLARO, Grapchics.BRANCO);
			Grapchics.desenhaTela(monstro.getNomeMonstro(), 10, coordenadaY, Grapchics.BRANCO);
			Grapchics.desenhaTela("||||||||||", 0, coordenadaY + 1, Grapchics.VERMELHO_CLARO, Grapchics.VERMELHO_CLARO);
			coordenadaY += 3;
		}
		Grapchics.desenhaTela("Shift: Esconder equipe",0,39, Grapchics.PRETO_CLARO);
		
		Grapchics.atualizarTela();
	}
	
	protected static void desenhaMonstroDetalhes(){
		Grapchics.limpaTela();
		
		if (Input.getCursorX() <= 0) Input.setCursorX(1);
		
		int tamanho = getTamanhoInventario();
		if (Input.getCursorX() > tamanho) Input.setCursorX(tamanho);
		
		monstroCarregado = getMonstroInventario(Input.getCursorX());
		if (monstroCarregado == null) return;
		
		String indicadorFavorito = monstroCarregado.isMonstroFavorito() ? " ["+(char)3+"]" : "";
		
		Grapchics.desenhaCentro("Detalhes",0, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("E: Voltar", 0, 1, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTela("Enter: Marcar/Desmarcar favorito",0,2, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTela("Shift: Ver habilidades",0,3, Grapchics.PRETO_CLARO);
		
		Grapchics.desenhaTela("____________________",0,4, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTela("Nome: "+monstroCarregado.getNomeMonstro() + indicadorFavorito,0,5, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("Nivel: "+monstroCarregado.getNivelAtual(),0,6, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("Classe: "+monstroCarregado.getClasseAtual(),0,7, Grapchics.BRANCO_CLARO);
		desenhaElementoMonstro(8);
		Grapchics.desenhaTela("Raridade: "+monstroCarregado.getRaridadeMonstro(),0,9, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("Forca: "+monstroCarregado.getForcaAtual(),0,10, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("Vida: "+monstroCarregado.getVidaAtual(),0,11, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("Velocidade: "+monstroCarregado.getSpeedAtual(),0,12, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("Estamina: "+monstroCarregado.getEstaminaAtual(),0,13, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("Tracos: "+Arrays.toString(monstroCarregado.getTracosIds()),0,14, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("____________________",0,15,Grapchics.PRETO_CLARO);
		
		Grapchics.desenhaTela("____________________",0,17,Grapchics.PRETO_CLARO);
		desenhaExp(18);
		Grapchics.desenhaTela("____________________",0,19,Grapchics.PRETO_CLARO);
		
		Grapchics.desenhaTela("Habilidades:",0,21,Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("____________________",0,22,Grapchics.PRETO_CLARO);
		posiçãoLinhaSkillsAtivas = 23;
		desenhaListaHabilidade();
		
		Grapchics.atualizarTela();
	}
	
	private static void desenhaExp(int linha){
		if (monstroCarregado.isNivelMaximo()){
			Grapchics.desenhaTela("NIVEL MAXIMO",0,linha, Grapchics.BRANCO_CLARO);
		}else{
			Grapchics.desenhaTela("Proximo nivel: "+monstroCarregado.getExpAtual()+"/"+monstroCarregado.getExpNecessaria()+" EXP",0,linha, Grapchics.BRANCO_CLARO);
		}
	}
	
	private static void desenhaElementoMonstro(int linha){
		Grapchics.desenhaTela("Elementos: "+monstroCarregado.getElementosAtuais(),0,linha, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela(monstroCarregado.getElementosAtuais(), 11, linha, 
		monstroCarregado.getCorDoElemento(monstroCarregado.getElementosAtuais()));
	}
	
	private static void desenhaListaHabilidade(){
		for (int i = 0; i < monstroCarregado.getQuantidadeMaxSlotsHabilidade(); i++){
			skillCarregada = monstroCarregado.getHabilidadeAtiva(i);
			if (skillCarregada != null){
				Grapchics.desenhaTela((i+1)+": "+skillCarregada.getNomeHabilidade(),0,posiçãoLinhaSkillsAtivas,Grapchics.BRANCO_CLARO);
				Grapchics.desenhaTela(skillCarregada.getNomeHabilidade(),3,posiçãoLinhaSkillsAtivas++,skillCarregada.getCorHabilidade());
			}else{
				Grapchics.desenhaTela("[VAZIO]",0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
			}
		}
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas,Grapchics.PRETO_CLARO);
		
		skillCarregada = monstroCarregado.getHabilidadeEspecial();
		if (skillCarregada != null && skillCarregada.isTipoEspecial(skillCarregada.getTipoHabilidade())){
			Grapchics.desenhaTela("Especial:",0,posiçãoLinhaSkillsAtivas+2,Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas+3,Grapchics.PRETO_CLARO);
			Grapchics.desenhaTela(skillCarregada.getNomeHabilidade(),0,posiçãoLinhaSkillsAtivas+4,skillCarregada.getCorHabilidade());
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas+5,Grapchics.PRETO_CLARO);
		}
	}
	
	protected static void desenhaHabilidadeDetalhes(){
		Grapchics.limpaTela();
		
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
		
		Grapchics.desenhaCentro("Habilidades",0, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("E: Voltar", 0, 1, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTela("Enter: Ativar/Desativar habilidade",0,2, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTela("Monstro: "+monstroCarregado.getNomeMonstro()+" Nv"+monstroCarregado.getNivelAtual(),0,3, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("____________________",0,4,Grapchics.PRETO_CLARO);
		
		posiçãoLinhaSkillsAtivas = 5;
		linhaAtual = posiçãoLinhaSkillsAtivas;
		for (int i = 0; i < monstroCarregado.getQuantidadeMaxSlotsHabilidade(); i++){
			skillCarregada = monstroCarregado.getHabilidadeAtiva(i);
			
			if (skillCarregada != null){
				if (Input.getCursorY() == linhaAtual){
					Grapchics.desenhaTela((i+1)+": "+skillCarregada.getNomeHabilidade(),0,posiçãoLinhaSkillsAtivas,Grapchics.BRANCO_CLARO);
					Grapchics.desenhaTela(skillCarregada.getNomeHabilidade(),3,posiçãoLinhaSkillsAtivas++,Grapchics.AMARELO_CLARO);
					skillMostrada = skillCarregada;
				}else{
					Grapchics.desenhaTela((i+1)+": ",0,posiçãoLinhaSkillsAtivas,Grapchics.BRANCO_CLARO);
					Grapchics.desenhaTela(skillCarregada.getNomeHabilidade(),3,posiçãoLinhaSkillsAtivas++,skillCarregada.getCorHabilidade());
				}
			}else{
				if (Input.getCursorY() == linhaAtual){
					Grapchics.desenhaTela("[VAZIO]",0,posiçãoLinhaSkillsAtivas++, Grapchics.AMARELO_CLARO);
					skillMostrada = null;
				}else{
					Grapchics.desenhaTela("[VAZIO]",0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
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
			Grapchics.desenhaTela("Especial:",0,posiçãoLinhaSkillsAtivas++,Grapchics.BRANCO_CLARO);
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
			if (Input.getCursorY() == linhaAtual){
				Grapchics.desenhaTela(skillCarregada.getNomeHabilidade(),0,posiçãoLinhaSkillsAtivas++,Grapchics.AMARELO_CLARO);
				skillMostrada = skillCarregada;
			}else{
				Grapchics.desenhaTela(skillCarregada.getNomeHabilidade(),0,posiçãoLinhaSkillsAtivas++,skillCarregada.getCorHabilidade());
			}
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
		}
		
		posiçãoLinhaSkillsAtivas += 1;
		infoHabilidade();
		
		linhaAtual++;
		posiçãoLinhaSkillsAtivas = linhaAtual;
		
		Grapchics.atualizarTela();
	}
	
	private static void listaArvoreHabilidades(){
		linhaAtual = posiçãoLinhaSkillsAtivas;
		for (int i = 0; i <= monstroCarregado.getTamanhoSkillsTree(); i++){
			skillCarregada = monstroCarregado.getHabilidadeArvoreId(i);
			if (skillCarregada == null) continue;
			
			if (!skillCarregada.isTipoEspecial(skillCarregada.getTipoHabilidade()) 
				&& !monstroCarregado.isHabilidadeAtiva(skillCarregada)){
				if (monstroCarregado.getNivelAtual() >= skillCarregada.getNivelNecessario()){
					
					if (Input.getCursorY() == linhaAtual){
						Grapchics.desenhaTela(skillCarregada.getNomeHabilidade()+" (Nv"+skillCarregada.getNivelNecessario()+")",
						0,posiçãoLinhaSkillsAtivas++,Grapchics.AMARELO_CLARO);
						skillMostrada = skillCarregada;
					}else{
						int tamanhoString = skillCarregada.getNomeHabilidade().length();
						
						Grapchics.desenhaTela(skillCarregada.getNomeHabilidade(),
						0,posiçãoLinhaSkillsAtivas,skillCarregada.getCorHabilidade());
						Grapchics.desenhaTela("(Nv"+skillCarregada.getNivelNecessario()+")",
						tamanhoString+1,posiçãoLinhaSkillsAtivas++,Grapchics.BRANCO_CLARO);
						
					}
					
				}else{
					if (Input.getCursorY() == linhaAtual){
						Grapchics.desenhaTela(skillCarregada.getNomeHabilidade()+" (Nv"+skillCarregada.getNivelNecessario()+")",
						0,posiçãoLinhaSkillsAtivas++,Grapchics.AMARELO_CLARO);
						skillMostrada = skillCarregada;
					}else{
						Grapchics.desenhaTela(skillCarregada.getNomeHabilidade()+" (Nv"+skillCarregada.getNivelNecessario()+")",
						0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
					}
				}
				linhaAtual++;
			}
		}
		posiçãoLinhaSkillsAtivas = linhaAtual;
	}
	
	private static void infoHabilidade(){
		if (skillMostrada != null){
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
			
			if (skillMostrada.getPoderHabilidade() > 0){
				Grapchics.desenhaTela("Poder: "+skillMostrada.getPoderHabilidade(),0,posiçãoLinhaSkillsAtivas++,Grapchics.BRANCO_CLARO);
			}
			if (skillMostrada.getPrecisaoBase() > 0){
				Grapchics.desenhaTela("Precisao: "+skillMostrada.getPrecisaoBase(),0,posiçãoLinhaSkillsAtivas++,Grapchics.BRANCO_CLARO);
			}
			if (skillMostrada.getEnergiaHabilidade() > 0){
				Grapchics.desenhaTela("Energia: "+skillMostrada.getEnergiaHabilidade(),0,posiçãoLinhaSkillsAtivas++,Grapchics.BRANCO_CLARO);
			}
			if (skillMostrada.getRecargaHabilidade() > 0){
				Grapchics.desenhaTela("Recarga: "+skillMostrada.getRecargaHabilidade(),0,posiçãoLinhaSkillsAtivas++,Grapchics.BRANCO_CLARO);
			}
			
			Grapchics.desenhaTela("____________________",0,posiçãoLinhaSkillsAtivas++,Grapchics.PRETO_CLARO);
		}
	}
	
    protected static void desenhaInventário(){
		Grapchics.limpaTela();
		
		reordenarListaInventario();
		if (monstrosInventario.isEmpty()){
			Grapchics.desenhaCentro("Inventario vazio.", 10, Grapchics.BRANCO_CLARO);
			Grapchics.atualizarTela();
			return;
		}
		
		tamanhoInventario = monstrosInventario.size();
		totalPaginas = (int) Math.ceil(tamanhoInventario / 24.0);
		
		String indicadorPagina = "Pagina "+paginaAtual+"/"+totalPaginas;
		
		Grapchics.desenhaCentro("Inventario - "+indicadorPagina,0, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("E: Voltar", 0, 1, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTela("Enter: Equipar/Desequipar", 0, 2, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTela("Shift: Ver detalhes", 0, 3, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTela("____________________",0,4, Grapchics.PRETO_CLARO);
		desenhaListaInventário();
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaInventário, Grapchics.PRETO_CLARO);
		
		posiçãoLinhaEquipe = 33;
		Grapchics.desenhaCentro("Equipe:",31, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("____________________",0,32, Grapchics.PRETO_CLARO);
		for (SlotEquipe slot : SlotEquipe.values()){
			Monsters monstroEquipe = equipeTabela.get(slot);
			
			if (monstroEquipe != null){
				nomeMonstroExibido = monstroEquipe.getNomeMonstro()+" Nv"+monstroEquipe.getNivelAtual();
				Grapchics.desenhaTela(nomeMonstroExibido, 0, posiçãoLinhaEquipe++, Grapchics.BRANCO_CLARO);
			}else{
				Grapchics.desenhaTela("[Vazio]", 0, posiçãoLinhaEquipe++, Grapchics.PRETO_CLARO);
			}
		}
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaEquipe, Grapchics.PRETO_CLARO);
		
		Grapchics.atualizarTela();
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
		String indicadorEquipado = monstro.isMonstroEquipado() ? " [E]" : "";
		String indicadorFavorito = monstro.isMonstroFavorito() ? " ["+(char)3+"]" : "";
		
			if (selecionado){
				nomeMonstroExibido = monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual();
				Grapchics.desenhaTela(nomeMonstroExibido + indicadorEquipado + indicadorFavorito, 0, posiçãoLinhaInventário++, 
				Grapchics.AMARELO_CLARO);
			}else{
				if (monstro.isMonstroEquipado() || monstro.isMonstroFavorito()){
					nomeMonstroExibido = monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual();
					Grapchics.desenhaTela(nomeMonstroExibido + indicadorEquipado + indicadorFavorito, 0, posiçãoLinhaInventário++, 
					Grapchics.BRANCO_CLARO);
				}else{
					nomeMonstroExibido = monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual();
					Grapchics.desenhaTela(nomeMonstroExibido + indicadorEquipado + indicadorFavorito, 0, posiçãoLinhaInventário++, 
					Grapchics.PRETO_CLARO);
				}
			}
		}
	}
	
	// ==================== AÇÕES DO JOGADOR ====================
	
	protected static void alternarHabilidadeAtiva(){
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
	
    protected static void alternarMonstroTabela(int id){
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

    protected static void alternarMonstroFavorito(int id){
        Monsters monstro = monstrosInventario.get(id);
        if (monstro == null) return;

        monstro.setMonstroFavorito(!monstro.isMonstroFavorito());
    }
	
    protected static void alternarPagina(boolean avancar){
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
	
    public static void removerMonstroInventario(int id){
        monstroCarregado = monstrosInventario.get(id);
        if (monstroCarregado == null)
            return;

        monstrosInventario.remove(id);
        monstroCarregado.setMonstroEquipado(false);

        slotEncontrado = null;
        for (Map.Entry<SlotEquipe, Monsters> entry : equipeTabela.entrySet()) {
            if (entry.getValue() == monstroCarregado) {
                slotEncontrado = entry.getKey();
                break;
            }
        }
        if (slotEncontrado != null) {
            equipeTabela.remove(slotEncontrado);
            reordenarEquipe();
        }

        // Reordena inventário
        Monsters[] monstrosAtuais = monstrosInventario.values().toArray(new Monsters[0]);
        monstrosInventario.clear();
        idInventario = 1;
        for (int i = 0; i < monstrosAtuais.length; i++) {
            monstrosInventario.put(idInventario++, monstrosAtuais[i]);
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
	
	public static Map<Integer, Monsters> getMonstrosInventario(){
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