package del.app.dnote.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import del.app.dnote.model.NoteModel;
import del.app.dnote.database.DbHelper;
import del.app.dnote.R;
import android.widget.Filterable;
import android.widget.Filter;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import android.widget.LinearLayout;
import androidx.core.app.NotificationCompatJellybean;
import android.widget.Toast;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.viewHolder> implements Filterable {

    Context context;
    Activity activity;
    List<NoteModel> arrayList;
	List<NoteModel> arrayListFull;
    NoteModel noteModel;
    DbHelper database_helper;
    public NotesAdapter(Context context,Activity activity, List<NoteModel> arrayList) {
        this.context = context;
        this.activity  = activity ;
        this.arrayList = arrayList;
		arrayListFull = new ArrayList<>(arrayList);
    }

    @Override
    public NotesAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_list, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotesAdapter.viewHolder holder, final int position) {
        noteModel = arrayList.get(position);
        holder.title.setText(noteModel.getTitle());
        holder.description.setText(noteModel.getNotes());
		holder.date.setText(noteModel.getDate());
		database_helper = new DbHelper(context);
        
        Boolean clickExpand = noteModel.getExpand();
		if(clickExpand == false){
			holder.expand.setVisibility(View.GONE);
			holder.imgExpand.setRotation(0);
		} else {
			holder.expand.setVisibility(View.VISIBLE);
			holder.imgExpand.setRotation(180);
		}

//        holder.expand.setVisibility(clickExpand ? View.VISIBLE : View.GONE);
		
        
//        holder.delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                        try{
//                            database_helper.delete(arrayList.get(position).getID());
//                            arrayList.remove(position);
//							arrayListFull.remove(position);
//                            notifyDataSetChanged();
//                        } catch (NullPointerException e){
//                            Toast.makeText(context, e.getMessage() +"\n"+ noteModel.getID(), Toast.LENGTH_SHORT).show();
//                        }
//                }
//            }); 
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, description, date;
        ImageView delete, edit, imgExpand;
        LinearLayout expand;
        public viewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
			date = (TextView) itemView.findViewById(R.id.date);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            edit = (ImageView) itemView.findViewById(R.id.edit);
            
            expand = (LinearLayout)itemView.findViewById(R.id.bot_layout);
            imgExpand = (ImageView)itemView.findViewById(R.id.img_expand);
            
            imgExpand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        noteModel = arrayList.get(getAdapterPosition());
//                        noteModel.setExpand(!noteModel.getExpand());
//                        notifyItemChanged(getAdapterPosition());
						
						if(expand.getVisibility() == View.GONE){
							ChangeBounds transition = new ChangeBounds();
							transition.setDuration(200L);
							TransitionManager.beginDelayedTransition(expand, transition);
							expand.setVisibility(View.VISIBLE);
							imgExpand.setRotation(180);
							noteModel = arrayList.get(getAdapterPosition());
							noteModel.setExpand(!noteModel.getExpand());
						}else{
							ChangeBounds transition = new ChangeBounds();
							transition.setDuration(200L);
							TransitionManager.beginDelayedTransition(expand, transition);
							expand.setVisibility(View.GONE);
							imgExpand.setRotation(0);
							noteModel = arrayList.get(getAdapterPosition());
							noteModel.setExpand(noteModel.getExpand());
							
						}
                    }
                });     
                
           edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //display edit dialog
                        showDialog(getAdapterPosition());
                    }
                });
				
			delete.setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View v) {
						database_helper.delete(arrayList.get(getAdapterPosition()).getID());
						arrayList.remove(getAdapterPosition());
						notifyDataSetChanged();
					}
				});
            
        }
    }

    public void showDialog(final int pos) {
        final EditText title, des;
        Button submit;
        final Dialog dialog = new Dialog(activity);
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

        title.setText(arrayList.get(pos).getTitle());
        des.setText(arrayList.get(pos).getNotes());

        submit.setOnClickListener(new View.OnClickListener() {;
				@Override
				public void onClick(View v) {
					if (title.getText().toString().isEmpty()) {
						title.setError("Please Enter Title");
					}else if(des.getText().toString().isEmpty()) {
						des.setError("Please Enter Description");
					}else {
						//updating note
						database_helper.updateNote(title.getText().toString(), des.getText().toString(), arrayList.get(pos).getID());
						arrayList.get(pos).setTitle(title.getText().toString());
						arrayList.get(pos).setNotes(des.getText().toString());
						dialog.cancel();
						//notify list
						notifyDataSetChanged();
					}
				}
			});
    }
	
	@Override
	public Filter getFilter() {
		return filter;
	}
	
	Filter filter = new Filter(){

		@Override
		protected Filter.FilterResults performFiltering(CharSequence p1) {
			
			List<NoteModel> filteredList = new ArrayList<>();
			
			if(p1 == null || p1.length() == 0){
				filteredList.addAll(arrayListFull);
			} else {
				String filterPattern = p1.toString().toLowerCase().trim();
				
				for(NoteModel item : arrayListFull){
					if(item.getTitle().toLowerCase().contains(filterPattern)){
					filteredList.add(item);
					}
				}
				
			}
			FilterResults results = new FilterResults();
			results.values = filteredList;
			
			return results;
		}

		@Override
		protected void publishResults(CharSequence p1, Filter.FilterResults p2) {
			arrayList.clear();
			arrayList.addAll((Collection<? extends NoteModel>) p2.values);
			notifyDataSetChanged();
		}
		
	};
}
