package cn.ac.iie.fidoclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rimon_kou on 14-12-22.
 */
public class Version implements Parcelable {

    public int majorVersion;
    public int minorVersion;

    public static final Creator<Version> CREATOR = new Creator<Version>() {
        @Override
        public Version createFromParcel(Parcel source) {
            return new Version(source);
        }

        @Override
        public Version[] newArray(int size) {
            return new Version[size];
        }
    };

    public Version(){

    }

    private Version(Parcel in){
        majorVersion = in.readInt();
        minorVersion = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(majorVersion);
        dest.writeInt(minorVersion);
    }
}
