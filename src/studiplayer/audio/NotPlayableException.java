package studiplayer.audio;

public class NotPlayableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String path;
	
	public NotPlayableException (String pathname, String msg){
		super(msg);
		this.path = pathname;
	}
	
	public NotPlayableException (String pathname, Throwable t){
		super(t);
		this.path = pathname;
	}
	
	public NotPlayableException(String pathname, String msg, Throwable t){
		super(msg, t);
		this.path = pathname;
	}

	@Override
	public String toString() {
		return "studiplayer.audio.NotPlayableException" + this.path + super.getMessage();
	}

	protected String getPath() {
		return path;
	}	
}
