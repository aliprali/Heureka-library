package heureka.cz.internal.library.repository;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tomas on 6.4.16.
 */

public class Book implements Parcelable {

    private Integer bookId;

    private String name;

    private String detailLink;

    private String lang;

    private String form;

    private String mime;

    private Integer total;

    @SerializedName(value="tags")
    private List<Tag> tags = new LinkedList<>();

    @SerializedName(value="holders")
    private ArrayList<Holder> holders = new ArrayList<>();

    @SerializedName(value="available")
    private ArrayList<BookAvailable> available = new ArrayList<>();

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailLink() {
        return detailLink;
    }

    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public ArrayList<Holder> getHolders() {
        return holders;
    }

    public void setHolders(ArrayList<Holder> holders) {
        this.holders = holders;
    }

    public ArrayList<BookAvailable> getAvailable() {
        return available;
    }

    public void setAvailable(ArrayList<BookAvailable> available) {
        this.available = available;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bookId);
        dest.writeString(this.name);
        dest.writeString(this.detailLink);
        dest.writeString(this.lang);
        dest.writeString(this.form);
        dest.writeString(this.mime);
        dest.writeValue(this.total);
        dest.writeTypedList(this.tags);
        dest.writeTypedList(holders);
        dest.writeList(this.available);
    }

    public Book() {
    }

    protected Book(Parcel in) {
        this.bookId = in.readInt();
        this.name = in.readString();
        this.detailLink = in.readString();
        this.lang = in.readString();
        this.form = in.readString();
        this.mime = in.readString();
        this.total = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tags = in.createTypedArrayList(Tag.CREATOR);
        this.holders = in.createTypedArrayList(Holder.CREATOR);
        this.available = new ArrayList<BookAvailable>();
        in.readList(this.available, BookAvailable.class.getClassLoader());
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", name='" + name + '\'' +
                ", detailLink='" + detailLink + '\'' +
                ", lang='" + lang + '\'' +
                ", form='" + form + '\'' +
                ", mime=" + mime + '\'' +
                ", total=" + total +
                ", tags=" + tags +
                ", holders=" + holders +
                ", available=" + available +
                '}';
    }
}
