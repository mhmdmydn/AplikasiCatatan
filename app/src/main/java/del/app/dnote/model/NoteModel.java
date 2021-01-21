package del.app.dnote.model;
import android.widget.ExpandableListAdapter;

public class NoteModel {


    private String ID;
    private String title;
    private String note;
	private String date;
    private Boolean isExpand;
    
    public NoteModel(){
        //Dummy Constructor
    }
	
    public NoteModel(String ID, String title, String note, String date){
        this.ID = ID;
        this.title = title;
        this.note = note;
        this.date = date;
        this.isExpand = false;
    }
    public Boolean getExpand(){
        return isExpand;
    }
    
    public void setExpand(Boolean isExpand){
        this.isExpand = isExpand;
    }
    
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return note;
    }

    public void setNotes(String note) {
        this.note = note;
    }
	
	public String getDate(){
		return date;
	}
	
	public void setDate(String date){
		this.date = date;
	}
}
