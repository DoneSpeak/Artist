<%@ page import="com.artist.model.Category" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.artist.model.Publisher" %><%--
  Created by IntelliJ IDEA.
  User: DoneSpeak
  Date: 2017/6/9
  Time: 10:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ArrayList<Publisher> publishers = (ArrayList<Publisher>) request.getAttribute("publishers");
    ArrayList<Category> categories = (ArrayList<Category>) request.getAttribute("categories");

//    设置当前publisher
    Integer publisherId = (Integer) request.getAttribute("publisherId");
    String publisherName = null;
    if(publisherId != null) {
        publisherId = publisherId <= -2 ? -2 : publisherId;
        if(publisherId == -1){
//         全部
            publisherName = "不限";
        }else if(publisherId == -2){
            publisherName = "发布单位";
        } else if (publisherId > -2) {
            for (Publisher publisher : publishers) {
                if (publisher.getId() == publisherId) {
                    publisherName = publisher.getName();
                    break;
                }
            }
        }
    }else{
        publisherId = -2;
        publisherName = "发文单位";
    }

//    设置当前 category
    Integer categoryId = (Integer) request.getAttribute("categoryId");
    String categoryName = null;
    if(categoryId != null) {
        categoryId = categoryId <= -2 ? -2 : categoryId;
        if(categoryId == -1){
//         全部
            categoryName = "全部";
        }else if(categoryId == -2){
            categoryName = "文章类别";
        } else if(categoryId > -1) {
            for (Category category : categories) {
                if (category.getId() == categoryId) {
                    categoryName = category.getName();
                    break;
                }
            }
        }
    }else{
        categoryId = -2;
        categoryName = "文章类别";
    }

//    设置当前 query
    String query = (String) request.getAttribute("query");
    query = query == null ? "": query;

    Integer region = (Integer) request.getAttribute("region");
    String regionStr = null;
    if(region != null) {
        switch (region) {
            case 0:
                regionStr = "文章标题";
                break;
            case 1:
                regionStr = "文章全文";
                break;
            case 2:
                regionStr = "附件标题";
                break;
            case 3:
                regionStr = "附件全文";
                break;
            default:
                regionStr = "文章全文";
                region = 1;
                break;
        }
    }else{
        region = 1;
        regionStr = "文章全文";
    }

    Integer duration = (Integer) request.getAttribute("duration");
    String durationStr = null;
    if(duration != null) {
        switch (duration) {
            case -1:
                durationStr = "不限";
                break;
            case 1:
                durationStr = "一天";
                break;
            case 3:
                durationStr = "三天";
                break;
            case 7:
                durationStr = "一周";
                break;
            case 15:
                durationStr = "半个月";
                break;
            case 30:
                durationStr = "一个月";
                break;
            case 90:
                durationStr = "三个月";
                break;
            case 183:
                durationStr = "半年";
                break;
            case 365:
                durationStr = "一年";
                break;
            case 730:
                durationStr = "两年";
                break;
            case 1825:
                durationStr = "五年";
                break;
            default:
                duration = -2;
                durationStr = "时间限制";
        }
    }else{
        duration = -2;
        durationStr = "时间限制";
    }


%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="/static/bootstrap-3.3.7/css/bootstrap.css" rel="stylesheet" />
    <link href="/static/css/global.css" rel="stylesheet" />
    <link href="/static/css/index.css" rel="stylesheet" />
    <title>ARTIST</title>
</head>
<body>
<div class="header">
    <div class="searcher">
        <img class="searcher-img" src="/static/img/artist-green.png"/>
        <form class="input-area" action="/search" method="get">
            <div class="input-group">
                <input type="text" name="query" class="form-control searcher-input" value="<%= query %>" aria-label="...">
                <input type="hidden" id="type" name="region" value="<%= region %>">
                <input type="hidden" id="klass" name="categoryId" value="<%= categoryId %>">
                <input type="hidden" id="duration" name="duration" value="<%= duration %>">
                <input type="hidden" id="publisher" name="publisherId" value="<%= publisherId %>">

                <div class="input-group-btn">
                    <button type="button" class="btn btn-default dropdown-toggle" id="search-type-btn" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <span class="search-type-text"><%= regionStr %></span> <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu dropdown-menu-right search-type-options">
                        <li class="dropdown-header">文章</li>
                        <li class="search-type-item" data-text="文章标题" data-value="0"><a href="javascript:;">文章标题</a></li>
                        <li class="search-type-item" data-text="文章全文" data-value="1"><a href="javascript:;">文章全文</a></li>
                        <li role="separator" class="divider"></li>
                        <li class="dropdown-header">附件</li>
                        <li class="search-type-item" data-text="附件标题" data-value="2"><a href="javascript:;">附件标题</a></li>
                        <li class="search-type-item" data-text="附件全文" data-value="3"><a href="javascript:;">附件全文</a></li>
                        <%--<li role="separator" class="divider"></li>--%>
                        <%--<li class="search-type-item" data-text="日期" data-value="-1"><a href="javascript:;">日期</a></li>--%>
                    </ul>
                </div>
                <span class="input-group-btn">
		        <button class="btn btn-default search-btn" type="submit"><span class="glyphicon glyphicon-search"></span></button>
		      </span>
            </div>
        </form>
    </div>
    <div class="search-options">
        <div class="left">
            <div class="dropdown">
				  <span class="dropdown-toggle option-btn category-option" type="button" id="type-dropdown-menu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
				    <span class="option-klass-text <%= categoryId != -2 ? "cur-option": "" %>"><%= categoryName %></span>
				    <span class="caret"></span>
				  </span>
                <ul class="dropdown-menu" aria-labelledby="type-dropdown-menu">
                    <li class="option-klass-item" data-text="全部" data-value="-1"><a href="javascript:;">全部</a></li>
                    <% for(int i = 0; i < categories.size(); i ++){ %>
                    <li class="option-klass-item" data-text="<%= categories.get(i).getName() %>" data-value="<%= categories.get(i).getId() %>"><a href="javascript:;"><%= categories.get(i).getName() %></a></li>
                    <% } %>
                </ul>
            </div>
        </div>
        <div class="left">
            <div class="dropdown">
				  <span class="dropdown-toggle option-btn" type="button" id="duration-dropdown-menu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
				    <span class="option-duration-text <%= duration != -2 ? "cur-option": "" %>"><%= durationStr %></span>
				    <span class="caret"></span>
				  </span>
                <ul class="dropdown-menu" aria-labelledby="duration-dropdown-menu">
                    <li class="option-duration-item" data-text="不限" data-value="-1"><a href="javascript:;">不限</a></li>
                    <li class="option-duration-item" data-text="一天" data-value="1"><a href="javascript:;">一天</a></li>
                    <li class="option-duration-item" data-text="三天" data-value="3"><a href="javascript:;">三天</a></li>
                    <li class="option-duration-item" data-text="一周" data-value="7"><a href="javascript:;">一周</a></li>
                    <li class="option-duration-item" data-text="半个月" data-value="15"><a href="javascript:;">半个月</a></li>
                    <li class="option-duration-item" data-text="一个月" data-value="30"><a href="javascript:;">一个月</a></li>
                    <li class="option-duration-item" data-text="三个月" data-value="90"><a href="javascript:;">三个月</a></li>
                    <li class="option-duration-item" data-text="半年" data-value="183"><a href="javascript:;">半年</a></li>
                    <li class="option-duration-item" data-text="一年" data-value="365"><a href="javascript:;">一年</a></li>
                    <li class="option-duration-item" data-text="两年" data-value="730"><a href="javascript:;">两年</a></li>
                    <li class="option-duration-item" data-text="五年" data-value="1825"><a href="javascript:;">五年</a></li>
                </ul>
            </div>
        </div>
        <div class="left">
				<span class="dropdown-toggle option-btn publishers-btn">
			    <span class="option-publisher-text <%= publisherId != -2 ? "cur-option": "" %>"><%= publisherName %></span>
			    <span class="caret"></span>
			  </span>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="publishers">
    <%
        Publisher unlimit = new Publisher();
        unlimit.setName("不限");
        unlimit.setId(-1);
        publishers.add(0,unlimit);
//        publishers.add(unlimit);
        for(int i = 0; i < publishers.size(); i ++){
    %>
    <ul>
        <% for(int j = 0; j < 20 && i < publishers.size(); j ++,i++){%>
        <li class="option-publisher-item" data-value="<%= publishers.get(i).getId() %>" data-text="<%= publishers.get(i).getName() %>"><%= (i+1) + ". " + publishers.get(i).getName() %></li>
        <% } %>
    </ul>
    <% } %>
    <div class="clear"></div>
</div>