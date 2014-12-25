// IUAFClient.aidl
package cn.ac.iie.fidoclient;

// Declare any non-default types here with import statements
import cn.ac.iie.fidoclient.IUAFErrorCallback;
import cn.ac.iie.fidoclient.IUAFResponseCallback;
import cn.ac.iie.fidoclient.Discovery;
import cn.ac.iie.fidoclient.UAFMessage;

interface IUAFClient {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    Discovery getDiscovery();
    void notifyUAFResult(in int responseCode, in String uafResponse);
    void processUAFMessage(in UAFMessage msg, in String origin,
            in Map channelBindings, in boolean checkPolicy, in IUAFResponseCallback cb,
            in IUAFErrorCallback errorCb);

}
