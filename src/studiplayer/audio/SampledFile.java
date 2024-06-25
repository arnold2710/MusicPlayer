package studiplayer.audio;
import java.util.concurrent.TimeUnit;

import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile {
private long duration;
	
SampledFile(){
super();
this.duration = 0;
}

SampledFile(String path) throws NotPlayableException{
	super(path);
}

public void play() throws NotPlayableException{
	try {
	BasicPlayer.play(getPathname());
	} catch (Exception e) {
		throw new NotPlayableException(getPathname(), "Konnte nicht abgespielt werden");
	}
}

public void togglePause() {
	BasicPlayer.togglePause();
}

public void stop() {
	BasicPlayer.stop();
}

public String formatDuration() {
	return timeFormatter(getDuration());
}

public String formatPosition() {
	long currpos = BasicPlayer.getPosition();
	return timeFormatter(currpos);
}

public static String timeFormatter(long timeInMicroSeconds) {
	long maxsize = TimeUnit.MICROSECONDS.toMinutes(timeInMicroSeconds);
	if (timeInMicroSeconds < 0 || maxsize > 99) {
		throw new RuntimeException("Ungültiger Parameterwert!");
	} else {
	long minutes = TimeUnit.MICROSECONDS.toMinutes(timeInMicroSeconds);
	timeInMicroSeconds -= TimeUnit.MINUTES.toMicros(minutes);
	long seconds = TimeUnit.MICROSECONDS.toSeconds(timeInMicroSeconds);
	return String.format("%02d:%02d", minutes, seconds);
	}
}


public void setDuration(long duration) {
	this.duration = duration;
}

public long getDuration() {
	return this.duration;
}
}
