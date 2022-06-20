import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FileReader {
    public static void main(String[] args) throws IOException {
        long start=System.currentTimeMillis();
        try(Reader data = new InputStreamReader(Files.newInputStream(Paths.get("/Users/michael-liang/Desktop/IO_Test/TestSet_01.txt")));
            Writer output=new FileWriter("/Users/michael-liang/Desktop/IO_Test/TestSet01_sorted.txt")
        ) {

//        单次读取容量50000
        ArrayList<String> buffer = new ArrayList<>(50000);
        char[] temp=new char[16];
        while(data.read(temp,0,16)!=-1) {

            buffer.add(String.valueOf(temp));
        }
        String[] str=new String[buffer.size()];
//        buffer.toArray(str);
//        Arrays.sort(str);

        buffer.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
//        List<String> list=buffer.stream().sorted().collect(Collectors.toList());

            for(String a:buffer)
                output.write(a);
            System.out.println("success");
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("error");
        }finally {
            System.out.println(System.currentTimeMillis()-start);
        }
    }
}
