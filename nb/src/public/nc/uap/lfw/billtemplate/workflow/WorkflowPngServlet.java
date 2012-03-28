package nc.uap.lfw.billtemplate.workflow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.pf.IWorkflowDefine;
import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.pub.BusinessException;
import nc.vo.wfengine.definition.WorkflowTypeEnum;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * ��������������ͼƬServlet
 * @author dengjt
 *
 */
public class WorkflowPngServlet  extends HttpServlet{
	private static final long serialVersionUID = 1L;
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		LfwLogger.error("WorkflowPngServlet" + "��ʼִ��");
		req.setCharacterEncoding("UTF-8");
		String billId= req.getParameter(NCApproveConstant.BILLID);
		String billType = req.getParameter(NCApproveConstant.BILLTYPE);
		String imageindex = req.getParameter("imageindex");
		IWorkflowDefine workflowDefine = NCLocator.getInstance().lookup(IWorkflowDefine.class);
		FileInputStream finput = null;
		OutputStream out = res.getOutputStream();
		res.setContentType("image/png"); 
		
		List<byte[]> workflowImages = null;
		try {
			//��ѯ����ͼ�ĸ����������������һ�𷵻�
			workflowImages = workflowDefine.toPNGImagesWithSubFlow(billId, billType, WorkflowTypeEnum.Approveflow.getIntValue());
			LfwLogger.error("WorkflowPngServlet��workflowImages�ĸ���" + workflowImages.size());
			if(imageindex == null || "".equals(imageindex))
				imageindex = "0";
			byte[] pngBytes = workflowImages.get(Integer.valueOf(imageindex));
			LfwLogger.error("WorkflowPngServlet��pngBytes" + pngBytes.length);
			out.write(pngBytes);
			out.flush();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			LfwLogger.error(e.getMessage(), e);
		}
		
		catch (NullPointerException e){
			int width = 200;
			int height = 80;
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//			 ��ȡͼ��������
			Graphics g = image.getGraphics();

			// �趨����ɫ
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);

			// �趨����
			g.setFont(new Font("Simsun", Font.BOLD, 14));

			// ���߿�
			//g.setColor(new Color(0x000000));
			//g.drawRect(0,0,width-1,height-1);

			g.setColor(Color.BLACK);
			//g.drawString(NCLangRes4VoTransl.getNCLangRes().getStrByID("yer", "yer_pfmsg01")/*��ǰ����δ�趨����!*/, 20, 40);
			g.drawString("��ǰ����δ�趨����", 20, 40);
			// ͼ����Ч
			g.dispose();

			// ���ͼ��ҳ��
			// ImageIO.write(image, "JPEG", response.getOutputStream());

			ServletOutputStream sos = res.getOutputStream();
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(sos);
			encoder.encode(image);
			sos.flush();
			sos.close();
		}
		catch (Exception e) {
			Logger.error(e.getMessage(), e);
//			finput = new FileInputStream("");
//			byte[] bytes = new byte[1024];
//			finput.read(b)
//			out.write(pngBytes);
			//out.flush();
		}
		finally {
			if(finput != null)
				finput.close();
		}
	}
}
