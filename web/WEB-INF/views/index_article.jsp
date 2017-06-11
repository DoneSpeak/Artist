<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.artist.model.Article" %>
<%@ page import="com.artist.model.Attach" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.artist.utils.Shower" %>
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
			%>
			<div class="result-item">
				<div class="title">
                    <%
                        String title = article.getTitle();
                        if(title.length() > 32){
                            title = title.substring(0,32) + "...";
                        }
                    %>
                    <a href="<%= article.getUrl() %>" target="_blank" title="<%= article.getTitle() %>"><%= title %></a>
                </div>
				<div class="info">
					<span class="klass info-item"><%= article.getCategory().getName() %></span>
                    <span class="publisher info-item"><%= article.getPublisher().getName() %></span>
                    <span class="time info-item">发布: <%= dateFormat.format(article.getPublished_time()) %></span>
                    <% if(article.getUpdate_time() != null && !article.getUpdate_time().equals(article.getPublished_time())){ %>
                    <%--更新时间未必会有--%>
                    <span class="time-update info-item">更新: <%= dateFormat.format(article.getUpdate_time()) %></span>
                    <% } %>
                    <%
                        for(int j = 0; j < article.getAttaches().size(); j ++){
                            Attach attach = article.getAttaches().get(j);
                            if(attach == null || attach.getFilename() == null){
                                continue;
                            }
                    %>
                    <span class="attach info-item">
                        <a href="<%= attach.getUrl() %>" title="<%= attach.getFilename() %>"><span class="glyphicon glyphicon-paperclip"></span></a>
                    </span>
                    <% } %>
				</div>
				<div class="abstract">
                    <p><%= article.getContent() %></p>
				</div>
			</div>
            <% } %>
		</div>
	</div>
<%@include file="_footer.jsp" %>