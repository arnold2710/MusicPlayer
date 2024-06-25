package studiplayer.audio;

import java.util.Comparator;

public class TitleComparator implements Comparator<AudioFile> {

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		String t1 = o1.getTitle();
		String t2 = o2.getTitle();
		return t1.compareTo(t2);
	}

}
