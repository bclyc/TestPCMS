package com.pcms.ctable;
import javax.swing.table.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import javax.swing.*;
class CombineTableUI extends BasicTableUI {
@Override
public void paint(Graphics g, JComponent c) {
  Rectangle r = g.getClipBounds();/** 返回当前剪贴区域的边界矩形**/
  rendererPane.removeAll();
  int firstCol = table.columnAtPoint(new Point(r.x, 0)); /**返回 point 所在的列索引；**/
/**如果结果不在 [0, getColumnCount()-1] 范围内，则返回 -1。**/
  int lastCol = table.columnAtPoint(new Point(r.x + r.width, 0));
/** -1 is a flag that the ending point is outside the table **/
  if (lastCol < 0) {
    lastCol = table.getColumnCount() - 1;  }
   for (int i = firstCol; i <= lastCol; i++) {
      paintCol(i, g);  } 
   paintGrid(g, 0, table.getRowCount() - 1, 0, table.getColumnCount() - 1);
 }
private void paintCol(int col, Graphics g) {
   Rectangle r = g.getClipBounds();
   for (int i = 0; i < table.getRowCount(); i++) {
   Rectangle r1 = table.getCellRect(i, col, true); //getCellRect(int row, int column,
/**boolean includeSpacing)返回位于 row 和 column 相交位置的单元格矩形。**/
   if (r1.intersects(r)){  //intersects(Rectangle r) 确定此 Rectangle
/**是否与指定的 Rectangle 相交 at least a part is visible **/
     int sk = ((CombineTable) table).combineData.visibleCell(i,col);
     paintCell(sk, col, g, r1);
/**increment the column counter**/
     i+= ((CombineTable)table).combineData.span(sk, col) - 1;
        }
    }
 }
private void paintCell(int row, int column, Graphics g, Rectangle area) {
   int verticalMargin = table.getRowMargin();
   int horizontalMargin = table.getColumnModel().getColumnMargin();
   area.setBounds(area.x + horizontalMargin / 2, area.y + verticalMargin / 2,
               area.width - horizontalMargin, area.height - verticalMargin);
   if (table.isEditing() && table.getEditingRow() == row &&
                  table.getEditingColumn() == column) {
     Component component = table.getEditorComponent();
     component.setBounds(area);
     component.validate(); /*确保组件具有有效的布局**/
   } else {
      TableCellRenderer renderer = table.getCellRenderer(row, column);
      Component component = table.prepareRenderer(renderer, row, column);//通过查询
/**row、column 处单元格值的数据模型和单元格选择状态来准备渲染器**/
      if (component.getParent() == null) {
/**component.getParent()返回：此组件的父级容器**/ 
           rendererPane.add(component);} 
      rendererPane.paintComponent(g, component, table, area.x, area.y, area.width, area.height, true);
/**在图形对象 g 上绘制一个单元格渲染器组件component.true，则组件 c 将在绘制之前被验证 **/
                             
      }
 }
private void paintGrid(Graphics g, int rMin, int rMax, int cMin, int cMax) {
       g.setColor(table.getGridColor());
  /**getCellRect(int row,int column,boolean includeSpacing)
*返回位于 row 和 column 相交位置的单元格矩形。如果 includeSpacing 为 true，则返回的值具有指定行和列的完整高度和宽度。
*如果为 false，则返回的矩形为单元格空间减去单元格间的间隙，以便在呈现期间设置该属性后，返回呈现和编辑的组件的真实边界。
 */           
   Rectangle minCell = table.getCellRect(rMin, cMin, true);
   Rectangle maxCell = table.getCellRect(rMax, cMax, true);
/**//*union(Rectangle r)计算此 Rectangle 与指定 Rectangle 的并集。
 *返回一个新的 Rectangle，它表示两个矩形的并集
**/
   Rectangle damagedArea = minCell.union(maxCell);
   if (table.getShowHorizontalLines()) { /**如果表绘制单元格之间的水平线，则返回 true**/
     CombineData cMap = ((CombineTable) table).combineData;
     for (int row = rMin; row <= rMax; row++) { //for1
        for (int column = cMin; column <= cMax; column++) {
          Rectangle cellRect = table.getCellRect(row, column, true);
          if (cMap.combineColumns.contains(column)) {
           int visibleCell = cMap.visibleCell(row, column);
           int span = cMap.span(row, column);
           if (span > 1 && row < visibleCell + span - 1) { }
           else {
              g.drawLine(cellRect.x,
              cellRect.y + cellRect.height - 1,
              cellRect.x + cellRect.width - 1,
              cellRect.y + cellRect.height - 1);
                    }
        } else {
     g.drawLine(cellRect.x, cellRect.y + cellRect.height - 1,
      cellRect.x + cellRect.width - 1, cellRect.y + cellRect.height - 1);
      }
    }
  }//for1
 } //if1
/*getShowVerticalLines()如果表绘制单元格之间的垂直线，则返回 true，否则返回 false。
*public ComponentOrientation getComponentOrientation()
*检索将用于排序此组件内的元素或文本的语言敏感的方向
*/
if (table.getShowVerticalLines()) {
  TableColumnModel cm = table.getColumnModel();
  int tableHeight = damagedArea.y + damagedArea.height;
  int x;
  if (table.getComponentOrientation().isLeftToRight()){
/**isLeftToRight()
*水平行：各项从左到右布局？垂直行：各行从左到右布局？
*这将为水平的、从左到右的书写系统（如罗马语）返回 true**/
   x = damagedArea.x;
   for (int column = cMin; column <= cMax; column++){
     int w = cm.getColumn(column).getWidth();
     x += w;
     g.drawLine(x - 1, 0, x - 1, tableHeight - 1); }  }
  else {
     x = damagedArea.x;
    for (int column = cMax; column >= cMin; column--) {
      int w = cm.getColumn(column).getWidth();
      x += w;
      g.drawLine(x - 1, 0, x - 1, tableHeight - 1); }
     }
   }
 }// paintGrid end
}