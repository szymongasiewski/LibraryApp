package com.example.libraryapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private BookViewModel bookViewModel;
    public static final int NEW_BOOK_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_BOOK_ACTIVITY_REQUEST_CODE = 2;
    private FloatingActionButton addBookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        bookViewModel.findAll().observe(this, adapter::setBooks);
        addBookButton = findViewById(R.id.add_button);
        addBookButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
                //               startActivityForResult(intent, NEW_BOOK_ACTIVITY_REQUEST_CODE);
                getResult.launch(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class BookHolder extends RecyclerView.ViewHolder{
        private TextView bookTitleTextView;
        private TextView bookAuthorTextView;

        public BookHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.book_list_item, parent, false));

            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookAuthorTextView = itemView.findViewById(R.id.book_author);
        }

        public void bind(Book book) {
            bookTitleTextView.setText(book.getTitle());
            bookAuthorTextView.setText(book.getAuthor());
            //book editing listeners
            bookTitleTextView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
                    intent.putExtra("bookTitle", book.getTitle());
                    intent.putExtra("bookAuthor", book.getAuthor());
                    intent.putExtra("bookId", book.getId());
                    getEditResult.launch(intent);
                }
            });

            bookAuthorTextView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
                    intent.putExtra("bookTitle", book.getTitle());
                    intent.putExtra("bookAuthor", book.getAuthor());
                    intent.putExtra("bookId", book.getId());
                    getEditResult.launch(intent);
                }
            });
            //book delete listeners
            bookTitleTextView.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View view) {
                    bookViewModel.delete(book);
                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_deleted),
                            Snackbar.LENGTH_LONG).show();
                    return false;
                }
            });

            bookAuthorTextView.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View view) {
                    bookViewModel.delete(book);
                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_deleted),
                            Snackbar.LENGTH_LONG).show();
                    return false;
                }
            });
        }
    }

    private class BookAdapter extends RecyclerView.Adapter<BookHolder>{
        private List<Book> books;

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position) {
            if(books != null){
                Book book = books.get(position);
                holder.bind(book);
            }else{
                Log.d("Main activity", "No books");
            }
        }

        @Override
        public int getItemCount() {
            if(books != null){
                return books.size();
            }else{
                return 0;
            }
        }

        void setBooks(List<Book> books){
            this.books = books;
            notifyDataSetChanged();
        }

        public List<Book> getBooks(){
            return books;
        }
    }
    /*
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == NEW_BOOK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
                Book book = new Book(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE),
                        data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR));
                bookViewModel.insert(book);
                Snackbar.make(findViewById(R.id.main_layout), getString(R.string.book_added),
                        Snackbar.LENGTH_LONG).show();
            }else{
                Snackbar.make(findViewById(R.id.main_layout), getString(R.string.empty_not_saved),
                        Snackbar.LENGTH_LONG).show();
            }
        }
    */
    ActivityResultLauncher<Intent> getResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Book book = new Book(result.getData().getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE),
                                result.getData().getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR));
                        bookViewModel.insert(book);
                        Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_added),
                                Snackbar.LENGTH_LONG).show();
                    }
                    else{
                        Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.empty_not_saved),
                                Snackbar.LENGTH_LONG).show();
                    }
                }
            }
    );

    ActivityResultLauncher<Intent> getEditResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Book book = new Book(result.getData().getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE),
                                result.getData().getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR));
                        book.setId(result.getData().getIntExtra(EditBookActivity.EXTRA_EDIT_BOOK_ID, 0));
                        bookViewModel.update(book);
                        Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_edited),
                                Snackbar.LENGTH_LONG).show();
                    }
                    else if(result.getResultCode() == 2){
                        Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_no_changes),
                                Snackbar.LENGTH_LONG).show();
                    }
                    else{
                        Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.empty_not_edited),
                                Snackbar.LENGTH_LONG).show();
                    }
                }
            }
    );
}