package MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.text.AbstractDocument.BranchElement;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller implements Initializable {

	@FXML
	Button play, reverse, next, tbtn;
	@FXML
	Text text, time;
	@FXML
	MenuItem mi1, mi2, mi3;
	@FXML
	Slider slider, volume;
	@FXML
	MenuButton mb1;
	@FXML
	MediaView view;
	@FXML
	ListView<String> listview;
	@FXML
	ContextMenu cmenu;
	@FXML
	AnchorPane pane;
	@FXML
	HBox hbox;

	Stage stage;
	String filename;
	String path;
	Media mp3;
	MediaPlayer p;
	boolean A = true;
	boolean B = true;
	int num = 1;
	long totalTime = 0, currentTime = 0;
	Model m = new Model();
	int start = 0, t = 1;
	Thread th;
	Random r;
	File f;
	ClipboardContent content;

	@FXML
	private void Load() {
		FileChooser fc = new FileChooser();
		fc.setTitle("파일 불러오기");
		List<File> file = fc.showOpenMultipleDialog(new Stage());
		if (file != null) {
			for (File f : file) {
				path = f.getAbsolutePath();
				path = path.replace("\\", "/");
				filename = f.getName();
				String[] c = filename.split("\\.");
				int l = c.length - 1;
				if (c[l].equals("mp3") || c[l].equals("mp4")) {
					m.addlist(filename, path);
					listview.getItems().add(filename);
				}
			}
			stage.setHeight(410);
			ready(start);
			listview.setVisible(true);
			slider.setVisible(true);
			volume.setVisible(true);
			play.setDisable(false);
			mi1.setDisable(true);
			mi2.setDisable(false);
			if (m.list.size() > 1) {
				next.setDisable(false);
				reverse.setDisable(false);
				tbtn.setDisable(false);

			}
		}
	}

	@FXML
	private void rightClick() {
		int index = listview.getSelectionModel().getSelectedIndex();
		listcontrol2(index);

	}

	@FXML
	private void Add() {

		String path;
		FileChooser fc = new FileChooser();
		fc.setTitle("파일 추가");
		List<File> file = fc.showOpenMultipleDialog(new Stage());
		if (file != null) {
			for (File f : file) {
				path = f.getAbsolutePath();
				path = path.replace("\\", "/");
				String filename = f.getName();
				String[] c = filename.split("\\.");
				int l = c.length - 1;
				if (c[l].equals("mp3") || c[l].equals("mp4")) {
					m.addlist(filename, path);
					listview.getItems().add(filename);
				}
				tbtn.setDisable(false);
				next.setDisable(false);
				reverse.setDisable(false);
			}
		}
		System.out.println("list size:" + m.list.size());
	}

	@FXML
	private void Next() {
		if (currentTime > 2) {
			if (B) {
				num = 1;
			} else {
				num = 2;
			}
			A = false;
		}
	}

	@FXML
	private void Reverse() {
		if (currentTime > 2) {
			num = 3;
			A = false;
		}
	}

	public void RE() {
		if (currentTime > 2) {
			p.stop();
			start -= 1;
			slider.setValue(0);
			p.seek(p.getStartTime());
			if (start < 0) {
				start = m.list.size() - 1;
			}
			System.out.println("reverse:" + start);
			ready(start);
		}
	}

	@FXML
	private void playtype() {
		if (tbtn.getText().equals("S")) {
			B = false;
			tbtn.setText("R");
		} else {
			B = true;
			tbtn.setText("S");
		}
	}

	@FXML
	private void play() {
		if (play.getText().equals("▶")) {
			p.play();
			System.out.println("play");
		} else if (play.getText().equals("■")) {
			p.pause();
			System.out.println("pouse");
		}
	}

	public void auto() {
		if (currentTime > 2) {
			A = false;
			p.stop();
			start += 1;
			slider.setValue(0);
			currentTime = 0;
			p.seek(p.getStartTime());
			if (start > m.list.size() - 1) {
				start = 0;
			}
			System.out.println("auto:" + start);
			ready(start);
		}
	}

	public void random() {
		if (currentTime > 2) {
			r = new Random();
			start = r.nextInt(m.list.size());
			A = false;
			p.stop();
			slider.setValue(0);
			currentTime = 0;
			p.seek(p.getStartTime());
			if (start > m.list.size() - 1) {
				start = 0;
			}
			System.out.println("ran:" + start);
			ready(start);
		}
	}

	public void ready(int start) {
		text.setText(m.list.get(start).getFilename());
		System.out.println("ready:" + start);
		mp3 = new Media(new File(m.list.get(start).getPath()).toURI().toString());
		p = new MediaPlayer(mp3);
		String filename = text.getText();
		String[] c = filename.split("\\.");
		int l = c.length - 1;
		if (c[l].equals("mp3")) {
			mp3();
			System.out.println("mp3");
		} else if (c[l].equals("mp4")) {
			view.setMediaPlayer(p);
			mp4();
			System.out.println("mp4");
		}
		p.setOnReady(new Runnable() {
			@Override
			public void run() {
				p.setOnPlaying(() -> {
					play.setText("■");
				});
				p.setOnPaused(() -> {
					play.setText("▶");
				});
				p.setOnStopped(() -> {
					play.setText("▶");
				});
				p.setAutoPlay(true);
				A = true;
				Time();
			}
		});

	}

	public void Time() {
		System.out.println("------------");
		System.out.println("timeth시작");
		p.setRate(1);
		totalTime = (long) p.getTotalDuration().toSeconds();
		th = new Thread(() -> {
			while (currentTime < totalTime && A) {
				currentTime = (long) p.getCurrentTime().toSeconds();
				Platform.runLater(() -> {
					slider.setValue((double) currentTime / totalTime * 100);
					time.setText((currentTime % 3600 / 60) + ":" + (currentTime % 3600 % 60) + "/"
							+ (totalTime % 3600 / 60) + ":" + (totalTime % 3600 % 60));
				});
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(300);
				System.out.println("timeth종료");
				switch (num) {
				case 1:
					auto();
					System.out.println("num:" + num);
					System.out.println(totalTime);
					break;
				case 2:
					random();
					System.out.println("num:" + num);
					break;
				case 3:
					RE();
					System.out.println("re:" + num);
					if (B) {
						num = 1;
						System.out.println("num:" + num);
					} else {
						num = 2;
						System.out.println("num:" + num);
					}
					break;
				case 4:
					ready(start);
					System.out.println("Cl:" + num);
					if (B) {
						num = 1;
						System.out.println("num:" + num);
					} else {
						num = 2;
						System.out.println("num:" + num);
					}
					break;

				default:
					System.out.println("num:" + num);
					break;
				}
			} catch (InterruptedException e) {

			}
		});
		th.start();

		slider.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				int t = (int) p.getTotalDuration().toSeconds();
				p.seek(Duration.seconds(t * slider.getValue() / 100));
			}

		});

		slider.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				int t = (int) p.getTotalDuration().toSeconds();
				p.seek(Duration.seconds(t * slider.getValue() / 100));
			}

		});

	}

	public void listcontrol1(int index) {
		if (currentTime > 2) {
			num = 4;
			A = false;
			System.out.println("clickindex:" + index);
			start = index;
			p.stop();
			p.seek(p.getStartTime());
		}
	}

	public void listcontrol2(int index) {
		m.removelist(index);
		listview.getItems().remove(index);
		System.out.println("list size:" + m.list.size());
		if (m.list.size() < 2) {
			reverse.setDisable(true);
			next.setDisable(true);
			tbtn.setDisable(true);
			if (m.list.size() == 0) {
				start = 0;
				currentTime = 0;
				totalTime = 0;
				p.stop();
				slider.setVisible(false);
				volume.setVisible(false);
				p.seek(p.getStartTime());
				play.setDisable(true);
				mi1.setDisable(false);
				mi2.setDisable(true);
				text.setText("");
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pane.setPrefHeight(200);
		tbtn.setDisable(true);
		listview.setVisible(false);
		play.setDisable(true);
		mi2.setDisable(true);
		reverse.setDisable(true);
		next.setDisable(true);
		slider.setVisible(false);
		slider.setValueChanging(true);
		slider.setValue(0);
		volume.setVisible(false);
		volume.setValue(30);
		volume.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> obsevable, Number oldVal, Number newVal) {
				p.setVolume(volume.getValue() / 100.0);
			}
		});
		listview.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (currentTime > 2) {
					if (e.getClickCount() == 2) {
						int index = listview.getSelectionModel().getSelectedIndex();
						totalTime = 0;
						slider.setValue(0);
						if (index <= m.list.size() - 1 || index >= 0) {
							listcontrol1(index);
						}

					}

				}
			}
		});
		pane.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				if (db.hasFiles()) {
					event.acceptTransferModes(TransferMode.COPY);
				} else {
					event.consume();
				}
			}
		});

		pane.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				if (db.hasFiles()) {
					String filepath = null;
					for (File file : db.getFiles()) {
						filepath = file.getAbsolutePath();
						filepath = filepath.replace("\\", "/");
						filename = file.getName();
						String[] c = filename.split("\\.");
						int l = c.length - 1;
						System.out.println(filename);
						System.out.println(filepath);
						if (c[l].equals("mp3") || c[l].equals("mp4")) {
							drag(filename, filepath);
						}
					}
					event.consume();
				}
			}
		});

	}

	public void drag(String filename, String filepath) {
		m.addlist(filename, filepath);
		System.out.println(m.list.size());
		listview.getItems().add(filename);
		if (m.list.size() == 1) {
			stage.setHeight(410);
			ready(start);
			listview.setVisible(true);
			slider.setVisible(true);
			volume.setVisible(true);
			play.setDisable(false);
			mi1.setDisable(true);
			mi2.setDisable(false);
			
		} else if (m.list.size() > 1) {
			next.setDisable(false);
			reverse.setDisable(false);
			tbtn.setDisable(false);
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void mp3() {
		view.setLayoutX(264.0);
		view.setLayoutY(100.0);
		view.setFitHeight(1);
		view.setFitWidth(1);
		text.setLayoutX(75);
		text.setLayoutY(49);
		text.setStyle("-fx-fill:black");
		hbox.setLayoutX(1.0);
		hbox.setLayoutY(144.0);
		hbox.setPrefWidth(349);
		slider.setPrefHeight(14);
		slider.setPrefWidth(245);
		slider.setLayoutX(53);
		slider.setLayoutY(78);
		time.setLayoutX(225);
		time.setLayoutY(107);
		time.setWrappingWidth(78.4000244140625);
		time.setStyle("-fx-fill:black");
		tbtn.setLayoutX(308);
		tbtn.setLayoutY(74);
		volume.setLayoutX(264);
		volume.setLayoutY(123);
		volume.setPrefHeight(15);
		volume.setPrefWidth(68);
		listview.setLayoutX(24);
		listview.setLayoutY(199);
		listview.setPrefWidth(303);
		listview.setPrefHeight(155);

		stage.setHeight(410);
		stage.setWidth(365);

	}

	public void mp4() {
		stage.setHeight(650);
		stage.setWidth(770);
		text.setLayoutX(33);
		text.setLayoutY(514);
		text.setWrappingWidth(250);
		text.setStyle("-fx-fill:white");
		hbox.setLayoutX(1.0);
		hbox.setLayoutY(545.0);
		hbox.setPrefWidth(300);
		slider.setPrefHeight(14);
		slider.setPrefWidth(549);
		slider.setLayoutX(14);
		slider.setLayoutY(461);
		time.setLayoutX(480);
		time.setLayoutY(493);
		time.setWrappingWidth(78.4000244140625);
		time.setStyle("-fx-fill:white");
		tbtn.setLayoutX(564);
		tbtn.setLayoutY(457);
		volume.setLayoutX(596);
		volume.setLayoutY(461);
		volume.setPrefHeight(15);
		volume.setPrefWidth(131);
		listview.setLayoutX(310);
		listview.setLayoutY(504);
		listview.setPrefWidth(418);
		listview.setPrefHeight(90);
		view.setLayoutX(27);
		view.setLayoutY(51);
		view.setFitHeight(550);
		view.setFitWidth(700);
	}

}
