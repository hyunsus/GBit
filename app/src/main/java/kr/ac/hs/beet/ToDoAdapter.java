package kr.ac.hs.beet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder>{
    private ArrayList<TodoItem> mTodoItems;
    private Context mContext;
    private MyDbHelper mTodoDBHelper;

    public ToDoAdapter(ArrayList<TodoItem> todoItems, Context mContext) {
        this.mTodoItems = todoItems;
        this.mContext = mContext;
        mTodoDBHelper = new MyDbHelper(mContext);
    }

    @NonNull
    @Override
    public ToDoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_list, parent, false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.ViewHolder holder, int position) {
        holder.checkBox.setText(mTodoItems.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mTodoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private EditText text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            EditText text = itemView.findViewById(R.id.newTaskText);
            checkBox = itemView.findViewById(R.id.todoCheckBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int curPos = getAdapterPosition(); // 현재 리스트 클릭한 아이템 위치
                    TodoItem todoItem = mTodoItems.get(curPos);

                    String[] strChoiceItems = {"수정하기", "삭제하기"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("원하는 작업을 선택 해주세요");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if(position == 0){ // 수정하기
                                //팝업 띄우기
                                Dialog dialog = new Dialog(mContext);
                                dialog.setContentView(R.layout.todo_dialog_edit);
                                EditText et_content = dialog.findViewById(R.id.newTaskText);
                                Button btn_ok = dialog.findViewById(R.id.newTaskButton);
                                et_content.setText(todoItem.getContent());
                                //커서를 글자 마지막으로 이동
                                et_content.setSelection(et_content.getText().length() - 1);

                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Update table
                                        String content = et_content.getText().toString();
                                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); // 현재 시간 (연월일시분초) 받아오기
                                        String beforeTime = todoItem.getWriteDate();

                                        mTodoDBHelper.UpdateTodo(content, currentTime, beforeTime);

                                        // Update UI
                                        todoItem.setContent(content);
                                        todoItem.setWriteDate(currentTime);
                                        notifyItemChanged(curPos, todoItem);
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "목록 수정이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.show();
                            }else if(position == 1){ // 삭제하기
                                // Delete table
                                String beforeTime = todoItem.getWriteDate();
                                mTodoDBHelper.DeleteTodo(beforeTime);

                                // Delete UI
                                mTodoItems.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext, "목록이 제거 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();
                }
            });
        }
    }
    //액티비티에서 호출되는 함수, 현재 어댑터에 새로운 게시글 아이템을 전달받아 추가
    public void addItem(TodoItem _item){
        mTodoItems.add(0, _item);
        notifyItemInserted(0);
    }
}
