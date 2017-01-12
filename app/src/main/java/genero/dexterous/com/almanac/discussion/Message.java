package genero.dexterous.com.almanac.discussion;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by piyush on 1/6/16.
 */
@ParseClassName("Message")

public class Message extends ParseObject {

    public String getUserId() {

        return getString("userId");

    }



    public String getBody() {

        return getString("body");

    }

    public  String getUsername()
    {
        return getString("userName");
    }


    public void setUserId(String userId) {

        put("userId", userId);

    }
    public void setUsername(String userName)
    {
        put("userName",userName);
    }



    public void setBody(String body) {

        put("body", body);

    }

}