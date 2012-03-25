package nc.uap.lfw.file.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.vo.pub.BusinessException;

import org.apache.commons.io.IOUtils;

/**
 * 文件服务
 * 
 * @author licza
 * 
 */
public class LocalFileSystem implements ILfwFileSystem {
	/** 文件存储路径 **/
	/** 块大小 **/
	protected final int BUFSIZE = 1024 * 1024;
	protected String filePath;

	public LocalFileSystem(String filePath) {
		this.filePath = filePath;
	}

	public Object download(Serializable fileVO, OutputStream out) throws Exception {
		return download(fileVO, out, 0L);
	}

	public void upload(Serializable fileVO, InputStream input) throws Exception {
		if (fileVO instanceof LfwFileVO) {
			LfwFileVO fileInfo = (LfwFileVO) fileVO;
			String path = createFilePath(fileInfo);
			if (path == null)
				throw new LfwRuntimeException("文件路径错误", new FileNotFoundException());
			if(input == null)
				return;
			OutputStream output = null;
			try {
				File f = getFile(path);
				output = new FileOutputStream(f);
				byte[] buf = new byte[BUFSIZE];
				int len = -1;
				while ((len = input.read(buf)) != -1) {
					output.write(buf, 0, len);
					output.flush();
				}
			} catch (Exception e) {
				throw e;
			} finally {
				IOUtils.closeQuietly(output);
			}
		}
	}

	public void deleteFile(Serializable fileVO) throws Exception {
		// 从本地文件删除
		if (fileVO instanceof LfwFileVO) {
			LfwFileVO fileInfo = (LfwFileVO) fileVO;
			String path = createFilePath(fileInfo);
			if (path == null)
				throw new LfwRuntimeException("文件路径错误", new FileNotFoundException());
			File tempFile = new File(path);
			// 删除文件
			tempFile.delete();
			File parent = tempFile.getParentFile();
			// 如果父目录孩子数==0,逐级删除父目录
			while (parent != null && parent.listFiles() != null && parent.listFiles().length == 0) {
				parent.delete();
				parent = parent.getParentFile();
			}
		}
	}

	public void deleteFile(Serializable[] fileVOs) throws Exception {
		if (fileVOs != null) {
			for (Serializable fileVO : fileVOs) {
				deleteFile(fileVO);
			}
		}
	}

	/**
	 * @param path
	 * @param fileName
	 * @return 获得文件对象，若文件或所在目录不存在，先创建
	 * @throws IOException
	 */
	private File getFile(String path) throws IOException {
		File dirFile = new File(path);
		if (dirFile.exists() && dirFile.isFile()) {
			// 如果文件存在
			return dirFile;
		} else {
			if (!dirFile.getParentFile().exists()) {
				// 如果所在目录不存在，创建目录
				dirFile.getParentFile().mkdirs();
			}
			// 创建新的文件
			dirFile.createNewFile();

			return dirFile;
		}
	}

	/**
	 * 计算文件真实存储路径：通过构造唯一字符串OID，将其分段获得路径 具体方法：
	 * OID的构造方法，决定了每个子目录中文件的数量。如果OID是由n个不同的字符组成，以K位为一段拆分
	 * 那么子文件夹下，存储的文件数最大为【n的k次幂-1.】，这种方法解决了操作系统那个对文件数量的限制。
	 * 例如：一个文档对象OID是由10位数字组成的字符串2341123689; 以3位为一段，则从后向前拆分为4段：2/341/123/689，
	 * 取前三段可作为真实的存放路径，文件夹下最多存放文件的数量是999.
	 * 
	 */
	protected String createFilePath(LfwFileVO fileVersionVO) {
		String id = fileVersionVO.getPk_lfwfile();
		if (id == null || id.length() == 0) {
			return null;
		}
		int idLength = id.length();
		// K为每段的长度
		final int k = 3;
		int rootPathLength = idLength % k;
		StringBuilder sb = new StringBuilder();
		sb.append(filePath);
		// 返回文件分隔符
		String seprator = System.getProperty("file.separator");
		if (rootPathLength != 0) {
			sb.append(seprator);
			sb.append(id.substring(0, rootPathLength));
		}
		for (int i = 0; i < idLength / k; i++) {
			sb.append(seprator);
			sb.append(id.substring(i * k, i * k + k));
		}
		sb.append(seprator);
		sb.append(id);
		return sb.toString();
	}

	public Object download(Serializable fileVO, OutputStream output, long begin) throws Exception {
		if (fileVO instanceof LfwFileVO) {
			LfwFileVO fileInfo = (LfwFileVO) fileVO;
			String path = createFilePath(fileInfo);
			if (path == null)
				throw new LfwRuntimeException("文件路径错误", new FileNotFoundException());
			// 从本地磁盘读取文件，写入output
			InputStream input = null;
			try {
				input = new FileInputStream(path);
				if (begin > 0) {
					long actualnum = input.skip(begin);
					if (actualnum < 0) {
						throw new BusinessException("掉过字节数为负数");
					} else {
						if (begin > actualnum) {
							throw new BusinessException("实际掉过字节数小于开始值");
						}
					}
				}
				// 每次读本地文件块
				byte[] buf = new byte[BUFSIZE];
				int len = -1;
				while ((len = input.read(buf)) != -1) {
					output.write(buf, 0, len);
					output.flush();
				}
			} catch (Exception e) {
				LfwLogger.error(e.getMessage(),e);
				// 将异常写入输出流
				throw e;
			} finally {
				try {
					if (input != null) {
						input.close();
					}
				} catch (Exception e2) {
					LfwLogger.error(e2.getMessage(),e2);
				}
			}
		}
		return null;
	}

	public boolean existInFs(String fileNo) throws Exception {
		LfwFileVO fileInfo = new FileManager().getFileQryService().getFile(fileNo);
		String path = createFilePath(fileInfo);
		if (path == null)
			return false;
		return new File(path).exists();
	}

	public void signVirtualFile(String fileNo) throws Exception {
		
	}
}
