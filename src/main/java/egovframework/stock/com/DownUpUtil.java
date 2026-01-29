package egovframework.stock.com;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownUpUtil {

	/*	url download */
    public static String httpDownload(String sourceUrl, String sourceFile) {
        FileOutputStream fos = null;
        InputStream is = null;

		String targetUrl = sourceUrl;
		String targetFilename = sourceFile;

        try {
			fos = new FileOutputStream(targetFilename);
            URL url = new URL(targetUrl);
            URLConnection urlConnection = url.openConnection();
            is = urlConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int readBytes;

            while ((readBytes = is.read(buffer)) != -1) {
                fos.write(buffer, 0, readBytes);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }

                if (is != null) {
                    is.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

		return targetFilename;

    }
}
