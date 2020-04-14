package com.example.collegeconnect;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static com.example.collegeconnect.R.drawable.button_design3;
import static com.example.collegeconnect.R.drawable.upload;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> implements Filterable {

    private Context context;
    private DatabaseReference DatabaseReference;
    private ArrayList<Upload> noteslist;
    private ArrayList<Upload> noteslistfull;
    private ArrayList<String> selectedTags;

    public NotesAdapter(Context context, ArrayList<Upload> noteslist) {
        this.context = context;
        this.noteslist = noteslist;
        noteslistfull = new ArrayList<>(noteslist);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notes,parent,false);
        return new NotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        final RecyclerView.Adapter[] recyclerAdapter = new RecyclerView.Adapter[1];

        selectedTags = new ArrayList<>();
        final Upload notes = noteslist.get(position);
        holder.title.setText(notes.getName());
        holder.author.setText(notes.getAuthor());
        holder.noOfDown.setText("No. of Downloads = " + String.valueOf(notes.getDownload()));
        holder.itv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int downloads = notes.getDownload() + 1;

                Upload upload = new Upload(notes.getName(),
                        notes.getCourse(),
                        notes.getSemester(),
                        notes.getBranch(),
                        notes.getUnit(),
                        notes.getAuthor(), downloads, notes.getUrl(), notes.getTimestamp(),notes.getUploaderMail(),notes.getTags());
                DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
                mDatabaseReference.child(notes.getTimestamp()+"").setValue(upload);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(notes.getUrl()), "application/pdf");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(intent, "Choose an Application:"));
            }
        });
        DatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.STORAGE_PATH_UPLOADS+notes.getTimestamp()+"/tags");
        DatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> arrayList = (ArrayList<String>) dataSnapshot.getValue();

                if (arrayList != null) {

                    recyclerAdapter[0] = new TagsAdapter(context, arrayList);
                    holder.recyclerView.setAdapter(recyclerAdapter[0]);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(context,v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.report, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.report:
//                                Toast.makeText(context, "Report Notes", Toast.LENGTH_SHORT).show();
                                ReportsDialog reportsDialog = new ReportsDialog(notes.getTimestamp());
                                reportsDialog.show(((AppCompatActivity) context).getSupportFragmentManager(),"Report Dialog");
                                break;
                            case R.id.tagover:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                LayoutInflater inflater = ((AppCompatActivity)context).getLayoutInflater();
                                View view = inflater.inflate(R.layout.layout_tag_dialog, null);
                                final boolean[] etuB = {true};
                                final boolean[] shortB = {true};
                                final boolean[] longB = {true};

                                final boolean[] ttpB = {true};
                                final Button etu = view.findViewById(R.id.etuTag);
                                final Button shortt = view.findViewById(R.id.shortTag);
                                final Button longt = view.findViewById(R.id.longTag);
                                final Button ttp = view.findViewById(R.id.ttpTag);
//                                Button doneButton = view.findViewById(R.id.doneButton);

                                etu.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        etuB[0] = !etuB[0];
                                        if (!etuB[0] && !selectedTags.contains("Easy to understand")){
                                            selectedTags.add("Easy to understand");
                                        }
                                        v.setBackgroundResource(etuB[0] ? R.drawable.button_design : R.drawable.button_design3);

                                    }
                                });
                                shortt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        shortB[0] = !shortB[0];
                                        if (!shortB[0] && !selectedTags.contains("Short")){
                                            selectedTags.add("Short");
                                        }
                                        v.setBackgroundResource(shortB[0] ? R.drawable.button_design : R.drawable.button_design3);
                                    }
                                });
                                longt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        longB[0] = !longB[0];
                                        if (!longB[0] && !selectedTags.contains("Long")){
                                            selectedTags.add("Long");
                                        }
                                        v.setBackgroundResource(longB[0] ? R.drawable.button_design : R.drawable.button_design3);
                                    }
                                });
                                ttp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ttpB[0] = !ttpB[0];
                                        if (!ttpB[0] && !selectedTags.contains("To the point")){
                                            selectedTags.add("To the point");
                                        }
                                        v.setBackgroundResource(ttpB[0] ? R.drawable.button_design : R.drawable.button_design3);
                                    }
                                });

//                                doneButton.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        DatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.STORAGE_PATH_UPLOADS+notes.getTimestamp());
//                                        DatabaseReference.child("tags").setValue(selectedTags);
//
//                                        if (!etuB[0]){
//                                            etuB[0]=true;
//                                            etu.setBackgroundResource(R.drawable.button_design);
//                                        }
//                                        if (!shortB[0]){
//                                            shortB[0]=true;
//                                            shortt.setBackgroundResource(R.drawable.button_design);
//                                        }
//                                        if (!longB[0]){
//                                            longB[0]=true;
//                                            longt.setBackgroundResource(R.drawable.button_design);
//                                        }
//                                        if (!ttpB[0]){
//                                            ttpB[0]=true;
//                                            ttp.setBackgroundResource(R.drawable.button_design);
//
//                                        }
//                                        click=true;
//                                    }
//                                });
                                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.STORAGE_PATH_UPLOADS+notes.getTimestamp());
                                        DatabaseReference.child("tags").setValue(selectedTags);

                                        if (!etuB[0]){
                                            etuB[0]=true;
                                            etu.setBackgroundResource(R.drawable.button_design);
                                        }
                                        if (!shortB[0]){
                                            shortB[0]=true;
                                            shortt.setBackgroundResource(R.drawable.button_design);
                                        }
                                        if (!longB[0]){
                                            longB[0]=true;
                                            longt.setBackgroundResource(R.drawable.button_design);
                                        }
                                        if (!ttpB[0]){
                                            ttpB[0]=true;
                                            ttp.setBackgroundResource(R.drawable.button_design);

                                        }
                                    }
                                });

                                builder.setView(view);
                                AlertDialog dialog = builder.create();
                                dialog.show();


                        }
                        return true;
                    }
                });
                popup.show();
            }
        });


//        holder.tags.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getTags();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return noteslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,author,noOfDown;
        ImageButton report;
        Button tags;
        RelativeLayout itv;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.authorname);
            report = itemView.findViewById(R.id.reportButton);
            noOfDown = itemView.findViewById(R.id.download);
            itv = itemView.findViewById(R.id.relate);
//            tags = itemView.findViewById(R.id.addTags);
            recyclerView = itemView.findViewById(R.id.tagsRecycler);
        }
    }
    @Override
    public Filter getFilter() {
        return notesfilter;
    }
    private Filter notesfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Upload> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length()==0){
                filteredList.addAll(noteslistfull);
            }
            else
            {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (Upload item: noteslistfull){
                    if(item.getName().toLowerCase().contains(filterpattern) || item.getAuthor().toLowerCase().contains(filterpattern)){
                        filteredList.add(item);
                    }
                }
            }
             FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            noteslist.clear();
            noteslist.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

}
