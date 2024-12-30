/*
 * 마일리지 관리 프로그램의 버전 - 4
 * 
 * 초기 화면 설정 + 메뉴바 호출을 진행하는 8. setupInitialScreen 메서드를 추가함
 * 
 * "Mileage Manager"라고 써있는 초기화면을 출력하는 메서드를 추가함
 * boolean 변수를 활용하여 초기화면에서 메뉴화면으로 넘어갈때, 화면이 초기화되도록 구현
 * 
 * 하지만 6. show_ImageGallery를 아직 동시에 구현하지 못했다. 
 */

import java.util.*;
import java.util.List;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MileageManager_4 extends JFrame {
    HashMap<String, Integer> mileageMap = new HashMap<>();
    private boolean isGridShown = false; // 확인 플래그 추가
    private JLabel initialLabel; // 초기 화면의 "Mileage Manager" 표시용 라벨
    
    public static void main(String[] args) {
        System.out.println("*** 마일리지 관리 프로그램입니다.***");

        MileageManager_4 manager = new MileageManager_4();        
        manager.get_Mileage_From_File();
        manager.control_Mileage();
        manager.highest_Mileage();
        manager.setupInitialScreen(); // 초기 화면 설정 메서드 추가

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

    // 5. GridLayout을 이용해서 출력 (isGridShown을 활용하여 초기화면을 제어하는 부분 추가)
    public void show_GridLayout() {
        if (isGridShown)
        {
            return;
        }
        isGridShown = true;

        // 기존 초기 화면 삭제
        getContentPane().removeAll();

        setTitle("고객별 마일리지 확인");
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
        //revalidate(); // 화면 갱신
        //repaint();
        setVisible(true);
    }

    // 6. 이미지 갤러리 구현 -> 잠시 삭제
    
    // 7. 메뉴바 생성 및 설정
    public void create_MenuBar() {
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
    }
    
    // 8. 초기 화면 설정 + 메뉴바 호출 (새로 추가한 메서드)
    public void setupInitialScreen() {
        setTitle("Mileage Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        initialLabel = new JLabel("Mileage Manager", SwingConstants.CENTER);
        initialLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        c.add(initialLabel, BorderLayout.CENTER);

        create_MenuBar();

        setSize(400, 300);
        setVisible(true);
    }
}
