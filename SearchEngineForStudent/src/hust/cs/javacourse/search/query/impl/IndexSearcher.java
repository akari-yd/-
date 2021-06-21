package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

public class IndexSearcher extends AbstractIndexSearcher {
    /**
     * 从指定索引文件打开索引，加载到index对象里. 一定要先打开索引，才能执行search方法
     *
     * @param indexFile ：指定索引文件
     */
    @Override
    public void open(String indexFile) throws IOException {
        super.index.load(new File(indexFile));
        System.out.println(index.toString());
    }

    /**t
     * 根据单个检索词进行搜索
     *
     * @param queryTerm ：检索词
     * @param sorter    ：排序器
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        Vector<AbstractHit> hits = new Vector<AbstractHit>();
        AbstractPostingList postings = index.search(queryTerm);
        if(postings==null) return null;
        postings.sort();
        for(int i=0;i<postings.size();i++){
            AbstractPosting posting=postings.get(i);
            if(posting.getFreq()==0) break;;
            Map<AbstractTerm, AbstractPosting> termPostingMapping = new HashMap<>();
            termPostingMapping.put(queryTerm,posting);
            AbstractHit hit=new Hit(posting.getDocId(),index.getDocName(posting.getDocId()),termPostingMapping);
            hit.setScore(sorter.score(hit));
            hits.add(hit);
        }
        sorter.sort(hits);
        return hits.toArray(new AbstractHit[0]);
    }

    /**
     * 根据二个检索词进行搜索
     *
     * @param queryTerm1 ：第1个检索词
     * @param queryTerm2 ：第2个检索词
     * @param sorter     ：    排序器
     * @param combine    ：   多个检索词的逻辑组合方式
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine) {
        Vector<AbstractHit> hits = new Vector<AbstractHit>();
        AbstractPostingList postings1 = index.search(queryTerm1);
        AbstractPostingList postings2 = index.search(queryTerm2);
        postings1.sort();
        postings2.sort();
        int i=0,j=0;
        while(i<postings1.size()&&j<postings2.size()){
            AbstractPosting posting1=postings1.get(i),posting2=postings2.get(j);
            if(combine==LogicalCombination.AND){
                if(posting1.getDocId()<posting2.getDocId()){
                    i++;
                }
                else if(posting1.getDocId()>posting2.getDocId()){
                    j++;
                }
                else{
                    Map<AbstractTerm, AbstractPosting> termPostingMapping = new HashMap<>();
                    termPostingMapping.put(queryTerm1,posting1);
                    termPostingMapping.put(queryTerm2,posting2);
                    AbstractHit hit=new Hit(posting1.getDocId(),index.getDocName(posting1.getDocId()),termPostingMapping);
                    hit.setScore(hit.getScore());hits.add(hit);
                    i++;j++;
                }
            }
            else if(combine==LogicalCombination.OR){
                if(posting1.getDocId()<posting2.getDocId()){
                    Map<AbstractTerm, AbstractPosting> termPostingMapping = new HashMap<>();
                    termPostingMapping.put(queryTerm1,posting1);
                    AbstractHit hit=new Hit(posting1.getDocId(),index.getDocName(posting1.getDocId()),termPostingMapping);
                    hit.setScore(sorter.score(hit));hits.add(hit);
                    i++;
                }
                else if(posting1.getDocId()>posting2.getDocId()){
                    Map<AbstractTerm, AbstractPosting> termPostingMapping = new HashMap<>();
                    termPostingMapping.put(queryTerm2,posting2);
                    AbstractHit hit=new Hit(posting2.getDocId(),index.getDocName(posting2.getDocId()),termPostingMapping);
                    hit.setScore(sorter.score(hit));hits.add(hit);
                    j++;
                }
                else{
                    Map<AbstractTerm, AbstractPosting> termPostingMapping = new HashMap<>();
                    termPostingMapping.put(queryTerm1,posting2);
                    termPostingMapping.put(queryTerm2,posting2);
                    AbstractHit hit=new Hit(posting1.getDocId(),index.getDocName(posting1.getDocId()),termPostingMapping);
                    hit.setScore(sorter.score(hit));hits.add(hit);
                    i++;j++;
                }
            }
        }
        if(combine==LogicalCombination.OR){
            if(i==postings1.size()){
                while(j<postings2.size()){
                    AbstractPosting posting2=postings2.get(j);
                    Map<AbstractTerm, AbstractPosting> termPostingMapping = new HashMap<>();
                    termPostingMapping.put(queryTerm2,posting2);
                    AbstractHit hit=new Hit(posting2.getDocId(),index.getDocName(posting2.getDocId()),termPostingMapping);
                    hit.setScore(sorter.score(hit));hits.add(hit);
                    j++;
                }
            }
            else{
                while(i<postings1.size()){
                    AbstractPosting posting1=postings1.get(i);
                    Map<AbstractTerm, AbstractPosting> termPostingMapping = new HashMap<>();
                    termPostingMapping.put(queryTerm1,posting1);
                    AbstractHit hit=new Hit(posting1.getDocId(),index.getDocName(posting1.getDocId()),termPostingMapping);
                    hit.setScore(sorter.score(hit));hits.add(hit);
                    i++;
                }
            }
        }
        sorter.sort(hits);
        return hits.toArray(new AbstractHit[0]);
    }
}
