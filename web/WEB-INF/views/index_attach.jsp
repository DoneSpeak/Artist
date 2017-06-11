<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.artist.model.Article" %>
<%@ page import="com.artist.model.Attach" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    ArrayList<Article> articles = (ArrayList<Article>) request.getAttribute("articles");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
%>
<%@include file="_header.jsp" %>
<div class="main">
    <div class="result-list">
        <%
            for(int i = 0; i < articles.size(); i ++){
                Article article = articles.get(i);
//                此时每篇文章只有一个附件
                Attach attach = article.getAttaches().get(0);
        %>
        <div class="result-item">
            <div class="title">
                <%

                    String title = attach.getFilename();
                    if(title.length() > 32){
                        title = title.substring(0,32) + "...";
                    }
                %>
                <span class="glyphicon glyphicon-paperclip"></span>&nbsp;&nbsp;<a href="<%= attach.getUrl() %>" target="_blank" title="<%= attach.getFilename() %>"><%= title %></a>
            </div>
            <div class="info">
                <span class="klass info-item"><%= article.getCategory().getName() %></span>
                <span class="publisher info-item"><%= article.getPublisher().getName() %></span>
                <span class="time info-item">发布: <%= dateFormat.format(article.getPublished_time()) %></span>
                <% if(article.getUpdate_time() != null && !article.getUpdate_time().equals(article.getPublished_time())){ %>
                <%--更新时间未必会有--%>
                <span class="time-update info-item">更新: <%= dateFormat.format(article.getUpdate_time()) %></span>
                <% } %>
                <span class="attach info-item"><a href="<%= article.getUrl() %>" target="_blank" title="<%= article.getTitle() %>">查看原文</a></span>
            </div>
            <div class="abstract">
                <p><%= attach.getContent() %></p>
            </div>
        </div>
        <% } %>
    </div>
</div>
<%@include file="_footer.jsp" %>