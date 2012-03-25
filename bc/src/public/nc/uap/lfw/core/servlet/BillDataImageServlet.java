package nc.uap.lfw.core.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.md.innerservice.MDQueryService;
import nc.md.model.IBean;
import nc.md.model.MetaDataException;
import nc.md.persist.framework.processor.BeanListFromColumnLableProcessor;
import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.pub.SuperVO;
/**
 * 
 * @author dengjt
 *
 */
public class BillDataImageServlet extends HttpServlet {
	private static final long serialVersionUID = -4045364032696239519L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// 设置页面不缓存
		resp.setHeader("Pragma", "No-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);

		String tableName = req.getParameter("tn");
		String dataField = req.getParameter("df");
		String pkField = req.getParameter("pf");
		String pkValue = req.getParameter("pv").trim();
		String onSql = req.getParameter("onsql");
		String beanid = req.getParameter("beanid");
		//查询图片的sql
		String sql = "";
		if(onSql != null && !"".equals(onSql)){
			sql = "select " + dataField + " from " + tableName +  " on " + onSql  + " where " + pkField + " = '" + pkValue + "'";
		}
		else{
			sql = "select " + dataField + " from " + tableName + " where " + pkField + " = '" + pkValue + "'";
		}
		//图片字节
		byte[] bytes = null;
		try {
			//图片所在元数据
			IBean entity  = MDQueryService.lookupMDQueryService().getBusinessEntityByFullName(beanid);
			try {
				//根据元数据查询对象数据
				Object obj = new BaseDAO().executeQuery(sql,new BeanListFromColumnLableProcessor(Class.forName(entity.getFullClassName()), entity));
				ArrayList<Object> objList =  (ArrayList<Object>) obj;
				SuperVO vo = (SuperVO) objList.get(0);
				//得到图片对应的字节
				bytes = (byte[]) vo.getAttributeValue(dataField);
			} catch (DAOException e) {
				LfwLogger.error(e.getMessage(), e);
			} catch (ClassNotFoundException e) {
				LfwLogger.error(e.getMessage(), e);
			}
		} catch (MetaDataException e1) {
			// TODO Auto-generated catch block
			LfwLogger.error(e1.getMessage(), e1);
		}
		OutputStream out = resp.getOutputStream();
		resp.setContentType("image/jpeg");
		out.write(bytes);
		out.flush();
	} 
}

