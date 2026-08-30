package util.commands;

import main.Inventory;
import bestiary.Monsters;
import manager.MonstersManager;

public class MonsterCommand implements Command {
	public MonsterCommand(){}
	
    @Override
    public void executar(String[] args) throws Exception{
        if (args.length < 1){
            System.out.println(getUso());
            return;
        }
        
        String subcomando = args[0];
        
        switch (subcomando){
            case "levelUp":
                if (args.length < 3){
                    System.out.println("Uso: monster levelUp <id_Inventário> <nível>");
                    return;
                }
                int id = Integer.parseInt(args[1]);
				int valor = Integer.parseInt(args[2]);
                Monsters monstro = Inventory.getMonstroInventario(id);
				if (monstro == null){
					System.out.println("Monstro (ID_Inventário:"+id+")"+" não encontrado.");
				}else{
					if (monstro.isNivelMaximo()){
						System.out.println(monstro.getNomeMonstro()+" (ID_Inventário:"+id+")"+
						" está no nível máximo ("+monstro.getNivelMaximo()+").");
					}else{
						monstro.subirNivel(valor);
						System.out.println(monstro.getNomeMonstro()+" (ID_Inventário:"+id+")"+" subiu para o nível "+monstro.getNivelAtual()+".");
					}
				}
                break;
				
			case "levelUpAll":
                if (args.length < 2){
                    System.out.println("Uso: monster levelUpAll <nível>");
                    return;
                }
				valor = Integer.parseInt(args[1]);
				int tamanhoInventário = Inventory.getTamanhoInventario();
				
				for (int m = 1; m <= tamanhoInventário; m++){
					monstro = Inventory.getMonstroInventario(m);
					if (monstro == null) continue;
					
					if (monstro.isNivelMaximo()){
						System.out.println(monstro.getNomeMonstro()+" (ID_Inventário:"+ m+")"+
						" está no nível máximo ("+monstro.getNivelMaximo()+").");
					}else{
						monstro.subirNivel(valor);
						System.out.println(monstro.getNomeMonstro()+" (ID_Inventário:"+ m+")"+
						" subiu para o nível "+monstro.getNivelAtual()+".");
					}
				}
                break;
        }
    }
    
    @Override
    public String getUso(){
		String uso = "Subcomandos: \n"+
		"levelUp <id_Inventário> <nível> \n"+
		"levelUpAll <nível>";
		
        return uso;
    }
    
    @Override
    public String getDescricao(){
        return "Gerencia os monstros do inventário.";
    }
	
	//===
}