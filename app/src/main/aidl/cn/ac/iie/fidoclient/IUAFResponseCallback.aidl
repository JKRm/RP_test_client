// IUAFResponseCallback.aidl
package cn.ac.iie.fidoclient;

// Declare any non-default types here with import statements
import cn.ac.iie.fidoclient.UAFMessage;

interface IUAFResponseCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void response(in UAFMessage uafResponse);
}
