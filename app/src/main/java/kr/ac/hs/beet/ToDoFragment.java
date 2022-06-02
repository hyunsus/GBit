package kr.ac.hs.beet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ToDoFragment extends Fragment {

    private static final String TAG = "ToDoFragment";
    private RecyclerView mRv_todo;
    private FloatingActionButton mBtn_write;
    private ArrayList<TodoItem> mTodoItems;
    private ArrayList<TodoItem> newTodoItem;
    private MyDbHelper mDBHelper;
    private ToDoAdapter mAdapter;
    EditText et_Date;
    EditText et_date;
    String tododate;
    String col3;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {

        // 달력에서 날짜 선택
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    // Date 부분에 날짜 변경
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        et_date = getView().findViewById(R.id.Date);
        et_date.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        mDBHelper = new MyDbHelper(getActivity());
        mRv_todo = view.findViewById(R.id.tasksRecyclerView);
        mBtn_write = view.findViewById(R.id.fab);
        mTodoItems = new ArrayList<>();



        mBtn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.todo_dialog_edit);
                EditText et_content = dialog.findViewById(R.id.newTaskText);
                Button btn_ok = dialog.findViewById(R.id.newTaskButton);

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Insert Databasse
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); // 현재 시간 (연월일시분초) 받아오기
                        mDBHelper.InsertTodo(et_content.getText().toString(), currentTime);
                        // Insert UI
                        TodoItem item = new TodoItem();
                        item.setContent(et_content.getText().toString());
                        item.setWriteDate(currentTime);

                        mAdapter.addItem(item);
                        mRv_todo.smoothScrollToPosition(0);
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "할일 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });

        //db에서 날짜 가져오기
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Todo.TABLE_NAME, null);
        if(c.moveToFirst()){
            do{
                col3 = c.getString(2);
                Log.i(TAG,"col3: " + col3);
            }while (c.moveToNext());
        }
        c.close();
        db.close();


        // 날짜 띄우기
        et_Date = view.findViewById(R.id.Date);
        et_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                Log.i(TAG,"et_Date: " + et_Date.getText().toString());
                tododate = et_Date.getText().toString();
                if(tododate.equals(col3)){
                    loadRecentDB(); // load recent DB
                }
            }
        });

        return view;
    }

    private void loadRecentDB() { // 저장되어 있던 DB를 가져온다.
        newTodoItem = mDBHelper.getTodoList(tododate);
        mAdapter = new ToDoAdapter(newTodoItem, getActivity());
        mRv_todo.setHasFixedSize(true);
        mRv_todo.setAdapter(mAdapter);
    }
}
