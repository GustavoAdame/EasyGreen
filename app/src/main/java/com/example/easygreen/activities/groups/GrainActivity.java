package com.example.easygreen.activities.groups;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;
import com.example.easygreen.adapters.GroupAdapter;
import com.example.easygreen.models.Group;
import com.example.easygreen.models.Ingredient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

public class GrainActivity extends AppCompatActivity {
    private GroupAdapter groupAdapter;
    private List<Ingredient> ingredients = new ArrayList<>();
    private RecyclerView rvGrainItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grain);
        displayRecyclerView();
        getGrainGroup();
    }

    private void displayRecyclerView() {
        rvGrainItems = findViewById(R.id.rvGrainItems);
        groupAdapter = new GroupAdapter(ingredients);
        rvGrainItems.setAdapter(groupAdapter);
        rvGrainItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getGrainGroup() {
        ParseQuery<Group> group = ParseQuery.getQuery(Group.class);
        group.whereEqualTo(Group.KEY_group_name, "Grains");
        group.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> groups, ParseException e) {
                if(e == null){
                    ParseRelation<Ingredient> ingredientRelation = groups.get(0).getRelation(Group.KEY_group_list);
                    ingredientRelation.getQuery().findInBackground(new FindCallback<Ingredient>() {
                        @Override
                        public void done(List<Ingredient> ingredientList, ParseException e) {
                            for (Ingredient item: ingredientList) {
                                ingredients.add(item);
                            }
                            groupAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
}