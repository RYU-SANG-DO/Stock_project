/**
 *------------------------------------------------------------------------------
 * PROJ : newehrd
 * NAME : FileUtil
 * DESC : 
 * Author : gwangrak.hong
 * IssueID : 
 * VER  : v1.0  * 
 * Copyright 2007 HANWHA S&C All rights reserved
 *------------------------------------------------------------------------------
 *                  변         경         사         항                       
 *------------------------------------------------------------------------------
 *    DATE     AUTHOR                      DESCRIPTION                        
 * ----------  ------  --------------------------------------------------------- 
 * 2007. 12. 26  	gwangrak.hong  최초 프로그램 작성                                     
 *----------------------------------------------------------------------------
 */ 
package egovframework.stock.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class FileUtil {

	/**
	 * 디렉토리를 생성한다.
	 * @param directory 생성할 디렉토리 패스
	 * @return 디렉토리 생성에 성공했거나 이미 존재하는 디렉토리면 true, 문제가 있으면 false를 리턴
	 */
	public static boolean makeDir(String directory) throws Exception
	{
		boolean makeOk = true;

		if (directory == null)
		{
			////System.out.println("FileUtil.makeDir() >> saveDirectory cannot be null");
			return false;
			//throw new Exception("FileUtil.makeDir() >> saveDirectory cannot be null");
		}

		File dir = new File(directory);

		// 만들고자 하는 디렉토리가 존재하는지 체크
		if (!dir.isDirectory()) // 지정된 디렉토리가 존재하는지, 그리고 파일인지 체크
		{
			if (!dir.mkdirs()) // 디렉토리가 없으면 새로 생성한다.
			{
				////System.out.println("FileUtil.makeDir() >> Cannot make directory");
				return false;
				//throw new Exception("FileUtil.makeDir() >> Cannot make directory");
			}
		}

		// 생성한 디렉토리가 쓰기권한이 있는지 체크
		if (!dir.canWrite())
		{
			////System.out.println("FileUtil.makeDir() >> Not writable");
			return false;
			//throw new Exception("FileUtil.makeDir() >> Not writable");
		}

		return true;
	}

	/**
	 * 파일을 삭제한다.
	 * @param path 삭제할 파일의 경로
	 * @param svrfile 삭제할 파일의 이름
	 * @return 삭제 성공시 true, 문제가 있으면 false를 리턴
	 */
	public static synchronized boolean deleteFile(String path, String svrfile) throws Exception
	{
		if (!StringUtil.nvl(path).equals("") && !StringUtil.nvl(svrfile).equals(""))
		{
			File f = null;

			try
			{
				f = new File(path, svrfile);

				if (f.isFile())
				{
					f.delete();
				}
				else
				{
					////System.out.println("FileUtil.deleteFile() >> No file");
				}
			}
			finally
			{
				f = null;
			}
		}

		return true;
	}

	/**
	 * 파일을 삭제한다.
	 * @param path 삭제할 파일의 경로와 이름
	 * @return 삭제 성공시 true, 문제가 있으면 false를 리턴
	 */
	public static boolean deleteFile(String path) throws Exception
	{
		boolean result = true;

		File f = null;
		try
		{
			f = new File(path);

			if (!f.exists()) // 파일이 없으면 지워졌다고 간주한다.
			{
				result = true;
			}
			else if (!f.isDirectory()) // 디렉토리가 아니면
			{
				result = f.delete();
			}
			else // 디렉토리이면 삭제하지 않는다.
				{
				result = true;
			}
		}
		finally
		{
			f = null;
		}

		return result;
	}

	/**
	 * 디렉토리를 삭제한다.(재귀적으로 호출되면서 삭제가 이루어진다.)
	 * @param path 삭제할 디렉토리의 패스
	 * @return 삭제 성공시 true, 문제가 있으면 false를 리턴
	 */
	public static boolean deleteDir(String path) throws Exception
	{
		File f = null;
		File fTmp = null;
		String[] files = null;
		boolean result = false;

		try
		{
			f = new File(path);

			if (!f.exists()) // 파일이 없으면 지워졌다고 간주한다.
			{
				result = true;
			}
			else if (f.isDirectory()) // 디렉토리이면 (파일이면 ? 삭제를 안한다..)
			{
				boolean b = true;

				files = f.list(); // 디렉토리내의 파일리스트를 구한다.

				for (int i = 0; i < files.length; i++) // 각 파일을 삭제시도한다.
				{
					String filepath = path + File.separator + files[i];

					fTmp = new File(filepath);

					if (fTmp.isDirectory()) // 디렉토리이면
					{
						b = deleteDir(filepath);
					}
					else
					{
						b = deleteFile(filepath);
					}

					if (!b)
						break; // 삭제에 실패하면 삭제작업을 중단한다.
				}

				if (b) // 하위파일들이 모두 삭제되었으면
					result = f.delete(); // 자신을 지운다.
				else
					result = b;
			}
			else
			{
				result = false; // 파일은 지우지 않는다.
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally
		{
			f = null;
			fTmp = null;
			files = null;
		}

		return result;
	}

	/**
	 * 다운로드 HTML 코드를 리턴
	 * @param svrdir 파일이 있는 디렉토리의 패스
	 * @param svrfile 다운로드할 파일의 서버 파일 이름
	 * @param upfile 다운로드할 파일의 원래 이름
	 * @return 다운로드 HTML 코드
	 */
	public static String getDownloadUrl(String pSvrDir, String pSvrFile, String pOldFile) throws Exception
	{
		String sResult = "";
		String sEncodedSvrdir = "";
		String sUpFileEncoded = "";

		sEncodedSvrdir = URLEncoder.encode(pSvrDir, "euc-kr");
		sUpFileEncoded = URLEncoder.encode(pOldFile, "euc-kr");

		sResult =
			"<a href=\"/servlet/Download?p_svrdir="
				+ sEncodedSvrdir
				+ "&p_svrfile="
				+ pSvrFile
				+ "&p_upfile="
				+ sUpFileEncoded
				+ "\" class='text'>"
				+ pOldFile
				+ "</a>";

		/*		sResult =
					"<a href='#' onclick=\"javascript:window.open('/servlet/Download?p_svrdir="
						+ sEncodedSvrdir
						+ "&p_svrfile="
						+ pSvrFile
						+ "&p_upfile="
						+ sUpFileEncoded
						+ "','','width=350,height=350');\" class='text'>"
						+ pUpFile
						+ "</a>";
		*/

		return sResult;
	}

	/**
	 * 다운로드 HTML 코드(링크걸린 첨부파일 아이콘만)를 리턴
	 * @param svrdir 파일이 있는 디렉토리의 패스
	 * @param svrfile 삭제할 파일 이름
	 * @param upfile 삭제할 파일의 원래 이름
	 * @param linkname 링크 이름
	 * @return 다운로드 HTML 코드
	 */
	public static String getDownloadUrl2(String pSvrDir, String pSvrFile, String pOldFile) throws Exception
	{
		String sResult = "";
		String sEncodedSvrdir = "";

		sEncodedSvrdir = URLEncoder.encode(pSvrDir, "euc-kr");
		sResult =
			"<a href=\"/servlet/Download?p_svrdir="
				+ sEncodedSvrdir
				+ "&p_svrfile="
				+ pSvrFile
				+ "&p_upfile="
				+ pOldFile
				+ "\" class='text'>"
				+ "<img src=\"/img/lms/bbs/attach_file.gif\" align=\"absmiddle\" border=\"0\">"
				+ "</a>";

		return sResult;
	}

	/**
	 * 이미지 URL에 대한 HTML코드를 리턴
	 * @param pSvrDir
	 * @param pSvrFile
	 * @param pOldFile
	 * @return
	 * @throws Exception
	 */
	public static String getImgUrl(String pSvrDir, String pSvrFile, String pOldFile) throws Exception
	{
		String sResult = "";
		String sEncodedSvrdir = "";
		String sUpFileEncoded = "";
		if (pOldFile.equals(null) || pOldFile.equals(""))
		{
			sResult = "";
		}
		else
		{
			sEncodedSvrdir = URLEncoder.encode(pSvrDir, "euc-kr");
			sUpFileEncoded = URLEncoder.encode(pOldFile, "euc-kr");

			sResult =
				""
					+ "<img name=\"iMg\" src=\"/servlet/Download?p_svrdir="
					+ sEncodedSvrdir
					+ "&p_svrfile="
					+ pSvrFile
					+ "&p_upfile="
					+ sUpFileEncoded
					+ "\" alt="
					+ pOldFile
					+ " onclick=\"javascript:window.open('/com/popup/imgview/"
					+ "origin_size_img_view.jsp?p_svrdir="
					+ sEncodedSvrdir
					+ "&p_svrfile="
					+ pSvrFile
					+ "&p_upfile="
					+ sUpFileEncoded
					+ "','','resizable=yes,width=350,height=350');\" style=\"cursor:hand\"><br>"
					+ "";
		}

		return sResult;
	}

	/**
		 * 이미지 URL에 대한 HTML코드를 리턴
		 * @param pSvrDir
		 * @param pSvrFile
		 * @param pOldFile
		 * @return
		 * @throws Exception
		 */
	public static String getWavUrl(String pSvrDir, String pSvrFile, String pOldFile) throws Exception
	{
		String sResult = "";
		String sEncodedSvrdir = "";
		String sUpFileEncoded = "";
		if (pOldFile.equals(null) || pOldFile.equals(""))
		{
			sResult = "";
		}
		else
		{
			sEncodedSvrdir = URLEncoder.encode(pSvrDir, "euc-kr");
			sUpFileEncoded = URLEncoder.encode(pOldFile, "euc-kr");

			sResult =
				"<embed autostart=\"false\" src=\"/servlet/Download?p_svrdir="
					+ sEncodedSvrdir
					+ "&p_svrfile="
					+ pSvrFile
					+ "&p_upfile="
					+ sUpFileEncoded
					+ "\"><br><font color=red>[재생을 누르세요]</font>&nbsp;&nbsp;"
					+ pOldFile;
		}

		return sResult;
	}

	/**
	 * 다운로드 HTML 코드를 리턴
	 * @param svrdir 파일이 있는 디렉토리의 패스
	 * @param svrfile 삭제할 파일 이름
	 * @param upfile 삭제할 파일의 원래 이름
	 * @param linkname 링크 이름
	 * @return 다운로드 HTML 코드
	 */
	public static String getDownloadUrl(String pSvrDir, String pSvrFile, String pOldFile, String pLinkName)
		throws Exception
	{
		String sResult = "";
		String sEncodedSvrdir = "";

		sEncodedSvrdir = URLEncoder.encode(pSvrDir, "euc-kr");
		sResult =
			"<a href=\"/servlet/Download?p_svrdir="
				+ sEncodedSvrdir
				+ "&p_svrfile="
				+ pSvrFile
				+ "&p_upfile="
				+ pOldFile
				+ "\" class='text'>"
				+ pLinkName
				+ "</a>";

		return sResult;
	}




	/**
	 *	파일을 다른 이름으로 복사 합니다.
	 *  <br><font size=2>2005/01/24. Kim JongJin</font>
	 *  <p>
	 *
	 *	@param copyingfile 복사할 파일 경로 및 이름
	 *	@param copyedfile  복사된 파일 경로 및 이름
	 *	@return 파일 복사 성공시 true를 리턴 합니다.
	 */
	public static boolean copy( String copyingfile, String copyedfile ) throws Exception { 

		if( copyingfile == null || copyedfile == null )
			return false;

		FileInputStream in = new FileInputStream ( copyingfile ); 
		FileOutputStream out = new FileOutputStream ( copyedfile ); 

		byte[] buffer = new byte[16]; 
		int numberRead; 
		while ( (numberRead=in.read (buffer) ) >= 0 ) 
			out.write ( buffer, 0, numberRead ); 
		out.close (); 
		in.close (); 
		return true;
		
	} 	
	
	/*	url download */
    public static String nullFileCreate(String sourceUrl, String sourceFile) {
        FileOutputStream fos = null;
        InputStream is = null;
        String msg = "파일다운로드완료";

		String targetUrl = sourceUrl;
		String targetFilename = sourceFile;

        try {
			fos = new FileOutputStream(targetFilename);
            //URL url = new URL(targetUrl);
            //URLConnection urlConnection = url.openConnection();
            //is = urlConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int readBytes;

			/*
			 * while ((readBytes = is.read(buffer)) != -1) { fos.write(buffer, 0,
			 * readBytes); }
			 */

        } 	catch (Exception e) {
        	msg ="파일다운로드오류";
        	 //e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }

                if (is != null) {
                    is.close();
                }

            } catch (IOException e) {
            	msg ="파일다운로드오류";
                //e.printStackTrace();
            }
        }

		return msg;

    }
    
    public static void unzip(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                String filePath = destDir + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    try (FileOutputStream fos = new FileOutputStream(filePath)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = zipIn.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                } else {
                    new File(filePath).mkdirs();
                }
                zipIn.closeEntry();
            }
        }
    }
    
    // 바이너리 파일 다운로드
    public static void downloadBinaryFile(String fileURL, String savePath) throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(savePath)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("Binary file downloaded to: " + savePath);
        }
    }

    // ZIP 파일 생성
    public static void createZipFile(String inputFilePath, String zipFilePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            File fileToZip = new File(inputFilePath);
            try (FileInputStream fis = new FileInputStream(fileToZip)) {
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zos.putNextEntry(zipEntry);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("File added to ZIP: " + inputFilePath);
        }
    }
    
    public static void downloadZipFile(String fileURL, String savePath) throws IOException {
        // URL 연결
        URL url = new URL(fileURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int responseCode = connection.getResponseCode();
        System.out.println(responseCode);
        // HTTP 응답 확인
        if (responseCode == HttpURLConnection.HTTP_OK) {
//        	File dir = new File(savePath);
//
//    		// 만들고자 하는 디렉토리가 존재하는지 체크
//    		if (!dir.isDirectory()) // 지정된 디렉토리가 존재하는지, 그리고 파일인지 체크
//    		{
//    			if (!dir.mkdirs()) // 디렉토리가 없으면 새로 생성한다.
//    			{
//    				////System.out.println("FileUtil.makeDir() >> Cannot make directory");
////    				return false;
//    				//throw new Exception("FileUtil.makeDir() >> Cannot make directory");
//    			}
//    		}
            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(savePath)) {

                // 데이터 읽기 및 저장
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                System.out.println("Download complete!");
            }
        } else {
            System.out.println("No file to download. Server replied with code: " + responseCode);
        }
        connection.disconnect();
    }

}
