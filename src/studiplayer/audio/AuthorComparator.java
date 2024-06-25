package studiplayer.audio;

import java.util.Comparator;

public class AuthorComparator implements Comparator<AudioFile> {

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		String a1 = o1.getAuthor();
		String a2 = o2.getAuthor();
		return a1.compareTo(a2);
	}

}
