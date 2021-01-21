package del.app.dnote.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import del.app.dnote.R;
import android.widget.EditText;
import androidx.core.view.inputmethod.EditorInfoCompat;
import android.text.TextWatcher;
import android.text.Editable;
import androidx.appcompat.widget.Toolbar;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import del.app.dnote.helper.TextUndoRedo;
import del.app.dnote.database.DbHelper;

public class AddActivity extends AppCompatActivity implements TextUndoRedo.TextChangeInfo  {

	@Override
	public void textAction() {
		if(mMenu != null && !charSeq.isEmpty()){
		mMenu.getItem(0).setEnabled(TUR.canUndo());
		mMenu.getItem(1).setEnabled(TUR.canRedo());
		} else{
			mMenu.getItem(0).setEnabled(false);
			mMenu.getItem(1).setEnabled(false);
		}
	}
	
	
	private EditText note;
	private Toolbar toolbar;
	private Menu mMenu;
	private TextUndoRedo TUR;
	private String charSeq;
	private DbHelper dBHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		setupView();
		init();
		textAction();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mMenu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.note_menu,menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		int id = item.getItemId();
		switch (id){
			case R.id.undo:
				//do something
				showToast("Undo Clicked");
				TUR.exeUndo();
				return true;
			case R.id.redo:
				//do something    
				showToast("Redo Clicked");
				TUR.exeRedo();
				return true;
			case R.id.clear:
				//do something
				showToast("Clear Clicked");
				note.getText().clear();
				return true;
			case R.id.save:
				//do something             
				showToast("Save Clicked");
				dBHelper = new DbHelper(this);
				if(toolbar.getTitle() != null){
					if(note.length() != 0){
						dBHelper.addNotes(toolbar.getTitle().toString().trim(), note.getText().toString().trim());
						finish();
					} else{
						showToast("Please Add Note");
					}
				} else{
					showToast("Please Add Title");
				}
				
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	
   private void setupView(){
	   note = (EditText) findViewById(R.id.editTextCus);
	   TUR = new TextUndoRedo(note, this);
	   toolbar = findViewById(R.id.toolbar);
	   setSupportActionBar(toolbar);
	   
	   
   }
   private void init(){
	   note.addTextChangedListener(twEdittext);
   }
   
	private TextWatcher twEdittext = new TextWatcher(){

		@Override
		public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
			
		}

		@Override
		public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
			String charSeq = note.getText().toString().trim();
			textAction();
		}

		@Override
		public void afterTextChanged(Editable p1) {
			
		}
};
   
   
  private void showToast(String msg){
	   Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onBackPressed() {
	  super.onBackPressed();
	  showToast("Saved");
	  dBHelper = new DbHelper(this);
	  if(toolbar.getTitle() != null){
		  if(note.length() != 0){
			  dBHelper.addNotes(toolbar.getTitle().toString().trim(), note.getText().toString().trim());
			  finish();
		  } else{
			  showToast("Please Add Note");
		  }
	  } else{
		  showToast("Please Add Title");
	  }
  }
   
}

