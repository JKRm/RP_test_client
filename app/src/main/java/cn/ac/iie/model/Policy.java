package cn.ac.iie.model;

import java.util.List;

/**
 * Created by wangyang on 14/12/31.
 */
public class Policy {
    private List<List<p>> accepted;
    private List<p> disallowed;

    class p {
        List<Integer> supportedAuthAlgs;
        List<Integer> supportedSchemes;
        int authenticationFactor;
        int attachment;
        int secureDisplay;
        int keyProtection;
        String aaid;
    }
}
