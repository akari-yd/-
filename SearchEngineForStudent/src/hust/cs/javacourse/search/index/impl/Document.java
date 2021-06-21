package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;

import java.io.*;
import java.util.List;

public class Document extends AbstractDocument {
    public Document(){

    }
    public Document(int docId, String docPath){
        this.docId  = docId;
        this.docPath = docPath;
    }

    public Document(int docId, String docPath,List<AbstractTermTuple> tuples){
        this.docId  = docId;
        this.docPath = docPath;
        this.tuples = tuples;
    }
    /**
     * 获得文档id
     *
     * @return ：文档id
     */
    @Override
    public int getDocId() {
        return super.docId;
    }

    /**
     * 设置文档id
     *
     * @param docId ：文档id
     */
    @Override
    public void setDocId(int docId) {
        super.docId=docId;
    }

    /**
     * 获得文档绝对路径
     *
     * @return ：文档绝对路径
     */
    @Override
    public String getDocPath() {
        return super.docPath;
    }

    /**
     * 设置文档绝对路径
     *
     * @param docPath ：文档绝对路径
     */
    @Override
    public void setDocPath(String docPath) {
        super.docPath=docPath;
    }

    /**
     * 获得文档包含的三元组列表
     *
     * @return ：文档包含的三元组列表
     */
    @Override
    public List<AbstractTermTuple> getTuples() {
        return super.tuples;
    }

    /**
     * 向文档对象里添加三元组, 要求不能有内容重复的三元组
     *
     * @param tuple ：要添加的三元组
     */
    @Override
    public void addTuple(AbstractTermTuple tuple) {
        if(!this.contains(tuple)){
            //System.out.println(tuple);
            super.tuples.add(tuple);
        }
    }

    /**
     * 判断是否包含指定的三元组
     *
     * @param tuple ： 指定的三元组
     * @return ： 如果包含指定的三元组，返回true;否则返回false
     */
    @Override
    public boolean contains(AbstractTermTuple tuple) {
        return super.tuples.contains(tuple);
    }

    /**
     * 获得指定下标位置的三元组
     *
     * @param index ：指定下标位置
     * @return：三元组
     */
    @Override
    public AbstractTermTuple getTuple(int index) {
        return super.tuples.get(index);
    }

    /**
     * 返回文档对象包含的三元组的个数
     *
     * @return ：文档对象包含的三元组的个数
     */
    @Override
    public int getTupleSize() {
        return super.tuples.size();
    }

    /**
     * 获得Document的字符串表示
     *
     * @return ： Document的字符串表示
     */
    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("docId:" + docId + "docPath:" + docPath + "Tuples:");
        for(int i=0;i<this.getTupleSize();i++){
            s.append(super.tuples.get(i).toString()+'\n');
        }
        return s.toString();
    }
}
