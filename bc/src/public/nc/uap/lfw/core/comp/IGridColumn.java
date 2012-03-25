package nc.uap.lfw.core.comp;
/**
 * GridColumn标识接口
 *
 */
public interface IGridColumn extends Cloneable{
	public Object clone();
	public void setGridComp(GridComp grid);
	public GridComp getGridComp();
}
