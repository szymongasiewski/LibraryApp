package com.example.libraryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class EditBookActivity extends AppCompatActivity {

    public static final String EXTRA_EDIT_BOOK_TITLE = "EDIT_BOOK_TITLE";
    public static final String EXTRA_EDIT_BOOK_AUTHOR = "EDIT_BOOK_AUTHOR";
    public static final String EXTRA_EDIT_BOOK_ID = "EDIT_BOOK_ID";

    private EditText editTitleEditText;
    private EditText editAuthorEditText;

    private int bookId;
    private String bookTitleBeforeEdit;
    private String bookAuthorBeforeEdit;
    private static final int bookNotEdited = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        editTitleEditText = findViewById(R.id.edit_book_title);
        editAuthorEditText = findViewById(R.id.edit_book_author);
        final Button button = findViewById(R.id.button_save);

        Intent intent = getIntent();

        if(intent.hasExtra("bookTitle")){
            this.setTitle("Edit Book");
            editTitleEditText.setText(intent.getStringExtra("bookTitle"));
            editAuthorEditText.setText(intent.getStringExtra("bookAuthor"));
            bookTitleBeforeEdit = intent.getStringExtra("bookTitle");
            bookAuthorBeforeEdit = intent.getStringExtra("bookAuthor");
            bookId = intent.getIntExtra("bookId", 0);
            button.setOnClickListener(view -> {
                Intent replyIntent = new Intent();
                if(TextUtils.isEmpty(editTitleEditText.getText())
                        || TextUtils.isEmpty(editAuthorEditText.getText())){
                    setResult(RESULT_CANCELED, replyIntent);
                }else if(TextUtils.equals(bookTitleBeforeEdit, editTitleEditText.getText().toString())
                        && TextUtils.equals(bookAuthorBeforeEdit ,editAuthorEditText.getText().toString())){
                    setResult(bookNotEdited, replyIntent);

                }
                else{
                    String title = editTitleEditText.getText().toString();
                    replyIntent.putExtra(EXTRA_EDIT_BOOK_TITLE, title);
                    String author = editAuthorEditText.getText().toString();
                    replyIntent.putExtra(EXTRA_EDIT_BOOK_AUTHOR, author);
                    replyIntent.putExtra(EXTRA_EDIT_BOOK_ID, bookId);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            });

        }else{
            this.setTitle("Add book");
            button.setOnClickListener(view -> {
                Intent replyIntent = new Intent();
                if(TextUtils.isEmpty(editTitleEditText.getText())
                        || TextUtils.isEmpty(editAuthorEditText.getText())){
                    setResult(RESULT_CANCELED, replyIntent);
                }else{
                    String title = editTitleEditText.getText().toString();
                    replyIntent.putExtra(EXTRA_EDIT_BOOK_TITLE, title);
                    String author = editAuthorEditText.getText().toString();
                    replyIntent.putExtra(EXTRA_EDIT_BOOK_AUTHOR, author);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            });
        }
    }
}