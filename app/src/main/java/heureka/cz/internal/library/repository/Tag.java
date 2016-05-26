package heureka.cz.internal.library.repository;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tomas on 19.5.16.
 */
public class Tag implements Parcelable {

    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tag);
    }

    public Tag() {
    }

    protected Tag(Parcel in) {
        this.tag = in.readString();
    }

    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel source) {
            return new Tag(source);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    @Override
    public String toString() {
        return tag;
    }
}
