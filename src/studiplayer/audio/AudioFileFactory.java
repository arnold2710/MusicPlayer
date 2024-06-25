package studiplayer.audio;
public class AudioFileFactory {

	public static AudioFile createAudioFile(String path) throws NotPlayableException {
		if (path.toLowerCase().endsWith("wav")) {
			return new WavFile(path);
		} else if(path.toLowerCase().endsWith("ogg") || path.toLowerCase().endsWith("mp3")) {
			return new TaggedFile(path);
		}else {
				throw new NotPlayableException (path, "Unkown suffix for AudioFile");
			}
	}
}
