package nc.uap.lfw.core.comp;
/**
 * GridColumn��ʶ�ӿ�
 *
 */
public interface IGridColumn extends Cloneable{
	public Object clone();
	public void setGridComp(GridComp grid);
	public GridComp getGridComp();
}
