package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;

import java.io.*;
import java.util.*;

/**
 * AbstractIndex的具体实现类
 */
public class Index extends AbstractIndex {
    public Index(){

    }
    /**
     * 返回索引的字符串表示
     *
     * @return 索引的字符串表示
     */
    @Override
    public String toString() {
        return "docIdToDocPathMapping:" +super.docIdToDocPathMapping.toString()+ "          termToPostingListMapping:" + super.termToPostingListMapping.toString();
    }

    /**
     * 添加文档到索引，更新索引内部的HashMap
     *
     * @param document ：文档的AbstractDocument子类型表示
     */
    @Override
    public void addDocument(AbstractDocument document) {
        try {
            super.docIdToDocPathMapping.put(document.getDocId(), document.getDocPath());
            for (AbstractTermTuple word:document.getTuples()) {
                AbstractPostingList list=super.termToPostingListMapping.get(word.term);//获取单词的倒排索引
                if(list==null){//如果该单词还没有倒排索引，则增加一个
                    list=new PostingList();
                    List<Integer> l=new ArrayList<>();
                    l.add(word.curPos);
                    list.add(new Posting(document.getDocId(),word.freq,l));
                    super.termToPostingListMapping.put(word.term, list);
                }
                else{
                    int index=list.indexOf(document.getDocId());
                    if(index==-1){//对应docId没有记录，则新增记录
                        List<Integer> l=new ArrayList<>();
                        l.add(word.curPos);
                        list.add(new Posting(document.getDocId(),word.freq,l));
                    }
                    else{//增加对应docId的一个记录，频率+1且位置列表增加
                        list.get(index).setFreq(list.get(index).getFreq()+1);
                        list.get(index).getPositions().add(word.curPos);
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     * </pre>
     */
    @Override
    public void load(File file) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            readObject(in);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     * </pre>
     */
    @Override
    public void save(File file) {
        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            this.writeObject(out);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 返回指定单词的PostingList
     *
     * @param term : 指定的单词
     * @return ：指定单词的PostingList;如果索引字典没有该单词，则返回null
     */
    @Override
    public AbstractPostingList search(AbstractTerm term) {
        AbstractPostingList list = super.termToPostingListMapping.get(term);
        return list==null?new PostingList():list;
    }

    /**
     * 返回索引的字典.字典为索引里所有单词的并集
     *
     * @return ：索引中Term列表
     */
    @Override
    public Set<AbstractTerm> getDictionary() {
        return new HashSet<AbstractTerm>(super.termToPostingListMapping.keySet());
    }

    /**
     * <pre>
     * 对索引进行优化，包括：
     *      对索引里每个单词的PostingList按docId从小到大排序
     *      同时对每个Posting里的positions从小到大排序
     * 在内存中把索引构建完后执行该方法
     * </pre>
     */
    @Override
    public void optimize() {
        if(super.termToPostingListMapping==null) return;
        for(AbstractPostingList lists:super.termToPostingListMapping.values()){
            for(int i=0;i<lists.size();i++){
                lists.get(i).sort();
            }
            lists.sort();
        }
    }

    /**
     * 根据docId获得对应文档的完全路径名
     *
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    @Override
    public String getDocName(int docId) {
        if(super.docIdToDocPathMapping==null) return null;
        return super.docIdToDocPathMapping.get(docId);
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try{
            out.writeObject(super.docIdToDocPathMapping.size());
            for(Map.Entry entry:super.docIdToDocPathMapping.entrySet()){
                out.writeObject(entry.getKey());
                out.writeObject(entry.getValue());
            }
            out.writeObject(super.termToPostingListMapping.size());
            for(Map.Entry entry:super.termToPostingListMapping.entrySet()){
                out.writeObject(entry.getKey());
                out.writeObject(entry.getValue());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try{
            int n;
            n = (int) in.readObject();
            for(int i=0;i<n;i++){
                super.docIdToDocPathMapping.put((int)in.readObject(),(String)in.readObject());
            }
            n = (int) in.readObject();
            for(int i=0;i<n;i++){
                super.termToPostingListMapping.put((AbstractTerm) in.readObject(),(AbstractPostingList) in.readObject());
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }
}
