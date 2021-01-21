package del.app.dnote.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import del.app.dnote.R;
import del.app.dnote.adapter.NotesAdapter;
import del.app.dnote.database.DbHelper;
import del.app.dnote.model.NoteModel;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    
    private Toolbar toolbar;
	private ArrayList<NoteModel> arrayList;
    private RecyclerView recyclerView;
    private FloatingActionButton actionButton;
    private DbHelper database_helper;
	private NotesAdapter adapter;
    
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
		
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        actionButton = (FloatingActionButton) findViewById(R.id.add);
        database_helper = new DbHelper(this);
		
		init();
		
        actionButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getApplicationContext(), AddActivity.class));
					//showDialog();
				}
			});
			
		
    }
    
    private void setupView(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    private void init(){
		arrayList = new ArrayList<>(database_helper.getNotes());
		
		if(arrayList.size() == 0){
			Toast.makeText(MainActivity.this, "Data kosong", Toast.LENGTH_SHORT).show();
		}else{
			recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
			recyclerView.setItemAnimator(new DefaultItemAnimator());
			adapter = new NotesAdapter(getApplicationContext(), this, arrayList);
			recyclerView.setAdapter(adapter);
			recyclerView.getAdapter().notifyDataSetChanged();
			
		}
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
		
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				
					return false;
					}
			@Override
			public boolean onQueryTextChange(String s) {
				// UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                if(arrayList != null){
				adapter.getFilter().filter(s);
                }
				return false;
				}
				});
        return true;
    }
	
	
	//display dialog
    public void showDialog() {
        final EditText title, des;
        Button submit;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        title = (EditText) dialog.findViewById(R.id.title);
        des = (EditText) dialog.findViewById(R.id.description);
        submit = (Button) dialog.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {;
				@Override
				public void onClick(View v) {
					if (title.getText().toString().isEmpty()) {
						title.setError("Please Enter Title");
					}else if(des.getText().toString().isEmpty()) {
						des.setError("Please Enter Description");
					}else {
						database_helper.addNotes(title.getText().toString(), des.getText().toString());
						dialog.cancel();
						init();
					}
				}
			});
    }
}
