package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.LengthTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.PatternTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.StopWordTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;

import java.io.*;

public class DocumentBuilder extends AbstractDocumentBuilder {

    /**
     * <pre>
     * 由解析文本文档得到的TermTupleStream,构造Document对象.
     * @param docId             : 文档id
     * @param docPath           : 文档绝对路径
     * @param termTupleStream   : 文档对应的TermTupleStream
     * @return ：Document对象
     * </pre>
     */
    @Override
    public AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream) throws IOException {
        AbstractDocument doc = new Document();
        doc.setDocPath(docPath);
        doc.setDocId(docId);
        AbstractTermTuple T;
        while((T=termTupleStream.next())!=null){
            //System.out.println(T.term.getContent() + " " + T.curPos); //所有文档中通过了三个过滤器的单词
            doc.addTuple(T);
        }
        termTupleStream.close();
        return doc;
    }

    /**
     * <pre>
     * 由给定的File,构造Document对象.
     * 该方法利用输入参数file构造出AbstractTermTupleStream子类对象后,内部调用
     *      AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream)
     * @param docId     : 文档id
     * @param docPath   : 文档绝对路径
     * @param file      : 文档对应File对象
     * @return          : Document对象
     * </pre>
     */
    @Override
    public AbstractDocument build(int docId, String docPath, File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        AbstractTermTupleStream scanner = new TermTupleScanner(reader);
        AbstractTermTupleStream patternfilter = new PatternTermTupleFilter(scanner);
        AbstractTermTupleStream stopwordfilter = new StopWordTermTupleFilter(patternfilter);
        AbstractTermTupleStream lengthfilter = new LengthTermTupleFilter(stopwordfilter);
        return build(docId,docPath,lengthfilter);
    }
}
