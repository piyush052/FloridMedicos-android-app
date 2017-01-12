package genero.dexterous.com.almanac.discussion;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import genero.dexterous.com.almanac.R;

/**
 * Created by piyush on 1/6/16.
 */

public class ChatListAdapter extends ArrayAdapter<Message> {

    private String mUserId;

    private String mUserName;


    public ChatListAdapter(Context context, String userId, List<Message> messages) {

        super(context, 0, messages);

        this.mUserId = userId;
        // this.mUserName=sUserName;


    }




    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).

                    inflate(R.layout.chat_item, parent, false);

            final ViewHolder holder = new ViewHolder();




            holder.imageLeft = (TextView)convertView.findViewById(R.id.ivProfileLeft);

            holder.imageRight = (TextView)convertView.findViewById(R.id.ivProfileRight);

            holder.body = (TextView)convertView.findViewById(R.id.tvBody);

            convertView.setTag(holder);

        }

        final Message message = (Message)getItem(position);

        final ViewHolder holder = (ViewHolder)convertView.getTag();

        final boolean isMe = message.getUserId().equals(mUserId);

        // Show-hide image based on the logged-in user.

        // Display the profile image to the right for our user, left for other users.

        if (isMe) {

            holder.imageRight.setVisibility(View.VISIBLE);

            convertView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            holder.imageLeft.setVisibility(View.GONE);

            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

        } else {

            holder.imageLeft.setVisibility(View.VISIBLE);

            holder.imageRight.setVisibility(View.GONE);
            convertView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));

            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

        }

        final TextView profileView = isMe ? holder.imageRight : holder.imageLeft;

        //Picasso.with(getContext()).load(getProfileUrl(message.getUserId())).into(profileView);
          profileView.setText(message.getUsername());
        //"<font color='#EE0000'>~ "+message.getUsername()+"</font>"+"\n"+

        holder.body.setText(""+message.getBody());

        return convertView;

    }



    // Create a gravatar image based on the hash value obtained from userId

    private static String getProfileUrl(final String userId) {

        String hex = "";

        try {

            final MessageDigest digest = MessageDigest.getInstance("MD5");

            final byte[] hash = digest.digest(userId.getBytes());

            final BigInteger bigInt = new BigInteger(hash);

            hex = bigInt.abs().toString(16);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";

    }



    final class ViewHolder {

        public TextView imageLeft;

        public TextView imageRight;

        public TextView body;

    }



}