package studiplayer.audio;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class PlayList implements Iterable<AudioFile>{
private LinkedList<AudioFile> list;
private String search;
private SortCriterion sortCriterion;
private ControllablePlayListIterator iterator;
private AudioFile currentFile;


	public PlayList(){
		this.list = new LinkedList<AudioFile>();
		this.sortCriterion = studiplayer.audio.SortCriterion.DEFAULT;
		this.iterator = new ControllablePlayListIterator(this.list, this.search, this.sortCriterion);
		this.search = null;
	}
	
	public PlayList(String m3uPathname){
		this.list = new LinkedList<AudioFile>();
		loadFromM3U(m3uPathname);
		this.sortCriterion = studiplayer.audio.SortCriterion.DEFAULT;
		this.iterator = new ControllablePlayListIterator(this.list, this.search, this.sortCriterion);
		this.search = null;
	}
	
	public AudioFile currentAudioFile() {
		if (this.list.isEmpty()) {
			return null;
		} else if (this.iterator == null) {
		    this.iterator = new ControllablePlayListIterator(this.list, this.search, this.sortCriterion);
		}
			this.currentFile = this.iterator.getAudioFile();
			return this.currentFile;
		}

	public void add(AudioFile file){
		this.list.add(file);
	}
	
	public void remove(AudioFile file) {
		this.list.remove(file);
	}
	
	public int size() {
		return this.list.size();
	}
		
	public void nextSong() {
		this.currentFile = this.iterator.next();
	}
	
	public void loadFromM3U(String pathname) {
		this.list.clear();
		Scanner scanner = null;
		
		try {
			scanner = new Scanner(new File(pathname));
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (!line.startsWith("#") && !line.isBlank()) {
					try {
						AudioFile af = AudioFileFactory.createAudioFile(line);
						this.list.add(af);
					} catch (NotPlayableException e) {
						e.printStackTrace();
					}
				}
			} 
			} catch (Exception e) {
				throw new RuntimeException(e);
		} finally {
			try {
				scanner.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	public void saveAsM3U (String pathname) {
		FileWriter writer = null;
		String sep = System.getProperty("line.separator");
		try {
			writer = new FileWriter(pathname);
			for (AudioFile line : this.list) {
				writer.write(line.getPathname() + sep);
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to write file " + pathname + "!");
		} finally {
			try {
			//System.out.println("File " + pathname + " written!");
			writer.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	public void jumpToAudioFile(AudioFile file) {
		this.currentFile = this.iterator.jumpToAudioFile(file);
	}
	
	
	public List<AudioFile> getList(){
		return this.list;
	}
	
	public String getSearch() {
		return search;
	}	

	public void setSearch(String search) {
		this.search = search;
		this.iterator = new ControllablePlayListIterator(this.list, this.search, this.sortCriterion);
	}

	public SortCriterion getSortCriterion() {
		return this.sortCriterion;
	}

	public void setSortCriterion(SortCriterion sortCriterion) {
		this.sortCriterion = sortCriterion;
		this.iterator = new ControllablePlayListIterator(this.list, this.search, this.sortCriterion);
	}

	@Override
	public Iterator<AudioFile> iterator() {
		return new ControllablePlayListIterator(this.list, this.search, this.sortCriterion);
	}

	public ControllablePlayListIterator getIterator() {
		return iterator;
	}

	public void setIterator(ControllablePlayListIterator iterator) {
		this.iterator = iterator;
	}

	@Override
	public String toString() {
		return list.toString();
	}
	
}
