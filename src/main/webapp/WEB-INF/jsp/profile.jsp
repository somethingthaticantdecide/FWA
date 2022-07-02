<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<style>
    #sessions {
        margin-top: 150px;
        width: 32%;
        overflow-y:scroll;
        height:200px;
        display:block;
        margin-right: 34%;
        float: right;
    }
    #avatar {
        margin-top: 200px;
        margin-left: 500px;
        margin-right: auto;
        float: left;
    }
    #upload {
        margin-left: 500px;
        margin-right: auto;
        width: 128px;
    }
    #images {
        margin-left: 500px;
        margin-right: auto;
        width: 39%;
        overflow-y: scroll;
        height: 200px;
        display: block;
    }
</style>
<body>
<img id="avatar" src="data:image/png;base64, ${avatar}" height="128" width="128">
<table id="sessions" border="1">
    <thead>
    <tr>
        <td width="200px">Data</td>
        <td width="250px">Time</td>
        <td width="200px">Ip</td>
    </tr>
    </thead>
    <c:forEach var="element" items="${sessions}">
        <tr>
            <td><c:out value="${element.date}"/></td>
            <td><c:out value="${element.time}"/></td>
            <td><c:out value="${element.ip}"/></td>
        </tr>
    </c:forEach>
</table>

<form id="upload" method="POST" action="upload" enctype="multipart/form-data" >
    File:
    <input type="file" name="file" id="file" /> <br/>
    <br/>
    <input type="submit" value="Upload" name="upload"/>
</form>

<table id="images" border="1">
    <thead>
    <tr>
        <td width="450px">File name</td>
        <td width="150px">Size</td>
        <td width="150px">MIME</td>
    </tr>
    </thead>
    <c:forEach var="image" items="${images}" >
        <tr>
            <td><a href="images/${image.filename}"> <c:out value="${image.filename}"/></a></td>
            <td><c:out value="${image.size}"/></td>
            <td><c:out value="${image.mime}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
