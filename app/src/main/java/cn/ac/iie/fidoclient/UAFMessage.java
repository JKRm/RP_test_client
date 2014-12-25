package cn.ac.iie.fidoclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rimon_kou on 14-12-22.
 */
public class UAFMessage implements Parcelable{

    public String uafProtocolMessage;
    public String additionalData;

    public static final Creator<UAFMessage> CREATOR = new Creator<UAFMessage>() {
        @Override
        public UAFMessage createFromParcel(Parcel source) {
            return new UAFMessage(source);
        }

        @Override
        public UAFMessage[] newArray(int size) {
            return new UAFMessage[size];
        }
    };

    public UAFMessage(){

    }

    private UAFMessage(Parcel in){
        uafProtocolMessage = in.readString();
        additionalData = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uafProtocolMessage);
        dest.writeString(additionalData);
    }
}
