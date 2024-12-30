/*
 * 마일리지 관리 프로그램의 버전 - 2
 * 
 * 5. show_GridLayout을 통해서 출력하는 과정에서 체크박스를 이용하는 부분을 추가했다.
 * 6. show_ImageGallery를 통해서 고객의 마일리지값에 따른 등급을 나누어서 금/은/동메달이 출력된다.
 * 
 * 하지만 show_GridLayout과 show_ImageGallery 각각은 출력이 가능하지만,
 * 2가지 기능을 동시에 출력하는 것은 실패하였다.
 * 
 * 따라서 메뉴바를 통해서 위의 2가지 기능을 동시에 구현하도록 수정할 예정이다.
 */

import java.util.*;
import java.util.List;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MileageManager_2 extends JFrame{
    HashMap<String, Integer> mileageMap = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("*** 마일리지 관리 프로그램입니다.***");

        MileageManager_2 manager = new MileageManager_2();        
        manager.get_Mileage_From_File();
        manager.control_Mileage();
        manager.highest_Mileage();
        //manager.show_GridLayout();
        manager.show_ImageGallery();
        
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
         
    // 5. GridLayout을 이용해서 출력 (체크박스를 이용하여 출력하는 부분 추가)
    public void show_GridLayout() {
    	setTitle("최종 마일리지 정보");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	GridLayout grid = new GridLayout(mileageMap.size()+1, 2, 5, 5);
    	grid.setVgap(5);

        Container c = getContentPane();
        c.setLayout(grid);
        c.add(new JLabel("고객 이름"));
    	c.add(new JLabel("마일리지"));

        for (Map.Entry<String, Integer> entry : mileageMap.entrySet()) 
        {
            JCheckBox checkBox = new JCheckBox(entry.getKey());
            JLabel mileageLabel = new JLabel(""); 

            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (checkBox.isSelected())
                    {
                        mileageLabel.setText(String.valueOf(entry.getValue()));
                    } 
                    else
                    {
                        mileageLabel.setText("");
                    }
                }
            });

            c.add(checkBox);
            c.add(mileageLabel);
        }
    	
    	setSize(400, 300);
    	setVisible(true);
    }
    
    // 6. 이미지 갤러리 구현
    Iterator<Map.Entry<String, Integer>> iterator;
    Map.Entry<String, Integer> currentEntry;
    JLabel nameLabel; 
    JLabel imageLabel; 
    JButton leftButton, rightButton; 
    
    public void show_ImageGallery() {
        setTitle("최종 마일리지 정보");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        nameLabel = new JLabel("", SwingConstants.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        c.add(nameLabel, BorderLayout.NORTH);

        imageLabel = new JLabel("", SwingConstants.CENTER);
        c.add(imageLabel, BorderLayout.CENTER);

        MenuPanel menuPanel = new MenuPanel();
        c.add(menuPanel, BorderLayout.SOUTH);

        iterator = mileageMap.entrySet().iterator();
        if (iterator.hasNext())
        {
            currentEntry = iterator.next();
            updateInfo();
        }

        setSize(400, 500);
        setVisible(true);
    }

    // 6-1 고객 이름과 마일리지 및 이미지 업데이트
    public void updateInfo() {
        if (currentEntry == null)
        {
        	return;
        }

        String name = currentEntry.getKey();
        int mileage = currentEntry.getValue();
        nameLabel.setText(name + " : " + mileage);

        String mileageGrade;
        if (mileage < 1000)
        {
            mileageGrade = "images/bronze.png";
        } 
        else if (1000 <= mileage && mileage < 3000) 
        {
            mileageGrade = "images/silver.png"; 
        } 
        else 
        {
            mileageGrade = "images/gold.png";
        }

        imageLabel.setIcon(new ImageIcon(mileageGrade));
    }

    // 6-2 좌우 화살표 버튼 구현
    class MenuPanel extends JPanel {
        public MenuPanel() {
            setLayout(new GridLayout(1, 2, 10, 10));

            leftButton = new JButton(new ImageIcon("images/left_arrow.png"));
            leftButton.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    moveToLeft();
                }
            });
            add(leftButton);

            rightButton = new JButton(new ImageIcon("images/right_arrow.png"));
            rightButton.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    moveToRight();
                }
            });
            add(rightButton);
        }
        
        private void moveToLeft() {
            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(mileageMap.entrySet());
            int index = entryList.indexOf(currentEntry);
            if (index > 0) 
            {
                currentEntry = entryList.get(index - 1);
                updateInfo();
            }
        }

        private void moveToRight() {
            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(mileageMap.entrySet());
            int index = entryList.indexOf(currentEntry);
            if (index < entryList.size() - 1) 
            {
                currentEntry = entryList.get(index + 1);
                updateInfo();
            }
        }
    }
   
}

