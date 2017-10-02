package com.example.menno_000.mennolont_pset4;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

// import static com.example.menno_000.mennolont_pset4.R.id.test;

public class MainActivity extends AppCompatActivity {
    DBHelper helper;
    Context context;
    ArrayList<ToDo> todoList;
    ArrayList<ToDo> todoLister;
    ToDo todo;
    ListView todoView;
    TextView todoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        todoView = (ListView) findViewById(R.id.output);

        // Create the database helper.
        context = this;
        helper = new DBHelper(this);

        todoLister = helper.read();

        if (todoLister.isEmpty()) {
            ToDo todo1 = new ToDo("Welcome to the to-do list app!", "Not completed");
            helper.create(todo1);

            ToDo todo2 = new ToDo("You can add a new to-do in the bar above", "Not completed");
            helper.create(todo2);

            ToDo todo3 = new ToDo("Click on an item to complete it, hold to delete an item", "Not completed");
            helper.create(todo3);
        }


        // Open the database and print the to-do items.
        updateScreen();

        // Delete on LongClick
        todoView.setOnItemLongClickListener(new deleteToDo());
        todoView.setOnItemClickListener(new completeToDo());
    }

    private class deleteToDo implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View v, int i, long id) {

            ToDoAdapter todoAdapter = new ToDoAdapter(MainActivity.this, todoList);
            ToDo todo = todoAdapter.getItem(i);
            helper.delete(todo);

            todoList = helper.read();

            loadTaskList();
            return true;
        }
    }

    private class completeToDo implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {

            ToDoAdapter taskAdapter = new ToDoAdapter(MainActivity.this, todoList);
            ToDo todo = taskAdapter.getItem(i);

            if(todo.getCompleted().equals("Not completed")) {
                v.setBackgroundColor(Color.parseColor("#04B486"));
                assert todo != null;
                todo.setCompleted("Completed");
            }
            else {
                v.setBackgroundColor(Color.parseColor("#FFFFFF"));
                assert todo != null;
                todo.setCompleted("Not completed");
            }

            helper.update(todo);
            loadTaskList();
        }
    }


    public void updateScreen() {

        todoList = helper.read();

        List<String> strings = new ArrayList<>(todoList.size());
        for (ToDo todo: todoList) {

            String TITLE = (todo != null ? todo.getTitle(): null);

            strings.add(TITLE);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.custom_layout, R.id.itemName, strings);
        todoView.setAdapter(arrayAdapter);

        loadTaskList();
    }

    private class ToDoAdapter extends ArrayAdapter<ToDo>{
        ToDoAdapter(Context context, ArrayList<ToDo> taskList){
            super(context, 0, taskList);
        }

        @NonNull
        @Override
        public View getView(int i, View v, @NonNull ViewGroup parent) {
            ToDo todo = getItem(i);

            if (v == null){
                v = LayoutInflater.from(getContext()).inflate(R.layout.custom_layout, parent, false);
            }

            todoTitle = (TextView) v.findViewById(R.id.itemName);
            assert todo != null;
            todoTitle.setText(todo.getTitle());

            if (todo.getCompleted().equals("Completed")){
                v.setBackgroundColor(Color.parseColor("#04B486"));
            }
            else {
                v.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            return v;
        }
    }


    private void loadTaskList() {
        ToDoAdapter taskAdapter = new ToDoAdapter(this, todoList);
        assert todoView != null;
        todoView.setAdapter(taskAdapter);
    }


    public void add(View view) {

        EditText editText = (EditText) findViewById(R.id.addInput);
        String input = editText.getText().toString();

        if(input.equals("")) {
            Toast.makeText(MainActivity.this, "Please insert some text first", Toast.LENGTH_SHORT).show();
        }
        else {
            editText.setText("");

            // Create a new to-do item to store in the database.
            todo = new ToDo(input, "Not completed");

            helper.create(todo);

            Toast.makeText(MainActivity.this, "Item added", Toast.LENGTH_SHORT).show();

            updateScreen();
        }
    }
}
