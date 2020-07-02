/**
 * ListOfExpensesAdapter is used for displaying the list view
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */
package com.expensetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensetracker.ui.home.AddDailyExpenseActivity;
import java.util.List;

public class ListOfExpensesAdapter extends RecyclerView.Adapter<ListOfExpensesAdapter.ViewHolder>
{
    List<Expense> expensesList;
    SharedPreferences sharedPreferences;
    String username;
    DBHelper dbHelper;
    TextView placeHolder;
    boolean isDailyExpenseList;
    HomeScreenActivity homeScreenActivity;
    String date;
    public ListOfExpensesAdapter(HomeScreenActivity homeScreenActivity,boolean isDailyExpenseList,String date) {
        this.homeScreenActivity = homeScreenActivity;
        dbHelper=new DBHelper(homeScreenActivity);
        this.isDailyExpenseList=isDailyExpenseList;
        sharedPreferences=homeScreenActivity.getSharedPreferences("expensetracker", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");
        if(isDailyExpenseList) {
            expensesList = dbHelper.fetchDailyExpense(username,date);
        }else {
            placeHolder=homeScreenActivity.findViewById(R.id.place_holder_text_saved_expenses);
            placeHolder.setVisibility(View.GONE);
            expensesList = dbHelper.fetchSavedExpense(username);
        }
        this.date=date;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.make_model_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if(expensesList!=null && expensesList.size()>0) {
        final Expense expense=expensesList.get(holder.getAdapterPosition());
        holder.expenseName.setText(expense.getExpenseName());
        holder.expenseAmount.setText(String.valueOf(expense.getExpenseAmount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, AddDailyExpenseActivity.class);
                if(expense.getDailyExpenseID()!=0)
                    intent.putExtra("ACTIVITY_TYPE","UPDATE_DAILY_EXPENSE");
                else
                    intent.putExtra("ACTIVITY_TYPE","UPDATE_SAVED_EXPENSE");
                intent.putExtra("EXPENSE",expense);
                context.startActivity(intent);
            }
        });
        }
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView expenseName,expenseAmount;
        LinearLayout expenseEntry;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseAmount=itemView.findViewById(R.id.expense_name);
            expenseName=itemView.findViewById(R.id.expense_amount);
            expenseEntry=itemView.findViewById(R.id.cv_layout);
        }

    }
}
