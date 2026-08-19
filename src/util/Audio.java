package util;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class Audio {
    private static Clip clipMusicaFundo;

    private Audio(){
    }
	
    public static void tocarSom(String caminho){
        try {
            File arquivoSom = new File(caminho);
            if (arquivoSom.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(arquivoSom);
                Clip clipSom = AudioSystem.getClip();
                clipSom.open(audioInput);
                clipSom.start(); 
            }else{
                System.out.println("Som não encontrado: "+caminho);
            }
        }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e){
            System.out.println("Erro ao tocar o som: "+e.getMessage());
        }
    }
	
    public static void tocarMusica(boolean loop, String caminho){
        pararMusica();
		
        try {
            File arquivoMusica = new File(caminho);
            if (arquivoMusica.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(arquivoMusica);
                clipMusicaFundo = AudioSystem.getClip();
                clipMusicaFundo.open(audioInput);

			    if (loop){
					clipMusicaFundo.loop(Clip.LOOP_CONTINUOUSLY);
			    }else{
					clipMusicaFundo.start(); 
			    }
            }else{
                System.out.println("Música não encontrada: "+caminho);
            }
        }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e){
            System.out.println("Erro ao tocar a música: "+e.getMessage());
        }
    }

    public static void pararMusica(){
        if (clipMusicaFundo != null && clipMusicaFundo.isRunning()){
            clipMusicaFundo.stop();
            clipMusicaFundo.close();
            clipMusicaFundo = null;
        }
    }
	
	//===
}