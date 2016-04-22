package solution;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.regex.Pattern;

public class VerifyFile {
	
	private HashMap<String,String[]> configures = new HashMap<String,String[]>();
	private String[] keywords = new String[0];
	private String header1;
	private String header2;
	private int flag = 0;
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public HashMap<String, String[]> getConfigures() {
		return configures;
	}
	public void setConfigures(HashMap<String, String[]> configures) {
		this.configures = configures;
	}
	public String[] getKeywords() {
		return keywords;
	}
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
	public String getHeader1() {
		return header1;
	}
	public void setHeader1(String header1) {
		this.header1 = header1;
	}
	public String getHeader2() {
		return header2;
	}
	public void setHeader2(String header2) {
		this.header2 = header2;
	}
	
	public VerifyFile(String confFile,String keywFile,String header1,String header2) throws Exception {
		StringBuilder conSb = new StringBuilder();
		StringBuilder keywSb = new StringBuilder();
		//try {
			FileInputStream fileCon = new FileInputStream(new File(confFile));
			FileInputStream fileKeyw = new FileInputStream(new File(keywFile));
			int c = 0;
			while((c = fileCon.read()) != -1)
				conSb.append((char)c);
			while((c = fileKeyw.read()) != -1)
				keywSb.append((char)c);
		//} catch (Exception e) {
		//	e.printStackTrace();
		//}
//		System.out.println(conSb);
//		System.out.println(keywSb);
		
		ArrayList<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile("\\[[a-zA-Z0-9]{8}\\]");
		Matcher matcher = pattern.matcher(conSb.toString());
		while(matcher.find())
			list.add(matcher.group());
		String[] string = conSb.toString().split("\\[[a-zA-Z0-9]{8}\\]");
		int index = 0;
		for(String s : string) {
			if(!s.equals(""))
				configures.put(list.get(index++),s.trim().split("\\s+"));
		}
		
//		for(Map.Entry<String, String[]> entry : configures.entrySet()) {
//			System.out.print(entry.getKey() + ":");
//			for(String ss : entry.getValue())
//				System.out.print(ss + " ");
//			System.out.println();
//		}
		
		list = new ArrayList<String>();
		pattern = Pattern.compile("[0-9]+=[A-Z].*");
		matcher = pattern.matcher(keywSb.toString());
		while(matcher.find())
			list.add(matcher.group().split("=")[1]);
		keywords = list.toArray(new String[0]);
		
//		for(String s : keywords)
//			System.out.println(s);
		
		this.header1 = header1;
		this.header2 = header2;
	}
	
	public String sectionVerify() {
		StringBuilder result = new StringBuilder();
		for(Map.Entry<String, String[]> entry : configures.entrySet()) {
			int lineNumber = 1;
			for(String s : entry.getValue()) {
				String section = entry.getKey().substring(1, entry.getKey().length()-1);
				if(!ruleNo0(lineNumber,s).equals("")) {
					result.append(section + "/" + ruleNo0(lineNumber,s));
					lineNumber++;
					continue;
				}
				if(!ruleNo1(lineNumber,s).equals("")) {
					result.append(section + "/" + ruleNo1(lineNumber,s));
					lineNumber++;
					continue;
				}
				if(!ruleNo2(lineNumber,s).equals("")) {
					result.append(section + "/" + ruleNo2(lineNumber,s));
					lineNumber++;
					continue;
				}
				if(!ruleNo3(lineNumber,s).equals("")) {
					result.append(section + "/" + ruleNo3(lineNumber,s));
					lineNumber++;
					continue;
				}
				if(!ruleNo4(lineNumber,s).equals("")) {
					result.append(section + "/" + ruleNo4(lineNumber,s));
					lineNumber++;
					continue;
				}
				if(!ruleNo5(lineNumber,s).equals("")) {
					result.append(section + "/" + ruleNo5(lineNumber,s));
					lineNumber++;
					continue;
				}
				if(!ruleNo6(lineNumber,s).equals("")) {
					result.append(section + "/" + ruleNo6(lineNumber,s));
					lineNumber++;
					continue;
				}
				lineNumber++;
			}
		}
		ArrayList<String> string = new ArrayList<String>();
		for(String s : result.toString().split("\r\n")) {
			string.add(s);
		}
		string.sort(null);
		String over = new String();
		for(String s : string)
			over += s + "\r\n";
		return over;
	}
	//****************** 返回格式 行号/part[,part,part] 错误原因 ****************************
	//整行：字符串用/分为六部分
	public String ruleNo0(int lineNumber,String configureText) {
		StringBuilder result = new StringBuilder();
		Pattern pattern = Pattern.compile("[^/]/[^/]");
		Matcher matcher = pattern.matcher(configureText);
		int groups = 0;
		while(matcher.find())
			groups++;
		pattern = Pattern.compile(".*/[RW]/.*");
		matcher = pattern.matcher(configureText);
		if(groups != 4 || !matcher.matches())
			result.append(lineNumber +" 语句划分错误" + "\r\n");
		String s = result.toString();
		return result.toString();
	}
	
	//Part1:必须两个固定字符串其中之一
	public String ruleNo1(int lineNumber,String configureText) {
		if(this.getFlag() == 1)
			return null;
		StringBuilder result = new StringBuilder();
		int flag = 0;
		String lineHeader = configureText.split("/")[0].split("=")[1];
		for(String head : new String[]{this.getHeader1(),this.getHeader2()}) {
			if(head.equals(lineHeader)) {
				flag = 1;
			}
		}
		if(flag == 0)
			result.append(lineNumber + "/" + "1" + " 错误" + "\r\n");
		return result.toString();
	}
	
	//Part2       
	//如果有尖括号，尖括号出现的每个字符串包括尖括号，必须是Part4 的子字符串
	//如果无尖括号则Part4=NULL,Part5=NULL
	public String ruleNo2(int lineNumber,String configureText) {
		if(this.getFlag() == 1)
			return null;
		StringBuilder result = new StringBuilder();
		String part2 = configureText.split("/")[1];
		String part4 = configureText.split("/")[3];
		String part5 = configureText.split("/")[4];	
		if(part2.matches(".*<[a-zA-Z0-9]+>.*")) {
			Pattern pattern = Pattern.compile("<.{3,8}>");
			Matcher matcher = pattern.matcher(part4);
			HashSet<String> part4Set = new HashSet<String>();
			while(matcher.find()) {
				part4Set.add(matcher.group());
			}
			
			matcher = pattern.matcher(part2);
			HashSet<String> part2Set = new HashSet<String>();
			while(matcher.find()) {
				part2Set.add(matcher.group());
			}
			for(String s : part2Set.toArray(new String[0])) {
				if(!part4Set.contains(s)) {
					result.append(lineNumber + "/" + "2" + " 错误" + "\r\n");
				}
			}
		} else {
			if(!part4.equals("NULL") && part5.equals("NULL"))
				result.append(lineNumber + "/" + "4" + " 错误" + "\r\n");
			if(!part4.equals("NULL") && !part5.equals("NULL"))
				result.append(lineNumber + "/" + "4,5" + " 错误" + "\r\n");
			if(part4.equals("NULL") && !part5.equals("NULL"))
				result.append(lineNumber + "/" + "5" + " 错误" + "\r\n");
		}
		return result.toString();
	}
	
	//Part3 
	//只能写W或者R  
	//如果Part2有？号,part3=R，同时Part6必须是除NULL之外的"关键字"
	//如果Part2中没有？号,Part3=W，同时Part6=NULL
	public String ruleNo3(int lineNumber,String configureText) {
		if(this.getFlag() == 1)
			return null;
		StringBuilder result = new StringBuilder();
		String part2 = configureText.split("/")[1];
		String part3 = configureText.split("/")[2];
		String part6 = configureText.split("/")[5];
		HashSet<String> keySet = new HashSet<String>();
		for(String s : keywords)
			keySet.add(s);
		if(part2.indexOf("?") != -1) {
			if(!part3.equals("R") || !keySet.contains(part6)) {
				if(!part3.equals("R") && !keySet.contains(part6)) {
					result.append(lineNumber + "/" + "3,6" + " 错误");
				}
				if(part3.equals("R") && !keySet.contains(part6)) {
					result.append(lineNumber + "/" + "6" + " 错误");
				}
				if(!part3.equals("R") && keySet.contains(part6)) {
					result.append(lineNumber + "/" + "3" + " 错误");
				}
				result.append("\r\n");
			}
		} else {
			if(!part3.equals("W") || !part6.equals("NULL")) {
				if(!part3.equals("W") && !part6.equals("NULL")) {
					result.append(lineNumber + "/" + "3,6" + " 错误");
				}
				if(part3.equals("W") && !part6.equals("NULL")) {
					result.append(lineNumber + "/" + "6" + " 错误");
				}
				if(!part3.equals("W") && part6.equals("NULL")) {
					result.append(lineNumber + "/" + "3" + " 错误");
				}
				result.append("\r\n");
			}
		}
		return result.toString();
	}
	
	//Part4 
	//Part4可为NULL
	//当Part4!=NULL，则Part2，Part4，Part5中参数个数相同
	//Part2中有尖括号，参数个数为带尖括号子字符串个数=Part4中逗号个数+1=Part5中逗号个数+1
	public String ruleNo4(int lineNumber,String configureText) {
		if(this.getFlag() == 1)
			return null;
		StringBuilder result = new StringBuilder();
		String part2 = configureText.split("/")[1];
		String part4 = configureText.split("/")[3];
		String part5 = configureText.split("/")[4];
		int part2Cnt = 0;
		Pattern pattern = Pattern.compile("<[a-zA-Z0-9]{3,5}>");
		Matcher matcher = pattern.matcher(part2);
		if(matcher.matches()){
			while(matcher.find())
				part2Cnt++;
			int part4Cnt = 0;
			int part5Cnt = 0;
			for(char c : part4.toCharArray()){
				if(c == ',')
					part4Cnt++;
			}
			for(char c : part5.toCharArray()){
				if(c == ',')
					part5Cnt++;
			}
			if(part4Cnt != part5Cnt || part2Cnt != part5Cnt+1 || part4Cnt+1 != part2Cnt) {
				result.append(lineNumber + "/" + "2,4,5" + " 错误");
				result.append("\r\n");
			}
		}
		return result.toString();
	}
		
	//Part5
	//Part4为NULL时,Part5必须为NULL
	//Part5可以为NULL
	//如果Part5!=NULL，则可以为关键字之一
	//或者为其他字符串，若果字符串中含有E,e,+,-，逗号 之外其他字符则无错。具体格式不做检查
	//%%%  问题：part5可能的值有NULL，由数字、E,e,+,-，逗号组成的字符串？   %%%%%%
	public String ruleNo5(int lineNumber,String configureText) {
		if(this.getFlag() == 1)
			return null;
		StringBuilder result = new StringBuilder();
		String part4 = configureText.split("/")[3];
		String part5 = configureText.split("/")[4];
		if(part4.equals("NULL")) {
			if(!part5.equals("NULL")) {
				result.append(lineNumber + "/" + "5" + " 错误");
				result.append("\r\n");
			}
		} else {
			HashSet<String> keySet = new HashSet<String>();
			for(String s : keywords)
				keySet.add(s);
			String[] ks = part5.split(",");
			ArrayList<String> list = new ArrayList<String>();
			for(String s : ks)
				list.add(s);
			if(keySet.containsAll(list) || part5.equals("NULL") || part5.matches("[0-9Ee\\+\\-,]+")) {
				//result.append(" 正确");
			} else {
				result.append(lineNumber + "/" + "5" + " 错误" + "\r\n");
			}
		}
		return result.toString();
	}
	
	//字符串用/分为六部分，关键字必须全部大写，否则错误
	public String ruleNo6(int lineNumber,String configureText) {
		if(this.getFlag() == 1)
			return null;
		StringBuilder result = new StringBuilder();
		for(String key : getKeywords()) {
			Pattern pattern = Pattern.compile(key);
			Matcher matcher = pattern.matcher(configureText.toUpperCase());
			while(matcher.find()) {
				int startIndex = matcher.start();
				int endIndex = matcher.end();
				if(!key.equals(configureText.substring(startIndex,endIndex))) {
					int position = 1;
					for(char c : configureText.substring(0,endIndex).toCharArray())
						if(c == '/')
							position++;
					result.append(lineNumber + "/" + position + ",");
				}
			}
			if(result.toString().indexOf(result.toString().length()-1) == ',') {
				int end = result.toString().length()-1;
				result.deleteCharAt(end);
				result.append(" 大小写错误" + "\r\n");
			}
		}
		return result.toString();
	}
	
	public static void main(String[] args) {
		
		//VerifyFile vf = new VerifyFile("Configue.ini","KeyWord.ini","811A","A7040");
		//System.out.println("===================   Result of Verify  ====================");
		//System.out.println(vf.sectionVerify());
	}
}
