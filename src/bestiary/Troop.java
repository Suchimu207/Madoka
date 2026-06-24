package bestiary;

import java.util.ArrayList;
import java.util.List;

public class Troop {
    protected static class Inimigo {
        private final Monsters monstro;
        private final int nivel;
        private final String apelido;

        protected Inimigo(Monsters monstro, int nivel, String apelido){
            this.monstro = monstro;
            this.nivel = nivel;
            this.apelido = apelido;
			
			this.monstro.setNivelBase(this.nivel);
			this.monstro.setNomeMonstro(this.apelido);
        }

        public Monsters getMonstroTropa(){ 
			return monstro; 
		}
        public int getNivel(){ 
			return nivel; 
		}
        public String getApelido(){ 
			return apelido; 
		}
    }

    private static int id;
    private static List<Inimigo> inimigos;
    private static int exp;
    private static int ouro;

    public Troop(int id, List<Inimigo> inimigos, int exp, int ouro){
        this.id = id;
        this.inimigos = new ArrayList<>(inimigos);
        this.exp = exp;
        this.ouro = ouro;
    }

    public Troop(Troop troopRequerida){
        this.id = troopRequerida.getId();
        this.inimigos = new ArrayList<>();
        for (Inimigo i : troopRequerida.getInimigos()){
            this.inimigos.add(new Inimigo(i.getMonstroTropa(), i.getNivel(), i.getApelido()));
        }
        this.exp = troopRequerida.getExp();
        this.ouro = troopRequerida.getOuro();
    }

    public int getId(){ 
		return id; 
	}
    public List<Inimigo> getInimigos(){ 
		return new ArrayList<>(inimigos); 
	}
    public int getExp(){ 
		return exp; 
	}
    public int getOuro(){ 
		return ouro; 
	}
	
	//===
}