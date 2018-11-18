package com.example.book;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TicketListActivity extends AppCompatActivity{

    // 데이터 읽어오기 위한 어댑터 설정
    private FirebaseRecyclerAdapter<CardItem, ViewHolder> mFirebaseAdapter;
    public static final String MESSAGES_CHILD = "TicketBook";
    private DatabaseReference mFirebaseDatabaseReference;
    private RecyclerView mRecyclerView;
    String email;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ticketImageView;
        TextView title;
        TextView contents;
        Button share;
        Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_text);
            contents = itemView.findViewById(R.id.contents_text);
            share = itemView.findViewById(R.id.share_button);
            delete = itemView.findViewById(R.id.delete_button);
            ticketImageView = itemView.findViewById(R.id.ticket_image);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);
        // 넘어온 이메일 값받기
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mRecyclerView = findViewById(R.id.recycler_view);

        // 추가 버튼
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TicketListActivity.this, DataActivity.class));
            }
        });

        // 데이터 전체 내용 얻겠다.
        // 쿼리 수행 위치
        Query query = mFirebaseDatabaseReference.child(MESSAGES_CHILD);
        // 옵션
        FirebaseRecyclerOptions<CardItem> options =
                new FirebaseRecyclerOptions.Builder<CardItem>()
                .setQuery(query, CardItem.class)
                .build();

        // 어댑터
        mFirebaseAdapter = new FirebaseRecyclerAdapter<CardItem, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, int position, CardItem model) {
                // 데이터 가져오기
                holder.title.setText(model.getTitle());
                holder.contents.setText(model.getContents());
                // 이메일로 로그인 한 경우
                if(model.getPhotoUrl() == null){
                    holder.ticketImageView.setImageDrawable(ContextCompat.getDrawable
                            (TicketListActivity.this, R.drawable.ticket_2));
                }else{
                    holder.ticketImageView.setImageURI(Uri.parse(model.getPhotoUrl()));
                    Glide.with(TicketListActivity.this)
                            .load(model.getPhotoUrl())
                            .into(holder.ticketImageView);
                }

                // 클릭 이벤트 처리
                // 전체
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TicketListActivity.this, "Item Selected", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(TicketListActivity.this, DataActivity.class));

                    }
                });
                // 공유
                holder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TicketListActivity.this, "Share Selected", Toast.LENGTH_SHORT).show();

                    }
                });
                // 삭제
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TicketListActivity.this, "Delete Selected", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_card, viewGroup, false);
                return new ViewHolder(view);
            }
        };

        // 리사이클러뷰에 레이아웃 매니저와 어댑터를 설정
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);

    }


    // 메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.memu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Toast.makeText(TicketListActivity.this, email, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TicketListActivity.this, ChatActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                //startActivity(new Intent(TicketListActivity.this, ChatActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 데이터 읽어올 수 있다.
    @Override
    protected void onStart() {
        super.onStart();
        // firebaseRecyclerAdapter 실시간 쿼리 시작
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // firebaseRecyclerAdapter 실시간 쿼리 중지
        mFirebaseAdapter.stopListening();
    }
}
