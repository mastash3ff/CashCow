package utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author Brandon Sheffield
 *
 */
public class Dumps {
	
	/** Testing functions*/
	public static void dumpList(List<Object> l){
		System.out.println("List size: " + l.size());
		System.out.println("Dump list of Object");
		for (Object s : l){
			System.out.println(s);
		}
		
	}
	
	public static void dumpQueue(Queue<Object> q){
		System.out.println("Queue size: " + q.size());
		System.out.println("Dump queue of Objects");
		for (Object s : q){
			System.out.println(s);
		}
		
	}
	
	public static void dumpCollection(Collection<Object> c){
		System.out.println("Collection size: " + c.size());
		System.out.println("Dump collection of Objects");
		for (Object o : c){
			System.out.println(o);
		}
		
	}
	
	public static void dumpMap(Map<Object,Object> m){
		System.out.println("Map size: " + m.size());
		System.out.println("Dump Keys: " + m.keySet());
		System.out.println("Values: " + m.values());
	}
	
	public static void listDirFiles() throws IOException{
		File tmp_f = new File("./src/");
		File[] files = tmp_f.listFiles();
		for (File f : files){
			System.out.println(f.getCanonicalFile());
		}
		
	}

}
