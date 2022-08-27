package com.example.bookly.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bookly.R;

public class AddPostFragment extends Fragment {

    EditText postContentEt;
    AppCompatButton postBtn;
    ImageView uploadImageIv, postImageIv;
    Uri uri;

    public AddPostFragment() {
        // Required empty public constructor
    }


    public static AddPostFragment newInstance(String param1, String param2) {
        AddPostFragment fragment = new AddPostFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        postContentEt = view.findViewById(R.id.postContentEt);
        postBtn = view.findViewById(R.id.postBtn);
        uploadImageIv = view.findViewById(R.id.uploadIv);
        postImageIv = view.findViewById(R.id.postImageIv);

        postContentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String content = postContentEt.getText().toString();
                if (!content.isEmpty()){
                    postBtn.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.post_btn_bg));
                    postBtn.setTextColor(getContext().getResources().getColor(R.color.white));
                    postBtn.setEnabled(true);
                } else {
                    postBtn.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.post_active_btn));
                    postBtn.setTextColor(getContext().getResources().getColor(R.color.gray));
                    postBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        uploadImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null){
            uri = data.getData();
            postImageIv.setImageURI(uri);
            postImageIv.setVisibility(View.VISIBLE);

            postBtn.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.post_btn_bg));
            postBtn.setTextColor(getContext().getResources().getColor(R.color.white));
            postBtn.setEnabled(true);
        }
    }
}