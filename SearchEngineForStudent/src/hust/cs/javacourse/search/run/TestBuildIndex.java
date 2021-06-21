package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.DocumentBuilder;
import hust.cs.javacourse.search.index.impl.Index;
import hust.cs.javacourse.search.index.impl.IndexBuilder;
import hust.cs.javacourse.search.parse.impl.LengthTermTupleFilter;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StopWords;

import java.io.*;
import java.util.Arrays;

/**
 * 测试索引构建
 */
public class TestBuildIndex {
    /**
     *  索引构建程序入口
     * @param args : 命令行参数
     */
    public static void main(String[] args) throws IOException {
        AbstractDocumentBuilder documentBuilder = new DocumentBuilder();
        AbstractIndexBuilder indexBuilder = new IndexBuilder(documentBuilder);
        AbstractIndex index = indexBuilder.buildIndex(Config.DOC_DIR);
        if(index==null){
            System.out.println("Data Error!");
            return;
        }
        index.optimize();
        String out_file = Config.INDEX_DIR + "out.txt";
        FileWriter out = new FileWriter(out_file);
        for(AbstractTerm term:index.getDictionary()){
            System.out.println("\"" + term + "\" " + index.search(term));
            out.write("\"" + term + "\" " + index.search(term) + "\n");
        }
        String out_name = Config.INDEX_DIR + "index.dat";
        File file = new File(out_name);
        index.save(file);
    }
}
