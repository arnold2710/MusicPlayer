package studiplayer.ui;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
//import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
//import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
//import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.*;

public class Player extends Application {
	boolean useCertPlayList;
	private PlayList playList;
	public static final String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u";
	//public static final String DEFAULT_PLAYLIST = "playlists/playList.cert.m3u";

	private static final String PLAYLIST_DIRECTORY = "playlists/";
	private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
	private static final String NO_CURRENT_SONG = "-";
	private Button filterButton = new Button("Anzeigen");
	private Button playButton = createButton("play.jpg");
	private Button stopButton = createButton("stop.jpg");
	private Button pauseButton = createButton("pause.jpg");
	private Button nextButton = createButton("next.jpg");
	private Label playTimeLabel = new Label(INITIAL_PLAY_TIME_LABEL);
	private ChoiceBox<SortCriterion> sortChoiceBox = new ChoiceBox<>();
	private Label currentSongLabel = new Label(NO_CURRENT_SONG);
	private TextField searchTextField = new TextField();
	private Label playListLabel = new Label(PLAYLIST_DIRECTORY);
	private SongTable songTable;
	private PlayerThread pThread;
	private TimerThread tThread;
	private AudioFile firstAudioFile;
	
	public Player() {
		this.playList = new PlayList();
		this.useCertPlayList = true;
	}
	
	public void start(Stage stage) throws Exception {
		if (!this.useCertPlayList) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			fileChooser.showOpenDialog(stage);
			this.playList = new PlayList(fileChooser.showOpenDialog(stage).getAbsolutePath());
		} else {
			this.playList = new PlayList(DEFAULT_PLAYLIST);
		}
		BorderPane mainPane = new BorderPane();
		stage.setTitle("APA Player");
		
		
		//Filterformular
		TitledPane gridTitlePane = new TitledPane();
		GridPane grid = new GridPane();
		grid.setVgap(4);
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.add(new Label("Suchtext"), 0, 0);
		grid.add(searchTextField, 1, 0);
		grid.add(new Label("Sortierung"), 0, 1);
		ObservableList<SortCriterion> oList = FXCollections.observableArrayList();
		for (SortCriterion sortieren : SortCriterion.values()) {
			oList.add(sortieren);
		}
		sortChoiceBox.getItems().addAll(oList);
		sortChoiceBox.setValue(SortCriterion.DEFAULT);
		grid.add(sortChoiceBox, 1, 1);
		grid.add(filterButton, 2, 1);
		gridTitlePane.setText("Filter");
		gridTitlePane.setContent(grid);
		sortChoiceBox.setPrefWidth(150);
		
		
		GridPane.setMargin(searchTextField, new Insets(0, 10, 0, 10));
		GridPane.setMargin(sortChoiceBox, new Insets(0, 10, 0, 10));
		mainPane.setTop(gridTitlePane);
		
		//SongTable
		
		this.songTable = new SongTable(this.playList);
		/*this.songTable.setRowSelectionHandler(e -> {
			this.playList.currentAudioFile().stop();
			this.playList.jumpToAudioFile(this.songTable.getSelectionModel().getSelectedItem().getAudioFile());
			try {
				this.playList.currentAudioFile().play();
			} catch (NotPlayableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});*/
		
		
		filterButton.setOnAction(e -> {
			String textField = searchTextField.getText();
			this.playList.setSearch(textField);
			SortCriterion selectedSortCriterion = sortChoiceBox.getSelectionModel().getSelectedItem();
			this.playList.setSortCriterion(selectedSortCriterion);
			this.songTable.refreshSongs();
		});
		
		mainPane.setCenter(this.songTable);
		
		//Info
		GridPane info = new GridPane();
		info.setHgap(5);
		info.setPadding(new Insets(5, 5, 5, 5));
		info.add(new Label("Playlist"), 0, 0);
		info.add(playListLabel, 1, 0);
		info.add(new Label("Aktueller Song"), 0, 1);
		info.add(currentSongLabel, 1, 1);
		info.add(new Label("Abspielzeit"), 0, 2);
		info.add(playTimeLabel, 1, 2);
		
		GridPane.setMargin(currentSongLabel, new Insets(0, 0, 0, 10));
		GridPane.setMargin(playListLabel, new Insets(0, 0, 0, 10));
		GridPane.setMargin(playTimeLabel, new Insets(0, 0, 0, 10));
		
		// Buttons
		setButtonStates(false, true, true, false);
		FlowPane buttons = new FlowPane();
		/*playButton = createButton("play.jpg");
		pauseButton = createButton("pause.jpg");
		stopButton = createButton("stop.jpg");
		nextButton = createButton("next.jpg");*/
		
		
		
		playButton.setOnAction(e -> {
			this.firstAudioFile = this.playList.currentAudioFile();
			playSong();
		});
		
		pauseButton.setOnAction(e -> {
			pauseSong();
		});
		
		stopButton.setOnAction(e -> {
			stopSong();
		});
		
		nextButton.setOnAction(e -> {
			skipToNextSong();
		});
		
		buttons.getChildren().addAll(playButton, pauseButton, stopButton, nextButton);
		buttons.setHgap(0);
		buttons.setAlignment(Pos.CENTER);
		FlowPane.setMargin(playButton, new Insets(0, 0, 10, 0));
		FlowPane.setMargin(pauseButton, new Insets(0, 0, 10, 0));
		FlowPane.setMargin(stopButton, new Insets(0, 0, 10, 0));
		FlowPane.setMargin(nextButton, new Insets(0, 0, 10, 0));
		VBox vbox = new VBox(info, buttons);
		mainPane.setBottom(vbox);
		Scene scene = new Scene(mainPane, 600, 400);
		stage.setScene(scene);
		
		this.songTable.refreshSongs();
		stage.show();
	}
	
	public void loadPlayList(String pathname) {
		if (pathname == null || pathname.isEmpty()) {
			//this.playList = new PlayList(DEFAULT_PLAYLIST);
			this.playList.loadFromM3U(DEFAULT_PLAYLIST);
			this.firstAudioFile = this.playList.currentAudioFile();
			this.songTable.refreshSongs();
		} else {
			//this.playList = new PlayList(pathname);
			this.playList.loadFromM3U(pathname);
			this.songTable.refreshSongs();

		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	private Button createButton(String iconfile) {
		Button button = null;
		try {
		URL url = getClass().getResource("/icons/" + iconfile);
		Image icon = new Image(url.toString());
		ImageView imageView = new ImageView(icon);
		imageView.setFitHeight(20);
		imageView.setFitWidth(20);
		button = new Button("", imageView);
		button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		button.setStyle("-fx-background-color: #fff;");
		} catch (Exception e) {
		System.out.println("Image " + "icons/"
		+ iconfile + " not found!");
		System.exit(-1);
		}
		return button;
	}
	
	private void playSong() {
			this.pThread = new PlayerThread();
			this.tThread = new TimerThread();
			this.pThread.start();
			this.tThread.start();
			setButtonStates(true, false, false, false);
			//updateSongInfo(this.playList.currentAudioFile());
			//this.playList.currentAudioFile().play();
	}
	
	private void pauseSong() {
		if (this.tThread.isAlive()) {
			this.tThread.terminate();
		} else {
			this.tThread = new TimerThread();
			this.tThread.start();
		}
		setButtonStates(true, false, false, false);
		this.playList.currentAudioFile().togglePause();
	}
	
	private void stopSong() {
		setButtonStates(false, true, true, false);
		this.pThread.terminate();
		this.tThread.terminate();
		//updateSongInfo(firstAudioFile);
		//this.tThread = null;
		updateSongInfo(null);
		this.playList.currentAudioFile().stop();
		System.out.println("erwartet: " + firstAudioFile.toString());
		System.out.println("echt: " + this.playList.currentAudioFile().toString());
	}
	
	private void skipToNextSong() {
		this.tThread.terminate();
		this.pThread.terminate();
		this.playList.currentAudioFile().stop();
		setButtonStates(true, false, false, false);
		this.playList.nextSong();
		playSong();
	}
	
	private void setButtonStates(boolean playButtonState,
			boolean pauseButtonState, boolean stopButtonState,
			boolean nextButtonState) {
		this.playButton.setDisable(playButtonState);
		this.pauseButton.setDisable(pauseButtonState);
		this.stopButton.setDisable(stopButtonState);
		this.nextButton.setDisable(nextButtonState);
	}
	
	private void updateSongInfo(AudioFile af) {
		Platform.runLater(() -> {
		if (af == null) {
		// hier currentSongLabel und playTimeLabel belegen
			this.currentSongLabel.setText(NO_CURRENT_SONG);
			this.playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
		} else {
				this.currentSongLabel.setText(af.getTitle());
				this.playTimeLabel.setText(af.formatPosition());
			}
		});
		}
	
	class TimerThread extends Thread{
		
	boolean stopped = false;
	
	public void run() {
		while (!stopped) {
			updateSongInfo(playList.currentAudioFile());
			//System.out.println("Eeeeee");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void terminate() {
		stopped = true;
	}
	}
	
	class PlayerThread extends Thread {
		
		boolean stopped = false;
		
		public void run() {
			while (!stopped) {
				try {
					playList.currentAudioFile().play();
				} catch (Exception e) {
					
				}
				if(!stopped) {
				playList.nextSong();
				}
			}
		}
		
		
		public void terminate() {
			stopped = true;
		}
	}

	public void setPlayList(PlayList playList) {
		this.playList = playList;
	}

	public void setUseCertPlayList(boolean useCertPlayList) {
		this.useCertPlayList = useCertPlayList;
	}
}
