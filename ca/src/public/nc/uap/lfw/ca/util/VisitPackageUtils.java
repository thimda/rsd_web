/**
 * 
 */
package nc.uap.lfw.ca.util;


import nc.uap.lfw.ca.dom.JProgram;
import nc.uap.lfw.ca.jdt.core.dom.ASTNode;
import nc.uap.lfw.ca.jdt.core.dom.CompilationUnit;
import nc.uap.lfw.ca.jdt.core.dom.Name;
import nc.uap.lfw.ca.jdt.core.dom.PackageDeclaration;
import nc.uap.lfw.ca.log.CaLogger;


/**
 * @author chouhl
 *
 */
public class VisitPackageUtils {
	
	public static void visitPackage(CompilationUnit unit,JProgram jprogram){
		if(unit == null){
			return;
		}
		PackageDeclaration pd = unit.getPackage();
		visitPackageDeclaration(pd,jprogram);
	}

	private static void visitPackageDeclaration(ASTNode node ,JProgram jprogram){
		if(node == null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(node.getNodeType());
		
//		try{
//			List<Annotation> anns = (List<Annotation>)c.getMethod("annotations", null).invoke(node, null);
//			visitAnnotations(anns,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		try{
			Name n = (Name)c.getMethod("getName", null).invoke(node, null);
			jprogram.setNodeType(node.getNodeType());
			jprogram.setContentType(JProgram.CONTENT_NAME);
			jprogram.setJpackage(n.getFullyQualifiedName());
//			VisitUtils.visitName(n, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
}
