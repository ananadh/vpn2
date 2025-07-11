package com.mzhslab.koreavpn.utils;

import android.text.TextUtils;

import com.mzhslab.koreavpn.Model.api_model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ping {

    public static float ping(api_model responsePojo) {

        String result;
        float time = 1000.0f;
        result = execeutecmd("ping -c 3 -i 0.2 -W 1 " + responsePojo.getIp_ping());
        if (!TextUtils.isEmpty(result)) {
            Pattern pat;
            Matcher mc;
            pat = Pattern.compile("=\\s*([\\d\\.]+)/([\\d\\.]+)/([\\d\\.]+)/.*\\s*ms");
            mc = pat.matcher(result);
            if (mc.find()) {
                time = Float.parseFloat(mc.group(1));
            }
        }

        return time;
    }

    public static String execeutecmd(String command) {
        final String libs = "LD_LIBRARY_PATH=/vendor/lib*:/system/lib* ";
        return execCommand(new String[]{libs + command});
    }

    public static String execCommand(String[] commands) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return "";
        }
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("sh");
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }

                // donnot use os.writeBytes(commmand), avoid chinese charset error
                os.write(command.getBytes());
                os.writeBytes("\n");
                os.flush();
            }
            os.writeBytes("exit\n");
            os.flush();

            result = process.waitFor();
            // get command result


            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                // if(ComUtils.DEBUG) T.w(3,s);
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }
        }
        if (result == 0) {
            if (successMsg != null) {
                return successMsg.toString();
            }
        } else {
            if (errorMsg != null) {
                return errorMsg.toString();
            }
        }
        return null;
    }
}