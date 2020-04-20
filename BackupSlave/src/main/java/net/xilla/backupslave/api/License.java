package net.xilla.backupslave.api;

import net.xilla.backupcore.api.config.ConfigManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class License {

    private static boolean licensed;

    public static boolean isLicensed() {
        return licensed;
    }

    public License() {
        String api = getAPI(ConfigManager.getInstance().getConfig("license.json").getString("license"));
        licensed = api.contains("{\"STATUS\":\"VALID\"}");
    }

    private static String getAPI(String license) {
        try {
            URL url = new URL("http://api.xilla.net/verify_backup_slave.php?license=" + license);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.addRequestProperty("Host", "xilla.net");
            httpCon.addRequestProperty("Connection", "keep-alive");
            httpCon.addRequestProperty("Cache-Control", "max-age=0");
            httpCon.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpCon.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
            httpCon.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            HttpURLConnection.setFollowRedirects(false);
            httpCon.setInstanceFollowRedirects(false);
            httpCon.setDoOutput(true);
            httpCon.setUseCaches(true);

            httpCon.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder a = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                a.append(inputLine);
            in.close();

            httpCon.disconnect();

            return a.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
