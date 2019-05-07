package com.example.dflet.scripttanklogindemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PublisherIdeaList extends AppCompatActivity {

    private static User m_User;
    private Toolbar toolbar;
    protected ScriptTankApplication myApp;
    private RecyclerView recyclerView;
    private ArrayList<String> ideaTitles;
    private ArrayList<String> writers;
    private SearchResultAdapter adapter;
    private ArrayList<TestItem> testItems;
    private FirebaseFunctions functions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher_idea_list);
        myApp = (ScriptTankApplication)this.getApplicationContext(); //these three lines need to be in every
        m_User = myApp.getM_User();
        functions = FirebaseFunctions.getInstance();
        toolbar = findViewById(R.id.toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(toolbar);

        getPublisherIdeas(m_User.getKey()).addOnCompleteListener(new OnCompleteListener<HashMap<String, Object>>(){
            public void onComplete(@NonNull Task<HashMap<String, Object>> task){
                // If the firebase function executes successfully
                if(task.isSuccessful()){
                    // Log 'success' in the Logcat
                    Log.d("Success", "Success");
                    // Set results to the result of the firebase function
                    HashMap<String, Object> results = task.getResult();
                    // Update RecyclerView with results
                    updateListAdapter(results);
                    // Debug stuff
                    System.out.println(results.size());
                    System.out.println(results);
                }
                else {
                    // If the firebase fails
                    Exception e = task.getException();
                    if (e instanceof FirebaseFunctionsException) {
                        // Logs error in the firebase console
                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                        FirebaseFunctionsException.Code code = ffe.getCode();
                        Object details = ffe.getDetails();
                    }
                    // Prints error message in Logcat
                    System.err.println(e.getMessage());
                }
            }

        });
    }


    // Gets all bought Ideas
    private Task<HashMap<String, Object>> getPublisherIdeas(String query){
        // Data put into function
        Map<String, Object> data = new HashMap<>();
        data.put("query", query);

        // Call firebase function
        return functions.getHttpsCallable("getPublisherIdeas")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, HashMap<String, Object>>() {
                    @Override
                    public HashMap<String, Object> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        HashMap<String, Object> result = (HashMap<String, Object>) task.getResult().getData();
                        return result;
                    }
                });
    }

    //Update List Adapter
    private void updateListAdapter(HashMap<String, Object> results){
        // Arrays for different text in the RecyclerView entry
        ideaTitles = (ArrayList<String>)results.get("Ideas");
        writers = (ArrayList<String>)results.get("Writers");

        // For each entry in ideaTitles
        for(int i = 0; i < ideaTitles.size(); i++) {
            // Add new search result
            testItems.add(new TestItem(R.drawable.ic_person_black_24dp, ideaTitles.get(i), writers.get(i)));
        }

        // Notify the adapter that the data has changed, changing the RecyclerView entries
        adapter.notifyDataSetChanged();
    }



//    m_User.fb_id
//    Intent intent = new Intent(HomeActivity.this,
//            ViewExtractsActivity.class);
//    intent.putExtra(getString(R.string.user_profile_intent), (Parcelable) m_User);
}
