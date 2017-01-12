package genero.dexterous.com.almanac.discussion;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import genero.dexterous.com.almanac.Home.HomeActivity;
import genero.dexterous.com.almanac.R;


public class ChatActivity extends Activity {



    public void changeStatusBarColor(){

        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }






    }

    private ListView lvChat;

    private ArrayList<Message> mMessages=new ArrayList<Message>();

    private ChatListAdapter mAdapter;

    private Handler handler = new Handler();

// Keep track of initial load to scroll to the bottom of the ListView

    private boolean mFirstLoad;

    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;


    private static final String TAG = ChatActivity.class.getName();

    private static String sUserId;

    private static String sUserName;

    public static final String USER_ID_KEY = "userId";



    private EditText etMessage;

    private ImageButton btSend;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(ChatActivity.this, HomeActivity.class);
        startActivity(in);
    }
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);


//        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.show();
//            actionBar.setDisplayUseLogoEnabled(true);
//            actionBar.setTitle("Join the discussion");
//            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));
//
//        }
        // User login


        if (ParseUser.getCurrentUser() != null) { // start with existing user

            startWithCurrentUser();

        } else { // If not logged in, login as a new anonymous user

            // login();
            Intent in=new Intent(ChatActivity.this,SignUpActivity.class);
            //  in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //  in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);

        }

        handler.postDelayed(runnable, 100);

    }






    private Runnable runnable = new Runnable() {

        @Override

        public void run() {

            refreshMessages();

            handler.postDelayed(this, 100);

        }

    };



    private void refreshMessages() {

        receiveMessage();

    }





    // Create an anonymous user using ParseAnonymousUtils and set sUserId

    private void login() {

        ParseAnonymousUtils.logIn(new LogInCallback() {

            @Override

            public void done(ParseUser user, ParseException e) {

                if (e != null) {

                    Log.d(TAG, "Anonymous login failed: " + e.toString());

                } else {

                    startWithCurrentUser();

                }

            }

        });

    }



    // Get the userId from the cached currentUser object

    private void startWithCurrentUser() {
        Log.e("piyush ","user is logged in");

        sUserId = ParseUser.getCurrentUser().getObjectId();

        sUserName= (String) ParseUser.getCurrentUser().get("username");

        setupMessagePosting();

    }



// Setup button event handler which posts the entered message to Parse

    private void setupMessagePosting() {

        etMessage = (EditText) findViewById(R.id.etMessage);

        btSend = (ImageButton) findViewById(R.id.btSend);

        lvChat = (ListView) findViewById(R.id.lvChat);

        //   mMessages = new ArrayList<Message>();


        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.

        lvChat.setTranscriptMode(1);

        mFirstLoad = true;

        mAdapter = new ChatListAdapter(ChatActivity.this, sUserId, mMessages);

        lvChat.setAdapter(mAdapter);

        btSend.setOnClickListener(new View.OnClickListener() {



            @Override

            public void onClick(View v) {

                String body = etMessage.getText().toString();

                // Use Message model to create new messages now

                Message message = new Message();

                message.setUserId(sUserId);

                message.setBody(body);

                message.setUsername(sUserName);


                message.saveInBackground(new SaveCallback() {

                    @Override

                    public void done(ParseException e) {

                        receiveMessage();

                    }

                });

                etMessage.setText("");

            }

        });

    }


    private void receiveMessage() {

        // Construct query to execute


        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);

        // Configure limit and sort order

        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);

        query.orderByAscending("createdAt");







        // Execute query to fetch all messages from Parse asynchronously

        // This is equivalent to a SELECT query with SQL

        query.findInBackground(new FindCallback<Message>() {

            public void done(List<Message> messages, ParseException e) {

                if (e == null) {


//..........................  error here......
                    if (mMessages.isEmpty()) {


                    } else {
                        mMessages.clear();
                    }

                    mMessages.addAll(messages);


                    mAdapter.notifyDataSetChanged(); // update adapter

                    // Scroll to the bottom of the list on initial load

                    if (mFirstLoad) {

                        lvChat.setSelection(mAdapter.getCount() - 1);

                        mFirstLoad = false;

                    }

                } else {

                    Log.d("message", "Error: " + e.getMessage());

                }

            }

        });

    }



}