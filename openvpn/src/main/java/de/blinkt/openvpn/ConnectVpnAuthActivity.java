package de.blinkt.openvpn;

import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.util.Log;

public class ConnectVpnAuthActivity extends Activity {
    public static final String KEY_CONFIG = "config";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    private String mConfig;
    private String mUsername;
    private String mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConfig = getIntent().getStringExtra(KEY_CONFIG);
        mUsername = getIntent().getStringExtra(KEY_USERNAME);
        mPassword = getIntent().getStringExtra(KEY_PASSWORD);
        Intent intent = VpnService.prepare(this);
        if (intent != null) {
            startActivityForResult(intent, 0);
        } else {
            startVpn();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            startVpn();
        }
        finish();
    }

    private void startVpn() {
        OpenVpnConnector.startVpnInternal(this, mConfig, mUsername, mPassword);
//        try {
////           String file="client\n" +
////                    "dev tun\n" +
////                    "proto udp\n" +
////                    "remote 23.82.193.175 1194\n" +
////                    "resolv-retry infinite\n" +
////                    "remote-random\n" +
////                    "nobind\n" +
////                    "tun-mtu 1500\n" +
////                    "tun-mtu-extra 32\n" +
////                    "mssfix 1450\n" +
////                    "persist-key\n" +
////                    "persist-tun\n" +
////                    "ping 15\n" +
////                    "ping-restart 0\n" +
////                    "ping-timer-rem\n" +
////                    "reneg-sec 0\n" +
////                    "comp-lzo no\n" +
////                    "\n" +
////                    "remote-cert-tls server\n" +
////                    "\n" +
////                    "auth-user-pass\n" +
////                    "verb 3\n" +
////                    "pull\n" +
////                    "fast-io\n" +
////                    "cipher AES-256-CBC\n" +
////                    "auth SHA512\n" +
////                    "<ca>\n" +
////                    "-----BEGIN CERTIFICATE-----\n" +
////                    "MIIFCjCCAvKgAwIBAgIBATANBgkqhkiG9w0BAQ0FADA5MQswCQYDVQQGEwJQQTEQ\n" +
////                    "MA4GA1UEChMHTm9yZFZQTjEYMBYGA1UEAxMPTm9yZFZQTiBSb290IENBMB4XDTE2\n" +
////                    "MDEwMTAwMDAwMFoXDTM1MTIzMTIzNTk1OVowOTELMAkGA1UEBhMCUEExEDAOBgNV\n" +
////                    "BAoTB05vcmRWUE4xGDAWBgNVBAMTD05vcmRWUE4gUm9vdCBDQTCCAiIwDQYJKoZI\n" +
////                    "hvcNAQEBBQADggIPADCCAgoCggIBAMkr/BYhyo0F2upsIMXwC6QvkZps3NN2/eQF\n" +
////                    "kfQIS1gql0aejsKsEnmY0Kaon8uZCTXPsRH1gQNgg5D2gixdd1mJUvV3dE3y9FJr\n" +
////                    "XMoDkXdCGBodvKJyU6lcfEVF6/UxHcbBguZK9UtRHS9eJYm3rpL/5huQMCppX7kU\n" +
////                    "eQ8dpCwd3iKITqwd1ZudDqsWaU0vqzC2H55IyaZ/5/TnCk31Q1UP6BksbbuRcwOV\n" +
////                    "skEDsm6YoWDnn/IIzGOYnFJRzQH5jTz3j1QBvRIuQuBuvUkfhx1FEwhwZigrcxXu\n" +
////                    "MP+QgM54kezgziJUaZcOM2zF3lvrwMvXDMfNeIoJABv9ljw969xQ8czQCU5lMVmA\n" +
////                    "37ltv5Ec9U5hZuwk/9QO1Z+d/r6Jx0mlurS8gnCAKJgwa3kyZw6e4FZ8mYL4vpRR\n" +
////                    "hPdvRTWCMJkeB4yBHyhxUmTRgJHm6YR3D6hcFAc9cQcTEl/I60tMdz33G6m0O42s\n" +
////                    "Qt/+AR3YCY/RusWVBJB/qNS94EtNtj8iaebCQW1jHAhvGmFILVR9lzD0EzWKHkvy\n" +
////                    "WEjmUVRgCDd6Ne3eFRNS73gdv/C3l5boYySeu4exkEYVxVRn8DhCxs0MnkMHWFK6\n" +
////                    "MyzXCCn+JnWFDYPfDKHvpff/kLDobtPBf+Lbch5wQy9quY27xaj0XwLyjOltpiST\n" +
////                    "LWae/Q4vAgMBAAGjHTAbMAwGA1UdEwQFMAMBAf8wCwYDVR0PBAQDAgEGMA0GCSqG\n" +
////                    "SIb3DQEBDQUAA4ICAQC9fUL2sZPxIN2mD32VeNySTgZlCEdVmlq471o/bDMP4B8g\n" +
////                    "nQesFRtXY2ZCjs50Jm73B2LViL9qlREmI6vE5IC8IsRBJSV4ce1WYxyXro5rmVg/\n" +
////                    "k6a10rlsbK/eg//GHoJxDdXDOokLUSnxt7gk3QKpX6eCdh67p0PuWm/7WUJQxH2S\n" +
////                    "DxsT9vB/iZriTIEe/ILoOQF0Aqp7AgNCcLcLAmbxXQkXYCCSB35Vp06u+eTWjG0/\n" +
////                    "pyS5V14stGtw+fA0DJp5ZJV4eqJ5LqxMlYvEZ/qKTEdoCeaXv2QEmN6dVqjDoTAo\n" +
////                    "k0t5u4YRXzEVCfXAC3ocplNdtCA72wjFJcSbfif4BSC8bDACTXtnPC7nD0VndZLp\n" +
////                    "+RiNLeiENhk0oTC+UVdSc+n2nJOzkCK0vYu0Ads4JGIB7g8IB3z2t9ICmsWrgnhd\n" +
////                    "NdcOe15BincrGA8avQ1cWXsfIKEjbrnEuEk9b5jel6NfHtPKoHc9mDpRdNPISeVa\n" +
////                    "wDBM1mJChneHt59Nh8Gah74+TM1jBsw4fhJPvoc7Atcg740JErb904mZfkIEmojC\n" +
////                    "VPhBHVQ9LHBAdM8qFI2kRK0IynOmAZhexlP/aT/kpEsEPyaZQlnBn3An1CRz8h0S\n" +
////                    "PApL8PytggYKeQmRhl499+6jLxcZ2IegLfqq41dzIjwHwTMplg+1pKIOVojpWA==\n" +
////                    "-----END CERTIFICATE-----\n" +
////                    "</ca>\n" +
////                    "key-direction 1\n" +
////                    "<tls-auth>\n" +
////                    "#\n" +
////                    "# 2048 bit OpenVPN static key\n" +
////                    "#\n" +
////                    "-----BEGIN OpenVPN Static key V1-----\n" +
////                    "e685bdaf659a25a200e2b9e39e51ff03\n" +
////                    "0fc72cf1ce07232bd8b2be5e6c670143\n" +
////                    "f51e937e670eee09d4f2ea5a6e4e6996\n" +
////                    "5db852c275351b86fc4ca892d78ae002\n" +
////                    "d6f70d029bd79c4d1c26cf14e9588033\n" +
////                    "cf639f8a74809f29f72b9d58f9b8f5fe\n" +
////                    "fc7938eade40e9fed6cb92184abb2cc1\n" +
////                    "0eb1a296df243b251df0643d53724cdb\n" +
////                    "5a92a1d6cb817804c4a9319b57d53be5\n" +
////                    "80815bcfcb2df55018cc83fc43bc7ff8\n" +
////                    "2d51f9b88364776ee9d12fc85cc7ea5b\n" +
////                    "9741c4f598c485316db066d52db4540e\n" +
////                    "212e1518a9bd4828219e24b20d88f598\n" +
////                    "a196c9de96012090e333519ae18d3509\n" +
////                    "9427e7b372d348d352dc4c85e18cd4b9\n" +
////                    "3f8a56ddb2e64eb67adfc9b337157ff4\n" +
////                    "-----END OpenVPN Static key V1-----\n" +
////                    "</tls-auth>";
////
////            Log.d("checkProfile55" , "checkProfile55");
//        } catch (RuntimeException e) {
//        }
    }
}
