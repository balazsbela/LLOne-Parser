package scanner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageProperties {
	public static final List<Character> operators = Arrays.asList('+', '-', '*', '/');
	public static final List<Character> separators = Arrays.asList(' ', '<', '>', '[',
			']', '(', ')', ';');
	public static final List<String> reservedWords = Arrays.asList("more", "stable",
			"when", "work", "is", "isnot", "bigger", "smaller", "samebigger",
			"samesmaller", "be", "hi", "bye", "numb", "char", "message", "ask",
			"otherwise");
	public static final Map<String,Integer> mapping;
	
	static {
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.put("more",2);
		map.put("when",3);
		map.put("work",4);
		map.put("isnot",5);
		map.put("is",6);
		map.put("bigger",7);
		map.put("smaller",8);
		map.put("samebigger",9);
		map.put("samesmaller",10);
		map.put("be",11);
		map.put("message",12);
		map.put("ask",13);
		map.put("hi",14);
		map.put("bye",15);
		map.put("numb",16);
		map.put("char",17);
		map.put("+",18);
		map.put("-",19);
		map.put("*",20);
		map.put("/",21);
		map.put("(",22);
		map.put(")",23);
		map.put(";",24);
		map.put("and",25);
		map.put("or",26);
		map.put("not",27);
		map.put("[",28);
		map.put("]",29);
		map.put("<",30);
		map.put(">",31);
		map.put("boolean",32);
		map.put("stable",33);			
		map.put("otherwise",34);
		mapping = Collections.unmodifiableMap(map);
	}
}
