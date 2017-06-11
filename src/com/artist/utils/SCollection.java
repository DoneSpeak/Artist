package com.artist.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeSet;

public class SCollection {
	/**
	 * @param list_1 列表1
	 * @param list_2 列表2
	 * @param inOrder 两个列表是否已经有序了
	 */
	public static ArrayList<Integer> intersectWithSkip(ArrayList<Integer> list_1, ArrayList<Integer> list_2, boolean inOrder){
		ArrayList<Integer> result =  new ArrayList<Integer>();
		int index_1 = 0;
		int index_2 = 0;
//		计算两个表的跳表步长
		int stepSize_1 = (int)Math.ceil(Math.sqrt(list_1.size()));
		int stepSize_2 = (int)Math.ceil(Math.sqrt(list_2.size()));
		
		if(!inOrder){
			//列表传入无序，进行有序化
			 Collections.sort(list_1);
			 Collections.sort(list_2);
		}
		
//		以两个列表的大小为边界进行循环
		while( index_1 < list_1.size() && index_2 < list_2.size() ){
			if(list_1.get(index_1) == list_2.get(index_2)){
//				如果两值相等，则添加到结果集合中
				result.add(list_1.get(index_1));
				// 添加之后同时指向下一个
				index_1 ++;
				index_2 ++;
			}else if(list_1.get(index_1)<list_2.get(index_2)){
//				如果左值小于右值，则左值跳表
//				这里 index_1 + stepSize_1 < list_size() 是跳表范围测试
//				这里的 list_1.get(index_1 + stepSize_1) <= list_2.get(index_2) 是跳表测试
				if(index_1 + stepSize_1 < list_1.size() && index_1 % stepSize_1 == 0 && list_1.get(index_1 + stepSize_1) <= list_2.get(index_2)){
					while(index_1 + stepSize_1 < list_1.size() && list_1.get(index_1 + stepSize_1) <= list_2.get(index_2)){
//						可以跳表，则跳表
						index_1 = index_1 + stepSize_1;
					}
				}else{
//					如果无法跳表，则只能往后移动一位
					index_1 ++;
				}
			}else{
//				右值小于左值时的操作，原理同上
//				if(index_2 + stepSize_2 < list_2.size() && list_2.get(index_2 + stepSize_2) <= list_1.get(index_1)){
				if(index_2 + stepSize_2 < list_2.size() && index_2 % stepSize_2 == 0 && list_2.get(index_2 + stepSize_2) <= list_1.get(index_1)){
					while(index_2 + stepSize_2 < list_2.size() && list_2.get(index_2 + stepSize_2) <= list_1.get(index_1)){
						index_2 = index_2 + stepSize_2;
					}
				}else{
					index_2 ++;
				}
			}
		}
		return result;
	}

	private void testIntersect(){
			
		
		Integer[] list_1 = {1, 5, 6, 7, 10, 15, 33};
		Integer[] list_2 = {1, 5, 7, 10, 22, 29, 33};
		
		ArrayList<Integer> list_array_1 = new ArrayList<Integer>();
		ArrayList<Integer> list_array_2 = new ArrayList<Integer>();
		
		for(Integer docId : list_1){
			list_array_1.add(docId);
		}
		
		for(Integer docId : list_2){
			list_array_2.add(docId);
		}
		
		ArrayList<Integer> result_list = SCollection.intersectWithSkip(list_array_1, list_array_2,false);
		
		System.out.println("输入集合：");
		System.out.println("集合 1：" + list_array_1);
		System.out.println("集合 2：" + list_array_2);
		System.out.println("交集：" + result_list);
	}
	
	public static void main(String[] args){
//		new SCollection().testIntersect();

		Random random = new Random();
		TreeSet<Integer> set = new TreeSet<Integer>();
		int n = 1000000;
		
		for(int k = 0; k < 10; k ++){
			while(set.size() < n){
				set.add(random.nextInt(n*2));
			}
			ArrayList<Integer> list_1 = new ArrayList<Integer>();
			for(int i: set){
				list_1.add(i);
			}
	//		System.out.println(list_1);
			set.clear();
			while(set.size() < n){
				set.add(random.nextInt(n*2));
			}
			ArrayList<Integer> list_2 = new ArrayList<Integer>();
			for(int i: set){
				list_2.add(i);
			}
	//		System.out.println(list_1);
	//		System.out.println(list_2);
			long start = System.nanoTime();
			SCollection.intersectWithSkip(list_1, list_2,false);
			long end = System.nanoTime();
			System.out.println("有hasSkip:" + (end - start));

		}
	}
}

