/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication2;

import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 *
 * @author DELL
 */
public class JavaApplication2 {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    static JRadioButton radioBtn1 = new JRadioButton("EDIFACT");
    static JRadioButton radioBtn2 = new JRadioButton("X12");
    static JRadioButton radioBtnIN = new JRadioButton("IN");
    static JRadioButton radioBtnOUT = new JRadioButton("OUT");    
    /**
     * @param args the command line arguments
     */
    public static boolean checkDup(String tmpCheck, ArrayList<String> list){
        for(int i= 0; i < list.size(); i++){
            if(tmpCheck.equals(list.get(i))){
                return true;
            }
        }
         return false;
    } 
    public static ArrayList<String> getListField(String[] allLine){
        int i;
        ArrayList<String> listField = new ArrayList<String>();
        String field;
        for(i = 0; i < allLine.length; i++){
            if(allLine[i].contains(":FIELD")){
                field = "";
                if (allLine[i].indexOf("LOOP") >= 0 &&  allLine[i].indexOf(":FIELD") >= 0){
                    field = (allLine[i].substring(allLine[i].indexOf("LOOP"), allLine[i].indexOf(":FIELD")));
                }
                else if (allLine[i].indexOf("HEADER") >= 0 && allLine[i].indexOf(":FIELD") >= 0){
                    field = (allLine[i].substring(allLine[i].indexOf("HEADER"), allLine[i].indexOf(":FIELD")));;
                }               
                if (checkDup(field, listField) == false){
                        if (allLine[i].indexOf("LOOP") >= 0 &&  allLine[i].indexOf(":FIELD") >= 0){
                            listField.add(field);
                        }
                        else if (allLine[i].indexOf("HEADER") >= 0 && allLine[i].indexOf(":FIELD") >= 0){
                            listField.add(field);;
                        }
                }
            }
        }
        return listField;
    }
    public static boolean checkFieldEDIFACTIN(String data){
        if (data.indexOf("LOOP") >= 0 &&  data.indexOf(":FIELD;") >= 0){
            return true;
        }
        else if (data.indexOf("HEADER") >= 0 && data.indexOf(":FIELD;") >= 0){
            return true;
        }
        return false;
    }
    public static boolean checkFieldX12OUT(String data){
        if (data.indexOf("ISA") >= 0 &&  data.indexOf(";") >= 0){
            return true;
        }
        return false;
    }    
    public static int getPos(String[] allLine, String data){
        int i = 0;
        int pos = -1,pos2 = -1, pos3 = -1;
        String dataRm = "";
        dataRm = rmIndex(data);
        for(i = 0; i < allLine.length; i++){
            pos2 = allLine[i].indexOf(data);
            pos3 = allLine[i].indexOf(dataRm);
            if (pos2 > 0 || pos3 > 0){
                pos = i;
            }
        }
        System.out.print("TEST pos "+pos );
        return pos;
    }
     
    public static String getStrBef(String[] allLine, int pos, String data){
        String result = "";
        int i=0;
        int firstPos;
        String dataRm = "";
        if( data.indexOf("[")> 0){
            dataRm = rmIndex(data);
        }
       
        for(i = pos; i >= 0; i--){
            firstPos = getFirstPos(allLine[i]);
            String tmpStr;
            tmpStr = allLine[i];
            if (tmpStr.trim().equals("")){
                continue;
            }
//            if(i > 0 && getFirstPos(allLine[i]) == getFirstPos(allLine[i-1])){
//                continue;
//            }
            
            if (radioBtn1.isSelected() == true && radioBtnIN.isSelected() == true){
                if (dataRm.equals("") == false){
                    if (i != pos && checkFieldEDIFACTIN(tmpStr) == true && tmpStr.contains(dataRm) == false && tmpStr.contains(data) == false){
                        continue;
                    }
                }else if (checkFieldEDIFACTIN(tmpStr) == true && tmpStr.contains(data) == false ){
                     continue;  
                }
                result = tmpStr+"\n"+result;                          
            }else if(radioBtn2.isSelected() == true && radioBtnOUT.isSelected() == true){
                if (dataRm.equals("") == false){
                    if (i != pos && checkFieldX12OUT(tmpStr) == true && tmpStr.contains(dataRm) == false && tmpStr.contains(data) == false){
                        continue;
                    }
                }else if (checkFieldX12OUT(tmpStr) == true && tmpStr.contains(data) == false ){
                    continue;
                }
                result = tmpStr+"\n"+result;                
            }
            
            
            if (allLine[i].trim().equals("") == false && firstPos == 0){
                break;
            }            
        }
        return result;
    }
    public static String getStrAf(String[] allLine, int pos, String data){
        int i=0;
        String result = "";
        String dataRm = "";
        dataRm = rmIndex(data);     
        int firstPos, firstPosOrg;
        
        if ((allLine.length > pos+1) && allLine[pos+1] != null &&  allLine[pos+1].contains("endif") == true){
            result = result +"\n"+ allLine[pos+1];
        }        
        firstPosOrg = getFirstPos(allLine[pos]);
        for(i = pos + 1; i<allLine.length; i++){
            String tmpStr;
            tmpStr = allLine[i];
            firstPos = getFirstPos(allLine[i]); 
            
            if (tmpStr.trim().equals("")){
                continue;
            }  
            if (radioBtn1.isSelected() == true && radioBtnIN.isSelected() == true){
                if (dataRm.equals("") == false){
                    if (i != pos && checkFieldEDIFACTIN(tmpStr) == true && tmpStr.contains(dataRm) == false && tmpStr.contains(data) == false){
                        continue;
                    }
                }else   
                if (checkFieldEDIFACTIN(tmpStr) == true && tmpStr.contains(data) == false ){
                     continue;  
                }
                                 
            }else if(radioBtn2.isSelected() == true && radioBtnOUT.isSelected() == true){
                if (dataRm.equals("") == false){
                    if (i != pos && checkFieldX12OUT(tmpStr) == true && tmpStr.contains(dataRm) == false && tmpStr.contains(data) == false ){
                        continue;
                    }
                }else   
                if (checkFieldX12OUT(tmpStr) == true && tmpStr.contains(data) == false ){
                    continue;
                }
            }
            System.out.println("firstPos "+firstPos);
            System.out.println("firstPosOrg "+firstPosOrg);
            if (firstPos < (firstPosOrg)){
                result = result +"\n"+ allLine[i];
             } 
            //System.out.println("Rs "+allLine[i]);
            if (allLine[i].trim().equals("") == false && firstPos == 0){
                break;
            }                  
        }
        return result;
    }
    public static int getFirstPos(String data){
        int pos = 0;
        String fChar = data.trim();
        if (fChar.equals("") == false){
            pos = data.indexOf(fChar.substring(0, 1));
        }        
        return pos;
    }
    public static String removeUn(String data){
        String[] arrOfStr = data.split("\\n");
        String result = "";
        int i = 0;
        for(i = 0; i< arrOfStr.length; i++){
            if(arrOfStr[i].trim().startsWith("if") && arrOfStr[i+1].trim().startsWith("else") && arrOfStr[i+2].trim().startsWith("endif")){
                i = i+ 2;
            }
            else if(arrOfStr[i].trim().startsWith("if") && arrOfStr[i+1].trim().startsWith("endif")){
                i = i+ 1;
            }else if(arrOfStr[i].trim().startsWith("for") && arrOfStr[i+1].trim().startsWith("next")){
                i = i+ 1;
            }else{
                result =  result + "\n"+ arrOfStr[i];
            }
            
        }
        return result;
    }
    public static String rmIndex(String data){
        String tmpStr = data;
        String dataCon = "";
        while(tmpStr.indexOf("[")> 0){
            dataCon = tmpStr.substring(tmpStr.indexOf("["), tmpStr.indexOf("]")+1);
            tmpStr = tmpStr.replace(dataCon, ""); 
        }     
        return tmpStr;
    }   
    public static void main(String[] args) {
        JFrame frame=new JFrame();
        JPanel panel=new JPanel();
        frame.setSize(1300,1200);
        frame.setTitle("Registration");
        
        JButton open =new JButton();
//        JButton create=new JButton();
        open.setPreferredSize(new Dimension(120,30));
        open.setText("Open input file");
//        JLabel lablename=new JLabel("Enter Input data");
        JTextPane tname=new JTextPane();
        //tname.setColumns(100);
        JScrollPane scroll = new JScrollPane(tname);
        tname.setPreferredSize(new Dimension(1200,400));
        scroll.setViewportView(tname);
//        JScrollPane scroll = new JScrollPane(tname, 
//                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
//                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);        
//        //scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR);            
       
        //JLabel lablename2=new JLabel("Enter your name");
        TextField tname2=new TextField("");
        //tname.setColumns(100);

        
        radioBtn1.setBounds(50, 60, 170, 30);
        radioBtn2.setBounds(50, 100, 170, 30);
        ButtonGroup group = new ButtonGroup();
        group.add(radioBtn1);
        group.add(radioBtn2);        
        radioBtn1.setSelected(true);


        radioBtnIN.setBounds(50, 60, 170, 30);
        radioBtnOUT.setBounds(50, 100, 170, 30); 
        ButtonGroup group2 = new ButtonGroup();
        group2.add(radioBtnIN);
        group2.add(radioBtnOUT);        
        radioBtnIN.setSelected(true);
        
//        ButtonGroup bg = new ButtonGroup();
//        bg.add(radioBtn1);
//        bg.add(radioBtn2);
        
        JLabel lablename2=new JLabel("Enter data to get");
        tname2.setPreferredSize(new Dimension(900,30));        

        JButton login=new JButton();
        login.setPreferredSize(new Dimension(90,30));
        login.setText("Generate");
    
        JTextPane tname3=new JTextPane();
        //tname.setColumns(100);
        tname3.setPreferredSize(new Dimension(1200,400));
        JScrollPane scroll3 = new JScrollPane(tname3);
        panel.add(radioBtn1);
        panel.add(radioBtn2);        
        panel.add(open);
        panel.add(radioBtnIN);
        panel.add(radioBtnOUT);      
        panel.add(scroll);
        //panel.add(tname);

        panel.add(lablename2);
        panel.add(tname2);
        panel.add(login);
        panel.add(scroll3);
        
        frame.add(panel);
        frame.setVisible(true);
        login.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
              String[] arrOfStr = tname.getText().split("\\n");
              int pos = -1;
              String tmpStrBef, tmpStrAf = "";
              pos = getPos(arrOfStr, tname2.getText()); 
              //tmpStr = "TEST "+pos;
              tmpStrBef = getStrBef(arrOfStr, pos, tname2.getText());
              tmpStrAf = getStrAf(arrOfStr, pos, tname2.getText());
        
              String result = removeUn(tmpStrBef+"\n"+tmpStrAf);
              result = removeUn(result);
              result = removeUn(result);
              tname3.setText(result.trim());
           }
        });
        open.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
              JFileChooser fileChooser = new JFileChooser();
              int option = fileChooser.showOpenDialog(frame);
              if(option == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile(); 
                String tmpAllData;
                tmpAllData = "";
                try {
                      //File myObj = new File("filename.txt");
                      Scanner myReader = new Scanner(file);
                      System.out.print("Start print2");
                      while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        tmpAllData = tmpAllData + data + "\n";
                        System.out.println(data);
                      }
                      myReader.close();
                    } catch (FileNotFoundException e2) {
                      System.out.println("An error occurred.");
                      e2.printStackTrace();
                    }  
                tname.setText("TEST "+"\n" + "TESt2");
              }else{
                 tname3.setText("Open command canceled");
              }
           }
        });
      
    }
 
}
