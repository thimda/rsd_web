package nc.uap.lfw.pqm;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.CRC32;

import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.log.LfwLogger;

public class PqmUtil {

	private static File BASE_FILE = new File(RuntimeEnv.getInstance()
			.getNCHome(), "performance_record");
	private static File MP_FILE = new File(
			RuntimeEnv.getInstance().getNCHome(),
			"resources/monitor.properties");
	private static Properties sprop = new Properties();
	private static final String SPLIT_PRE = "\\$\\$";
	private static final String WRITE_TO_CLIENT_BYTE = "writetoclientbytes";
	private static final String READ_FROM_CLIENT_BYTE = "readfromclientbytes";
	private static final String BEGIN_TIME = "begintime";
	private static final String COST_TIME = "costtime";
	private static final String SERVER_BASE = "performance_record";
	private static final String BUSI_ACTION = "busiaction";
	private static final String[] INDICATORS = new String[] {
			READ_FROM_CLIENT_BYTE, WRITE_TO_CLIENT_BYTE };
	private static SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");
	private static SimpleDateFormat SDF1 = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");
	private static String PJ_SERVER = "localhost";
	private static String PJ_PORT = "8061";

	static {
		try {
			sprop.load(new FileInputStream(MP_FILE));
			if (sprop.get("PJPORT") != null
					&& sprop.get("PJPORT").toString().trim().length() > 0) {
				PJ_PORT = sprop.get("PJPORT").toString().trim();
			}

			if (sprop.get("PJSERVER") != null
					&& sprop.get("PJSERVER").toString().trim().length() > 0) {
				PJ_SERVER = sprop.get("PJSERVER").toString().trim();
			}
		} catch (Exception e) {

		}
	}

	private long[] indicator_values = new long[INDICATORS.length];
	private String busiAction = "";
	private int remoteCallNum = 0;
	private String firstBeginTime = "";
	private String beginTime = "";
	private String costTime = "";

	public String parseLog(File[] files) throws Exception {
		reset();
		parseLogFile(files);
		StringBuffer sb = new StringBuffer();
		sb.append("remoteallcount=").append(remoteCallNum).append(";  ");
		for (int k = 0; k < INDICATORS.length; k++) {
			sb.append(INDICATORS[k]).append("=").append(indicator_values[k])
					.append(";  ");
		}

		sb.append("costtime=").append(getCostTime()).append("; ");
		String dateStr = SDF.format(new Date(System.currentTimeMillis()));

		if (!BASE_FILE.exists()) {
			BASE_FILE.mkdirs();
		}
		File jf = new File(BASE_FILE, dateStr + "pjlog_" + busiAction + ".jar");

		jar(files, jf, true);
		String serverPath = SERVER_BASE + File.separator
				+ InetAddress.getLocalHost().getHostName() + jf.getName();
		sb.append("file=").append(serverPath);
		upload(jf, serverPath);

		return sb.toString();
	}

	private long getCostTime() {
		try {
			long fbegin = SDF1.parse(firstBeginTime).getTime();
			long ebegin = SDF1.parse(beginTime).getTime();
			long cost = Long.parseLong(costTime);
			return ebegin - fbegin + cost;
		} catch (ParseException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return 0;
	}

	private void reset() {
		indicator_values = new long[INDICATORS.length];
		busiAction = "";
		remoteCallNum = 0;
		firstBeginTime = "";
		beginTime = "";
		costTime = "";
	}

	private void parseLogFile(File[] files) throws Exception {
		for (File file : files) {
			FileInputStream fis = null;
			BufferedReader reader = null;
			try {
				fis = new FileInputStream(file);
				reader = new BufferedReader(new InputStreamReader(fis));
				String line = null;
				while ((line = reader.readLine()) != null) {
					parseStr(line);
				}
			} finally {
				if (fis != null) {
					fis.close();
				}
				if (reader != null) {
					reader.close();
				}
			}
		}
	}

	private void parseStr(String str) {
		String[] ss = str.split("\n");
		for (String s : ss) {
			parseLine(s);
		}
	}

	private void parseLine(String line) {
		String[] ss = line.split(SPLIT_PRE);
		String msgStr = null;
		boolean isSummary = false;
		for (String s : ss) {
			s = s.trim();
			if (s.startsWith("file=") && s.indexOf("mwsummary-log") > -1) {
				isSummary = true;
			} else if (s.startsWith("msg=")) {
				msgStr = s.substring("msg=".length());
			}
			if (isSummary && msgStr != null) {
				break;
			}
		}
		if (isSummary) {
			remoteCallNum++;
			if (null != msgStr) {
				parseMsg(msgStr);
			}
		}

	}

	private void parseMsg(String msgStr) {
		if (msgStr.indexOf("remoteCallMethod=nc.bs.pub.IClusterFinder") > -1) {
			remoteCallNum--;
			return;
		}
		String[] ss = msgStr.split(";");

		for (String s : ss) {
			s = s.trim();

			if (s.startsWith(BUSI_ACTION)) {
				if (busiAction.equals("") || busiAction.equals("unkown")) {
					busiAction = s.substring(BUSI_ACTION.length() + 1);
				}
				continue;
			}

			if (s.startsWith(BEGIN_TIME)) {
				beginTime = s.substring(BEGIN_TIME.length() + 1);
				if (firstBeginTime.equals("")) {
					firstBeginTime = beginTime;
				}
				continue;
			}

			if (s.startsWith(COST_TIME)) {
				costTime = s.substring(COST_TIME.length() + 1);
				continue;
			}

			for (int i = 0; i < INDICATORS.length; i++) {
				String indicator = INDICATORS[i];
				if (s.startsWith(indicator)) {
					int value = Integer
							.parseInt(s.substring(indicator.length() + 1));
					indicator_values[i] += value;
					break;
				}
			}
		}

	}

	private static void jar(File[] sourceFiles, File target, boolean compress)
			throws FileNotFoundException, IOException {
		if (!target.getParentFile().exists()) {
			target.getParentFile().mkdirs();
		}
		JarOutputStream out = new JarOutputStream(new FileOutputStream(target));
		if (compress) {
			out.setLevel(JarOutputStream.DEFLATED);
		} else {
			out.setLevel(JarOutputStream.STORED);
		}
		CRC32 crc = new CRC32();
		byte[] buffer = new byte[1024 * 1024];

		int sourceDirLength = sourceFiles[0].getParentFile().getAbsolutePath()
				.length() + 1;
		for (int i = 0; i < sourceFiles.length; i++) {
			File file = sourceFiles[i];
			addFile(file, out, crc, sourceDirLength, buffer);
		}
		out.close();
	}

	private static void addFile(File file, JarOutputStream out, CRC32 crc,
			int sourceDirLength, byte[] buffer) throws FileNotFoundException,
			IOException {
		if (file.isDirectory()) {
			File[] fileNames = file.listFiles();
			for (int i = 0; i < fileNames.length; i++) {
				addFile(fileNames[i], out, crc, sourceDirLength, buffer);
			}
		} else {
			String entryName = file.getAbsolutePath()
					.substring(sourceDirLength);
			JarEntry entry = new JarEntry(entryName);
			out.putNextEntry(entry);
			FileInputStream in = new FileInputStream(file);
			int read;
			long size = 0;
			while ((read = in.read(buffer)) != -1) {
				crc.update(buffer, 0, read);
				out.write(buffer, 0, read);
				size += read;
			}
			entry.setCrc(crc.getValue());
			entry.setSize(size);
			entry.setTime(file.lastModified());
			in.close();
			out.closeEntry();
			crc.reset();
		}
	}

	private void upload(File src, String path) {
		StringBuffer urlStr = new StringBuffer();
		urlStr.append("http://" + PJ_SERVER + ":" + PJ_PORT);
		urlStr.append("/service/~pjmanager/nc.bs.nbd.servlet.UploadResourceServlet");
		URL url = null;
		OutputStream output = null;
		try {
			url = new URL(urlStr.toString());
			if (!isConnect(url)) {
				return;
			}
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Content-type",
					"application/x-java-serialized-object");
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			output = conn.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(output);
			oos.writeObject(path);
			oos.flush();

			InputStream fis = null;
			BufferedInputStream bis = null;
			try {
				fis = new FileInputStream(src);
				bis = new BufferedInputStream(fis);
				byte[] buffer = new byte[1024 * 4];
				int len = -1;
				while ((len = bis.read(buffer)) > 0) {
					output.write(buffer, 0, len);
				}
				output.flush();
			} finally {
				if (bis != null) {
					bis.close();
				}
				if (fis != null) {
					fis.close();
				}
			}
			conn.getInputStream();

		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);;
		}
	}

	private boolean isConnect(URL url) {
		URLConnection con = null;
		OutputStream output = null;
		try {
			con = url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			output = con.getOutputStream();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if(output != null)
					output.close();
			} catch (Exception e) {
				LfwLogger.error(e);
			}
		}
		return true;
	}

}
