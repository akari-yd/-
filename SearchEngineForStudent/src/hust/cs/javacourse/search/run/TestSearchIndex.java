package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.util.Config;

import java.io.*;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     *  搜索程序入口
     * @param args ：命令行参数
     */
    public static void main(String[] args) throws IOException {
        String index_name = Config.INDEX_DIR + "index.dat";
        String search_word1,search_word2;
        search_word1 = "google";
        search_word2 = "recognition";
        String ans_name = Config.INDEX_DIR + search_word1 + ".txt";
        AbstractIndexSearcher.LogicalCombination combination;
        combination = AbstractIndexSearcher.LogicalCombination.OR;
        AbstractTerm term1 = new Term(search_word1);
        AbstractTerm term2 = new Term(search_word2);
        AbstractIndexSearcher indexSearcher = new IndexSearcher();
        indexSearcher.open(index_name);
        Sort sorter = new SimpleSorter();
        AbstractHit[] hits = null;
        hits = indexSearcher.search(term1,term2,sorter,combination);
        FileWriter out = new FileWriter(ans_name);
        if(hits==null){
            System.out.println("Not Found!\n");
        }
        else {
            for (int i = 0; i < hits.length; i++) {
                System.out.println(hits[i]);
                out.write(hits[i].toString());
            }
        }
        out.close();
    }
}