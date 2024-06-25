package studiplayer.audio;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ControllablePlayListIterator implements Iterator<AudioFile> {
	private List<AudioFile> liste = new LinkedList<AudioFile>();
	private int index = 0;
	
	public ControllablePlayListIterator(List<AudioFile> liste){
		this.liste = liste;
		this.index = 0;
	}
	
	public ControllablePlayListIterator(List<AudioFile> liste, String search, SortCriterion SortCriterion) {
		this.index = 0;
		if (SortCriterion == studiplayer.audio.SortCriterion.DEFAULT && (search == null || search.isEmpty())) {
			this.liste = liste;
		} else {
			for (AudioFile file : liste) {
				if (search == null || search.isEmpty() || file.getAuthor().contains(search) || file.getTitle().contains(search) || file instanceof TaggedFile && ((TaggedFile) file).getAlbum() != null && ((TaggedFile) file).getAlbum().contains(search) )
				this.liste.add(file);
			}
		}
		
		switch (SortCriterion) {
			case AUTHOR:
				 this.liste.sort(new AuthorComparator());
				break;
			case TITLE:
				this.liste.sort(new TitleComparator());
				break;
			case ALBUM:
				this.liste.sort(new AlbumComparator());
				break;
			case DURATION:
				this.liste.sort(new DurationComparator());
				break;
			case DEFAULT:
				break;
		}
	}
		
	public boolean hasNext() {
		return this.index < this.liste.size();
	}

	public AudioFile next() {
		return (hasNext() ? this.liste.get(this.index++): null);	
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public AudioFile jumpToAudioFile (AudioFile file) {
		if (this.liste.contains(file)) {
			if(this.liste.indexOf(file) > 0 && hasNext()) {
			this.index = this.liste.indexOf(file)-1;
			} else {
				this.index = this.liste.indexOf(file);
			}
			return next();
		} else {
		return null;
		}
	}
	
	public AudioFile getAudioFile() {
		if(this.index >= this.liste.size()) {
			this.index = 0;
		}
		return this.liste.get(this.index);
	}

}
