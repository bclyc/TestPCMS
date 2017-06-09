package com.pcms.ctable;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class CombineTable extends JTable {
	public CombineData combineData;
	public CombineTable(TableModel tableModel) {
		super(tableModel);
		super.setUI(new CombineTableUI());
	}
	public CombineTable(CombineData combineData, TableModel tableModel) {
	  super(tableModel);
	  this.combineData = combineData;
	  for (Integer column : combineData.combineColumns) { /**设置CombineColumns保留的列Render**/
	  	TableColumn tableColumn = super.columnModel.getColumn(column);
	  	tableColumn.setCellRenderer(new CombineColumnRender());
	    }
	  super.setUI(new CombineTableUI());
	 }
	public void setCombineData(CombineData combineData) {
	  this.combineData = combineData;
	  for (Integer column : combineData.combineColumns) {
	    TableColumn tableColumn = super.columnModel.getColumn(column);
	     tableColumn.setCellRenderer(new CombineColumnRender());} 
	  }
@Override/*返回位于 row 和 column 相交位置的单元格矩形,includeSpacing 为 true
*则返回的值具有指定行和列的完整高度和宽度**/
public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
/** required because getCellRect is used in JTable constructor**/
   if (combineData == null) {
   return super.getCellRect(row, column, includeSpacing);  }
 /**add widths of all spanned logical cells **/
   int sk = combineData.visibleCell(row, column);//
/**Rectangle r1 = super.getCellRect(row, sk, includeSpacing); **/
   Rectangle rect1 = super.getCellRect(sk, column, includeSpacing);/* 返回特定的单元格矩形*/
   if (combineData.span(sk, column) != 1) {
      for (int i = 1; i < combineData.span(sk, column); i++) {
/**r1.width += getColumnModel().getColumn(sk + i).getWidth();**/
         rect1.height += this.getRowHeight(sk + i);  } 
    }
   return rect1;
  }
@Override
/**返回 point 所在的行索引；如果结果不在 [0, getRowCount()-1] 范围内，则返回 -1**/
public int rowAtPoint(Point p) {
   int column = super.columnAtPoint(p);
/* 返回 point 所在的列索引；如果结果不在 [0, getColumnCount()-1] 范围内，则返回 -1。*/
  if (column < 0) { return column; }
  int row = super.rowAtPoint(p);
  return combineData.visibleCell(row, column);
  }
@Override
/*如果 row 和 column 位置的单元格是可编辑的，则返回 true。**/
public boolean isCellEditable(int row, int column) {
   if (combineData.combineColumns.contains(column)) {
      return false;}
   return super.isCellEditable(row, column);
   }
@Override
/**如果指定的索引位于行和列的有效范围内，并且位于该指定位置的单元格被选定，则返回 true**/
public boolean isCellSelected(int row, int column) {
   if (combineData.combineColumns.contains(column)) {
            return false;} 
    return super.isCellSelected(row, column);
   }
}