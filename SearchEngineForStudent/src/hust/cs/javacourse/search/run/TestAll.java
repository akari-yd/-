package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Posting;
import hust.cs.javacourse.search.index.impl.PostingList;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestAll {
    public static void main(String[] args) {
        String file1 = FileUtil.read("D:\\下载\\qq下载\\Java新实验一\\Java新实验一\\SearchEngineForStudent\\src\\hust\\cs\\javacourse\\search\\index\\AbstractIndex.java");
        String file2 = FileUtil.read("D:\\下载\\HUSTER-CS-master\\HUSTER-CS-master\\Java实验\\Java实验1提交版\\SearchEngineForStudent\\src\\hust\\cs\\javacourse\\search\\index\\AbstractIndex.java");
        System.out.println(file1.length() + " " + file2.length());
        for(int i=0;i<file1.length();i++){
            if(file1.charAt(i)!=file2.charAt(i)){
                System.out.println("file1:" + file1.charAt(i) + ":file2:" + file2.charAt(i) + ":index:" + i);
            }
        }
    }
}
