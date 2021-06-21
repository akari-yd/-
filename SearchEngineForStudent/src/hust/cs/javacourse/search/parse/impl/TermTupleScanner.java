package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.util.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;

public class TermTupleScanner extends AbstractTermTupleScanner {
    protected int num=0;
    public TermTupleScanner(BufferedReader reader){
        super(reader);
    }
    /**
     * 获得下一个三元组
     *
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() throws IOException {
        String word= new String("");
        int ch = -1;
        while((ch=input.read())!=-1){
            //System.out.print(Config.STRING_SPLITTER_REGEX.indexOf((char)ch));
            if(Config.STRING_SPLITTER_REGEX.indexOf((char)ch)>=0) break;
            word=word+(char)ch;
        }

        if(ch==-1&&word.equals("")) return null;

        if(word.equals("")) return next();
        word=word.toLowerCase(Locale.ROOT);
        AbstractTermTuple T = new TermTuple(word,num++);

        return T;
    }
}
