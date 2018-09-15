import com.sun.deploy.net.HttpRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HttpClient {

    public static CurlCommandLine parseCurlCommandLine(String cmd){
        CurlCommandLine curlCommandLine = new CurlCommandLine();

        String[] cmdArray = cmd.split(" ");

        if (cmdArray[0].equals("httpc")){
            curlCommandLine.setValid(true);
        } else{
            curlCommandLine.setValid(false);
        }

        if (cmd.contains("get")){
            curlCommandLine.setRequestType("get");
        }

        if (cmd.contains("post")){
            curlCommandLine.setRequestType("post");
        }

        if (cmd.contains("-v")){
            curlCommandLine.setVerbose(true);
        } else {
            curlCommandLine.setVerbose(false);
        }

        if (cmd.contains("-h")){
            curlCommandLine.setHaveHeaders(true);
            String headerString = findTarget("-h",cmdArray);
            String[] headerSplit = headerString.split(";");
            ArrayList<String> headers = new ArrayList<>();
            for (int i = 0; i < headerSplit.length; i++) {
                headers.add(headerSplit[i]);
            }
            curlCommandLine.setHeaders(headers);
        } else {
            curlCommandLine.setHaveHeaders(false);
        }

        if (cmd.contains("-d")){
            curlCommandLine.setHaveInlineData(true);
            String inlineData = findTarget("-d", cmdArray);
            curlCommandLine.setInlineData(inlineData);
        }else {
            curlCommandLine.setHaveInlineData(false);
        }

        if (cmd.contains("-f")) {
            curlCommandLine.setHaveFile(true);
            String file = findTarget("-f",cmdArray);
            curlCommandLine.setFile(file);
        } else {
            curlCommandLine.setHaveFile(false);
        }

        curlCommandLine.setUrl(cmdArray[cmdArray.length - 1]);

        return curlCommandLine;
    }

    private static String findTarget(String target, String[] cmdArray) {
        for (int i = 0; i < cmdArray.length; i++) {
            if (cmdArray[i].equals(target)) {
                if ((i + 1) < cmdArray.length &&
                        !cmdArray[i + 1].equals("-v") &&
                        !cmdArray[i + 1].equals("-h") &&
                        !cmdArray[i + 1].equals("-d") &&
                        !cmdArray[i + 1].equals("-f")) {
                    return cmdArray[i + 1];
                }
            }
        }

        String notFind = "notFind";
        return notFind;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String cmd = scanner.nextLine();

        CurlCommandLine commandLine = parseCurlCommandLine(cmd);

        HttpLibrary httpLibrary = new HttpLibrary(commandLine);

        System.out.println(cmd);
    }
}