package com.maxk.notebook.blog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.maxk.notebook.maxkgi.R;
import com.squareup.picasso.Picasso;

public class BlogMainActivity extends AppCompatActivity {

    private RecyclerView blogList;
    private DatabaseReference databaseRef;
    private DatabaseReference databaseRefUsers;
    // private DatabaseReference databaseRefCurrentUser;
    private DatabaseReference databaseRefLike;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Query queryCurrentUser;
    private String user_id;

    private Boolean processLike;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                /* ToDo:
                if (user == null) {
                    Intent loginIntent = new Intent(BlogMainActivity.this, LoginActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                } else {
                    user_id = auth.getCurrentUser().getUid();
                }
                */
            }
        };

        databaseRef = FirebaseDatabase.getInstance().getReference().child(Config.BLOG_PATH);
        databaseRefUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseRefLike = FirebaseDatabase.getInstance().getReference().child("Likes");

        // String currentUserId = "kgi"; // auth.getCurrentUser().getUid();
        // databaseRefCurrentUser = FirebaseDatabase.getInstance().getReference().child("Blog");
        // queryCurrentUser = databaseRefCurrentUser.orderByChild("uid").equalTo(currentUserId);

        databaseRefUsers.keepSynced(true);
        databaseRef.keepSynced(true);
        databaseRefLike.keepSynced(true);

        blogList = (RecyclerView) findViewById(R.id.blogList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        blogList.setHasFixedSize(true);
        blogList.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        // checkUserExist();
        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                        Blog.class,
                        R.layout.blog_row,
                        BlogViewHolder.class,
//                        queryCurrentUser
                        databaseRef
                ) {
                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

                        final String postKey = getRef(position).getKey();

                        viewHolder.setTitle("제목: " + model.getTitle());
                        viewHolder.setDesc(model.getDescription());
                        viewHolder.setImage(getApplicationContext(), model.getImageURL());
                        viewHolder.setUsername(model.getUsername());

                        viewHolder.setLikeBtn(postKey);

                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent singleBlogIntent = new Intent(BlogMainActivity.this, BlogSingleActivity.class);
                                singleBlogIntent.putExtra("postKey", postKey);
                                startActivity(singleBlogIntent);
                            }
                        });

                        viewHolder.likeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                processLike = true;
                                databaseRefLike.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (processLike) {
                                            if (dataSnapshot.child(postKey).hasChild(auth.getCurrentUser().getUid())) {

                                                databaseRefLike.child(postKey).child(auth.getCurrentUser().getUid()).removeValue();
                                                processLike = false;

                                            } else {

                                                databaseRefLike.child(postKey).child(auth.getCurrentUser().getUid()).setValue("RandomValue");
                                                processLike = false;

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                    }
                };

        blogList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageButton likeBtn;
        DatabaseReference databaseRefLike;
        FirebaseAuth auth;

        public BlogViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            likeBtn = (ImageButton) view.findViewById(R.id.likeBtn);
            databaseRefLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            auth = FirebaseAuth.getInstance();

            databaseRefLike.keepSynced(true);
        }

        void setTitle(String title) {
            TextView postTitle = (TextView) view.findViewById(R.id.postTitle);
            postTitle.setText(title);
        }

        void setLikeBtn(final String postKey) {
            databaseRefLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // ToDo:
                    /*
                    if (dataSnapshot.child(postKey).hasChild(auth.getCurrentUser().getUid())) {
                        likeBtn.setImageResource(R.drawable.ic_like);
                    } else {
                        likeBtn.setImageResource(R.drawable.ic_like_grey);
                    }
                    */
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        void setDesc(String desc) {
            TextView postDescription = (TextView) view.findViewById(R.id.postDesc);
            postDescription.setText(desc);
        }

        void setImage(Context context, String imageUrl) {
            ImageView postImage = (ImageView) view.findViewById(R.id.postImage);
            Picasso.with(context).load(imageUrl).into(postImage);
        }

        void setUsername(String username) {
            TextView postUsername = (TextView) view.findViewById(R.id.postUsername);
            postUsername.setText(username);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_post_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add) {
            if( auth.getCurrentUser() != null ) {
                startActivity(new Intent(BlogMainActivity.this, PostActivity.class));
            } else {
                Toast.makeText(this, "관리자만 공지를 등록할 수 있습니다.", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(BlogMainActivity.this, LoginActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }
            finish();
        }

        if (item.getItemId() == R.id.action_logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        auth.signOut();
    }


    private void checkUserExist() {

        databaseRefUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(user_id)) {
                    Intent setupIntent = new Intent(BlogMainActivity.this, SetupActivity.class);
                    setupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
