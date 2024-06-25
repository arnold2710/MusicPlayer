package studiplayer.audio;
import java.util.Map;
import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile {
private String album;

public TaggedFile(){
	super();
}

public TaggedFile(String path) throws NotPlayableException{
	super(path);
	readAndStoreTags();
}

public String getAlbum() {
	if (this.album == null) {
		return null;
	}
	return this.album.trim();
}

public void readAndStoreTags() throws NotPlayableException{
	try {
	Map<String, Object> tagMap = TagReader.readTags (getPathname());
	if((String) tagMap.get("author") != null) {
	setAuthor((String) tagMap.get("author"));
	}
	if ((String) tagMap.get("title") != null) {
		setTitle((String) tagMap.get("title"));
	}
	this.album = (String) tagMap.get("album");
	setDuration((long) tagMap.get("duration"));
	} catch (Exception e) {
		throw new NotPlayableException (getPathname(), "Tags konnten nicht gelesen werden");
	}
}


public String toString() {
	if (this.album != null) {
		return super.toString() + " - " + this.album.trim() + " - " + formatDuration();
	} else {
		return super.toString() + " - " + formatDuration();
	}
}
}
