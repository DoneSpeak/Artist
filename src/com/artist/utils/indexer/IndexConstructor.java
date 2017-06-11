package com.artist.utils.indexer;

import java.util.*;

import com.artist.dao.ArticleDao;
import com.artist.model.*;
import com.artist.model.enums.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IndexConstructor {
//  以{region:{word:[{2:100},{3:9},{4:4}]}}的方式记录词典，后面的数组表示出现该词项的{文档编号:词频}
    private HashMap<Region, Postings> regionMapPostings;
//    {region{id: tf}} 记录文档的编号和对应文档的词项数
    private HashMap<Region,HashMap<Integer, Integer>> regionDocLengths;
//  处理的文档总数 - 不同的域，总数不同 - 可以从regionDocLenghts.get(region).size() 得到，这里无需重复计算
//    private HashMap<Region,Integer> docNum;

    @Autowired
    private ArticleDao articleDao;

    public IndexConstructor(){
    }

    public HashMap<Region, Postings> getRegionMapPostings() {
        return regionMapPostings;
    }

    public HashMap<Region,HashMap<Integer, Integer>> getDocLenghts() {
        return regionDocLengths;
    }

    public int getDocNum(Region region) {
        if(regionDocLengths.containsKey(region)){
            return regionDocLengths.get(region).size();
        }
        return 0;
    }

    public void clear(){
        regionMapPostings = new HashMap<Region, Postings>();
        regionDocLengths = new HashMap<Region,HashMap<Integer, Integer>>();
    }

    //	 TODO  从数据库获取数据进行创建倒排索引 - 需要结合spring使用
    public HashMap<Region, Postings> createPostingses() throws Exception {
        ArrayList<Article> articles = articleDao.getAll();
        return createPostingses(articles, true);
    }

//    处理完成后，通过两个getter方法获取相应的数据
	public HashMap<Region, Postings> createPostingses(ArrayList<Article> articles, boolean inOrder){
//        清空上次创建的内容
        regionMapPostings = new HashMap<Region, Postings>();
        regionDocLengths = new HashMap<Region,HashMap<Integer, Integer>>();

        Postings articleTitlePostings = new Postings();
        Postings articleContentPostings = new Postings();
        Postings attachFilenamePostings = new Postings();
        Postings attachContentPostings = new Postings();

        if(!inOrder){
//            TODO 无序，需要对articles进行升序排序 - 暂时无需理会
        }
        for(Article article: articles){
            add2Postings(articleTitlePostings,articleContentPostings,attachFilenamePostings,attachContentPostings,article);
        }

        regionMapPostings.put(Region.ARTICLETITLE,articleTitlePostings);
        regionMapPostings.put(Region.ARTICLECONTENT,articleContentPostings);
        regionMapPostings.put(Region.ATTACHFILENAME,attachFilenamePostings);
        regionMapPostings.put(Region.ATTACHCONTENT,attachContentPostings);

        return regionMapPostings;
    }

    private void add2Postings(Postings articleTitlePostings, Postings articleContentPostings,
                             Postings attachFilenamePostings, Postings attachContentPostings, Article article){

//        HtmlDownloader downloader = new HtmlDownloader();
        int tfSum = 0;

//        处理标题生成postings
        tfSum = add2Postings(articleTitlePostings, article.getId(), article.getTitle());
        add2RegionDocLenghts(Region.ARTICLETITLE, article.getId(),tfSum);
//        处理内容生成postings
        tfSum = add2Postings(articleContentPostings, article.getId(), article.getContent());
        add2RegionDocLenghts(Region.ARTICLECONTENT, article.getId(),tfSum);

        ArrayList<Attach> attaches = article.getAttaches();

//            处理文章附件，创建附件postings
        if(attaches == null || attaches.size() == 0){
            return;
        }
        for(Attach attach: attaches){
            if(attach.getUrl() == null){
//               由null构成的attach
                continue;
            }
//        处理标题生成postings
            tfSum = add2Postings(attachFilenamePostings, attach.getId(), attach.getFilename());
            add2RegionDocLenghts(Region.ATTACHFILENAME, attach.getId(),tfSum);
//        处理内容生成postings
//          由于附件是随着文章添加的，所以只要确保文章的id是升序的，附件的id自然也是升序的
            tfSum = add2Postings(attachContentPostings, attach.getId(), attach.getContent());
            add2RegionDocLenghts(Region.ATTACHCONTENT, attach.getId(),tfSum);
        }
    }

    private void add2RegionDocLenghts(Region region, int id, int tf){
        if(regionDocLengths.containsKey(region)){
            regionDocLengths.get(region).put(id,tf);
        }else{
            HashMap<Integer,Integer> docIdsTF = new HashMap<Integer,Integer>();
            docIdsTF.put(id,tf);
            regionDocLengths.put(region,docIdsTF);
        }
    }

    private int add2Postings(Postings postings, int id, String content){
        int tfSum = 0;
        HashMap<String, Integer> termTF = IndexTool.termTokenize(content);
        Set<String> termStrSet = termTF.keySet();
        for(String t: termStrSet){
            int tf = termTF.get(t);
            tfSum += tf;
            Pair pair = new Pair(id,tf);
            postings.add(t,pair);
        }
        return tfSum;
    }

    public static void Change(STerm term){
        term.setId(1L);
        term.setDf(3L);
        term.setTerm("0000");
    }

    public static void main(String[] args){
        STerm term = new STerm();
        term.setDf(0L);
        term.setId(0L);
        term.setTerm("");
        Change(term);
        System.out.print(term.getTerm());
        System.out.print(term.getId());
        System.out.print(term.getDf());
    }
}
