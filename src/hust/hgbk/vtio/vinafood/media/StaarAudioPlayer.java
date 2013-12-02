package hust.hgbk.vtio.vinafood.media;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class StaarAudioPlayer {
	static StaarAudioPlayer player = new StaarAudioPlayer();
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private String url = "";
	
	public static StaarAudioPlayer getInstance(){
		return player;
	}
	
	public void setUrl(String url) throws Exception{
		this.url = url;
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setDataSource(url);
	}
	
	public void play() throws IllegalStateException, IOException{
		mediaPlayer.prepare();
		if (mediaPlayer != null){
			mediaPlayer.start();
		}
		
	}

	
	public void stop(){
		mediaPlayer.stop();
		mediaPlayer.release();
		mediaPlayer = null;
	}

	
	public boolean isPlaying(){
		if (mediaPlayer.isPlaying()){
			return true;
		} else {
			return false;
		}
	}
}
