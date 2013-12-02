package hust.hgbk.vtio.vinafood.texttospeech;


import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;


public class TextToSpeechService {
	public static TextToSpeech speaker;
	
	public static void speak(Context context, final String text){
		try {
			speaker = new TextToSpeech(context, new OnInitListener() {
				@Override
				public void onInit(int arg0) {
					speaker.setSpeechRate(1);
					speaker.speak(text, TextToSpeech.QUEUE_FLUSH, null);
				}
			});
		} catch (Exception e) {
			// missing data, install it
            Intent installIntent = new Intent();
            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            context.startActivity(installIntent);
		}
		
	}
	
	public static void stop(Context context){
		if (speaker.isSpeaking()){
			speaker.stop();
		}
	}
	
}
