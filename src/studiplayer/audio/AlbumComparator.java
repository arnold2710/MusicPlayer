package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		if ( o1 instanceof TaggedFile && o2 instanceof TaggedFile) {
		TaggedFile al1 = (TaggedFile) o1;
		TaggedFile al2 = (TaggedFile) o2;
		String a1 = al1.getAlbum();
		String a2 = al2.getAlbum();
		if (a2 == null) {
			return 1;
		} else if (a1 == null) {
			return -1;
		}
		return a1.compareToIgnoreCase(a2);
		} else if (o1 instanceof TaggedFile && !(o2 instanceof TaggedFile)){
			return 1;
		} else if(!(o1 instanceof TaggedFile) && o2 instanceof TaggedFile) {
			return -1;
		} else {
			return 0;
		}
		
	}

}
