package nc.uap.lfw.ca.jdt.internal.compiler.parser;

/**
 * Internal import structure for parsing recovery 
 */
import nc.uap.lfw.ca.jdt.internal.compiler.ast.ASTNode;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.ImportReference;

public class RecoveredImport extends RecoveredElement {

	public ImportReference importReference;
public RecoveredImport(ImportReference importReference, RecoveredElement parent, int bracketBalance){
	super(parent, bracketBalance);
	this.importReference = importReference;
}
/* 
 * Answer the associated parsed structure
 */
public ASTNode parseTree(){
	return importReference;
}
/*
 * Answer the very source end of the corresponding parse node
 */
public int sourceEnd(){
	return this.importReference.declarationSourceEnd;
}
public String toString(int tab) {
	return tabString(tab) + "Recovered import: " + importReference.toString(); //$NON-NLS-1$
}
public ImportReference updatedImportReference(){

	return importReference;
}
public void updateParseTree(){
	this.updatedImportReference();
}
/*
 * Update the declarationSourceEnd of the corresponding parse node
 */
public void updateSourceEndIfNecessary(int bodyStart, int bodyEnd){
	if (this.importReference.declarationSourceEnd == 0) {
		this.importReference.declarationSourceEnd = bodyEnd;
		this.importReference.declarationEnd = bodyEnd;
	}
}
}
