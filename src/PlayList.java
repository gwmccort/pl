import java.util.ArrayList;


public class PlayList {
	String name;
	int id;
	String persistentId;
	boolean allItems;
	ArrayList<Integer> tracks = new ArrayList<Integer>();

	public PlayList(String name, int id, String pId, boolean allItems){
		super();
		this.name = name;
		this.id = id;
		this.persistentId = pId;
		this.allItems = allItems;
	}

	public void addTrack(int id){
		tracks.add(new Integer(id));
	}
}
