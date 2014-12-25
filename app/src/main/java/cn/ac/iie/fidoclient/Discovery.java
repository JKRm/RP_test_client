package cn.ac.iie.fidoclient;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rimon_kou on 14-12-22.
 */
public class Discovery implements Parcelable{

    public List<Version> supportedUAFVersions = new ArrayList<Version>();
    public String clientVendor;
    public Version clientVersion;
    public List<Authenticator> availableAuthenticators = new ArrayList<Authenticator>();

    public static final Creator<Discovery> CREATOR = new Creator<Discovery>() {
        @Override
        public Discovery createFromParcel(Parcel source) {
            return new Discovery(source);
        }

        @Override
        public Discovery[] newArray(int size) {
            return new Discovery[size];
        }
    };

    public Discovery(){

    }

    private Discovery(Parcel in){
        in.readTypedList(supportedUAFVersions, Version.CREATOR);
        clientVendor = in.readString();
        clientVersion = in.readParcelable(null);
        in.readTypedList(availableAuthenticators, Authenticator.CREATOR);
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(supportedUAFVersions);
        dest.writeString(clientVendor);
        dest.writeParcelable(clientVersion, 0);
        dest.writeTypedList(availableAuthenticators);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
