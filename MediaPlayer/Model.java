package MediaPlayer;

import java.util.ArrayList;

class Playlist{
	String filename,path;

	public Playlist(String filename, String path) {	
		this.filename=filename;
		this.path=path;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}	
	
}
public class Model {
	ArrayList<Playlist> list=new ArrayList<Playlist>();
	
	public void addlist(String filename,String path) {
		list.add(new Playlist(filename, path));
		
	}
	public void removelist(int index) {
		list.remove(index);
	}
		
}
