package main;

import bestiary.Monsters;
import bestiary.MonstersManager;
import bestiary.Skills;

import asciiPanel.AsciiPanel;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Inventory {
	protected static enum SlotEquipe {
        SLOT_1,
        SLOT_2,
        SLOT_3,
        SLOT_4,
        SLOT_5,
        SLOT_6;
    }
	
	private static Map<Integer, Monsters> monstrosInventario;
    private static List<Monsters> monstrosOrdenados;
    private static EnumMap<SlotEquipe, Monsters> equipeTabela;
    private static SlotEquipe slotEncontrado;

    private static Monsters monstroCarregado;
    private static int idInventario, tamanhoInventario, paginaAtual, totalPaginas,
    inicioLista, fimLista, posiçãoLinhaInventário, posiçãoLinhaEquipe;
    private static String nomeMonstroExibido;
	
	private Inventory(){
	}
	
	protected static void inicializarInventario(){
        monstrosInventario = new HashMap<Integer, Monsters>();
        monstrosOrdenados = new ArrayList<Monsters>();
        equipeTabela = new EnumMap<>(SlotEquipe.class);
        idInventario = 1;
        paginaAtual = 1;
        inicioLista = 1;
        fimLista = 1;
    }
	
	protected static void adicionarMonstroInventário(int id){
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

    protected static void removerMonstroInventario(int id){
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
	
    protected static void desenhaInventário(){
		Grapchics.limpaTela();
		
		reordenarListaInventario();
		if (monstrosInventario.isEmpty()){
			Grapchics.desenhaCentro("Inventario vazio.", 10, AsciiPanel.brightWhite);
			Grapchics.atualizarTela();
			return;
		}
		
		tamanhoInventario = monstrosInventario.size();
		totalPaginas = (int) Math.ceil(tamanhoInventario / 24.0);
		
		String indicadorPagina = "Pagina "+paginaAtual+"/"+totalPaginas;
		
		Grapchics.desenhaCentro("Inventario - "+indicadorPagina,0, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("E: Voltar", 0, 1, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Enter: Equipar/Desequipar", 0, 2, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Shift: Ver detalhes", 0, 3, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("____________________",0,4, AsciiPanel.brightWhite);
		desenhaListaInventário();
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaInventário, AsciiPanel.brightWhite);
		
		posiçãoLinhaEquipe = 33;
		Grapchics.desenhaCentro("Equipe:",31, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("____________________",0,32, AsciiPanel.brightWhite);
		for (SlotEquipe slot : SlotEquipe.values()){
			Monsters monstroEquipe = equipeTabela.get(slot);
			
			if (monstroEquipe != null){
				nomeMonstroExibido = monstroEquipe.getNomeMonstro()+" Nv"+monstroEquipe.getNivelAtual();
				Grapchics.desenhaTela(nomeMonstroExibido, 0, posiçãoLinhaEquipe++, AsciiPanel.brightWhite);
			}else{
				Grapchics.desenhaTela("[Vazio]", 0, posiçãoLinhaEquipe++, AsciiPanel.brightBlack);
			}
		}
		Grapchics.desenhaTela("____________________",0,posiçãoLinhaEquipe, AsciiPanel.brightWhite);
		
		Grapchics.atualizarTela();
	}
	
	private static void desenhaListaInventário(){
		inicioLista = (paginaAtual - 1) * 24;
		fimLista = Math.min(inicioLista + 24, monstrosOrdenados.size());
		
		if (Terminal.cursorY < inicioLista + 1) Terminal.cursorY = fimLista;
		if (Terminal.cursorY > fimLista) Terminal.cursorY = inicioLista+1;
		
		posiçãoLinhaInventário = 5;
		for (int i = inicioLista+1; i <= fimLista; i++){
		Monsters monstro = monstrosInventario.get(i);
		if (monstro == null) continue;

		boolean selecionado = (i == Terminal.cursorY);
		String indicadorEquipado = monstro.isMonstroEquipado() ? " [E]" : "";
		String indicadorFavorito = monstro.isMonstroFavorito() ? " [F]" : "";
		
			if (selecionado){
				nomeMonstroExibido = monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual();
				Grapchics.desenhaTela(nomeMonstroExibido + indicadorEquipado + indicadorFavorito, 0, posiçãoLinhaInventário++, 
				AsciiPanel.brightYellow, AsciiPanel.brightBlack);
			}else{
				nomeMonstroExibido = monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual();
				Grapchics.desenhaTela(nomeMonstroExibido + indicadorEquipado + indicadorFavorito, 0, posiçãoLinhaInventário++, 
				AsciiPanel.brightWhite);
			}
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
	
	//===
}