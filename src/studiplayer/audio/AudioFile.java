package studiplayer.audio;
import java.io.File;

public abstract class AudioFile {
	private String pathname;
	private String filename;
	String sep = "/";
	private String author;
	private String title;
	
	public AudioFile(){
	this.pathname = "";
	this.filename = "";
	this.author = "";
	this.title ="";	
	}
	
	public AudioFile(String path) throws NotPlayableException{
		parsePathname(path);
		parseFilename(filename);
		File test = new File(pathname);
		if (!test.canRead()) {
			throw new NotPlayableException(pathname, "Datei kann nicht gelesen werden");
		}
	}
	
	private boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
	}
	private boolean isLinux() {
		return System.getProperty("os.name").toLowerCase().indexOf("lin") >= 0;
	}
	
	public void parsePathname(String path) {
	path = path.trim();
	int counter = 0;
	if (isWindows()) {
		sep = "\\";
	}
	
	if (path.length() <= 3 && path.contains("-") == false) {
		pathname = "";
		filename = "";
	} else if (path.contains("-") && path.length()<2) {
		pathname = "-";
		filename = "-";
	} else {	
	String erster = path.substring(0,1);
	char ende = path.charAt(path.length() - 1);
	String[] split = path.split("[/\\\\]");
	
	for (String Empty : split) {
		if(!Empty.isEmpty()) {
			counter++;
		}
		

	}
	
	String[] result = new String[counter];
	counter = 0;
	
	for (String Platzhalter : split) {
		if (!Platzhalter.isEmpty()) {
			result[counter++] = Platzhalter;
		}
	}
	
	if (isLinux() && result[0].contains(":")) {
		String tmp = result[0];
		tmp = sep + tmp.substring(0,1);
		result[0] = tmp;
	}
	
		if (erster.equals("\\") || erster.equals("/")) {
			pathname = sep + String.join(sep, result);
		} else {
			pathname = String.join(sep, result);
		}
		if (ende != '/' && ende != '\\') {
			filename = result[result.length - 1];
		} else {
			pathname += sep;
			filename = "";
		}
	}
	}
	
	public void parseFilename (String filename) {
		if (filename.isEmpty()) {
			this.author = "";
			this.title = "";
		}
		else if (filename == "-") {
			this.author = "";
			this.title ="-";
		} else if (filename.length() <= 4){
			this.author = "";
			this.title = "";
		} else {
			int letzterPunkt = filename.lastIndexOf('.');
			String ende = filename;
			if (letzterPunkt != -1) {
				ende = filename.substring(0, letzterPunkt);
			}
			String[] split = ende.split(" - ");
			if (split.length != 1) {
				author = split[0];
				author = author.trim();
				title = split[1];
				title = title.trim();
			} else {
				author = "";
				title = ende;
			}
		}
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getPathname() {
		return this.pathname;
	}
	
	public String getFilename() {
		this.filename = this.filename.trim();
		return this.filename;
	}
	

	protected void setAuthor(String author) {
		if (author == null) {
			this.author = "";
		} else {
		this.author = author.trim();
		}
	}

	protected void setTitle(String title) {
		if (title != null) {
		this.title = title.trim();
	} else {
		this.title = title;
	}
	}

	public String toString() {
		//String author = getAuthor();
		//String title = getTitle();
		
		if (this.author == null || this.author.isEmpty()) {
			return this.title;
		}else {
			return this.author + " - " + this.title;
		}
	}
	
	public abstract void play() throws NotPlayableException;
	public abstract void togglePause();
	public abstract void stop();
	public abstract String formatDuration();
	public abstract String formatPosition();
}
