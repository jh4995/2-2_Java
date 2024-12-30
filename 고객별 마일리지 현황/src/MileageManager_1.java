/*
 * 마일리지 관리 프로그램의 버전 - 1
 * 
 * 1. 파일로부터 마일리지 데이터를 불러오고
 * 2. 해당 마일리지값들에 대한 연산 혹은 새로운 데이터를 추가하며
 * 3. 이후 모든 데이터를 콘솔에 출력하고
 * 4. 가장 마일리지가 높은 고객을 마지막으로 콘솔에 출력하고
 * 5. show_GridLayout을 통해서 모든 "고객 이름"과 "마일리지"를 GUI창에서 다시 출력한다.
 * 
 * 향후 이미지갤러리 기능을 추가할 예정이다.
 */

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MileageManager_1 extends JFrame{
    HashMap<String, Integer> mileageMap = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("*** 마일리지 관리 프로그램입니다.***");

        MileageManager_1 manager = new MileageManager_1();        
        manager.get_Mileage_From_File();
        manager.control_Mileage();
        manager.highest_Mileage();
        manager.show_GridLayout();
        
        System.out.println("프로그램을 종료합니다.");
    }
        
    // 1. mileage.txt 파일로부터 초기 마일리지 데이터 읽어오기
    public void get_Mileage_From_File() {
        try{
        	File file = new File(".\\mileage.txt");
        	Scanner sc = new Scanner(file);
        	
            while (sc.hasNext())
            {
                String name = sc.next();
                if (name.equals("그만"))
                {
                	break;
                }
                int mileage = sc.nextInt();
                mileageMap.put(name, mileageMap.getOrDefault(name, 0) + mileage);
                // getOrDefault 메소드 추가 활용
            }
            sc.close();
        } 
        
        catch (FileNotFoundException e)
        {
        	System.out.println("FileNotFoundException");
        }
        catch (IOException e)
        {
            System.out.println("IOException");
        }
        catch (Exception e)
        {
        	System.out.println("Exception occured");
        }

        // 초기 마일리지 출력
        System.out.println("\n초기 마일리지 정보:");
        show_Mileage();
    }

    // 2. 마일리지 누적, 합산, 차감 / 새로운 이름과 마일리지 추가 / 검색 기능
    public void control_Mileage() {
       Scanner sc = new Scanner(System.in);
       
       while (true)
       {
           System.out.print("\n이름과 마일리지>> ");
           
           String name = sc.next();
           if(name.equals("그만"))
           {
        	   break;
           }
           int mileage = sc.nextInt();
           
           mileageMap.put(name, mileageMap.getOrDefault(name, 0) + mileage);
           // getOrDefault 메소드 추가 활용
       }

       // 최종 마일리지 출력
       System.out.println("\n최종 마일리지 정보:");
       show_Mileage();

       // 검색 기능
       while (true)
       {
           System.out.print("\n검색할 이름>> ");
           
           String name = sc.next();
           if (name.equals("그만"))
           {
        	   break;
           }
           Integer mileage = mileageMap.get(name);
           
           if (mileage == null)
           {
               System.out.println(name + " 고객은 마일리지가 없습니다.");
           } 
           else
           {
               System.out.println(name + "의 마일리지: " + mileage);
           }
       }       
       sc.close();
    }

    // 3. (이름:마일리지) 형태로 모든 마일리지 정보 출력
    public void show_Mileage() {
    	Set<String> keys = mileageMap.keySet();
    	Iterator<String> it = keys.iterator();
    	while(it.hasNext())
    	{
    		String name = it.next();
    		Integer mileage = mileageMap.get(name);
    		System.out.print("(" + name + ":" + mileage +")");
    	}		
        System.out.println();
    }
    
    // 4. 검색까지 종료된 이후, 프로그램 종료 전에, 가장 마일리지가 높은 고객을 출력
    public void highest_Mileage() {
    	int maxMileage = Collections.max(mileageMap.values());
    	String maxName = null;
    	for(String name : mileageMap.keySet())
    	{
    		if(mileageMap.get(name) == maxMileage)
    		{
    			maxName = name;
    			break;
    		}
    	}   	
    	System.out.println("\n가장 마일리지가 높은 고객은 " + maxName + "입니다.");
    }
         
    // 5. GridLayout을 이용해서 출력
    public void show_GridLayout() {
    	setTitle("최종 마일리지 정보");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	GridLayout grid = new GridLayout(mileageMap.size()+1, 2);
    	grid.setVgap(5);
    	
    	Container c = getContentPane();
    	c.setLayout(grid);
    	c.add(new JLabel("고객 이름"));
    	c.add(new JLabel("마일리지"));
    	
    	// entrySet() 추가 활용
    	for(Map.Entry<String, Integer> entry : mileageMap.entrySet())
    	{
    		c.add(new JLabel(entry.getKey()));
    		c.add(new JLabel(String.valueOf(entry.getValue())));
    	}
    	
    	setSize(400, 300);
    	setVisible(true);
    }
   
}