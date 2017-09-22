# Artist

A Searsher for www.szu.edu.cn/board/

该系统是对深圳大学公文通的检索系统的简单实现，既然是简单实现，也就说只实现了初始的功能，现在暂时只能对一个月的公文通进行检索，而且是系统初始化时所抓取的数据。之后有时间会不断让系统升级，使得可以实时检索和有更高的性能。

# 系统结构及最终效果
## 系统结构
![系统结构](http://img.blog.csdn.net/20170718135516180?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvRG9uZVNwZWFr/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
## 最终效果
基本的页面结构：
![系统最终效果](http://img.blog.csdn.net/20170718135342696?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvRG9uZVNwZWFr/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
 
各个选项详细：
![系统最终效果](http://img.blog.csdn.net/20170718135613973?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvRG9uZVNwZWFr/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
# 爬虫及文本提取器
## 页面分析
下面仅仅通过显示的图片进行说明页面的分析，准确的分析需要通过页面html代码进行更详细的分析。
![页面分析](http://img.blog.csdn.net/20170718135823156?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvRG9uZVNwZWFr/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

这里的文本提取器使用的是POI对Office文档进行文本提取（适应了97-2003版本 和 2007+版本），使用pdfbox对PDF文档进去文本提取。同时使用了简单工厂模式进行封装。
![文本提取器](http://img.blog.csdn.net/20170718140011524?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvRG9uZVNwZWFr/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

# 索引构造系统
该索引构造系统使用的是自己实现的算法，而非第三方（如lucene）工具。系统的基本结构如下：
![索引构造器结果](http://img.blog.csdn.net/20170718140530824?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvRG9uZVNwZWFr/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
**索引构造器：**
将传入的文章按照文章标题、文章内容、附件标题、附件内容进行创建倒排索引。
 
**索引管理器：**
保存、添加和获取不同域的倒排索引、文件长度表。
 
**索引检索器：**
根据传入的query，计算文件与query的余弦相似度，并进行排序返回。
 
## 索引构造器
将传入的文章按照文章标题、文章内容、附件标题、附件内容进行创建倒排索引。 

遍历文章列表：

1. 对文章标题构建倒排索引
2. 对文章内容构建倒排索引
3. 对文章附件标题构建倒排索引
4. 对文章附件内容构建倒排索引

处理主要分为两个部分：

1. 将内容分词，添加到创建的倒排索引中
2. 计算内容的词汇量，添加到文件长度表中

为了能够对文本进行正确的分析，这里对第三方的开源分词工具[HanNP](https://github.com/hankcs/HanLP)进行了分装为类WordSegmenter。此外，分词的工作交由类IndexTool。该类进行方法的分装，分词时将调用IndexTool.termTokenize()方法，该方法中会将英文词汇统一转化为小写。

## 索引管理器
两个重要仓库：
regionMapPostings: 保存不同域的倒排索引
regionDocLengths:    保存不同域的{文档编号和对应的文档词频}

此外regionDocLengths还隐式地保存了不同域的文档总数。并分装了相应的获取不同域的文档总数方法。

## 索引管理器
添加不同域的倒排索引表到索引管理器中，添加不同域的文档长度记录表到索引管理器中已经从中获取相关数据。

## 索引检索器
根据传入的query，计算文件与query的余弦相似度，并进行排序返回。 

检索器整合了构造器和管理器，其中管理器是最重要的部件，是检索的数据来源。 

`CosineScore`是检索器最重要的方法，用于计算文档与请求的相似度，从而确定放回符合用户请求的文档内容。其中Wf_td和w_tq分别是词项t在请求中的权重和词项t在文档中的权重。均为TF-IDF。 

```C
CosineScore(q)
float Score[N] = 0
Initialize Length[N]
for each query term t
do calculate w_tq and fetch postings list for t
    for each pair(d, tf_td) in postings list
    do Scores[d] += wf_td * w_tq
Read the array Length[d]
for each d
do Scores[d] = Scores[d]/Length[d]
return Top K components of Scores[]
```

# 用户交互
整个系统作为一个web应用提供服务，使用了springMVC+mybatis的框架。

## Seacher类
该类除了初始化整个检索系统，还是信息检索的入口。 

start方法：初始化系统 
search方法：对query进行处理，得到按相似度从高到低有序的文章id数组。并利用该数组集合其他检索条件从数据库中获取数据，并返回到给用户。 

## Index类
MVC模型的控制器。
