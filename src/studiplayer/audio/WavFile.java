package studiplayer.audio;
import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {
	
public WavFile(){
	super();
}

public WavFile(String path) throws NotPlayableException{
	super(path);
	readAndSetDurationFromFile();
}

public void readAndSetDurationFromFile() throws NotPlayableException {
	float fr;
	long nof;
	try {
	WavParamReader.readParams(getPathname());
	fr = WavParamReader.getFrameRate();
	nof = WavParamReader.getNumberOfFrames();
	setDuration(computeDuration(nof, fr));
	} catch (Exception e) {
		throw new NotPlayableException(getPathname(), "Parameter können nicht gelesen werden");
	}
}



public String toString() {
	return super.toString() + " - " + formatDuration();
}

public static long computeDuration(long numberOfFrames, float frameRate) {
	frameRate = (frameRate/1000000);
	return (long) (numberOfFrames / frameRate + 1);
}

}
