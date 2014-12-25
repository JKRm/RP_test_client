package cn.ac.iie.fidoclient;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by rimon_kou on 14-12-22.
 */
public class Authenticator implements Parcelable{

    public String AAID;
    public String description;
    public String logo;
    public long userVerification;
    public long keyProtection;
    public long attachmentHint;
    public long secureDisplay;
    public String assertionScheme;
    public long additionalInfo;
    public int authenticationAlgorithm;
    public List<Version> supportedUAFVersions;

    public static final Creator<Authenticator> CREATOR
            = new Creator<Authenticator>() {
        @Override
        public Authenticator createFromParcel(Parcel source) {
            return new Authenticator(source);
        }

        @Override
        public Authenticator[] newArray(int size) {
            return new Authenticator[size];
        }
    };

    private Authenticator(Parcel in){
        AAID = in.readString();
        description = in.readString();
        logo = in.readString();
        userVerification = in.readLong();
        keyProtection = in.readLong();
        attachmentHint = in.readLong();
        secureDisplay = in.readLong();
        assertionScheme = in.readString();
        additionalInfo = in.readLong();
        authenticationAlgorithm = in.readInt();
        in.readTypedList(supportedUAFVersions, Version.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AAID);
        dest.writeString(description);
        dest.writeString(logo);
        dest.writeLong(userVerification);
        dest.writeLong(keyProtection);
        dest.writeLong(attachmentHint);
        dest.writeLong(secureDisplay);
        dest.writeString(assertionScheme);
        dest.writeLong(additionalInfo);
        dest.writeInt(authenticationAlgorithm);
        dest.writeTypedList(supportedUAFVersions);
    }
}
