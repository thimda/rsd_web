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
 * �ļ�����
 * 
 * @author licza
 * 
 */
public class LocalFileSystem implements ILfwFileSystem {
	/** �ļ��洢·�� **/
	/** ���С **/
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
				throw new LfwRuntimeException("�ļ�·������", new FileNotFoundException());
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
		// �ӱ����ļ�ɾ��
		if (fileVO instanceof LfwFileVO) {
			LfwFileVO fileInfo = (LfwFileVO) fileVO;
			String path = createFilePath(fileInfo);
			if (path == null)
				throw new LfwRuntimeException("�ļ�·������", new FileNotFoundException());
			File tempFile = new File(path);
			// ɾ���ļ�
			tempFile.delete();
			File parent = tempFile.getParentFile();
			// �����Ŀ¼������==0,��ɾ����Ŀ¼
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
	 * @return ����ļ��������ļ�������Ŀ¼�����ڣ��ȴ���
	 * @throws IOException
	 */
	private File getFile(String path) throws IOException {
		File dirFile = new File(path);
		if (dirFile.exists() && dirFile.isFile()) {
			// ����ļ�����
			return dirFile;
		} else {
			if (!dirFile.getParentFile().exists()) {
				// �������Ŀ¼�����ڣ�����Ŀ¼
				dirFile.getParentFile().mkdirs();
			}
			// �����µ��ļ�
			dirFile.createNewFile();

			return dirFile;
		}
	}

	/**
	 * �����ļ���ʵ�洢·����ͨ������Ψһ�ַ���OID������ֶλ��·�� ���巽����
	 * OID�Ĺ��췽����������ÿ����Ŀ¼���ļ������������OID����n����ͬ���ַ���ɣ���KλΪһ�β��
	 * ��ô���ļ����£��洢���ļ������Ϊ��n��k����-1.�������ַ�������˲���ϵͳ�Ǹ����ļ����������ơ�
	 * ���磺һ���ĵ�����OID����10λ������ɵ��ַ���2341123689; ��3λΪһ�Σ���Ӻ���ǰ���Ϊ4�Σ�2/341/123/689��
	 * ȡǰ���ο���Ϊ��ʵ�Ĵ��·�����ļ�����������ļ���������999.
	 * 
	 */
	protected String createFilePath(LfwFileVO fileVersionVO) {
		String id = fileVersionVO.getPk_lfwfile();
		if (id == null || id.length() == 0) {
			return null;
		}
		int idLength = id.length();
		// KΪÿ�εĳ���
		final int k = 3;
		int rootPathLength = idLength % k;
		StringBuilder sb = new StringBuilder();
		sb.append(filePath);
		// �����ļ��ָ���
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
				throw new LfwRuntimeException("�ļ�·������", new FileNotFoundException());
			// �ӱ��ش��̶�ȡ�ļ���д��output
			InputStream input = null;
			try {
				input = new FileInputStream(path);
				if (begin > 0) {
					long actualnum = input.skip(begin);
					if (actualnum < 0) {
						throw new BusinessException("�����ֽ���Ϊ����");
					} else {
						if (begin > actualnum) {
							throw new BusinessException("ʵ�ʵ����ֽ���С�ڿ�ʼֵ");
						}
					}
				}
				// ÿ�ζ������ļ���
				byte[] buf = new byte[BUFSIZE];
				int len = -1;
				while ((len = input.read(buf)) != -1) {
					output.write(buf, 0, len);
					output.flush();
				}
			} catch (Exception e) {
				LfwLogger.error(e.getMessage(),e);
				// ���쳣д�������
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
