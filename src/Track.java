
public class Track {
	int id;
	String name;
	String artist;
	String albumAtrist;
	String album;

	public Track(int id, String name, String artist, String albumAtrist,
			String album) {
		super();
		this.id = id;
		this.name = name;
		this.artist = artist;
		this.albumAtrist = albumAtrist;
		this.album = album;
	}

	public Track(Dict d){
		this(d.getInt("Track ID"), d.getString("Name"), d.getString("Artist"), d.getString("Album Artist"), d.getString("Album"));
	}

	@Override
	public String toString() {
		return "Track [id=" + id + ", name=" + name + ", artist=" + artist
				+ ", albumAtrist=" + albumAtrist + ", album=" + album + "]";
	}



}
