/*
 * 마일리지 관리 프로그램의 최종버전
 * 
 * 기존의 boolean 변수가 아닌, 화면을 아예 초기화해버리는 메서드를 추가함
 * 이를 통해서 show_GridLayout과 show_ImageGallery를 실행하는 경우, 쉽게 화면을 초기화할 수 있게 수정
 * 
 */

import java.util.*;
import java.util.List;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MileageManager extends JFrame {
    HashMap<String, Integer> mileageMap = new HashMap<>();
    private JLabel initialLabel; // 초기 화면의 "Mileage Manager" 표시용 라벨

    public static void main(String[] args) {
        System.out.println("*** 마일리지 관리 프로그램입니다.***");

        MileageManager manager = new MileageManager();        
        manager.get_Mileage_From_File();
        manager.control_Mileage();
        manager.highest_Mileage();
        manager.setupInitialScreen();

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
    
    // 5. GridLayout을 이용해서 출력 (화면 초기화 메서드를 호출하는 부분 추가)
    // 체크박스 -> p.618, p.620 예제 참조
    public void show_GridLayout() {     
        clearScreen(); // 화면 초기화 메서드 추가

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
        setVisible(true);
    }

    // 6. 이미지 갤러리 구현 (화면 초기화 메서드를 호출하는 부분 추가)
    // show_ImageGallery 및 updateInfo 메서드의 아이디어 
    // -> https://m.blog.naver.com/fkdltmxlr3/221698942669 및 https://blog.naver.com/varyeun/221647553832 2개 참고
    // 좌우 화살표 버튼 구현 아이디어-> https://cmj092222.tistory.com/92 참고
    Iterator<Map.Entry<String, Integer>> iterator;
    Map.Entry<String, Integer> currentEntry;
    JLabel nameLabel;
    JLabel imageLabel;
    JButton leftButton, rightButton;
    
    public void show_ImageGallery() {
        clearScreen(); // 화면 초기화 메서드 추가

        setTitle("고객별 등급 확인");
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

    // 7. 메뉴바 생성 및 설정
    // 메뉴바, 메뉴아이템 -> p.767 p.769 예제 참고
    public void create_MenuBar() {

        JMenuBar menuBar = new JMenuBar();

        // "직원용" 메뉴 생성
        JMenu staffMenu = new JMenu("직원용");

        // "마일리지 확인" 메뉴아이템 생성 + GridLayout 실행(체크박스)
        JMenuItem mileageCheckItem = new JMenuItem("마일리지 확인");
        mileageCheckItem.addActionListener(e -> show_GridLayout());

        // "등급 확인" 메뉴아이템 생성 + ImageGallery 출력
        JMenuItem gradeCheckItem = new JMenuItem("등급 확인");
        gradeCheckItem.addActionListener(e -> show_ImageGallery());
    
        staffMenu.add(mileageCheckItem); 
        staffMenu.add(gradeCheckItem);  
        menuBar.add(staffMenu);

        // "고객용" 메뉴 생성
        JMenu customerMenu = new JMenu("고객용");
        menuBar.add(customerMenu);

        setJMenuBar(menuBar);
    }
    
    // 8. 초기 화면 설정 + 메뉴바 호출
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
    
    // 9. 화면 초기화 메서드 (새로 추가한 메서드)
    // getContentPane 활용 -> https://stackoverflow.com/questions/20733755/jframe-getcontentpane-removeall-only-working-between-a-setvisiblefalse-and
    // 및 https://m.blog.naver.com/blayan/220549682964 및 https://okky.kr/questions/438950 참고
    private void clearScreen() {
        getContentPane().removeAll(); // 모든 컴포넌트 제거
    }

}
