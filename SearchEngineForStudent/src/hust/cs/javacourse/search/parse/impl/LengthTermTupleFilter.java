package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StopWords;

import java.io.IOException;
import java.util.Arrays;

public class LengthTermTupleFilter extends AbstractTermTupleFilter {
    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public LengthTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    /**
     * 获得下一个三元组
     *
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() throws IOException {
        AbstractTermTuple T=input.next();
        if(T==null) return null;
        if(T.term.getContent().length()>= Config.TERM_FILTER_MINLENGTH &&T.term.getContent().length()<=Config.TERM_FILTER_MAXLENGTH) {
            //System.out.println(T.term.getContent() + " " + T.term.getContent().length());
            return T;
        }
        return next();
    }
}
