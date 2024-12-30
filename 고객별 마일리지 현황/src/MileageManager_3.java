/*
 * 마일리지 관리 프로그램의 버전 - 3
 * 
 * 7. create_MenuBar 메서드를 통해서 메뉴바와 메뉴아이템을 생성하였다.
 * 이 과정에서 6. show_ImageGallery는 잠시 삭제해놓았다.
 * 
 * 하지만 메뉴아이템 "마일리지 확인"을 중복 클릭시, 중복하여서 show_GridLayout이 실행되는 문제가 존재한다.
 * 또한 6. show_ImageGallery도 같이 구현해야한다.
 */

import java.util.*;
import java.util.List;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MileageManager_3 extends JFrame {
    HashMap<String, Integer> mileageMap = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("*** 마일리지 관리 프로그램입니다.***");

        MileageManager_3 manager = new MileageManager_3();        
        manager.get_Mileage_From_File();
        manager.control_Mileage();
        manager.highest_Mileage();
        manager.create_MenuBar(); // 새롭게 추가한 메뉴바 생성 메서드 실행

        System.out.println("프로그램을 종료합니다.");
    }

    // 1. mileage.txt 파일로부터 초기 마일리지 데이터 읽어오기
    public void get_Mileage_From_File() {
        try {
            File file = new File(".\\mileage.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNext()) {
                String name = sc.next();
                if (name.equals("그만")) {
                    break;
                }
                int mileage = sc.nextInt();
                mileageMap.put(name, mileageMap.getOrDefault(name, 0) + mileage);
            }
            sc.close();
        } 

        catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException");
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (Exception e) {
            System.out.println("Exception occured");
        }

        // 초기 마일리지 출력
        System.out.println("\n초기 마일리지 정보:");
        show_Mileage();
    }

    // 2. 마일리지 누적, 합산, 차감 / 새로운 이름과 마일리지 추가 / 검색 기능
    public void control_Mileage() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("\n이름과 마일리지>> ");

            String name = sc.next();
            if(name.equals("그만")) {
                break;
            }
            int mileage = sc.nextInt();

            mileageMap.put(name, mileageMap.getOrDefault(name, 0) + mileage);
        }

        // 최종 마일리지 출력
        System.out.println("\n최종 마일리지 정보:");
        show_Mileage();

        // 검색 기능
        while (true) {
            System.out.print("\n검색할 이름>> ");

            String name = sc.next();
            if (name.equals("그만")) {
                break;
            }
            Integer mileage = mileageMap.get(name);

            if (mileage == null) {
                System.out.println(name + " 고객은 마일리지가 없습니다.");
            } else {
                System.out.println(name + "의 마일리지: " + mileage);
            }
        }        
        sc.close();
    }

    // 3. (이름:마일리지) 형태로 모든 마일리지 정보 출력
    public void show_Mileage() {
        Set<String> keys = mileageMap.keySet();
        Iterator<String> it = keys.iterator();
        while(it.hasNext()) {
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
        for(String name : mileageMap.keySet()) {
            if(mileageMap.get(name) == maxMileage) {
                maxName = name;
                break;
            }
        }       
        System.out.println("\n가장 마일리지가 높은 고객은 " + maxName + "입니다.");
    }

    // 5. GridLayout을 이용해서 출력
    public void show_GridLayout() {
        setTitle("고객별 마일리지 현황");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridLayout grid = new GridLayout(mileageMap.size() + 1, 2, 5, 5);
        grid.setVgap(5);

        Container c = getContentPane();
        c.setLayout(grid);
        c.add(new JLabel("고객 이름"));
        c.add(new JLabel("마일리지"));

        for (Map.Entry<String, Integer> entry : mileageMap.entrySet()) {
            JCheckBox checkBox = new JCheckBox(entry.getKey());
            JLabel mileageLabel = new JLabel(""); 

            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (checkBox.isSelected()) {
                        mileageLabel.setText(String.valueOf(entry.getValue()));
                    } else {
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

    // 6. 이미지 갤러리 구현 -> 잠시 삭제
    
    // 7. 메뉴바 생성 및 설정 (새로 추가한 메서드)
    public void create_MenuBar() {
        setTitle("고객별 마일리지 현황");

        JMenuBar menuBar = new JMenuBar();

        // "고객용" 메뉴 생성
        JMenu customerMenu = new JMenu("고객용");
           
        // "마일리지 확인" 메뉴아이템 생성 + GridLayout 실행(체크박스)
        JMenuItem mileageCheckItem = new JMenuItem("마일리지 확인");
        mileageCheckItem.addActionListener(e -> show_GridLayout());

        customerMenu.add(mileageCheckItem);
        customerMenu.add(new JMenuItem("등급 확인"));
        menuBar.add(customerMenu);

        // "직원용" 메뉴 생성
        JMenu staffMenu = new JMenu("직원용");
        menuBar.add(staffMenu);

        setJMenuBar(menuBar);
        setSize(400, 300);
        setVisible(true);
    }
}