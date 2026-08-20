package util;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class Audio {
    private static Clip clipMusicaFundo;
	
	private static String caminhoSE = "data/audio/se/";
	
    private Audio(){
    }
	
    public static void tocarSom(String caminho, float volume){
        try {
			caminho = caminhoSE+caminho+".wav";
			
            File arquivoSom = new File(caminho);
            if (arquivoSom.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(arquivoSom);
                Clip clipSom = AudioSystem.getClip();
                clipSom.open(audioInput);
                
                setarVolume(clipSom, volume);
                
                clipSom.start(); 
            }else{
                System.out.println("Som não encontrado: "+caminho);
            }
        }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e){
            System.out.println("Erro ao tocar o som: "+e.getMessage());
        }
    }
	
    public static void tocarMusica(boolean loop, String caminho, float volume){
        pararMusica();
		
        try {
            File arquivoMusica = new File(caminho);
            if (arquivoMusica.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(arquivoMusica);
                clipMusicaFundo = AudioSystem.getClip();
                clipMusicaFundo.open(audioInput);

                
                setarVolume(clipMusicaFundo, volume);

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
	
	// Método auxiliar que converte a escala linear (0.0f a 1.0f) para a escala logarítmica de Decibéis.
    private static void setarVolume(Clip clip, float volume){
        // Verifica se o controle de volume é suportado pelo sistema operacional.
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)){
            FloatControl controleVolume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            
            // Trava o limite do volume entre 0.0f (0%) e 1.0f (100%) para evitar que quebre o cálculo.
            volume = Math.max(0.0f, Math.min(volume, 1.0f));
            
            if (volume == 0.0f){
                // Se for 0, define como o volume mínimo suportado (mudo absoluto).
                controleVolume.setValue(controleVolume.getMinimum());
            }else{
                // Converte a porcentagem para Decibéis e aplica ao clip.
                float decibeis = 20f * (float) Math.log10(volume);
                controleVolume.setValue(decibeis);
            }
        }
    }
	
	//===
}