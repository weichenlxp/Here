package edu.hebut.here.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.hebut.here.R;
import static edu.hebut.here.data.MyContentResolver.*;

public class AddContainerAdapter extends RecyclerView.Adapter<AddContainerAdapter.MyViewHolder> {
    SharedPreferences sharedPreferences;
    int userID;
    private Context lContent;//定义上下文
    String[] containerName;
    private LayoutInflater inflater;

    //集合
    private List<String> listName = new ArrayList<>();

    public AddContainerAdapter(Context lContent) {
        this.lContent = lContent;
        sharedPreferences = this.lContent.getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        Cursor containerCursor = queryContainer(this.lContent, new String[]{"containerName"}, "userID=?", new String[]{String.valueOf(userID)});
        String[] temp = new String[containerCursor.getCount()];
        for (int i = 0;containerCursor.moveToNext();i++) {
            temp[i] = containerCursor.getString(0);
        }
        containerName=temp;
        //设置菜单行数与行内图标、名称、信息
        Collections.addAll(listName, temp);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //获取列表中每行的布局文件
        View view = LayoutInflater.from(lContent).inflate(R.layout.item_container, parent, false);
        return new MyViewHolder(view);
    }

    //设置列表中行所显示的内容
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //设置名称
        holder.name.setText(listName.get(position));
        holder.name.setOnClickListener(v -> {
            inflater=LayoutInflater.from(lContent);
            TableLayout dialogLayout = (TableLayout) inflater.inflate(R.layout.dialog_edit_account, null);
            new android.app.AlertDialog.Builder(lContent).setIcon(R.drawable.ic_edit).setTitle("编辑容器").setView(dialogLayout).setPositiveButton("保存", ((dialog, which) -> {
                EditText editText = dialogLayout.findViewById(R.id.edit_account);
                String temp = editText.getText().toString();
                Cursor cursor = queryContainer(lContent, new String[]{"_id"}, "containerName=? AND userID=?", new String[]{temp, String.valueOf(userID)});
                if (cursor.getCount()==0){
                    updateContainerName(lContent, temp, listName.get(position), userID);
                    holder.name.setText(temp);
                    listName.set(holder.getLayoutPosition(),temp);
                    Toast.makeText(this.lContent,"修改成功！",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this.lContent,"修改失败，容器不允许重名",Toast.LENGTH_SHORT).show();
                }

            })).setNegativeButton("取消", (dialog, which) -> {
            }).create().show();
        });
        //设置内容宽度为屏幕的宽度
        holder.layout_content.getLayoutParams().width = AddUtils.getScreenWidth(lContent);

        //删除按钮的方法
        holder.btn_delete.setOnClickListener(v -> {
            final AlertDialog.Builder alterDialog = new AlertDialog.Builder(this.lContent);
            alterDialog.setIcon(R.drawable.ic_close_overdue);//图标
            alterDialog.setTitle("确认删除");//文字
            alterDialog.setMessage("删除后，与该容器所关联的物品都会被删除！");
            alterDialog.setPositiveButton("确认", (dialog, which) -> {
                int n = holder.getLayoutPosition();//获取要删除行的位置
                deleteContainer(lContent, "containerName=? AND userID=?", new String[]{containerName[n], String.valueOf(userID)});
                listName.remove(n);//删除行中名字
                notifyItemRemoved(n);//删除行
            });
            alterDialog.setNegativeButton("取消", (dialog, which) -> {
            });
            alterDialog.show();
        });
    }

    //返回行的总数
    @Override
    public int getItemCount() {
        return listName.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView btn_delete;
        public TextView name;//名字
        public ViewGroup layout_content;//图标与信息布局

        //获取控件
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.containerName);
            layout_content = itemView.findViewById(R.id.layout_content);
            btn_delete = itemView.findViewById(R.id.delete_button);
        }
    }
}
