package heureka.cz.internal.library.repository;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ondrej on 9. 5. 2016.
 */
public class BookHolders {

    String user_name;
    String type;
    String date;
    @SerializedName("return")
    String returnB;
    String rate;
    String textRate;

    public String getBorrowDate(){
        return date;
    }

    public String getReturnDate(){
        return returnB;
    }

    public String getName(){
        return user_name;
    }

    public String getType(){
        return type;
    }

    public String getRate(){
        return rate;
    }

    public String getTextRate() {
        return textRate;
    }

    public void setTextRate(String textRate) {
        this.textRate = textRate;
    }
}
