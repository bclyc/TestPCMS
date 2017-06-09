package com.pcms.ctable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CombineData {
public ArrayList<Integer> combineColumns = new ArrayList<>();/**���ڱ�����Ҫ�ϲ����к�**/
private String[][] datas;//table�����ݣ���������ϲ��ĵ�Ԫ��
private ArrayList<HashMap<Integer, Integer>> rowPoss;
private ArrayList<HashMap<Integer, Integer>> rowCounts;
public CombineData() {       }
public CombineData(String[][] datas, int... combineColumns) {
	this.datas = datas;
	for (int i = 0; i < combineColumns.length; i++) {/**��ʼ���ϲ��к�*/
    if (combineColumns[i] < 0) { continue; }
    this.combineColumns.add(combineColumns[i]);
                  }
    process();
     }
public CombineData(String[][] datas, List<Integer> combineColumns) {
   this.datas = datas;
   for (Integer column : combineColumns) {
        if (column < 0) { continue; }
        this.combineColumns.add(column);
     }
   process();
  }
public void initData(String[][] datas, int... combineColumns) {
  this.datas = datas;
  this.combineColumns.clear();
  for (int i = 0; i < combineColumns.length; i++) {
    if (combineColumns[i] < 0) { continue;}
    this.combineColumns.add(combineColumns[i]);
     }
   process();
  }
public void initData(String[][] datas, List<Integer> combineColumns) {
   this.datas = datas;
   this.combineColumns.clear();
    for (Integer column : combineColumns) {
     if (column < 0) { continue; }
     
      this.combineColumns.add(column);
        }
    process();
  }
private void process() { /**Ϊ���бȶԴ���**/
	rowPoss = new ArrayList<HashMap<Integer, Integer>>();
	rowCounts = new ArrayList<HashMap<Integer, Integer>>();
	for (Integer integer : combineColumns) {
	     HashMap<Integer, Integer> rowPos = new HashMap<>();
	     HashMap<Integer, Integer> rowCount = new HashMap<>();
	     String pre = "";
	     int count = 0;
	     int start = 0;
/**for (int i = 0; i < datas.length; i++) { 
*    if (pre.equals(datas[i][integer])) { count++; } 
*    else {
*    rowCount.put(start, count);
*    pre = datas[i][integer];
*    count = 1;
*    start = i;
*   }
*  rowPos.put(i, start);
**/
    for (int i = 0; i < datas.length; i++) {
    	String[] data = datas[i];
/**���datas[i]�ĵ�integer���Ƿ����pre( ǰһ��),*/    
    if (pre.equals(data[integer])) {/** �൱��*datas[i][integer-]=datas[i][integer]*/
           count++; }  
    else { /**������*/
       rowCount.put(start, count);
       pre = data[integer];/*data[integer]=datas[i][integer] */ 
       count = 1;
       start = i;
        }
     rowPos.put(i, start);
   }
  rowCount.put(start, count);
  rowPoss.add(rowPos);
  rowCounts.add(rowCount);
  }
}
/**
* ����table��row��column�е�Ԫ����������
*/
public int span(int row, int column) {
	  int index = combineColumns.lastIndexOf(column);/** �����б���������ָ��Ԫ�ص�����������б���������Ԫ�أ��򷵻� -1��**/
	  if (index != -1) {
	   return rowCounts.get(index).get(rowPoss.get(index).get(row));
	   } else {
	  return 1;
	  }
 }
/**
* ����table��row��column�е�Ԫ�����ڵĺϲ���Ԫ�����ʼ��λ��
*/
public int visibleCell(int row, int column) {
  int index = combineColumns.lastIndexOf(column);
  if ((index != -1) && row > -1) {
    return rowPoss.get(index).get(row);
  } else {
  return row;
  }
 }
}